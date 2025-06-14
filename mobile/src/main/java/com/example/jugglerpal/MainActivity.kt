@file:OptIn(ExperimentalLayoutApi::class)
package com.example.jugglerpal

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.*
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import android.widget.Toast
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Spinner
import android.view.View
import com.google.android.gms.wearable.Node
import androidx.lifecycle.lifecycleScope
// IMPORTS QUE PRECISAS de Firestore/Auth:
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import android.app.Activity
import android.content.Intent
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.annotation.DrawableRes

// Composables básicos
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size

// Alinhamento e escala
import androidx.compose.ui.layout.ContentScale

// Recursos e unidades
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Texto e estilos
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset


data class ThrowRecord(val count: Int, val durationSeconds: Float, val frequency: Float)
data class HistoryItem(
    val dateTime: String,
    val throwRecords: List<ThrowRecord>,
    val note: String,
    val attempts: Int,
    val totalThrows: Int,
    val maxThrow: Int,
    val averageThrow: Float,
    val tps: Float,
    val jugglerName: String? = null,
    val jugglerNickName: String? = null,
    val jugglerPass: String? = null,
)

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    private val gson = Gson()
    private var allHistoryItems by mutableStateOf(listOf<HistoryItem>())
    private var selectedJuggler by mutableStateOf<String?>(null)

    private val exportJsonLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri -> uri?.let { exportJson(it) } }

    private val importJsonLauncher = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (!uris.isNullOrEmpty()) importMultipleJsons(uris) }

    private var allNodes by mutableStateOf<List<Node>>(emptyList())
    private var activeNodes by mutableStateOf<List<Node>>(emptyList())
    private var selectedNode by mutableStateOf<Node?>(null)

    private var refreshJob: Job? = null

    private var lastSensorUpdateTime by mutableStateOf(0L)
    private lateinit var firestore: FirebaseFirestore
    private val RC_SIGN_IN = 1001
    private val auth = FirebaseAuth.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o Firestore
        firestore = FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        }
        // (opcional) Autentica anónimamente para respeitar as rules
        //FirebaseAuth.getInstance().signInAnonymously()
        // (opcional) Autentica anónimamente para respeitar as rules
        FirebaseAuth.getInstance().signInAnonymously()

        loadConnectedNodes()

        setContent {
            JugglerPalScreen(
                allItems = allHistoryItems,
                selectedPerson = selectedJuggler,
                onPersonSelected = { selectedJuggler = it },
                onExportClicked = { exportJsonLauncher.launch("jugglerpal_history.json") },
                onImportClicked = { importJsonLauncher.launch(arrayOf("application/json")) },
                onSendToWearClicked = { sendHistoryToWear() },
                nodes = activeNodes,
                selectedNode = selectedNode,
                onNodeSelected = { selectedNode = it },
                onCheckWears = { refreshWearDevices() }

            )

        }




        //loadHistory()
    }


    override fun onStart() {
        super.onStart()
        Wearable.getMessageClient(this).addListener(this)
        activeNodes = emptyList()
        //requestHistoryFromWear()
        //startPeriodicCheck()
    }
    private fun startPeriodicCheck() {
        // cancela se já existir
        refreshJob?.cancel()
        refreshJob = lifecycleScope.launch {
            // enquanto a Activity estiver ativa
            while (isActive) {
                // chama o teu callback
                refreshWearDevices()
                // espera 30 segundos
                delay(600_000)
            }
        }
    }
    override fun onStop() {
        super.onStop()
        Wearable.getMessageClient(this).removeListener(this)
        refreshJob?.cancel()
    }
    private fun verifySavedInFirestore() {
        firestore.collection("history")
            .document("global")
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val savedJson = doc.getString("history_json")
                    Log.d("FirestoreVerify", "Documento existe! JSON size: ${savedJson?.length}")
                    Toast.makeText(
                        this@MainActivity,
                        "Documento existe!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.w("FirestoreVerify", "Documento não encontrado!")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreVerify", "Erro ao ler doc de verificação", e)
            }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/jugglerpal/history") {
            val message = String(messageEvent.data)


            // 1) Desserializa para lista
            val listType = object : TypeToken<List<HistoryItem>>() {}.type
            val items: List<HistoryItem> = gson.fromJson(message, listType)

            // 2) Guarda no estado para o Compose re-compor
            allHistoryItems = items

            // 3) Acede ao primeiro nome ou usa "juggler" como fallback
            val jugglerName = items
                .firstOrNull()
                ?.jugglerName
                ?: "juggler"
            val jugglerNickName = items
                .firstOrNull()
                ?.jugglerNickName
                ?: "juggler"

            firestore.collection("history")
                .document("${jugglerName} - ${jugglerNickName}")  // ou usa o UID do user
                .set(mapOf("history_json" to message))
                .addOnSuccessListener {
                    Log.d("Firestore", "Histórico sincronizado no Firebase com sucesso")
                    verifySavedInFirestore()

                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Falha a sincronizar no Firebase", e)
                }



            // identifica o Node que respondeu
            val responderId = messageEvent.sourceNodeId
            val responderNode = allNodes.find { it.id == responderId }
            if (responderNode != null && responderNode !in activeNodes) {
                activeNodes = activeNodes + responderNode
            }

            // mostra toast em inglês
            runOnUiThread {
                Toast.makeText(
                    this@MainActivity,
                    "Data synchronized with ${responderNode?.displayName ?: "device"}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            /*getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("history_json", message)
                .apply()
            loadHistory()*/
        }
    }





    private fun refreshWearDevices() {
        // limpa a lista de nós e de ativos
        allNodes = emptyList()
        activeNodes = emptyList()
        // carrega de novo todos e faz ping
        loadConnectedNodes()
        requestHistoryFromWear()
    }

    /** 1) Carrega todos os nós emparelhados */
    private fun loadConnectedNodes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val nodes = Tasks.await(
                    Wearable.getNodeClient(this@MainActivity).connectedNodes
                )
                withContext(Dispatchers.Main) {
                    allNodes = nodes
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro a obter nós Wear", e)
            }
        }
    }

    private fun requestHistoryFromWear() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val nodes = Tasks.await(Wearable.getNodeClient(this@MainActivity).connectedNodes)
                nodes.forEach { node ->
                    Wearable.getMessageClient(this@MainActivity)
                        .sendMessage(node.id, "/jugglerpal/fetch_history", ByteArray(0))
                        .addOnSuccessListener {
                            Log.d("MainActivity", "Pedido de history enviado a ${node.displayName}")
                            Toast.makeText(
                                this@MainActivity,
                                "Sync request sent to ${node.displayName}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Log.e("MainActivity", "Falha ao pedir history", it)
                        }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro a obter nós Wear", e)
            }
        }
    }

    private fun loadHistory() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val historyJson = prefs.getString("history_json", null)

        if (!historyJson.isNullOrBlank()) {
            val listType = object : TypeToken<List<HistoryItem>>() {}.type
            allHistoryItems = gson.fromJson(historyJson, listType)
        }
    }

    private fun exportJson(uri: Uri) {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val historyJson = prefs.getString("history_json", null) ?: return
        try {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { it.write(historyJson) }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Erro ao exportar", e)
        }
    }

    private fun importMultipleJsons(uris: List<Uri>) {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val allItems = mutableListOf<HistoryItem>()
        for (uri in uris) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val json = InputStreamReader(inputStream).readText()
                    val type = object : TypeToken<List<HistoryItem>>() {}.type
                    val items: List<HistoryItem> = gson.fromJson(json, type)
                    allItems.addAll(items)
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro ao importar", e)
            }
        }
        if (allItems.isNotEmpty()) {
            prefs.edit().putString("history_json", gson.toJson(allItems)).apply()
            loadHistory()
        }
    }

    private fun importHistoryFromFirebase() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val allItems = mutableListOf<HistoryItem>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1) Faz o fetch de todos os docs da coleção "history"
                val snapshot = Tasks.await(firestore.collection("history").get())

                // 2) Para cada doc, desserializa o campo "history_json"
                val type = object : TypeToken<List<HistoryItem>>() {}.type
                snapshot.documents.forEach { doc ->
                    doc.getString("history_json")?.takeIf { it.isNotBlank() }?.let { json ->
                        val items: List<HistoryItem> = gson.fromJson(json, type)
                        allItems.addAll(items)
                    }
                }
            } catch (e: Exception) {
                Log.e("ImportFirebase", "Erro ao importar do Firestore", e)
            }

            withContext(Dispatchers.Main) {
                if (allItems.isNotEmpty()) {
                    // 3) Guarda o JSON combinado nas prefs
                    val combinedJson = gson.toJson(allItems)
                    prefs.edit()
                        .putString("history_json", combinedJson)
                        .apply()

                    // 4) Atualiza o estado para o Compose
                    allHistoryItems = allItems

                    Toast.makeText(
                        this@MainActivity,
                        "Import from Firebase: ${allItems.size} records",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "No history found in Firebase",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    private fun sendHistoryToWear() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val historyJson = prefs.getString("history_json", null) ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val nodes = Tasks.await(Wearable.getNodeClient(this@MainActivity).connectedNodes)
                nodes.forEach { node ->
                    Wearable.getMessageClient(this@MainActivity)
                        .sendMessage(node.id, "/jugglerpal/restore", historyJson.toByteArray())
                        .addOnSuccessListener { Log.d("MainActivity", "Sent to ${node.displayName}") }
                        .addOnFailureListener { Log.e("MainActivity", "Failure to send", it) }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Impossible to Sync with wearable", e)
            }
        }
    }


    @Composable
    fun JugglerPalScreen(
        darkTheme: Boolean = true,
        allItems: List<HistoryItem>,
        selectedPerson: String?,
        onPersonSelected: (String?) -> Unit,
        onExportClicked: () -> Unit,
        onImportClicked: () -> Unit,
        onSendToWearClicked: () -> Unit,
        nodes: List<Node>,
        selectedNode: Node?,
        onNodeSelected: (Node) -> Unit,
        onCheckWears: () -> Unit
    ) {
        val persons = (allItems.map { it.jugglerName ?: "Unknown Juggler" }.toSet() + "All").sorted()
        val filteredItems = if (selectedPerson == null || selectedPerson == "All") allItems else allItems.filter { (it.jugglerName ?: "Unknown Juggler") == selectedPerson }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .background(Color(0xFFEFF1EE))
        ) {
            Spacer(Modifier.height(16.dp))

            // Aqui: nosso Box com imagem e texto sobreposto
            SmartwatchWithOverlayText(
                iconRes = R.drawable.smartwatch_face_shadow,
                overlay = "Import Workouts",
                modifier = Modifier
                    .size(350.dp)          // ajusta o tamanho do relógio
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                Chip(
                    label = {
                        Text(
                            text = "Get Watch Data",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            color = Color.DarkGray
                        )
                    },
                    onClick = onCheckWears,
                    modifier = Modifier.wrapContentWidth().height(30.dp),
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = Color(0xFFFBFDFA)
                    )
                )
                Chip(
                    label = {
                        Text(
                            text = "Send Data to Watch",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            color = Color.DarkGray
                        )
                    },
                    onClick = onSendToWearClicked,
                    modifier = Modifier.wrapContentWidth().height(30.dp),
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = Color(0xFFFBFDFA)
                    )
                )
            }

            /*Button(
                onClick = onCheckWears,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).background(Color(0xFFFBFDFA))
            ) {
                Text("Check Watches")
            }*/

            if (nodes.isEmpty()) {
                Text(
                    "No active watch. Please open JugglerPal on your watch and try again.",
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else {
                Text(
                    "Select a watch to sync: ${lastSensorUpdateTime}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        Spinner(ctx).apply {
                            adapter = ArrayAdapter(
                                ctx,
                                android.R.layout.simple_spinner_item,
                                nodes.map { it.displayName }
                            ).also {
                                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            }
                            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View?, pos: Int, id: Long
                                ) {
                                    onNodeSelected(nodes[pos])
                                }
                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }
                    },
                    update = { spinner ->
                        val idx = nodes.indexOfFirst { it.id == selectedNode?.id }
                        if (idx >= 0 && spinner.selectedItemPosition != idx) {
                            spinner.setSelection(idx)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(onClick = {importHistoryFromFirebase()}) { Text("Import from Cloud") }
                Button(onClick = onImportClicked) { Text("Import from File") }
                Button(onClick = onExportClicked) { Text("Export to file") }
            }

            Spacer(Modifier.height(16.dp))

            // Aqui: FlowRow em vez de LazyRow, com espaçamento e wrap automáticos
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                persons.forEach { person ->
                    Chip(
                        label = {
                            Text(
                                text = person,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = false,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                color = Color.DarkGray
                            )
                        },
                        onClick = { onPersonSelected(if (person == "All") null else person) },
                        modifier = Modifier.wrapContentWidth().height(30.dp),
                        colors = ChipDefaults.primaryChipColors(
                            backgroundColor = if (person == selectedPerson) Color.Red else Color.LightGray
                        )
                    )
                }
            }



            Spacer(Modifier.height(16.dp))

            if (filteredItems.isEmpty()) {
                Text("No data.", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    filteredItems.groupBy { it.note }.entries.sortedByDescending { it.value.sumOf { it.attempts } }
                        .forEach { (note, tricks) ->
                            val runs = tricks.sumOf { it.attempts }
                            val max = tricks.maxOfOrNull { it.maxThrow } ?: 0
                            val avg = if (runs > 0) tricks.sumOf { it.totalThrows }.toDouble() / runs else 0.0
                            Text("${note.take(20)} - Runs: $runs - Max: $max - Avg: %.1f".format(avg),color = Color.DarkGray)
                        }
                }
            }
        }
    }
    @Composable
    fun SmartwatchWithOverlayText(
        @DrawableRes iconRes: Int,
        overlay: String,
        modifier: Modifier = Modifier,
        textStyle: TextStyle = TextStyle(
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(2f, 2f),
                blurRadius = 4f
            )
        )
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Text(
                text = overlay,
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    // opcional: fundo semitransparente para legibilidade
                    .background(
                        color = Color.Black.copy(alpha = 1f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }

}



