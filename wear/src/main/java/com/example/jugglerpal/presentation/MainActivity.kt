package com.example.jugglerpal.presentation

// Android Framework
import android.content.Context
import android.content.Intent
import android.graphics.Color as AndroidColor
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.ui.input.pointer.pointerInteropFilter
import android.view.MotionEvent
import android.view.ViewGroup
// AndroidX – Core / Activity / Lifecycle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlin.math.log10
import kotlin.math.ln
import android.view.GestureDetector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectHorizontalDragGestures


// Jetpack Compose – Foundation
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.TextField
// Jetpack Compose – Material
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.Icon

// Jetpack Compose – Runtime
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.github.mikephil.charting.charts.HorizontalBarChart

// Jetpack Compose – UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
// Wear OS Compose Material
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

// Accompanist Pager
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

// Google Play Services - Wearable
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent

// Gson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// App Theme
import com.example.jugglerpal.presentation.theme.JugglerPalTheme
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import android.widget.TextView
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.CombinedData
// Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

// Java / Kotlin Utilities
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

import com.example.jugglerpal.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material.LocalTextStyle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper
import android.os.HandlerThread

import android.media.SoundPool
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Typeface
import kotlinx.coroutines.*
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.compose.ui.text.input.KeyboardType

import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.abs

import kotlin.math.max
import kotlin.math.min

// Classe de dados para armazenar cada registro
data class ThrowRecord(
    val count: Int,
    val durationSeconds: Float,
    val frequency: Float
)

data class HistoryItem(
    val dateTime: String,
    val throwRecords: List<ThrowRecord>,
    val note: String,
    val attempts: Int,
    val totalThrows: Int,
    val maxThrow: Int,
    val averageThrow: Float,
    val tps: Float,
    val jugglerName: String="",
    val jugglerNickName: String="",
    val jugglerPass: String="",
    val totalDuration: Float = 0f,
    val maxDuration: Float = 0f,
    val averageDuration: Float = 0f
)


class MetronomePlayer(
    private val context: Context,
    private val scope: LifecycleCoroutineScope
) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .build()
    private val tickId: Int
    private var job: Job? = null

    init {
        // coloque um arquivo tick.wav em res/raw
        tickId = soundPool.load(context, R.raw.tick, 1)
    }

    /** Começa a tocar no ritmo BPM */
    /*fun start(bpm: Int) {
        stop()
        val intervalMs = 60_000L / bpm
        job = scope.launch(Dispatchers.IO) {
            while (isActive) {
                soundPool.play(tickId, 1f, 1f, 1, 0, 1f)
                delay(intervalMs)
            }
        }
    }*/

    fun start(bpm: Int) {
        stop()
        val intervalMs = 60_000L / bpm
        job = scope.launch(Dispatchers.IO) {
            var beatCount = 0
            while (isActive) {
                //val rate = if (beatCount % 2 == 1) 0.9f else 1f
                val rate=1f
                soundPool.play(tickId, 1f, 1f, 1, 0, rate)
                //beatCount++
                delay(intervalMs)
            }
        }
    }

    /** Para o metrônomo */
    fun stop() {
        job?.cancel()
        job = null
    }

    /** Libera recursos (chame em onDestroy) */
    fun release() {
        stop()
        soundPool.release()
    }
}

class MainActivity : ComponentActivity(),
    SensorEventListener,
    MessageClient.OnMessageReceivedListener,
    TextToSpeech.OnInitListener {

    private var isScreenBlocked by mutableStateOf(false)

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var sensorThread: HandlerThread
    private lateinit var sensorHandler: Handler

    private val lastDiffs = mutableStateListOf<Int>()
    private val arrayDiffs = mutableStateListOf<Long>()
    private var showDiffsDialog by mutableStateOf(false)


    private var jugglerName by mutableStateOf("")
    private var jugglerNickName by mutableStateOf("")
    private var jugglerPass by mutableStateOf("")
    private var showAccountDialogOnStartup by mutableStateOf(false)



    private var accelerometerReading by mutableStateOf("T: ---")
    private var flankCount by mutableStateOf(0)
    private var wasBelowZ = false

    private var latestX by mutableStateOf(0f)
    private var latestY by mutableStateOf(0f)
    private var latestZ by mutableStateOf(0f)

    /*private var minX by mutableStateOf(Float.MAX_VALUE)
    private var maxX by mutableStateOf(Float.MIN_VALUE)
    private var minY by mutableStateOf(Float.MAX_VALUE)
    private var maxY by mutableStateOf(Float.MIN_VALUE)*/
    private var minZ by mutableStateOf(Float.MAX_VALUE)
    private var maxZ by mutableStateOf(Float.MIN_VALUE)
    private var thresholdZ by mutableStateOf(25f)
    private var gapZ by mutableStateOf(5f)
    private var windowSize by mutableStateOf(5)

    private var bpm by mutableStateOf(300)
    private var throwsInterval by mutableStateOf(50)
    private var timeInterval by mutableStateOf(30)
    private var tpsInterval by mutableStateOf(5f)


    private var swapZ by mutableStateOf(1)
    private var swapAudio by mutableStateOf(0) //0-mute, 1-vibrate, 2-high
    private var swapCount by mutableStateOf(0) //0-count, 1-time, 2-tps
    private var swapMetronome by mutableStateOf(0) //0-off, 1-on
    private var swapDisplay by mutableStateOf(0) //0-list, 1-graph
    private var swapGraph by mutableStateOf(0) //0-attempts, 1-histogram, 2-progress

    private var throwMultiplier by mutableStateOf(2)

    private val sensorSamples = mutableStateListOf<SensorSample>()
    private val lastSessionSamples = mutableStateListOf<SensorSample>()
    data class SensorSample(val timeMs: Long, val x: Float, val y: Float, val z: Float)

    // Lista reativa para armazenar os registros da série atual
    private val recordedRecords = mutableStateListOf<ThrowRecord>()

    // Tempo da última atualização (em ms)
    private var lastSensorUpdateTime by mutableStateOf(0L)

    // Marca o início da série atual
    private var seriesStartTime: Long? = null
    private val gson = Gson()

    private var lastAnnouncedCount = 0
    private var lastAnnouncedTime = System.currentTimeMillis()
    private lateinit var tts: TextToSpeech

    private lateinit var metronome: MetronomePlayer
    private var isMetronomeRunning = false
    private var saveAll by mutableStateOf(true)
    private var throwPhase by mutableStateOf(0)
    private var zFiltered = 0f
    private val alpha = 0.5f
    private val zWindow = ArrayDeque<Float>()   // fila FIFO
    //private var zFiltered by mutableStateOf(0f)  // seu valor filtrado
    private var isAccelReliable = 0
    private val accelPropertiesState = mutableStateListOf<Pair<String, Any?>>()


    private fun saveRecordedRecords() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val json = Gson().toJson(recordedRecords.toList())
        prefs.edit()
            .putString("recorded_records", json)
            .apply()
    }

    private fun loadRecordedRecords(): List<ThrowRecord> {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val json = prefs.getString("recorded_records", null) ?: return emptyList()
        val type = object : TypeToken<List<ThrowRecord>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setTheme(android.R.style.Theme_DeviceDefault)

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        jugglerName = prefs.getString("juggler_name", "") ?: ""
        jugglerNickName = prefs.getString("juggler_nickname", "") ?: ""
        jugglerPass = prefs.getString("juggler_pass", "") ?: ""

        /*val existing = prefs.getString("history", null)
        if (existing.isNullOrBlank()) {
            // nunca houve histórico ou foi apagado: põe o default
            val defaultJson = loadDefaultHistoryJson()
            prefs.edit()
                .putString("history", defaultJson)
                .apply()
        }*/



        thresholdZ = prefs.getFloat("thresholdZ", 25f)
        gapZ = prefs.getFloat("gapZ", 5f)
        windowSize = prefs.getInt("windowSize", 5)

        bpm = prefs.getInt("bpm", 300)
        throwsInterval = prefs.getInt("throwsInterval", 300)
        timeInterval = prefs.getInt("timeInterval", 300)
        tpsInterval = prefs.getFloat("tpsInterval", 5f)



        swapZ = prefs.getInt("swapZ", 1)
        swapAudio = prefs.getInt("swapAudio", 0)
        swapCount= prefs.getInt("swapCount", 0)
        swapMetronome= prefs.getInt("swapMetronome", 0)
        swapDisplay= prefs.getInt("swapDisplay", 0)
        swapGraph= prefs.getInt("swapGraph", 0)

        throwMultiplier = prefs.getInt("throwMultiplier", 2)  // <- lê o multiplier

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        accelerometer?.let { sensor ->
            accelPropertiesState.apply {
                clear()
                addAll(
                    listOf(
                        "Vendor" to sensor.vendor,
                        "MaxRange (a)" to sensor.maximumRange,
                        "MaxDelay (us)" to sensor.maxDelay,
                        "MinDelay (us)" to sensor.minDelay,
                        "Resolution (a)" to sensor.resolution,
                        "Power (mA)" to sensor.power,
                    )
                )
            }
        }





        // ➜ Novo: cria a thread dedicada ao sensor
        sensorThread = HandlerThread("SensorThread").apply { start() }
        sensorHandler = Handler(sensorThread.looper)


        lastSensorUpdateTime = System.currentTimeMillis()

        //tts = TextToSpeech(this, this)
        tts = TextToSpeech(this, this).apply {
            // Utterance branco de 1ms para carregar o motor
            playSilentUtterance(1, TextToSpeech.QUEUE_FLUSH, "warmup")
        }
        metronome = MetronomePlayer(this, lifecycleScope)
        // Coroutine para verificar inatividade
        lifecycleScope.launch {
            while (true) {
                delay(50)
                val diffTime = System.currentTimeMillis() - lastSensorUpdateTime
                if (flankCount >= 3*throwMultiplier && diffTime > 2000) {
                    resetCounter()
                    isScreenBlocked = false
                }
                if (flankCount > 0 && diffTime > 2000) {
                    flankCount = 0
                    metronome.stop()
                    isMetronomeRunning = false
                    seriesStartTime = null
                    isScreenBlocked = false
                }
                if (flankCount > 0 && diffTime < 2000) {
                    isScreenBlocked = true
                    //showDiffsDialog = false
                }
            }
        }

        //migrateHistoryWithNewFields(this)
        //migrateHistoryWithDurationFields(this)
        //migrateDurationsSince(this, "19-05-25", 0.25f)

        recordedRecords.clear()
        recordedRecords.addAll(loadRecordedRecords())
        showAccountDialogOnStartup =
            jugglerName.isBlank() ||
            jugglerNickName.isBlank() ||
            jugglerPass.isBlank()


        setContent {
            JugglerPalTheme {
                MyPagerScreen()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onStop() {
        super.onStop()
        Wearable.getMessageClient(this).removeListener(this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            //tts.language = Locale.getDefault()
            tts.language = Locale.US
            tts.setSpeechRate(1.2f)      // 10% mais rápido
            tts.setPitch(1.0f)           // tom normal
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        metronome.release()
        sensorThread.quitSafely() // ➜ Novo: encerra a thread do sensor
        super.onDestroy()
    }


    private fun announceFlankCount(count: Int) {
        val text = "$count throws"
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "FLANK_COUNT")
    }

    private fun announceTime(count: Int) {
        val totalTenths = (count * 10).toInt()
        val minutes     = floor(totalTenths / 600f).toInt()
        val secondsTenths = totalTenths % (600)
        val seconds     = floor(secondsTenths / 10f).toInt()
        if (minutes>0){
            val text = "$minutes minutes and $seconds seconds"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TIME_SECONDS")
        }else{
            val text = "$seconds seconds"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TIME_SECONDS")
        }
        //val text = "$count seconds"
        //tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TIME_SECONDS")
    }

    private fun announceFinalFlankCount(count: Int) {
        val text = "throws: $count throws"
        tts.setSpeechRate(0.9f)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "FLANK_COUNT")
    }

    private fun announceFinalTime(count: Float) {
        val totalTenths = (count * 10).toInt()
        val minutes     = floor(totalTenths / 600f).toInt()
        val secondsTenths = totalTenths % (600)
        val seconds     = floor(secondsTenths / 10f).toInt()
        val tenth       = secondsTenths % 10
        tts.setSpeechRate(0.9f)
        if (minutes>0){
            val text = "time: $minutes minutes and $seconds point $tenth seconds"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TIME_SECONDS")
        }else{
            val text = "time: $seconds point $tenth seconds"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TIME_SECONDS")
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {

            // 1) Pedido de restauração vindo do mobile (tal como já tinhas)
            "/jugglerpal/restore" -> {
                val message = String(messageEvent.data)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        prefs.edit().putString("history", message).apply()
                        Log.d("WearMainAct", "Histórico restaurado com sucesso!")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "History Updated!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("WearMainAct", "Erro ao restaurar histórico", e)
                    }
                }
            }

            // 2) Pedido de fetch que o mobile vai enviar em onStart()
            "/jugglerpal/fetch_history" -> {
                // Lê o JSON que tens guardado no Wear
                val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val historyJson = prefs.getString("history", null) ?: return

                // Envia de volta para o nó que fez o pedido
                Wearable.getMessageClient(this)
                    .sendMessage(
                        messageEvent.sourceNodeId,
                        "/jugglerpal/history",   // mesmo path que o mobile trata
                        historyJson.toByteArray()
                    )
                    .addOnSuccessListener {
                        Log.d("WearMainAct", "Enviei history de volta ao mobile")
                    }
                    .addOnFailureListener { e ->
                        Log.e("WearMainAct", "Falha ao enviar history de volta", e)
                    }
            }

        }
    }

    /*override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/jugglerpal/restore") {
            val message = String(messageEvent.data)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("history", message).apply()

                    Log.d("WearMainAct", "Histórico restaurado com sucesso!")

                    launch(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "History Updated!", Toast.LENGTH_SHORT).show()


                    }
                } catch (e: Exception) {
                    Log.e("WearMainAct", "Erro ao restaurar histórico", e)
                }
            }
        }
    }*/


    // Pager com 4 páginas:
    // Página 0: MainScreen
    // Página 1: GroupedHistoryScreen
    // Página 2: FilteredHistoryScreen
    // Página 3: HistoryDetailScreen


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MyPagerScreen() {
        val pagerState = rememberPagerState(initialPage = 0)
        var selectedNote by remember { mutableStateOf<String?>(null) }
        var selectedHistoryItem by remember { mutableStateOf<HistoryItem?>(null) }
        var selectedMonth by remember { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()

        var showAccountDialog by remember { mutableStateOf(showAccountDialogOnStartup) }
        var showSettingsDialog by remember { mutableStateOf(false) }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }
                .collect { page ->
                    if (page < 0) {
                        // Isto raramente acontece, mas por segurança
                        pagerState.scrollToPage(0)
                    }
                }
        }

        HorizontalPager(
            count = 5,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = !isScreenBlocked
        ) { page ->
            when (page) {
                0 -> MainScreen(
                    accelerometerReading = accelerometerReading,
                    recordedRecords = recordedRecords,
                    /*minX = minX, maxX = maxX,
                    minY = minY, maxY = maxY,*/
                    //minZ = minZ, maxZ = maxZ,
                    isScreenBlocked = isScreenBlocked,
                    sensorSamples = lastSessionSamples,        // <- aqui
                    thresholdZ = thresholdZ,
                    onThresholdChange = { newTh ->
                        // Atualiza o estado
                        thresholdZ = newTh
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putFloat("thresholdZ", newTh)
                            .apply()
                    },
                    gapZ = gapZ,
                    onGapChange = { newgz ->
                        // Atualiza o estado
                        gapZ = newgz
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putFloat("gapZ", newgz)
                            .apply()
                    },
                    windowSize = windowSize,
                    onWindowSizeChange = { newwindowSize ->
                        // Atualiza o estado
                        windowSize = newwindowSize
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("windowSize", newwindowSize)
                            .apply()
                    },
                    bpm = bpm,
                    onBpmChange = { newbpm ->
                        // Atualiza o estado
                        bpm = newbpm
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("bpm", newbpm)
                            .apply()
                    },
                    throwsInterval = throwsInterval,
                    onThrowsIntervalChange = { newthrowsInterval ->
                        // Atualiza o estado
                        throwsInterval = newthrowsInterval
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("throwsInterval", newthrowsInterval)
                            .apply()
                    },
                    timeInterval = timeInterval,
                    onTimeIntervalChange = { newtimeInterval ->
                        // Atualiza o estado
                        timeInterval = newtimeInterval
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("timeInterval", newtimeInterval)
                            .apply()
                    },
                    tpsInterval = tpsInterval,
                    onTpsIntervalChange = { newtpsInterval ->
                        // Atualiza o estado
                        tpsInterval = newtpsInterval
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putFloat("tpsInterval", newtpsInterval)
                            .apply()
                    },
                    swapZ = swapZ,
                    onSwapZChange = { newswapZ ->
                        // Atualiza o estado
                        swapZ = newswapZ
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapZ", newswapZ)
                            .apply()
                    },
                    swapAudio = swapAudio,
                    onSwapAudioChange = { newswapAudio ->
                        // Atualiza o estado
                        swapAudio = newswapAudio
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapAudio", newswapAudio)
                            .apply()
                    },
                    swapCount = swapCount,
                    onSwapCountChange = { newswapCount ->
                        // Atualiza o estado
                        swapCount = newswapCount
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapCount", newswapCount)
                            .apply()
                    },
                    swapMetronome = swapMetronome,
                    onSwapMetronomeChange = { newswapMetronome ->
                        // Atualiza o estado
                        swapMetronome = newswapMetronome
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapMetronome", newswapMetronome)
                            .apply()
                    },
                    swapDisplay = swapDisplay,
                    onSwapDisplayChange = { newswapDisplay ->
                        // Atualiza o estado
                        swapDisplay = newswapDisplay
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapDisplay", newswapDisplay)
                            .apply()
                    },
                    swapGraph = swapGraph,
                    onSwapGraphChange = { newswapGraph ->
                        // Atualiza o estado
                        swapGraph = newswapGraph
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapGraph", newswapGraph)
                            .apply()
                    },
                    throwMultiplier = throwMultiplier,
                    onThrowMultiplierChange = { newTM ->
                        // Atualiza o estado
                        throwMultiplier = newTM
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("throwMultiplier", newTM)
                            .apply()
                    },
                    metronome = metronome,
                    resetAccelerometer = { resetAccelerometer() },
                    onIncrementLast = { incrementLastRecord() },
                    onDecrementLast = { decrementLastRecord() },
                    onResetCounter = { deleteLastRecord() },
                    onResetListWithNote = { note -> resetList(note) },
                    showSettingsDialog    = showSettingsDialog,
                    onOpenSettingsDialog = { showSettingsDialog = true },
                    onCloseSettingsDialog = { showSettingsDialog = false },
                    showAccountDialog     = showAccountDialog,
                    onOpenAccountDialog   = { showAccountDialog = true },
                    onCloseAccountDialog  = { showAccountDialog = false },
                    showDiffsDialog     = showDiffsDialog,
                    onOpenDiffsDialog  = { showDiffsDialog = true },
                    onCloseDiffsDialog  = { showDiffsDialog = false },
                    lastDiffs           = lastDiffs,
                    saveAll = saveAll,
                    onSaveAllChange = { saveAll = it },
                    onOpenGroupedHistory = {
                        scope.launch { pagerState.animateScrollToPage(1) }
                    }
                )
                1 -> GroupedHistoryScreen (
                    { note ->
                        selectedNote = note
                        scope.launch { pagerState.animateScrollToPage(2) }
                    },
                    swapCount = swapCount,
                    onSwapCountChange = { newswapCount ->
                        // Atualiza o estado
                        swapCount = newswapCount
                        // Persiste no prefs
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("swapCount", newswapCount)
                            .apply()
                    },
                    onSendHistoryToPhone = { historyJson ->
                        scope.launch(Dispatchers.IO) {
                            sendMessageToPhone("/jugglerpal/history", historyJson)
                        }
                    }
                )
                2 -> {
                    // 1) captura numa val imutável
                    val note = selectedNote

                    if (note != null) {
                        FilteredTrickHistoryScreen(
                            selectedNote      = note,
                            swapCount         = swapCount,
                            onSwapCountChange = { newswapCount ->
                                swapCount = newswapCount
                                getSharedPreferences("app_prefs", MODE_PRIVATE)
                                    .edit()
                                    .putInt("swapCount", newswapCount)
                                    .apply()
                            },
                            onMonthSelected   = { month ->
                                selectedMonth = month
                                scope.launch { pagerState.animateScrollToPage(3) }
                            }
                        )
                    } else {
                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text  = "No Trick Selected",
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }

                }
                3 -> {
                    if (selectedNote != null && selectedMonth != null) {
                        FilteredHistoryScreen(
                            selectedNote = selectedNote!!,
                            selectedMonth = selectedMonth!!
                        ) { historyItem ->
                            selectedHistoryItem = historyItem
                            scope.launch { pagerState.animateScrollToPage(4) }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No Month Selected", color = MaterialTheme.colors.primary)
                        }
                    }
                }
                4 -> {
                    if (selectedHistoryItem != null) {
                        HistoryDetailScreen(historyItem = selectedHistoryItem!!) {
                            selectedHistoryItem = null
                            scope.launch { pagerState.animateScrollToPage(3) }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No WorkOut Selected", color = MaterialTheme.colors.primary)
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun MainScreen(
        accelerometerReading: String,
        recordedRecords: List<ThrowRecord>,
        /*minX: Float, maxX: Float,
        minY: Float, maxY: Float,*/
        //minZ: Float, maxZ: Float,
        thresholdZ: Float,
        onThresholdChange: (Float) -> Unit,
        gapZ: Float,
        onGapChange: (Float) -> Unit,
        windowSize: Int,
        onWindowSizeChange: (Int) -> Unit,
        bpm: Int,
        onBpmChange: (Int) -> Unit,
        throwsInterval: Int,
        onThrowsIntervalChange: (Int) -> Unit,
        timeInterval: Int,
        onTimeIntervalChange: (Int) -> Unit,
        tpsInterval: Float,
        onTpsIntervalChange: (Float) -> Unit,
        swapZ: Int,
        onSwapZChange: (Int) -> Unit,
        swapAudio: Int,
        onSwapAudioChange: (Int) -> Unit,
        swapCount: Int,
        onSwapCountChange: (Int) -> Unit,
        swapMetronome: Int,
        onSwapMetronomeChange: (Int) -> Unit,
        swapDisplay: Int,
        onSwapDisplayChange: (Int) -> Unit,
        swapGraph: Int,
        onSwapGraphChange: (Int) -> Unit,
        throwMultiplier: Int,
        onThrowMultiplierChange: (Int) -> Unit,
        sensorSamples: List<SensorSample>,
        onIncrementLast: () -> Unit,
        onDecrementLast: () -> Unit,
        onResetCounter: () -> Unit,
        onResetListWithNote: (String) -> Unit,
        metronome: MetronomePlayer,
        showSettingsDialog: Boolean,
        onOpenSettingsDialog: () -> Unit,
        onCloseSettingsDialog: () -> Unit,
        showAccountDialog: Boolean,
        onOpenAccountDialog: () -> Unit,
        onCloseAccountDialog: () -> Unit,
        showDiffsDialog: Boolean,
        onOpenDiffsDialog: () -> Unit,
        onCloseDiffsDialog: () -> Unit,
        resetAccelerometer: () -> Unit,
        lastDiffs: List<Int>,
        saveAll: Boolean,
        onSaveAllChange: (Boolean) -> Unit,
        isScreenBlocked: Boolean,
        onOpenGroupedHistory: () -> Unit
    ) {


        val primaryColor = MaterialTheme.colors.primary.toArgb()
        val configuration = LocalConfiguration.current
        val screenHeightPx = configuration.screenHeightDp * LocalDensity.current.density

        val context = LocalContext.current
        var showNoteDialog by remember { mutableStateOf(false) }
        var showMultDialog by remember { mutableStateOf(false) }

        var noteText by rememberSaveable { mutableStateOf("") }

        val customKeys1 = listOf('(', ',', ')', '*','[',']', '<', '>', '0')
        val customKeys2 = listOf('1', '2', '3', '4','5','6', '7', '8', '9')
        val customKeys3 = listOf('a', 'b', 'c', 'd','e','f', 'g', 'h', 'i')
        val customKeys4 = listOf('j', 'k', 'l', 'm','n','o', 'p', 'q', 'r')
        val customKeys5 = listOf('s', 't', 'u', 'v','w','x', 'y', 'z', '!')

        val selectedIndices = remember { mutableStateListOf<Int>() }

        /*val customKeys1a = listOf('1', '2', '3')
        val customKeys1b = listOf('(', ',', ')', '*','x')
        val customKeys2a = listOf('4', '5', '6')
        val customKeys2b = listOf('[', ']', '<','>','p')
        val customKeys3a = listOf('7', '8', '9')
        val customKeys3b = listOf('a', 'b', 'c', 'd','e')
        val customKeys4a = listOf(' ', '0', '!')
        val customKeys4b = listOf('f', 'g', 'h', 'i','j')
        val customKeys5 = listOf('k','m','n','o','q','r','s','t')*/
        val items = remember { mutableStateListOf<HistoryItem>() }
        LaunchedEffect(Unit) {
            try {
                val savedItems = loadHistoryItems(context)
                items.clear()
                items.addAll(savedItems)
            } catch (e: Exception) {
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit().remove("history").apply()
                items.clear()
            }
        }
        // Carrega todas as notas históricas
        val allNotes = remember {
            loadHistoryItems(context).map { it.note }.distinct()
        }
        // Estado para controlar a abertura do dropdown
        var showdialog by remember { mutableStateOf(false) }
        val suggestions = remember(noteText) {
            allNotes.filter { noteText.isBlank() || it.startsWith(noteText, ignoreCase = true) }
        }

        //var showSettingsDialog by remember { mutableStateOf(false) }
        var showSensorDialog by remember { mutableStateOf(false) }

        //var showAccountDialog by remember { mutableStateOf(false) }
        //val jugglerName = items.firstOrNull()?.jugglerName ?: ""
        //val jugglerNickName = items.firstOrNull()?.jugglerNickName ?: ""
        //val jugglerPass = items.firstOrNull()?.jugglerPass ?: ""
        var newJugglerName by remember { mutableStateOf(jugglerName) }
        var newJugglerNickName by remember { mutableStateOf(jugglerNickName) }
        var newJugglerPass by remember { mutableStateOf(decryptPass(jugglerPass)) }

        val allFilled =
            newJugglerName.isNotBlank() &&
            newJugglerNickName.isNotBlank() &&
            newJugglerPass.length == 4  // ou o teu critério

        var swapAudioValue by rememberSaveable { mutableStateOf(swapAudio) }
        var swapCountValue by rememberSaveable { mutableStateOf(swapCount) }
        swapCountValue=swapCount

        var swapMetronomeValue by rememberSaveable { mutableStateOf(swapMetronome) }
        var swapDisplayValue by rememberSaveable { mutableStateOf(swapDisplay) }
        var swapGraphValue by rememberSaveable { mutableStateOf(swapGraph) }


        var bpmValue by rememberSaveable { mutableStateOf(bpm) }
        var throwsIntervalValue by rememberSaveable { mutableStateOf(throwsInterval) }
        var timeIntervalValue by rememberSaveable { mutableStateOf(timeInterval) }
        var tpsIntervalValue by rememberSaveable { mutableStateOf(tpsInterval) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            /*TimeText(
                modifier = Modifier.align(Alignment.TopCenter),
                timeTextStyle = MaterialTheme.typography.body2.copy(
                    color = MaterialTheme.colors.primary,
                    fontSize = 12.sp
                )
            )*/

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /*Text(
                    text = "x$throwMultiplier",
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable(enabled = !isScreenBlocked) {
                            // calcula o próximo valor entre 1 e 4
                            val next = (throwMultiplier % 2) + 1
                            onThrowMultiplierChange(next)
                        }
                        .padding(start = 4.dp)  // espacinhos, se quiseres
                )*/
                //AccelerometerDisplay(reading = accelerometerReading)
                val bgColor = when (isAccelReliable.coerceIn(0, 3)) {
                    1 -> MaterialTheme.colors.primaryVariant
                    2 -> Color.Green
                    3 -> MaterialTheme.colors.primary
                    else -> Color.Red
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.08f)
                        .padding(horizontal = 65.dp, vertical = 0.dp)
                        .clip(RoundedCornerShape(0.dp)) // aplica cantos arredondados
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isScreenBlocked) {
                                //onOpenDiffsDialog()
                            }
                            .padding(top = 6.dp),
                        textAlign = TextAlign.Center,
                        color = bgColor,
                        fontSize = 13.sp,
                        text = "Workout"//accelerometerReading
                    )
                }
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.01f)
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                }*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.15f)
                        .padding(horizontal = 34.dp, vertical = 0.dp)
                        .clip(RoundedCornerShape(0.dp)) // aplica cantos arredondados
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Button(
                        onClick = {
                            swapDisplayValue = (swapDisplayValue + 1) % 2
                            onSwapDisplayChange(swapDisplayValue)
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(1f),
                        shape = CircleShape,
                        enabled = !isScreenBlocked,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        if (swapDisplayValue == 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit_content),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapDisplayValue == 1) {
                            if (swapGraphValue==0){
                                Icon(
                                    painter = painterResource(id = R.drawable.wos_attempts),
                                    contentDescription = "Graph",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                            if (swapGraphValue==1){
                                Icon(
                                    painter = painterResource(id = R.drawable.wos_histogram),
                                    contentDescription = "Graph",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                            if (swapGraphValue==2){
                                Icon(
                                    painter = painterResource(id = R.drawable.wos_progress),
                                    contentDescription = "Graph",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }

                    }
                    Button(
                        onClick = {
                            swapAudioValue = (swapAudioValue + 1) % 3
                            onSwapAudioChange(swapAudioValue)
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(1f),
                        shape = CircleShape,
                        enabled = !isScreenBlocked,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        if (swapAudioValue == 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.audio_volume_muted),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapAudioValue == 1) {
                            Icon(
                                painter = painterResource(id = R.drawable.audio_volume_vibrate),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapAudioValue == 2) {
                            Icon(
                                painter = painterResource(id = R.drawable.audio_volume_high),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                    Button(
                        onClick = {
                            swapCountValue = (swapCountValue + 1) % 3
                            onSwapCountChange(swapCountValue)
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(1f),
                        shape = CircleShape,
                        enabled = !isScreenBlocked,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        if (swapCount == 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.finger_counting),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapCount == 1) {
                            Icon(
                                painter = painterResource(id = R.drawable.chronometer_v2),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapCount == 2) {
                            Icon(
                                painter = painterResource(id = R.drawable.topspeed),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }

                    val throwsSteps = listOf(10, 20, 50, 100, 200, 500, 1000)
                    val timeSteps = listOf(10, 15, 30, 60, 120, 300, 600)
                    val tpsSteps =
                        listOf(4.5f, 4.6f, 4.7f, 4.8f, 4.9f, 5.0f, 5.1f, 5.2f, 5.3f, 5.4f, 5.5f)
                    if (swapCountValue == 1) { //time
                        Button(
                            onClick = {
                                val currentIndex = timeSteps.indexOf(timeIntervalValue)
                                    .let { if (it < 0) 0 else it }
                                timeIntervalValue = timeSteps[(currentIndex + 1) % timeSteps.size]
                                onTimeIntervalChange(timeIntervalValue)
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .weight(1f),
                            shape = CircleShape,
                            enabled = !isScreenBlocked,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                        ) {
                            Text(
                                text = "$timeIntervalValue\nsec",
                                color = MaterialTheme.colors.primary,
                                fontSize = 9.sp,
                                lineHeight = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()         // centra o texto no botão
                            )
                        }
                    }
                    if (swapCountValue == 0) { //throws
                        Button(
                            onClick = {
                                val currentIndex = throwsSteps.indexOf(throwsIntervalValue)
                                    .let { if (it < 0) 0 else it }
                                throwsIntervalValue =
                                    throwsSteps[(currentIndex + 1) % throwsSteps.size]
                                onThrowsIntervalChange(throwsIntervalValue)
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .weight(1f),
                            shape = CircleShape,
                            enabled = !isScreenBlocked,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                        ) {
                            Text(
                                text = "$throwsIntervalValue\nthrows",
                                color = MaterialTheme.colors.primary,
                                fontSize = 8.sp,
                                lineHeight = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()         // centra o texto no botão
                            )
                        }
                    }
                    if (swapCountValue == 2) { //throws per second
                        Button(
                            onClick = {
                                val currentIndex =
                                    tpsSteps.indexOf(tpsIntervalValue).let { if (it < 0) 0 else it }
                                tpsIntervalValue = tpsSteps[(currentIndex + 1) % tpsSteps.size]
                                onTpsIntervalChange(tpsIntervalValue)
                            },
                            modifier = Modifier
                                .size(20.dp)
                                .weight(1f),
                            shape = CircleShape,
                            enabled = !isScreenBlocked,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                        ) {
                            Text(
                                text = "$tpsIntervalValue\ntps",
                                color = MaterialTheme.colors.primary,
                                fontSize = 9.sp,
                                lineHeight = 9.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()         // centra o texto no botão
                            )
                        }
                    }
                    Button(
                        onClick = {
                            val next = (throwMultiplier % 2) + 1
                            onThrowMultiplierChange(next)
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(1f),
                        shape = CircleShape,
                        enabled = !isScreenBlocked,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Text(text = "x$throwMultiplier", fontSize = 14.sp, color = MaterialTheme.colors.primary)
                    }
                    /*Text(
                    text = "x$throwMultiplier",
                    color = MaterialTheme.colors.primaryVariant,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable(enabled = !isScreenBlocked) {
                            // calcula o próximo valor entre 1 e 4
                            val next = (throwMultiplier % 2) + 1
                            onThrowMultiplierChange(next)
                        }
                        .padding(start = 4.dp)  // espacinhos, se quiseres
                )*/
                    /*if (recordedRecords.isNotEmpty()) {
                        Button(
                            onClick = onIncrementLast,
                            modifier = Modifier
                                .size(20.dp)
                                .weight(0.8f),
                            shape = CircleShape,
                            enabled = !isScreenBlocked,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                        ) {
                            Text(text = "+", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                        }
                    } else {
                        Text(
                            text = "",
                            Modifier
                                .size(20.dp)
                                .weight(0.8f),
                        )
                    }*/

                    /*Button(onClick = {
                        swapMetronomeValue = (swapMetronomeValue+1) % 2
                        onSwapMetronomeChange(swapMetronomeValue)
                        //if (running) metronome.start(300)
                        //else        metronome.stop()
                    },
                        modifier = Modifier
                            .size(20.dp).weight(0.8f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        if(swapMetronomeValue==0) { //off
                            Icon(
                                painter = painterResource(id = R.drawable.metronome_off),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }else{ //on
                            Icon(
                                painter = painterResource(id = R.drawable.metronome_on),
                                contentDescription = "Sliders Options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                    val steps = listOf(60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360)
                    Button(
                        onClick = {
                            val currentIndex = steps.indexOf(bpmValue).let { if (it < 0) 0 else it }
                            bpmValue = steps[(currentIndex + 1) % steps.size]
                            onBpmChange(bpmValue)
                        },
                        modifier = Modifier
                            .size(24.dp).weight(0.8f),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Text(
                            text = "$bpmValue\nbpm",
                            color = MaterialTheme.colors.primary,
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()         // centra o texto no botão
                        )
                    }*/

                }


                /*Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "X: min ${"%.1f".format(minX)}  max ${"%.1f".format(maxX)}",
                        color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    Text(text = "Y: min ${"%.1f".format(minY)}  max ${"%.1f".format(maxY)}",
                        color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    Text(
                        text = "  max ${"%.0f".format(density)}  th ${"%.0f".format(screenHeightPx)}",
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 8.sp,
                        modifier = Modifier
                            .clickable { }
                    )
                }*/
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.001f)
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                }*/
                //CountsHistogramAccWorkout(recordedRecords)
                //CountsHistogramAcc(items)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.54f)
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color(0xFF444444)),
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isScreenBlocked) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                            ) {
                            Text(
                                text = accelerometerReading,
                                fontSize = 24.sp,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                    if (!isScreenBlocked) {

                        val reversedList = recordedRecords.reversed()

                        if (swapDisplay==1){

                            /*Box(
                                modifier = Modifier
                                    .weight(0.16f)
                                    .fillMaxHeight(0.76f)
                                    .padding(start = 12.dp, end = 0.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF444444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = onResetCounter,
                                        modifier = Modifier
                                            .size(24.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.trash),
                                            contentDescription = "Trash",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }

                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .size(24.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.clear),
                                            contentDescription = "Clear",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }
                                }
                            }*/

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .padding(end=8.dp)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                if (reversedList.isNotEmpty()) {
                                    val fakeHistoryItem = HistoryItem(
                                        dateTime = "now",
                                        throwRecords = recordedRecords.toList(),
                                        note = "",
                                        attempts = 0,
                                        totalThrows = 0,
                                        maxThrow = 0,
                                        averageThrow = 0f,
                                        tps = 0f
                                    )
                                    var totalDrag by remember { mutableStateOf(0f) }

                                    val graphTitle = when (swapCountValue) {
                                        0 -> when (swapGraphValue) {
                                            0 -> "Attempts (thr)"
                                            1 -> "Sorted Attempts (thr)"
                                            2 -> "Estimated Progress (thr)"
                                            else -> ""
                                        }
                                        1 -> when (swapGraphValue) {
                                            0 -> "Attempts (sec)"
                                            1 -> "Sorted Attempts (sec)"
                                            2 -> "Estimated Progress (sec)"
                                            else -> ""
                                        }
                                        2 -> when (swapGraphValue) {
                                            0 -> "Attempts (tps)"
                                            1 -> "Sorted Attempts (tps)"
                                            2 -> "Estimated Progress (tps)"
                                            else -> ""
                                        }
                                        else -> ""
                                    }

                                    Text(
                                        text = graphTitle,
                                        modifier = Modifier.padding(top = 2.dp, bottom=6.dp),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.primary
                                    )


                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f) // ocupa o espaço disponível no meio
                                            .pointerInput(Unit) {
                                                detectHorizontalDragGestures(
                                                    onDragEnd = {
                                                        if (totalDrag > 5) {
                                                            swapGraphValue =
                                                                (swapGraphValue - 1 + 3) % 3
                                                        } else if (totalDrag < -5) {
                                                            swapGraphValue =
                                                                (swapGraphValue + 1) % 3
                                                        }
                                                        onSwapGraphChange(swapGraphValue)
                                                        totalDrag = 0f
                                                    },
                                                    onHorizontalDrag = { _, dragAmount ->
                                                        totalDrag += dragAmount
                                                    }
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (swapCountValue==0){
                                            when (swapGraphValue) {
                                                0 -> AttemptsBarChart(recordedRecords)
                                                1 -> AttemptsBarSortChart(recordedRecords)
                                                2 -> ProgressEstimationChart(listOf(fakeHistoryItem))
                                            }
                                        }
                                        if (swapCountValue==1){
                                            when (swapGraphValue) {
                                                0 -> AttemptsDurationBarChart(recordedRecords)
                                                1 -> AttemptsDurationBarSortChart(recordedRecords)
                                                2 -> ProgressEstimationChart(listOf(fakeHistoryItem))
                                            }
                                        }
                                        if (swapCountValue==2){
                                            when (swapGraphValue) {
                                                0 -> AttemptsTpsBarChart(recordedRecords)
                                                1 -> AttemptsTpsBarSortChart(recordedRecords)
                                                2 -> ProgressEstimationChart(listOf(fakeHistoryItem))
                                            }
                                        }

                                    }

                                    Row(
                                        modifier = Modifier.padding(bottom = 4.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        repeat(3) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 4.dp)
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (swapGraphValue == index) MaterialTheme.colors.primary else Color.Gray
                                                    )
                                            )
                                        }
                                    }
                                }
                            }

                           /* Box(
                                modifier = Modifier
                                    .weight(0.16f)
                                    .fillMaxHeight(0.76f)
                                    .padding(start = 6.dp, end = 12.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF444444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = onIncrementLast,
                                        modifier = Modifier
                                            .size(24.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.club_add),
                                            contentDescription = "Add",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }
                                    Button(
                                        onClick = onDecrementLast,
                                        modifier = Modifier
                                            .size(24.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.club_sub),
                                            contentDescription = "Subtract",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }
                                }
                            }*/


                        }

                        if (swapDisplay==0){

                            Box(
                                modifier = Modifier
                                    .weight(0.16f)
                                    .fillMaxHeight(0.76f)
                                    .padding(start = 12.dp, end = 4.dp)
                                    .clip(RoundedCornerShape(0.dp))
                                    .background(Color(0xFF444444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = {
                                            onSaveAllChange(false)
                                            showNoteDialog = true
                                        },
                                        modifier = Modifier
                                            .size(20.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.save_arrow),
                                            contentDescription = "Trash",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }

                                    Button(
                                        onClick = onResetCounter,
                                        modifier = Modifier
                                            .size(20.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.trash),
                                            contentDescription = "Clear",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }
                                }
                            }

                            /*Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(start = 4.dp),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = {
                                        onSaveAllChange(false)
                                        showNoteDialog = true
                                    },
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(alignment = Alignment.CenterHorizontally),
                                    shape = RectangleShape,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                ) {
                                    Text(text = "S", fontSize = 14.sp, color = MaterialTheme.colors.primary)
                                }
                                Button(
                                    onClick = onResetCounter,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(alignment = Alignment.CenterHorizontally),
                                    shape = RectangleShape,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                ) {
                                    Text(text = "✕", fontSize = 14.sp, color = MaterialTheme.colors.primary)
                                }
                            }*/


                            Column(
                                modifier = Modifier
                                    .weight(0.68f)
                                    .padding(start=4.dp, end=4.dp, top=0.dp, bottom=4.dp)
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                if (reversedList.isNotEmpty()) {
                                    Text(
                                        text = "Attempts",
                                        modifier = Modifier.padding(top = 2.dp, bottom=2.dp),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.primary
                                    )
                                    reversedList.forEachIndexed { index, record ->
                                        val originalIndex = recordedRecords.lastIndex - index
                                        val isSelected = selectedIndices.contains(originalIndex)
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 2.dp)
                                                .fillMaxWidth()
                                                .background(if (isSelected) Color(0x44FFFFFF) else Color.Transparent)
                                                .clickable {
                                                    if (isSelected) {
                                                        selectedIndices.remove(originalIndex)
                                                    } else {
                                                        selectedIndices.add(originalIndex)
                                                    }
                                                },
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "${reversedList.size - index}:",
                                                modifier = Modifier.weight(0.16f),
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Start
                                            )
                                            Text(
                                                text = "${"%4d".format(record.count)}t",
                                                modifier = Modifier.weight(0.28f),
                                                color = Color.White,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = if (record.durationSeconds > 600)
                                                    record.durationSeconds.toTimeString()
                                                else
                                                    "%.1f".format(record.durationSeconds) + "s",
                                                modifier = Modifier.weight(0.28f),
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = buildAnnotatedString {
                                                    append("%.1f".format(record.frequency))
                                                    withStyle(SpanStyle(fontSize = 8.sp)) {
                                                        append("tps")
                                                    }
                                                },
                                                modifier = Modifier.weight(0.28f),
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Right
                                            )
                                        }
                                    }

                                }




                            }
                            /*Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(end = 4.dp),
                                verticalArrangement = Arrangement.SpaceEvenly, // espaços iguais antes, entre e depois
                                horizontalAlignment = Alignment.CenterHorizontally // centraliza horizontalmente
                            ) {
                                Button(
                                    onClick = onIncrementLast,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .weight(0.5f),
                                    shape = CircleShape,
                                    enabled = !isScreenBlocked,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                ) {
                                    Text(text = "+", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                                }
                                Button(
                                    onClick = onDecrementLast,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .weight(0.5f),
                                    shape = CircleShape,
                                    enabled = !isScreenBlocked,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                ) {
                                    Text(text = "-", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                                }
                            }*/
                            Box(
                                modifier = Modifier
                                    .weight(0.16f)
                                    .fillMaxHeight(0.76f)
                                    .padding(start = 4.dp, end = 12.dp)
                                    .clip(RoundedCornerShape(0.dp))
                                    .background(Color(0xFF444444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = onIncrementLast,
                                        modifier = Modifier
                                            .size(20.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.club_add),
                                            contentDescription = "Add",
                                            modifier = Modifier.size(20.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }
                                    Button(
                                        onClick = onDecrementLast,
                                        modifier = Modifier
                                            .size(24.dp),
                                        shape = RectangleShape,
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.club_sub),
                                            contentDescription = "Subtract",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color(0xFFFFA500)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.01f)
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                }*/
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.15f)
                        .clip(RoundedCornerShape(0.dp))
                        .background(Color.Black)
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    //contentPadding = PaddingValues(horizontal = 30.dp)
                ) {
                    //StatisticsDisplay(recordedRecords = recordedRecords)
                    val counts    = recordedRecords.map { it.count }.ifEmpty { listOf(0) }
                    val durations = recordedRecords.map { it.durationSeconds }.ifEmpty { listOf(0f) }
                    val tpsValues = recordedRecords.map { it.frequency }.ifEmpty { listOf(0f) }
                    val attempts = counts.size.takeIf { it > 0 } ?: 0


                    // Contadores
                    val lastCount      = counts.last()
                    val sumCount       = counts.sum()
                    val maxCount       = counts.maxOrNull()!!
                    val avgCount       = counts.average().toFloat()
                    val avgBestCount3  = counts.sortedDescending().take(3).average().toFloat()
                    val avgBestCount5  = counts.sortedDescending().take(5).average().toFloat()
                    val avgBestCount10 = counts.sortedDescending().take(10).average().toFloat()

                    // Durações
                    val lastDuration       = durations.last()
                    val sumDuration        = durations.sum()
                    val maxDuration        = durations.maxOrNull()!!
                    val avgDuration        = durations.average().toFloat()
                    val avgBestDuration3   = durations.sortedDescending().take(3).average().toFloat()
                    val avgBestDuration5   = durations.sortedDescending().take(5).average().toFloat()
                    val avgBestDuration10  = durations.sortedDescending().take(10).average().toFloat()

                    // TPS (throws per second)
                    val lastTps        = tpsValues.last()
                    val maxTps         = tpsValues.maxOrNull()!!
                    val avgTps         = tpsValues.average().toFloat()
                    val avgBestTps3    = tpsValues.sortedDescending().take(3).average().toFloat()
                    val avgBestTps5    = tpsValues.sortedDescending().take(5).average().toFloat()
                    val avgBestTps10   = tpsValues.sortedDescending().take(10).average().toFloat()

                    // Cria uma Row para exibir os outros valores, cada um centralizado
                    /*Row(
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {*/
                    if(swapCount==0) {
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("runs\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("$attempts")
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("total\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("$sumCount")
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("last\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("$lastCount")
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("max\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("$maxCount")
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("avg\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("%.1f".format(avgCount))
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("b03\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("%.1f".format(avgBestCount3))
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("b05\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("%.1f".format(avgBestCount5))
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 9.sp)) {
                                        append("b10\n")
                                    }
                                    withStyle(style = SpanStyle(fontSize = 12.sp)) {
                                        append("%.1f".format(avgBestCount10))
                                    }
                                },
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(lineHeight = 12.sp)
                            )
                        }




                    }
                    if(swapCount==1) {
                        item {
                            Text(
                                text = "runs\n${attempts}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "total\n${"%.1f".format(sumDuration)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "last\n${"%.1f".format(lastDuration)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "max\n${"%.1f".format(maxDuration)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "avg\n${"%.1f".format(avgDuration)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "b03\n${"%.1f".format(avgBestDuration3)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "b05\n${"%.1f".format(avgBestDuration5)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "b10\n${"%.1f".format(avgBestDuration10)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                    }
                    if(swapCount==2) {
                        item {
                            Text(
                                text = "runs\n${attempts}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "total\n${"%.1f".format(avgTps)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "last\n${"%.1f".format(lastTps)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "max\n${"%.1f".format(maxTps)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "avg\n${"%.1f".format(avgTps)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "b03\n${"%.1f".format(avgBestTps3)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "b05\n${"%.1f".format(avgBestTps5)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                        item {
                            Text(
                                text = "b10\n${"%.1f".format(avgBestTps10)}",
                                modifier = Modifier.fillParentMaxWidth(0.2f),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                        }
                    }
                }
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.01f)
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                }*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.08f)
                        .padding(horizontal = 70.dp, vertical = 0.dp)
                        .clip(RoundedCornerShape(12.dp)) // aplica cantos arredondados
                        .background(color = Color.Black),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                }
            }

            Button(
                onClick = { onOpenSettingsDialog() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 2.dp)
                    .padding(start = 48.dp)
                    .size(22.dp),
                shape = CircleShape,
                enabled = !isScreenBlocked,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.settings02),
                    contentDescription = "Sliders Options",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colors.primary
                )
                //Text("DEL\nLAST", color = Color.White, fontSize = 10.sp, lineHeight = 10.sp)
            }

            Button(
                onClick = {
                    onSaveAllChange(true)
                    showNoteDialog = true
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 2.dp)
                    .padding(end = 48.dp)
                    .size(22.dp),
                shape = CircleShape,
                enabled = !isScreenBlocked,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save02),
                    contentDescription = "Save Workout",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colors.primary
                )
            }

            Button(
                onClick = onOpenGroupedHistory,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 2.dp)
                    .size(22.dp),
                shape = CircleShape,
                enabled = !isScreenBlocked,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.broken_arrow),
                    contentDescription = "Save Workout",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colors.primary
                )
            }

            /*Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 22.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Texto pequeno acima do botão
                Text(
                    text = if (swapValue == 0) "  Watch Up   " else "Watch Down",
                    fontSize = 7.sp,
                    color = MaterialTheme.colors.primary
                )

                // Botão com ícone
                Button(
                    onClick = onDecrementLast, /*{
                        swapValue = (swapValue + 1) % 2
                        onSwapZChange(swapValue)
                    },*/
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.watchswap),
                        contentDescription = "Swap Orientation",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }*/

            /*if(recordedRecords.isNotEmpty()) {
                Button(
                    onClick = onDecrementLast,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = 22.dp, horizontal = 23.dp)
                        .size(40.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "−", fontSize = 22.sp, color = Color.White)
                }
                Button(
                    onClick = onIncrementLast,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(vertical = 22.dp, horizontal = 23.dp)
                        .size(40.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "+", fontSize = 20.sp, color = Color.White)
                }
            }*/

            /*Button(
                onClick = onResetCounter,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 4.dp, horizontal = 0.dp)
                    .size(20.dp),
                //shape = CircleShape,
                enabled = !isScreenBlocked,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Text(text = "✕", fontSize = 14.sp, color = MaterialTheme.colors.primary)
            }*/
        }

        if (showSettingsDialog) {
            Dialog(onDismissRequest = { onCloseSettingsDialog() }) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    color = Color.Black
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Spacer(modifier = Modifier.height(8.dp))



                        Button(
                            onClick = {showSensorDialog=true},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Sensor")
                        }

                        Button(
                            onClick = { onOpenAccountDialog() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Account")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onCloseSettingsDialog() },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Box(
                                Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✕", color = Color.White, fontSize = 14.sp)
                            }
                        }

                    }
                }
            }
        }
        if (showAccountDialog) {

            Dialog(onDismissRequest = { if (allFilled) onCloseAccountDialog() }) {
                //var newJugglerName by remember { mutableStateOf(jugglerName) }
                //var newJugglerNickName by remember { mutableStateOf(jugglerNickName) }
                //var newJugglerPass by remember { mutableStateOf(decryptPass(jugglerPass)) }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    color = Color.Black
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newJugglerName,
                            onValueChange = { newValue ->
                                if (newValue.length <= 15) {
                                    newJugglerName = newValue
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            textStyle = LocalTextStyle.current.copy(
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp
                            ),
                            label = {
                                Text(
                                    text = "Name",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Name",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor            = MaterialTheme.colors.onSurface,
                                cursorColor          = MaterialTheme.colors.primary,
                                focusedBorderColor   = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.primary,      // mesma cor quando desfocado
                                backgroundColor      = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newJugglerNickName,
                            onValueChange = { newValue ->
                                if (newValue.length <= 5) {
                                    newJugglerNickName = newValue
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            textStyle = LocalTextStyle.current.copy(
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp
                            ),
                            label = {
                                Text(
                                    text = "Nickname",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Nickname",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor            = MaterialTheme.colors.onSurface,
                                cursorColor          = MaterialTheme.colors.primary,
                                focusedBorderColor   = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.primary,      // mesma cor quando desfocado
                                backgroundColor      = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //val focusManager = LocalFocusManager.current
                        val keyboardController = LocalSoftwareKeyboardController.current
                        val isValid = newJugglerPass.length == 4

                        OutlinedTextField(
                            value = newJugglerPass,
                            onValueChange = { newValue ->
                                newJugglerPass = newValue.filter { it.isDigit() }.take(4)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction    = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (isValid) {
                                        keyboardController?.hide()
                                    }
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            textStyle = LocalTextStyle.current.copy(
                                color = MaterialTheme.colors.primary,
                                fontSize = 14.sp
                            ),
                            label = {
                                Text(
                                    text = "4 Digit Password",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "4 Digit Password",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor            = MaterialTheme.colors.onSurface,
                                cursorColor          = MaterialTheme.colors.primary,
                                focusedBorderColor   = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.primary,      // mesma cor quando desfocado
                                backgroundColor      = Color.Transparent
                            )
                        )


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    if (allFilled) onCloseAccountDialog()
                                },
                                enabled = allFilled,
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = if (allFilled) Color.Red else Color.DarkGray
                                )
                                //colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                            ) {
                                Text(text = "CANCEL", color = Color.White, fontSize = 10.sp)
                            }
                            Button(
                                onClick = {
                                    val offsets = listOf(7, 5, 3, 1)
                                    val encryptedPass = newJugglerPass
                                        .mapIndexed { idx, c ->
                                            // converte o char para código ASCII, soma o offset e volta a char
                                            (c.code + (offsets.getOrElse(idx) { 0 })).toChar()
                                        }
                                        .joinToString("")
                                    val updatedItems = items.map { hi ->
                                        hi.copy(
                                            jugglerName     = newJugglerName,
                                            jugglerNickName = newJugglerNickName,
                                            jugglerPass     = encryptedPass
                                        )
                                    }
                                    val gson = Gson()
                                    val json = gson.toJson(updatedItems)

                                    // GUARDA
                                    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                        .edit()
                                        .putString("history", json) // <---- usa "history" (sem _json)
                                        .apply()

                                    items.clear()
                                    items.addAll(updatedItems)

                                    val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
                                    prefs.edit()
                                        .putString("juggler_name", newJugglerName)
                                        .putString("juggler_nickname", newJugglerNickName)
                                        .putString("juggler_pass", encryptedPass)
                                        .apply()
                                    jugglerName = newJugglerName
                                    jugglerNickName = newJugglerNickName
                                    jugglerPass     = encryptedPass

                                    onCloseAccountDialog()
                                },
                                enabled = allFilled,
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = if (allFilled) Color(0xFF006400) else Color.DarkGray
                                )
                            ) {
                                Text(text = "SAVE", color = Color.White, fontSize = 10.sp)
                            }

                        }
                    }
                }
            }
        }
        if (showSensorDialog) {
            // Valor temporário para o slider
            var sliderThresholdValue by rememberSaveable { mutableStateOf(thresholdZ) }
            var sliderGapValue by rememberSaveable { mutableStateOf(gapZ) }
            var sliderWindowSizeValue by rememberSaveable { mutableStateOf(windowSize) }

            //var swapValue by rememberSaveable { mutableStateOf(swapZ) }
            var showSensorInfoDialog by remember { mutableStateOf(false) }


            Dialog(onDismissRequest = { showSensorDialog = false }) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                    color = Color.Black
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(((screenHeightPx-384)/4).toInt().dp))
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Force: ${"%.0f".format(sliderThresholdValue)}%",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            // botão “<”
                            Button(
                                onClick = {
                                    val newVal = (sliderThresholdValue - 5f).coerceIn(5f, 100f)
                                    sliderThresholdValue = newVal
                                },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Text("<")
                            }

                            // o teu Slider existente, mas agora com weight para ocupar o meio
                            Slider(
                                value = sliderThresholdValue,
                                onValueChange = {
                                    sliderThresholdValue = it
                                },
                                valueRange = 5f..100f,
                                steps = 18,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(22.dp)
                                    .padding(horizontal = 4.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colors.primary,
                                    activeTrackColor = MaterialTheme.colors.primary,
                                    inactiveTrackColor = MaterialTheme.colors.primaryVariant
                                )
                            )

                            // botão “>”
                            Button(
                                onClick = {
                                    val newVal = (sliderThresholdValue + 5f).coerceIn(5f, 100f)
                                    sliderThresholdValue = newVal
                                },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Text(">")
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Acuity: ${"%.0f".format(sliderGapValue)}%",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            // botão de decremento
                            Button(
                                onClick = {
                                    val newVal = (sliderGapValue - 5f).coerceIn(0f, 100f)
                                    sliderGapValue = newVal
                                    onGapChange(newVal)
                                },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Text("<")
                            }

                            // slider propriamente dito
                            Slider(
                                value = sliderGapValue,
                                onValueChange = {
                                    sliderGapValue = it
                                    onGapChange(it)
                                },
                                valueRange = 0f..100f,
                                steps = 19,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(22.dp)
                                    .padding(horizontal = 4.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colors.primary,
                                    activeTrackColor = MaterialTheme.colors.primary,
                                    inactiveTrackColor = MaterialTheme.colors.primaryVariant
                                )
                            )

                            // botão de incremento
                            Button(
                                onClick = {
                                    val newVal = (sliderGapValue + 5f).coerceIn(0f, 100f)
                                    sliderGapValue = newVal
                                    onGapChange(newVal)
                                },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Text(">")
                            }
                        }


                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Smooth: ${sliderWindowSizeValue}",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            // botão “<”
                            Button(
                                onClick = {
                                    val newVal = (sliderWindowSizeValue - 5).coerceIn(0, 100)
                                    sliderWindowSizeValue = newVal
                                },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Text("<")
                            }

                            // o teu Slider existente, mas agora com weight para ocupar o meio
                            Slider(
                                value = sliderWindowSizeValue.toFloat(),
                                onValueChange = {
                                    val stepped = (it / 5f).roundToInt() * 5
                                    sliderWindowSizeValue = stepped
                                },
                                valueRange = 0f..100f,
                                steps = 19,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(22.dp)
                                    .padding(horizontal = 4.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colors.primary,
                                    activeTrackColor = MaterialTheme.colors.primary,
                                    inactiveTrackColor = MaterialTheme.colors.primaryVariant
                                )
                            )

                            // botão “>”
                            Button(
                                onClick = {
                                    val newVal = (sliderWindowSizeValue + 5).coerceIn(0, 100)
                                    sliderWindowSizeValue = newVal
                                },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Text(">")
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))



                        /*Text(
                            text = if (swapValue == 0) "  face Up   " else "face down",
                            fontSize = 7.sp,
                            modifier = Modifier.padding(bottom=4.dp),
                            color = MaterialTheme.colors.primary
                        )*/
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 0.dp, horizontal = 22.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment  = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showSensorDialog = false },
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✕", color = Color.White, fontSize = 14.sp)
                                }
                            }
                            /*Button(
                                onClick = {
                                    //swapValue = (swapValue+1) % 2
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.watchswap),
                                    contentDescription = "Swap Orientation",
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colors.primary
                                )
                            }*/
                            Button(
                                onClick = {
                                    showSensorInfoDialog=true
                                },
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.info),
                                    contentDescription = "Sensor Info",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                            }
                            Button(
                                onClick = {
                                    onThresholdChange(sliderThresholdValue)
                                    onWindowSizeChange(sliderWindowSizeValue)
                                    onGapChange(sliderGapValue)
                                    //onSwapZChange(swapValue)
                                    showSensorDialog = false
                                },
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF006400)),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF006400))
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✔", color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
            if (showSensorInfoDialog) {

                Dialog(onDismissRequest = { showSensorInfoDialog = false }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp),
                        color = Color.Black
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(((screenHeightPx-384)/4).toInt().dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            val statusPainter = when (isAccelReliable) {
                                3 -> painterResource(id = R.drawable.emoji_laughing)
                                2 -> painterResource(id = R.drawable.emoji_smile)
                                1 -> painterResource(id = R.drawable.emoji_neutral)
                                else -> painterResource(id = R.drawable.emoji_dizzy)
                            }
                            val bgColor = when (isAccelReliable.coerceIn(0,3)) {
                                1 -> Color.Yellow
                                2 -> Color.Green
                                3 -> MaterialTheme.colors.primary
                                else -> Color.Red
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,  // Alinha ícone e texto verticalmente
                                horizontalArrangement = Arrangement.Center,       // (opcional) centraliza o conteúdo na largura disponível
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Status: ",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.primary
                                )
                                Icon(
                                    painter = statusPainter,
                                    contentDescription = "Sensor Info",
                                    modifier = Modifier.size(20.dp),
                                    tint = bgColor
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            accelPropertiesState.forEach { (label, value) ->
                                Text(
                                    text = "$label: ${value.toString().take(6)}",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.body2
                                        .copy(fontSize = 10.sp, lineHeight = 12.sp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(
                                text = "min z: ${"%.1f".format(minZ)}; max Z: ${"%.1f".format(maxZ)}",
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.padding(vertical = 0.dp),
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(

                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Button(
                                    onClick = { showSensorInfoDialog = false },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                                ) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("✕", color = Color.White, fontSize = 14.sp)
                                    }
                                }
                                Button(
                                    onClick = {
                                        minZ=100f
                                        maxZ=-100f
                                        resetAccelerometer()
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                                ) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("↺", color = Color.White, fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }







        if (showNoteDialog) {
            Dialog(onDismissRequest = { showNoteDialog = false }) {
                // Surface sem padding interno, ocupa toda a largura disponível
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Black,
                ) {
                    Column(modifier = Modifier.padding(0.dp)) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { showdialog = true  },
                                modifier = Modifier
                                    .height(20.dp)
                                    .background(MaterialTheme.colors.primary),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Trick",color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                        /*TextButton(
                            onClick = { showdialog = true },
                            modifier = Modifier
                                .fillMaxWidth().padding(0.dp)
                                // elimina a altura mínima que o Button coloca por defeito:
                                .defaultMinSize(minHeight = 0.dp, minWidth = 0.dp),
                            contentPadding = PaddingValues(0.dp),

                        ) {
                            Text(
                                text = "Trick",
                                fontSize = 17.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth().padding(0.dp)
                                // já não precisa de padding extra aqui
                            )
                        }*/

                        Spacer(modifier = Modifier.height(((screenHeightPx-384)/4).toInt().dp))
                        BasicTextField(
                            value = noteText,
                            onValueChange = {  },
                            readOnly = true,
                            textStyle = TextStyle(color = MaterialTheme.colors.primary, fontSize = 14.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .padding(horizontal = 48.dp)
                        )



                        //Spacer(modifier = Modifier.height(2.dp))



                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            customKeys1.forEach { char ->
                                Button(
                                    onClick = { noteText += char },
                                    modifier = Modifier
                                        .size(19.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Transparent,
                                        contentColor = MaterialTheme.colors.primary
                                    ),
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = char.toString(),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }


                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            customKeys2.forEach { char ->
                                Button(
                                    onClick = { noteText += char },
                                    modifier = Modifier
                                        .size(19.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Transparent,
                                        contentColor = MaterialTheme.colors.primary
                                    ),
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(char.toString(), fontSize = 14.sp)
                                    }
                                }
                            }

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            customKeys3.forEach { char ->
                                Button(
                                    onClick = { noteText += char },
                                    modifier = Modifier
                                        .size(19.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Transparent,
                                        contentColor = MaterialTheme.colors.primary
                                    ),
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(char.toString(), fontSize = 14.sp)
                                    }
                                }
                            }

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            customKeys4.forEach { char ->
                                Button(
                                    onClick = { noteText += char },
                                    modifier = Modifier
                                        .size(19.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Transparent,
                                        contentColor = MaterialTheme.colors.primary
                                    ),
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(char.toString(),  fontSize = 14.sp)
                                    }
                                }
                            }


                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            customKeys5.forEach { char ->
                                Button(
                                    onClick = { noteText += char },
                                    modifier = Modifier
                                        .size(19.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Transparent,
                                        contentColor = MaterialTheme.colors.primary
                                    ),
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(char.toString(),  fontSize = 14.sp)
                                    }
                                }
                            }


                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botões Cancelar / OK
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { showNoteDialog = false },
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✕", color = Color.White, fontSize = 14.sp)
                                }
                            }
                            Button(
                                onClick = { noteText += " " },
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("␣", color = Color.White, fontSize = 14.sp)
                                }
                            }
                            Button(
                                onClick = {
                                    if (noteText.isNotEmpty()) noteText = noteText.dropLast(1)
                                },
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("⌫", color = Color.White, fontSize = 14.sp)
                                }
                            }

                            Button(
                                onClick = {
                                    if (saveAll || selectedIndices.isNotEmpty()) {
                                        if (saveAll) {
                                            onResetListWithNote(noteText)
                                        } else {
                                            saveSelectedEntries(selectedIndices, noteText)
                                        }
                                        noteText = ""
                                        selectedIndices.clear()
                                        showNoteDialog = false
                                    }
                                },
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF006400)),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF006400))
                            ) {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✔", color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }

                        if (showdialog) {
                            Dialog(onDismissRequest = { showdialog = false }) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    color = Color.DarkGray,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            // altura máxima para permitir scroll
                                            .heightIn(max = 200.dp)
                                            .verticalScroll(rememberScrollState())
                                            .padding(8.dp)
                                    ) {
                                        suggestions.forEach { suggestion ->
                                            Text(
                                                text = suggestion,
                                                color = MaterialTheme.colors.primary,
                                                fontSize = 14.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        noteText = suggestion
                                                        showdialog = false
                                                    }
                                                    .padding(vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { showdialog = false },
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .size(24.dp),
                                            shape = CircleShape,
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                                        ) {
                                            Text("✕", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showDiffsDialog) {
            Dialog(
                onDismissRequest = {
                    minZ=100f
                    maxZ=-100f
                    onCloseDiffsDialog()
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Black,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(48.dp))

                        AttemptsBarChart(recordedRecords)
                        CountsHistogramWorkOut(recordedRecords)
                        val fakeHistoryItem = HistoryItem(
                            dateTime = "now",
                            throwRecords = recordedRecords.toList(),
                            note = "",
                            attempts = 0,
                            totalThrows = 0,
                            maxThrow = 0,
                            averageThrow = 0f,
                            tps = 0f
                        )
                        ProgressEstimationChart(listOf(fakeHistoryItem))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "min z: ${"%.1f".format(minZ)}; max Z: ${"%.1f".format(maxZ)}",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(vertical = 0.dp),
                            style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                    }

                    /*val grouped = lastDiffs
                        .groupingBy { it }
                        .eachCount()
                        .entries
                        .sortedBy { it.key }


                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Time interval",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn {

                            items(grouped) { (value, freq) ->
                                Text(
                                    text = "${value * 10}ms ($freq)",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }*/
                }
            }
        }

        /*if (showDiffsDialog) {
            AlertDialog(
                onDismissRequest = onCloseDiffsDialog,
                text = {
                    // Agrupa e ordena
                    val freqMap = lastDiffs.groupingBy { it }.eachCount()
                    val entries = freqMap.entries
                        .sortedBy { it.key }    // ordena pelo valor da diferença

                    // Mostra os pares
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Text(text = accelerometerReading)
                        entries.forEach { (bin, count) ->
                            val ms = bin * 10
                            Text(
                                text = "${ms}ms ($count)",
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                            )
                        }

                    }
                },
                confirmButton = {
                    TextButton(onClick = onCloseDiffsDialog) {
                        Text("OK")
                    }
                },
                backgroundColor = Color.Black,
                contentColor = MaterialTheme.colors.onSurface
            )
        }*/
    }


    @Composable
    fun GroupedHistoryScreen(
        onGroupSelected: (String) -> Unit,
        onSendHistoryToPhone: (String) -> Unit,
        swapCount: Int,
        onSwapCountChange: (Int) -> Unit
    ) {
        val context = LocalContext.current
        val items = remember { mutableStateListOf<HistoryItem>() }
        var swapCountValue by rememberSaveable { mutableStateOf(swapCount) }
        swapCountValue=swapCount

        LaunchedEffect(Unit) {
            try {
                val savedItems = loadHistoryItems(context)
                items.clear()
                items.addAll(savedItems)
            } catch (e: Exception) {
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit().remove("history").apply()
                items.clear()
            }
        }

        // Agrupa os itens pela nota
        val grouped = items.groupBy { it.note }
        val sortedGroups = grouped.entries
            .sortedByDescending { (_, historyList) ->
                historyList.sumOf { it.attempts }
            }
        //val jugglerName = items.firstOrNull()?.jugglerName ?: "Juggler"

        var isRenameJuggler by remember { mutableStateOf(false) }
        var newJugglerName by remember { mutableStateOf("") }
        //var jugglerName by remember { mutableStateOf(items.firstOrNull()?.jugglerName ?: "Juggler") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (grouped.isEmpty()) {
                Text(
                    text = "No Data",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {

                Row(
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {


                    Button(
                        onClick = {
                            swapCountValue = (swapCountValue + 1) % 2
                            onSwapCountChange(swapCountValue)
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                    ) {
                        if (swapCount == 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.finger_counting),
                                contentDescription = "Throws count",
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapCount == 1) {
                            Icon(
                                painter = painterResource(id = R.drawable.chronometer_v2),
                                contentDescription = "Time count",
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }

                    /*Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.bar_chart1),
                            contentDescription = "Throws count",
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                    Button(
                        onClick = {
                            swapCountValue = (swapCountValue + 1) % 2
                            onSwapCountChange(swapCountValue)
                        },
                        modifier = Modifier
                            .size(20.dp)
                            .weight(0.8f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent
                        ),
                    ) {
                        if (swapCount == 1) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar1),
                                contentDescription = "Throws count",
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                        if (swapCount == 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.sum),
                                contentDescription = "Time count",
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }*/

                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = jugglerName,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.body2.copy(fontSize = (30-jugglerName.length).coerceIn(10, 24).sp, lineHeight = 24.sp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Text(
                        text = "trick",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.49f),
                        style = MaterialTheme.typography.body2
                            .copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "runs",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.17f),
                        style = MaterialTheme.typography.body2
                            .copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "max",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.17f),
                        style = MaterialTheme.typography.body2
                            .copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "avg",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.17f),
                        style = MaterialTheme.typography.body2
                            .copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                }

                sortedGroups.forEach { (note, historyList) ->

                    val globalAttempts = historyList.sumOf { it.attempts }
                    val globalThrows = historyList.sumOf { it.totalThrows }
                    val globalDuration = historyList.sumOf { it.totalDuration.toDouble() }.toFloat()

                    val globalMaxThrow = historyList.maxOfOrNull { it.maxThrow } ?: 0
                    val globalAvgThrow = if (globalAttempts > 0) globalThrows.toDouble() / globalAttempts else 0f
                    val globalMaxDuration = historyList.maxOfOrNull { it.maxDuration } ?: 0f
                    val globalAvgDuration = if (globalAttempts > 0) globalDuration.toDouble() / globalAttempts else 0f


                    val globalTPS = if (globalDuration > 0.0) globalThrows.toDouble() / globalDuration else 0f



                    val hours = floor(globalDuration / 3600)
                    val minutes = (globalDuration % 3600) / 60
                    val seconds = globalDuration % 60


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp)
                            .clickable { onGroupSelected(note) }
                    ) {
                        /*Text(
                            text = "Trick: $note (${historyList.size} trng)",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.body2.copy(fontSize = 12.sp, lineHeight = 12.sp)
                        )*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = note,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(0.49f),
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )
                            Text(
                                text = "$globalAttempts",
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(0.17f),
                                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                            )

                            if (swapCountValue == 0) {
                                Text(
                                    text = "$globalMaxThrow",
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.weight(0.17f),
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                                )
                                Text(
                                    text = "${"%.1f".format(globalAvgThrow)}",
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.weight(0.17f),
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                                )
                            }
                            if (swapCountValue == 1) {
                                Text(
                                    text = globalMaxDuration.toTimeAnnotated(),
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.weight(0.17f),
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                                )
                                Text(
                                    text = globalAvgDuration.toFloat().toTimeAnnotated(),
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.weight(0.17f),
                                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                                )
                            }
                        }

                    }
                }

                //val items = remember { mutableStateListOf<HistoryItem>() }
                val globalAttempts = items.sumOf { it.attempts }
                val globalThrows = items.sumOf { it.totalThrows }
                val globalDuration = items.sumOf { it.totalDuration.toDouble() }.toFloat()
                val hours = floor(globalDuration / 3600)
                val minutes = (globalDuration % 3600) / 60
                val seconds = globalDuration % 60
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Other Stats",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=8.dp, bottom=4.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Attempts: ${globalAttempts}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Throws: ${globalThrows}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Time: ${"%.0f".format(hours)}h ${"%.0f".format(minutes)}m ${"%.0f".format(seconds)}s",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Spacer(modifier = Modifier.height(50.dp))

                /*Button(
                    onClick = {
                        //val context = LocalContext.current
                        //val historyItems = loadHistoryItems(context)
                        //val gson = Gson()
                        //val historyJson = gson.toJson(historyItems)
                        val gson = Gson()
                        val historyJson = gson.toJson(items)
                        onSendHistoryToPhone(historyJson)

                        onSendHistoryToPhone(historyJson)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Send")
                }*/
            }
        }
        if (isRenameJuggler) {
            AlertDialog(
                onDismissRequest = { isRenameJuggler = false },
                title = {
                    Text(
                        text = "Edit Juggler Name",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    OutlinedTextField(
                        value = newJugglerName,
                        onValueChange = { newJugglerName = it },
                        label = { Text("New Name", color = Color.White) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { isRenameJuggler = false },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                        ) {
                            Text(text = "CANCEL", color = Color.White, fontSize = 10.sp)
                        }
                        Button(
                            onClick = {
                                val updatedItems = items.map { it.copy(jugglerName = newJugglerName) }
                                val gson = Gson()
                                val json = gson.toJson(updatedItems)

                                // GUARDA
                                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("history", json) // <---- usa "history" (sem _json)
                                    .apply()

                                items.clear()
                                items.addAll(updatedItems)
                                //jugglerName = newJugglerName

                                isRenameJuggler = false
                            },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF006400))
                        ) {
                            Text(text = "SAVE", color = Color.White, fontSize = 10.sp)
                        }
                    }
                },
                backgroundColor = Color.Black
            )
        }

    }

    @Composable
    fun FilteredTrickHistoryScreen(
        selectedNote: String,
        onMonthSelected: (String) -> Unit,
        swapCount: Int,
        onSwapCountChange: (Int) -> Unit
    ) {



        val context = LocalContext.current
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = remember { Gson() }
        val listType = object : TypeToken<MutableList<HistoryItem>>() {}.type
        var swapCountValue by rememberSaveable { mutableStateOf(swapCount) }
        swapCountValue=swapCount

        val items = remember { mutableStateListOf<HistoryItem>() }
        LaunchedEffect(Unit) {
            try {
                items.clear()
                items.addAll(loadHistoryItems(context))
            } catch (_: Exception) {
                prefs.edit().remove("history").apply()
                items.clear()
            }
        }

        val dateFormatter = remember { SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()) }
        val monthFormatter = remember { SimpleDateFormat("MM-yy", Locale.getDefault()) }

        var showEditDialogF by remember { mutableStateOf(false) }
        var editedNoteText by rememberSaveable { mutableStateOf(selectedNote) }

        val monthlyGroups = items
            .filter { it.note == selectedNote }
            .groupBy { hi ->
                dateFormatter.parse(hi.dateTime)
                    ?.let { monthFormatter.format(it) }
                    ?: "??"
            }

        val sortedFiltered = monthlyGroups.entries
            .sortedByDescending { it.key }

        /*sortedFiltered.forEach { (month, list) ->
            val runs   = list.sumOf { it.attempts }
            val maxRun = list.maxOfOrNull { it.maxThrow } ?: 0
            val avgRun = if (list.isNotEmpty()) list.map { it.averageThrow }.average() else 0.0*/

        val totalEntries = items
            .filter { hi ->
                hi.note == selectedNote
            }

        val totalAttempts = totalEntries.sumOf { it.attempts }
        val totalThrows = totalEntries.sumOf { it.totalThrows }
        val totalDurationSum = totalEntries.sumOf { it.totalDuration.toDouble() }.toFloat()


        val hours = floor(totalDurationSum / 3600)
        val minutes = (totalDurationSum % 3600) / 60
        val seconds = totalDurationSum % 60

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (sortedFiltered.isEmpty()) {
                Text(
                    text = "No data in '$selectedNote'.",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        swapCountValue = (swapCountValue + 1) % 2
                        onSwapCountChange(swapCountValue)
                    },
                    modifier = Modifier
                        .size(20.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    if (swapCount == 0) {
                        Icon(
                            painter = painterResource(id = R.drawable.finger_counting),
                            contentDescription = "Throws count",
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                    if (swapCount == 1) {
                        Icon(
                            painter = painterResource(id = R.drawable.chronometer_v2),
                            contentDescription = "Time count",
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        /*.clickable {
                            editedNoteText = selectedNote
                            showEditDialogF = true
                        }*/
                        .padding(bottom = 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedNote,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.body2.copy(
                            fontSize = (30 - selectedNote.length).coerceIn(10, 24).sp,
                            lineHeight = 24.sp
                        )
                    )
                }

                // Cabeçalho da tabela
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Month",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.24f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "Days",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.19f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "Runs",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.19f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )

                    Text(
                        text = "Max",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.19f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)

                    )
                    Text(
                        text = "Avg",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.19f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)

                    )
                }

                // Linhas de cada mês
                sortedFiltered.forEach { (month, list) ->

                    val daysCount = list.map { it.dateTime.substringBefore(" ") }.toSet().size
                    val globalAttempts = list.sumOf { it.attempts }
                    val globalThrows = list.sumOf { it.totalThrows }
                    val globalDuration = list.sumOf { it.totalDuration.toDouble() }.toFloat()

                    val globalMaxThrow = list.maxOfOrNull { it.maxThrow } ?: 0
                    val globalAvgThrow = if (globalAttempts > 0) globalThrows.toDouble() / globalAttempts else 0f
                    val globalMaxDuration = list.maxOfOrNull { it.maxDuration } ?: 0f
                    val globalAvgDuration = if (globalAttempts > 0) globalDuration.toDouble() / globalAttempts else 0f

                    val globalTPS = if (globalDuration > 0.0) globalThrows.toDouble() / globalDuration else 0f

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMonthSelected(month) }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = month,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.24f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        Text(
                            text = daysCount.toString(),
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.19f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        Text(
                            text = globalAttempts.toString(),
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.19f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        if (swapCountValue == 0) {
                            Text(
                                text = globalMaxThrow.toString(),
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(0.19f),
                                style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                            )
                            Text(
                                text = String.format("%.1f", globalAvgThrow),
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(0.19f),
                                style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                            )
                        }
                        if (swapCountValue == 1) {
                            Text(
                                text = globalMaxDuration.toTimeAnnotated(),
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(0.19f),
                                style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                            )
                            Text(
                                text = globalAvgDuration.toFloat().toTimeAnnotated(),
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.weight(0.19f),
                                style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                            )
                        }

                    }
                }

                Text(
                    text = "Runs Distribution",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                CountsHistogram(totalEntries)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Runs Over X",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                CountsHistogramAcc(totalEntries)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Progress Estimation",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressEstimationChart(totalEntries)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Max Throws Progress",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=8.dp, bottom=0.dp),

                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                MaxThrowBarChart(totalEntries)

                Text(
                    text = "Avg Throws Progress",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                AverageThrowBarChart(totalEntries)
                Text(
                    text = "Other Stats",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=8.dp, bottom=4.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Attempts: ${totalAttempts}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Throws: ${totalThrows}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Time: ${"%.0f".format(hours)}h ${"%.0f".format(minutes)}m ${"%.0f".format(seconds)}s",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
            }
            Spacer(Modifier.height(50.dp))



            // Diálogo de edição em massa
            if (showEditDialogF) {
                EditTrickSurfaceDialog(
                    initialText = editedNoteText,
                    onSave = { newText ->
                        val updated = items.map { hi ->
                            if (hi.note == selectedNote) hi.copy(note = newText) else hi
                        }
                        prefs.edit()
                            .putString("history", gson.toJson(updated, listType))
                            .apply()
                        items.clear(); items.addAll(updated)
                        showEditDialogF = false
                    },
                    onCancel = {
                        showEditDialogF = false
                    }
                )
            }
        }
    }






    @Composable
    fun FilteredHistoryScreen(
        selectedNote: String,
        selectedMonth: String,
        onHistoryItemClicked: (HistoryItem) -> Unit
    ) {
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = remember { Gson() }
        val listType = object : TypeToken<MutableList<HistoryItem>>() {}.type

        // Estados para exibir diálogo de edição
        var showEditDialogF by remember { mutableStateOf(false) }
        var editedNoteText by rememberSaveable { mutableStateOf(selectedNote) }
        // Estado para confirmação de delete
        var showDeleteConfirmation by remember { mutableStateOf(false) }

        val dateFormatter = remember { SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()) }
        val monthFormatter = remember { SimpleDateFormat("MM-yy", Locale.getDefault()) }

        // Carrega e filtra itens
        val items = remember { mutableStateListOf<HistoryItem>() }
        LaunchedEffect(Unit) {
            try {
                items.clear(); items.addAll(loadHistoryItems(context))
            } catch (_: Exception) {
                prefs.edit().remove("history").apply()
                items.clear()
            }
        }
        val sortedFiltered = items
            .filter { hi ->
                hi.note == selectedNote &&
                        hi.dateTime.substring(3, 8) == selectedMonth
            }
            .sortedByDescending { dateFormatter.parse(it.dateTime) }

        val globalAttempts = sortedFiltered.sumOf { it.attempts }
        val globalThrows = sortedFiltered.sumOf { it.totalThrows }
        val globalDuration = sortedFiltered.sumOf { it.totalDuration.toDouble() }.toFloat()

        val globalMaxThrow = sortedFiltered.maxOfOrNull { it.maxThrow } ?: 0
        val globalAverage = if (globalAttempts > 0) globalThrows.toDouble() / globalAttempts else 0.0
        val globalMaxDuration = sortedFiltered.maxOfOrNull { it.maxDuration } ?: 0
        val globalAvgDuration = if (globalAttempts > 0) globalDuration.toDouble() / globalAttempts else 0.0

        val totalDurationSum = sortedFiltered.sumOf { hi ->
            if (hi.tps > 0f) hi.totalThrows.toDouble() / hi.tps.toDouble() else 0.0
        }
        val globalTPS = if (totalDurationSum > 0.0) globalThrows.toDouble() / totalDurationSum else 0.0
        val hours = floor(totalDurationSum / 3600)
        val minutes = (totalDurationSum % 3600) / 60
        val seconds = totalDurationSum % 60

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp),
            verticalArrangement = Arrangement.Center,          // centra verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // centra horizontalmente
        ) {



            if (sortedFiltered.isEmpty()) {
                Text(
                    text = "No data in '$selectedNote'.",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    /*.clickable {
                        editedNoteText = selectedNote
                        showEditDialogF = true
                    }*/
                    .padding(bottom = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sortedFiltered[0].note,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.body2.copy(fontSize = (30-sortedFiltered[0].note.length).coerceIn(10, 24).sp, lineHeight = 24.sp)
                    )
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "date",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.28f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "runs",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.16f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "max ",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.16f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "avg",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.15f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "tps",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.15f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                }
                sortedFiltered.forEach { historyItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { onHistoryItemClicked(historyItem) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${historyItem.dateTime.take(8)}",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.28f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        Text(
                            text = "${historyItem.attempts}",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.16f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        Text(
                            text = "${historyItem.maxThrow}",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.16f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        Text(
                            text = "${"%.1f".format(historyItem.averageThrow)}",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.15f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                        Text(
                            text = "${"%.1f".format(historyItem.tps)}",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(0.15f),
                            style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                        )
                    }

                }



                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.28f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "${globalAttempts}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.16f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 8.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "${globalMaxThrow}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.16f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 8.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "${"%.1f".format(globalAverage)}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.15f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                    Text(
                        text = "${"%.1f".format(globalTPS)}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.15f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 9.sp, lineHeight = 12.sp)
                    )
                }*/


                /*Text(
                    text = "Progress",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                ChunkedAverageLineChart(filtered)*/

                Text(
                    text = "Runs Distribution",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                CountsHistogram(sortedFiltered)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Runs Over X",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                CountsHistogramAcc(sortedFiltered)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Progress Estimation",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProgressEstimationChart(sortedFiltered)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Max Throws Progress",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=8.dp, bottom=0.dp),

                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                MaxThrowBarChart(sortedFiltered.reversed())

                Text(
                    text = "Avg Throws Progress",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=4.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                )
                AverageThrowBarChart(sortedFiltered.reversed())


                Text(
                    text = "Other Stats",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=16.dp, end=24.dp, top=8.dp, bottom=4.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Attempts: ${globalAttempts}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Throws: ${globalThrows}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "Total Time: ${"%.0f".format(hours)}h ${"%.0f".format(minutes)}m ${"%.0f".format(seconds)}s",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start=20.dp, end=24.dp, top=0.dp, bottom=0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                /*Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botão para apagar todas as entradas com a nota selecionada
                    Button(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Text(text = "DELETE", color = Color.White, fontSize = 10.sp)
                    }
                    // Botão para copiar todos os dados filtrados
                    Button(
                        onClick = {
                            val clipboardText = buildString {
                                append("Trick: ${sortedFiltered[0].note}\n")
                                sortedFiltered.forEach { historyItem ->
                                    append("Date:${historyItem.dateTime}\n")
                                    append("runs:${historyItem.attempts} thrs:${historyItem.totalThrows} max:${historyItem.maxThrow} avg:${"%.1f".format(historyItem.averageThrow)} tps:${"%.1f".format(historyItem.tps)}\n")
                                    append("\n")
                                }
                            }
                            clipboardManager.setText(AnnotatedString(clipboardText))
                            Toast.makeText(context, "Copied Data", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                    ) {
                        Text(text = "COPY", color = Color.White, fontSize = 10.sp)
                    }
                }*/
                Spacer(modifier = Modifier.height(50.dp))
            }
            // Diálogo de confirmação para delete
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = {
                        Text(
                            text = "Confirm Delete",
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Text(
                            text = "Are you sure you want to delete this entry?",
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    buttons = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Botão para cancelar a exclusão
                            Button(
                                onClick = { showDeleteConfirmation = false },
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                            ) {
                                Text(text = "NO", color = Color.White, fontSize = 10.sp)
                            }
                            // Botão para confirmar a exclusão
                            Button(
                                onClick = {
                                    deleteHistoryByNote(selectedNote, context)
                                    showDeleteConfirmation = false
                                    Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                            ) {
                                Text(text = "YES", color = Color.White, fontSize = 10.sp)
                            }
                        }
                    },
                    backgroundColor = Color.Black
                )
            }
        }
        // Diálogo de edição em massa
        if (showEditDialogF) {
            EditTrickSurfaceDialog(
                initialText = editedNoteText,
                onSave = { newText ->
                    // salva no prefs e atualiza items
                    editedNoteText = newText
                    val updated = items.map { hi ->
                        if (hi.note == selectedNote) hi.copy(note = newText) else hi
                    }
                    prefs.edit().putString("history", gson.toJson(updated, listType)).apply()
                    items.clear(); items.addAll(updated)
                    showEditDialogF = false
                },
                onCancel = {
                    showEditDialogF = false
                }
            )
        }

    }

    @Composable
    fun EditTrickSurfaceDialog(
        initialText: String,
        onSave: (String) -> Unit,
        onCancel: () -> Unit
    ) {
        Dialog(onDismissRequest = onCancel) {
            val context            = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current
            var text by remember { mutableStateOf(initialText) }

            Surface(
                modifier = Modifier
                    .fillMaxSize(),            // 1) ocupa toda a tela
                color = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 32.dp, horizontal = 16.dp),      // margem interna opcional
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // OutlinedTextField reduzido a 44.dp de altura
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.85f),
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colors.primary,
                            fontSize = 14.sp
                        ),
                        label = {
                            Text(
                                text = "Edit Tricks Name",
                                fontSize = 10.sp,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Edit Tricks Name",
                                fontSize = 12.sp,
                                color = MaterialTheme.colors.primaryVariant
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor                = MaterialTheme.colors.primary,
                            cursorColor              = MaterialTheme.colors.primary,
                            focusedBorderColor       = MaterialTheme.colors.primaryVariant,
                            unfocusedBorderColor     = MaterialTheme.colors.primaryVariant,
                            backgroundColor          = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                onCancel()
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Text("CANCEL", color = Color.White, fontSize = 10.sp)
                        }
                        Button(
                            onClick = {
                                onSave(text)
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Text("SAVE", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
    /*context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()*/


    // Função para atualizar a nota do history item no SharedPreferences
    private fun updateHistoryNote(historyItem: HistoryItem, newNote: String, context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingJson = prefs.getString("history", "[]")
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableListOf()

        // Atualiza o item que possui o mesmo dateTime (assumindo que seja único)
        val updatedHistory = existingHistory.map {
            if (it.dateTime == historyItem.dateTime) it.copy(note = newNote) else it
        }
        val newJson = gson.toJson(updatedHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }

    private fun addHistoryNote(historyItem: HistoryItem, newNote: String, context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingJson = prefs.getString("history", "[]")
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableListOf()

        // Cria novo item com nota
        val newItem = historyItem.copy(note = newNote)

        // Adiciona ao histórico
        existingHistory.add(newItem)

        // Grava de volta no SharedPreferences
        val newJson = gson.toJson(existingHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }


    @Composable
    fun HistoryDetailScreen(historyItem: HistoryItem, onBack: () -> Unit) {
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current

        // Estados para exibir ou não o diálogo de edição e armazenar a nota atual
        var isEditingNote by remember { mutableStateOf(false) }
        var isCopyNote by remember { mutableStateOf(false) }

        var editedNoteText by remember(historyItem) { mutableStateOf(historyItem.note) }
        var showDeleteConfirmation by remember { mutableStateOf(false) }

        // Cálculos das estatísticas
        val counts = historyItem.throwRecords.map { it.count }
        val seconds = historyItem.throwRecords.map { it.durationSeconds }
        val totalAttempts = counts.size
        val totalDuration = seconds.sum()
        val throws = counts.sum()
        val minThrow = counts.minOrNull() ?: 0
        val maxThrow = counts.maxOrNull() ?: 0
        val averageThrow = if (counts.isNotEmpty()) throws.toFloat() / totalAttempts else 0f
        val overallFrequency = if (totalDuration > 0) throws.toFloat() / totalDuration else 0f

        val variance = if (counts.isNotEmpty()) {
            counts.map { (it.toFloat() - averageThrow).pow(2) }
                .sum() / totalAttempts
        } else 0f
        val standardDeviation = sqrt(variance)

        // Ordena os valores de count em ordem decrescente
        val sortedCounts = counts.sortedDescending()
        val avgBest3 = if (sortedCounts.size >= 3) sortedCounts.take(3).average() else null
        val avgBest5 = if (sortedCounts.size >= 5) sortedCounts.take(5).average() else null
        val avgBest10 = if (sortedCounts.size >= 10) sortedCounts.take(10).average() else null


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(0.dp),
            verticalArrangement = Arrangement.Center,          // centra verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // centra horizontalmente
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // Nota editável (clicável)
            Box(modifier = Modifier
                .fillMaxWidth()
                //.clickable { isEditingNote = true }
                .padding(bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = editedNoteText,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body2.copy(fontSize = (30-editedNoteText.length).coerceIn(10, 24).sp, lineHeight = 24.sp)
                )
            }



            Text(
                text = historyItem.dateTime,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 0.dp),
                style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
            )

            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                AttemptsBarChart(historyItem.throwRecords)
            }
            Spacer(modifier = Modifier.height(8.dp))


            CountsHistogram(listOf(historyItem))
            Spacer(modifier = Modifier.height(8.dp))

            CountsHistogramAcc(listOf(historyItem))
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                ProgressEstimationChart(listOf(historyItem))
            }


            // Estatísticas
            Row(
                modifier = Modifier.padding(horizontal=24.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "run\n$totalAttempts",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "thr\n$throws",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "max\n$maxThrow",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "avg\n${"%.1f".format(averageThrow)}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "tps\n${"%.1f".format(overallFrequency)}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
            }
            Row(
                modifier = Modifier.padding(horizontal=24.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "min\n$minThrow",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                Text(
                    text = "std\n${"%.1f".format(standardDeviation)}",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.weight(0.8f),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                )
                avgBest3?.let {
                    Text(
                        text = "b03\n${"%.1f".format(it)}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                }
                avgBest5?.let {
                    Text(
                        text = "b05\n${"%.1f".format(it)}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                }
                avgBest10?.let {
                    Text(
                        text = "b10\n${"%.1f".format(it)}",
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp)
                    )
                }
            }



            Text(
                text = "Stats:",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .align(Alignment.Start),
                style = MaterialTheme.typography.body2,
            )
            historyItem.throwRecords.forEachIndexed { index, record ->
                Text(
                    text = "${index+1}: ${record.count}t, ${"%.1f".format(record.durationSeconds)}s, ${"%.1f".format(record.frequency)}tps",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(vertical = 0.dp),
                    style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                    textAlign = TextAlign.Center,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier.size(38.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text(text = "DEL", color = Color.White, fontSize = 10.sp)
                }
                Button(
                    onClick = { isEditingNote = true },
                    modifier = Modifier.size(38.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                ) {
                    Text(text = "EDIT", color = Color.White, fontSize = 10.sp)
                }
                Button(
                    onClick = { isCopyNote = true },
                    modifier = Modifier.size(38.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)
                    /*onClick = {
                        val clipboardText = buildString {
                            append("Data: ${historyItem.dateTime}\n")
                            append("Trick: ${editedNoteText}\n")
                            append("Stats:\n")
                            historyItem.throwRecords.forEachIndexed { index, record ->
                                append("${index+1}: ${record.count}t, ${"%.1f".format(record.durationSeconds)}s, ${"%.1f".format(record.frequency)}tps\n")
                            }
                            append("\n")
                            append("runs: $totalAttempts  thr: $throws  max: $maxThrow  avg: ${"%.1f".format(averageThrow)}  tps: ${"%.1f".format(overallFrequency)}\n")
                            append("min: $minThrow  std: ${"%.1f".format(standardDeviation)}")
                            avgBest3?.let { append("  b03: ${"%.1f".format(it)}") }
                            avgBest5?.let { append("  b05: ${"%.1f".format(it)}") }
                            avgBest10?.let { append("  b10: ${"%.1f".format(it)}") }
                        }
                        clipboardManager.setText(AnnotatedString(clipboardText))
                        Toast.makeText(context, "Copied Data", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)*/
                ) {
                    Text(text = "COPY", color = Color.White, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(20.dp))

        }

        if (isEditingNote) {
            EditTrickSurfaceDialog(
                initialText = editedNoteText,
                onSave = { newText ->
                    if (newText != editedNoteText) {
                        editedNoteText = newText
                        updateHistoryNote(historyItem, newText, context)
                        onBack() // Sai da tela após guardar
                    }
                    isEditingNote = false
                },
                onCancel = {
                    isEditingNote = false
                }
            )
        }
        if (isCopyNote) {
            EditTrickSurfaceDialog(
                initialText = editedNoteText,
                onSave = { newText ->
                    if (newText != editedNoteText) {
                        editedNoteText = newText
                        addHistoryNote(historyItem, newText, context)
                        onBack() // Sai da tela após guardar
                    }
                    isCopyNote = false
                },
                onCancel = {
                    isCopyNote = false
                }
            )
        }
        /*if (isEditingNote) {
            AlertDialog(
                onDismissRequest = { isEditingNote = false },
                title = {
                    Text(
                        text = "Edit Trick",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    OutlinedTextField(
                        value = editedNoteText,
                        onValueChange = { editedNoteText = it },
                        label = { Text("", color = Color.White) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            backgroundColor = Color.Black
                        )
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // cancel
                        Button(
                            onClick = { isEditingNote = false },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                        ) {
                            Text(text = "CANCEL", color = Color.White, fontSize = 10.sp)
                        }
                        // save
                        Button(
                            onClick = {
                                updateHistoryNote(historyItem, editedNoteText, context)
                                isEditingNote = false
                            },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF006400))
                        ) {
                            Text(text = "SAVE", color = Color.White, fontSize = 10.sp)
                        }
                    }
                },
                backgroundColor = Color.Black
            )
        }*/
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = {
                    Text(
                        text = "Confirm Delete",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete this entry?",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },

                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Botão para cancelar (círculo)
                        Button(
                            onClick = { showDeleteConfirmation = false },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                        ) {
                            Text(text = "NO", color = Color.White, fontSize = 10.sp)
                        }
                        // Botão para confirmar (círculo)
                        Button(
                            onClick = {
                                deleteHistoryItem(historyItem, context)
                                showDeleteConfirmation = false
                                Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show()
                                onBack()  // Retorna à tela de histórico após apagar
                            },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Text(text = "YES", color = Color.White, fontSize = 10.sp)
                        }
                    }
                },
                backgroundColor = Color.Black
            )
        }
    }

    @Composable
    fun ChunkedAverageLineChart(
        historyItems: List<HistoryItem>,
        chunkSize: Int = 50
    ) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        // Agrega todos os valores de averageThrow de todos os ThrowRecords
        val allValues: List<Int> = remember(historyItems) {
            historyItems.flatMap { run ->
                run.throwRecords.map { record -> record.count }
            }
        }

        // Agrupa em blocos de chunkSize e calcula média de cada bloco
        val chunkedAverages: List<Float> = remember(allValues, chunkSize) {
            if (allValues.isEmpty()) emptyList()
            else allValues.chunked(chunkSize).map { it.average().toFloat() }
        }

        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    description.isEnabled = false
                    setDrawGridBackground(false)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    isDragEnabled = true
                    setScaleEnabled(true)
                }
            },
            update = { chart ->
                val entries: List<Entry> = chunkedAverages.mapIndexed { index, avg ->
                    Entry(index.toFloat(), avg)
                }
                val dataSet = LineDataSet(entries, "Média por Blocos").apply {
                    color = primaryColor
                    setDrawCircles(false)
                    lineWidth = 2f
                    setDrawValues(false)
                }
                chart.data = LineData(dataSet)

                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                }
                chart.axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setXOffset(0f)
                }
                chart.axisRight.apply {
                    isEnabled = false
                    setDrawAxisLine(false)
                }
                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        )
    }


    @Composable
    fun ProgressEstimationChart(
        totalEntries: List<HistoryItem>,
        maxBars: Int = 8
    ) {
        val totalThrows = totalEntries.flatMap { it.throwRecords.map { tr -> tr.count } }

        if (totalThrows.isEmpty()) {
            Text(
                text = "No data",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            return
        }

        val mean = totalThrows.average()
        val variance = totalThrows.map { (it - mean).let { d -> d * d } }.average().coerceAtLeast(mean + 1)
        val r = (mean * mean) / (variance - mean)
        val p = r / (r + mean)
        fun lnGamma(x: Double): Double {
            val cof = doubleArrayOf(
                76.18009172947146, -86.50532032941677,
                24.01409824083091, -1.231739572450155,
                0.001208650973866179, -0.000005395239384953
            )
            var y = x
            var tmp = x + 5.5
            tmp -= (x + 0.5) * ln(tmp)
            var ser = 1.000000000190015
            for (j in 0..5) {
                y += 1.0
                ser += cof[j] / y
            }
            return -tmp + ln(2.5066282746310005 * ser / x)
        }
        fun log10InverseProbGreaterOrEqualK(k: Int): Float {
            var sum = 0.0
            for (i in 0 until k) {
                val logTerm = lnGamma(i.toDouble() + r) - lnGamma(i.toDouble() + 1.0) - lnGamma(r) + r * ln(p) + i * ln(1 - p)
                val term = kotlin.math.exp(logTerm)
                if (term < 1e-12) continue
                sum += term
            }
            val prob = 1.0 - sum.coerceIn(0.0, 1.0)
            return if (prob > 0) (ln(1.0 / prob) / ln(10.0)).toFloat() else Float.POSITIVE_INFINITY
        }

        val candidateSteps = listOf(1,2,5,10, 20, 50, 100, 200, 500, 1000)
        val effectiveStep = candidateSteps.firstOrNull { step ->
            val binCount = generateSequence(0) { it + step }
                .dropWhile {
                    val logVal = log10InverseProbGreaterOrEqualK(it)
                    10.0.pow(logVal.toDouble()) < 10
                }
                .takeWhile {
                    val logVal = log10InverseProbGreaterOrEqualK(it)
                    10.0.pow(logVal.toDouble()) <= 1_000
                }
                .count()
            binCount <= maxBars
        } ?: candidateSteps.last()

        val bins = generateSequence(0) { it + effectiveStep }
            .dropWhile {
                val logVal = log10InverseProbGreaterOrEqualK(it)
                10.0.pow(logVal.toDouble()) < 10
            }
            .takeWhile {
                val logVal = log10InverseProbGreaterOrEqualK(it)
                10.0.pow(logVal.toDouble()) <= 1_000
            }
            .toList()

        val nBins=bins.size

        val log10InvProbs = bins.map { k ->
            log10InverseProbGreaterOrEqualK(k)
        }

        val primaryColor = MaterialTheme.colors.primary.toArgb()
        val entries = bins.mapIndexed { index, k ->
            BarEntry(index.toFloat(), log10InvProbs[index])
        }

        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                val dataSet = BarDataSet(entries, "Progress Estimation").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 7f
                    /*valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry?): String {
                            return barEntry?.takeIf { it.x.toInt() == selectedIndex }?.y?.let {
                                val value = 10.0.pow(it.toDouble()).toInt()
                                if (value >= 1_000_000) "${value / 1_000_000}M"
                                else if (value >= 1_000) "${value / 1_000}k"
                                else value.toString()
                            } ?: ""
                        }
                    }*/
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry?): String {
                            return barEntry?.y?.let {
                                val value = 10.0.pow(it.toDouble())
                                return when {
                                    value >= 1_000_000 -> {
                                        val millions = value / 1_000_000
                                        when {
                                            millions >= 100 -> "${millions.toInt()}M"
                                            millions >= 10  -> String.format("%.1fM", millions)
                                            else            -> String.format("%.2fM", millions)
                                        }
                                    }
                                    value >= 100_000 -> "${(value / 1_000).toInt()}K"
                                    value >= 10_000  -> String.format("%.1fK", value / 1_000)
                                    value >= 1_000   -> String.format("%.2fK", value / 1_000)
                                    else             -> value.toInt().toString()
                                }
                            } ?: ""
                        }
                    }

                }
                chart.data = BarData(dataSet)
                chart.setMinOffset(0f)
                //chart.setViewPortOffsets(0f, 0f, 0f, 0f)
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        selectedIndex = e?.x?.toInt()
                    }

                    override fun onNothingSelected() {
                        selectedIndex = null
                    }
                })

                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    /*setLabelCount(nBins+2, true)
                    axisMinimum = -1f
                    axisMaximum = (nBins+0).toFloat()*/
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 7f
                    yOffset =2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.toInt()
                            return bins.getOrNull(idx)?.toString() ?: ""
                        }
                    }
                }

                chart.axisLeft.apply {
                    axisMinimum = 1f
                    setDrawGridLines(true)
                    isGranularityEnabled = true
                    //setLabelCount(6, true)
                    textColor = primaryColor
                    textSize = 7f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val actual = 10.0.pow(value.toDouble()).toInt()
                            return when {
                                actual >= 1_000_000 -> "1M"
                                actual >= 100_000 -> "100K"
                                actual >= 10_000 -> "10K"
                                actual >= 1_000 -> "1K"
                                actual >= 100 -> "100"
                                actual >= 10 -> "10"
                                else -> actual.toString()
                            }
                        }
                    }
                    setDrawAxisLine(false)
                    setXOffset(0f)
                }

                chart.invalidate()
            }
        )

        selectedIndex?.let { idx ->
            val logValue = log10InvProbs.getOrNull(idx) ?: 0.0f
            val value = 10.0.pow(logValue.toDouble()).toInt()
            Dialog(
                onDismissRequest = { selectedIndex = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),
                    elevation = 16.dp,
                    modifier = Modifier.size(width = 160.dp, height = 60.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Estimated Attempts to ${bins.getOrNull(idx) ?: "?"} throws: $value",
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CountsHistogramAcc(
        historyItems: List<HistoryItem>,
        maxBins: Int = 15
    ) {
        val counts = historyItems.flatMap { it.throwRecords.map { tr -> tr.count } }
        CountsHistogramA(counts, maxBins)
    }

    @Composable
    fun CountsHistogramAccWorkout(
        throwRecords: List<ThrowRecord>,
        maxBins: Int = 8
    ) {
        val counts = throwRecords.map { it.count }
        CountsHistogramA(counts, maxBins)
    }

    @Composable
    fun CountsHistogramA(
        allCounts: List<Int>,
        maxBins: Int = 5
    ) {
        // 1) extrai todos os counts
        if (allCounts.isEmpty()) {
            Text(
                text = "No Data",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            return
        }

        // 2) lista de candidatos de binSize
        val candidates = listOf(10,20,50,100,200,500,1000,2000,5000,10000)

        // 3) encontra maxCount
        val maxCount = allCounts.maxOrNull() ?: 0

        // 4) escolhe o menor binSize tal que (maxCount/binSize +1) <= maxBins
        val effectiveBinSize = candidates.firstOrNull { size ->
            (maxCount / size) + 1 <= maxBins
        } ?: candidates.last()

        // 5) calcula número real de bins
        val nBins = (maxCount / effectiveBinSize) + 1

        // 6) agrupa frequências por bin
        val freqPerBin = (0 until nBins).associateWith { binIdx ->
            val start = binIdx * effectiveBinSize
            val end   = start + effectiveBinSize - 1
            allCounts.count { it in start..end }
        }

        //7) CCDF
        val total = freqPerBin.values.sum()

        var acc = 0
        val ccdfPerBin: Map<Int, Int> = freqPerBin.mapValues { (_, freq) ->
            val ccdf = total - acc
            acc += freq
            ccdf
        }


        // Estado para saber qual bin foi clicado (ou null se nenhum)
        var selectedBin by remember { mutableStateOf<Int?>(null) }

        // 7) desenha o gráfico com AndroidView
        val primaryColor = MaterialTheme.colors.primary.toArgb()
        /*AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }

            },
            update = { chart ->
                // monta entradas
                val entries = ccdfPerBin.map { (binIdx, freq) ->
                    BarEntry(binIdx.toFloat(), freq.toFloat())
                }
                val dataSet = BarDataSet(entries, "Histogram").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 7f
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry): String {
                            val value = barEntry.y
                            return when {
                                value >= 1_000_000 -> {
                                    val millions = value / 1_000_000
                                    when {
                                        millions >= 100 -> "${millions.toInt()}M"
                                        millions >= 10  -> String.format("%.1fM", millions)
                                        else            -> String.format("%.2fM", millions)
                                    }
                                }
                                value >= 100_000 -> "${(value / 1_000).toInt()}K"
                                value >= 10_000  -> String.format("%.1fK", value / 1_000)
                                value >= 1_000   -> String.format("%.2fK", value / 1_000)
                                else             -> value.toInt().toString()
                            }
                        }
                    }
                }
                chart.data = BarData(dataSet)


                chart.setOnTouchListener { v, event ->
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                            v.performClick()  // ✅ necessário para acessibilidade
                        }
                    }
                    false  // deixa o gráfico continuar a tratar o toque
                }

                // configura eixo X
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(true)
                    position = XAxis.XAxisPosition.BOTTOM
                    isGranularityEnabled = true
                    granularity = 1f
                    //setLabelCount(nBins+1, true)
                    axisMinimum = -0.5f
                    axisMaximum = (nBins - 1) + 0.5f
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 7f
                    yOffset = 0f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val bin = value.toInt()
                            val start = bin * effectiveBinSize
                            val end = (bin + 1) * effectiveBinSize
                            return ">$start"
                        }
                    }
                }

                chart.axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setXOffset(0f)
                }
                chart.axisRight.isEnabled = false
                chart.isDragEnabled = true
                chart.setVisibleXRangeMaximum(8f)

                // ---- AQUI: regista o clique na barra ----
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry, h: Highlight) {
                        selectedBin = e.x.toInt()
                    }
                    override fun onNothingSelected() { /* nada */ }
                })

                chart.invalidate()
            }
        )*/

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            factory = { ctx ->
                HorizontalBarChart(ctx).apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    setDrawValueAboveBar(true)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    axisRight.isEnabled = false
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                val exponent =1f
                val bs       = effectiveBinSize
                val freqMap  = ccdfPerBin  // Map<Int,Int>

                // 1) Cria as entradas com transformação
                val rawEntries = freqMap.map { (binIdx, freq) ->
                    val yTrans = freq.toFloat().pow(exponent)
                    BarEntry(binIdx.toFloat(), yTrans)
                }

                // 2) Prepara o DataSet (com cor, tamanho e formatter)
                val dataSet = BarDataSet(rawEntries, "").apply {
                    color           = primaryColor
                    valueTextColor  = primaryColor
                    valueTextSize   = 9f
                    setDrawValues(true)

                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry): String {
                            val bin        = barEntry.x.toInt()
                            val threshold  = bin * bs
                            val ccdfValue  = freqMap[bin] ?: 0
                            val pct        = if (total > 0) ccdfValue * 100f / total else 0f
                            return "$ccdfValue (> $threshold)"
                        }
                    }
                }

                // 3) Define os dados no gráfico, usando o dataset acima
                val barData = BarData(dataSet).apply {
                    barWidth = 0.8f
                }
                chart.data = barData
                chart.setFitBars(true)
                chart.setClipValuesToContent(false)              // 1️⃣ permite desenhar labels para lá dos limites

                chart.setExtraOffsets(
                    /* left   = */ 0f,
                    /* top    = */ 0f,
                    /* right  = */ 80f,  // ajusta este valor até não cortar mais
                    /* bottom = */ 0f
                )
                // 4) Oculta todos os eixos
                chart.xAxis.isEnabled     = false
                chart.axisLeft.isEnabled  = false
                chart.axisRight.isEnabled = false

                // 5) Trata o toque
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry, h: Highlight) {
                        selectedBin = e.x.toInt()
                    }
                    override fun onNothingSelected() = Unit
                })

                chart.invalidate()
            }
        )


        // Se houver uma bin seleccionada, mostra um Dialog Compose “inline”
        selectedBin?.let { binIdx ->
            val threshold = binIdx * effectiveBinSize
            val ccdfValue = ccdfPerBin[binIdx] ?: 0
            val percentage = if (total > 0) (ccdfValue * 100f) / total else 0f

            Dialog(
                onDismissRequest = { selectedBin = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),  // preto semitransparente
                    elevation = 16.dp,
                    modifier = Modifier
                        .size(width = 120.dp, height = 80.dp)
                        .padding(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "> $threshold",
                            color = Color.White,
                            fontSize = 16.sp,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "$ccdfValue runs (${String.format("%.1f", percentage)}%)",
                            color = Color.White,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )

                    }
                }
            }
        }
    }



    @Composable
    fun CountsHistogram(
        historyItems: List<HistoryItem>,
        maxBins: Int = 30
    ) {
        // 1) extrai todos os counts
        val allCounts = historyItems.flatMap { it.throwRecords.map { tr -> tr.count } }
        if (allCounts.isEmpty()) {
            Text(
                text = "No Data",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            return
        }

        // 2) lista de candidatos de binSize
        val candidates = listOf(10,20,50,100,200,500,1000,2000,5000,10000)

        // 3) encontra maxCount
        val maxCount = allCounts.maxOrNull() ?: 0

        // 4) escolhe o menor binSize tal que (maxCount/binSize +1) <= maxBins
        val effectiveBinSize = candidates.firstOrNull { size ->
            (maxCount / size) + 1 <= maxBins
        } ?: candidates.last()

        // 5) calcula número real de bins
        val nBins = (maxCount / effectiveBinSize) + 1

        // 6) agrupa frequências por bin
        val freqPerBin = (0 until nBins).associateWith { binIdx ->
            val start = binIdx * effectiveBinSize
            val end   = start + effectiveBinSize - 1
            allCounts.count { it in start..end }
        }

        /* ------------------------------------------------------------------ */
        /* 7) CCDF: quantas entradas ficam ESTRITAMENTE ACIMA do limite do bin */
        /* ------------------------------------------------------------------ */
        //val sortedFreq = freqPerBin.toSortedMap()          // garante ordem crescente
        val total      = freqPerBin.values.sum() + (freqPerBin[0] ?: 0)          // total de amostras

        var acumulada = 0
        val ccdfPerBin: Map<Int, Int> = freqPerBin.mapValues { (_, freq) ->
            acumulada += freq            // CDF parcial (≤ bin)
            total - acumulada            // CCDF ( > bin )
        }

        // Estado para saber qual bin foi clicado (ou null se nenhum)
        var selectedBin by remember { mutableStateOf<Int?>(null) }

        // 7) desenha o gráfico com AndroidView
        val primaryColor = MaterialTheme.colors.primary.toArgb()
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }

            },
            update = { chart ->
                // monta entradas
                val entries = freqPerBin.map { (binIdx, freq) ->
                    BarEntry(binIdx.toFloat(), freq.toFloat())
                }
                val dataSet = BarDataSet(entries, "Histogram").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 7f
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry): String {
                            val value = barEntry.y
                            return when {
                                value >= 1_000_000 -> {
                                    val millions = value / 1_000_000
                                    when {
                                        millions >= 100 -> "${millions.toInt()}M"
                                        millions >= 10  -> String.format("%.1fM", millions)
                                        else            -> String.format("%.2fM", millions)
                                    }
                                }
                                value >= 100_000 -> "${(value / 1_000).toInt()}K"
                                value >= 10_000  -> String.format("%.1fK", value / 1_000)
                                value >= 1_000   -> String.format("%.2fK", value / 1_000)
                                else             -> value.toInt().toString()
                            }
                        }
                    }
                }
                chart.data = BarData(dataSet)


                chart.setOnTouchListener { v, event ->
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                            v.performClick()  // ✅ necessário para acessibilidade
                        }
                    }
                    false  // deixa o gráfico continuar a tratar o toque
                }

                // configura eixo X
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(true)
                    position = XAxis.XAxisPosition.BOTTOM
                    isGranularityEnabled = true
                    granularity = 1f
                    //setLabelCount(nBins+1, true)
                    axisMinimum = -0.5f
                    axisMaximum = (nBins - 1) + 0.5f
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 7f
                    yOffset = 0f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val bin = value.toInt()
                            val start = bin * effectiveBinSize
                            val end = (bin + 1) * effectiveBinSize
                            return "$start-$end"
                        }
                    }
                }

                chart.axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setXOffset(0f)
                }
                chart.axisRight.isEnabled = false
                chart.isDragEnabled = true
                chart.setVisibleXRangeMaximum(8f)

                // ---- AQUI: regista o clique na barra ----
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry, h: Highlight) {
                        selectedBin = e.x.toInt()
                    }
                    override fun onNothingSelected() { /* nada */ }
                })

                chart.invalidate()
            }
        )

        // Se houver uma bin seleccionada, mostra um Dialog Compose “inline”
        selectedBin?.let { binIdx ->
            val start = binIdx * effectiveBinSize
            val end = start + effectiveBinSize - 1
            val binValue = freqPerBin[binIdx] ?: 0

            // filtra e ordena os ThrowRecord desse bin
            val attemptsInBin = historyItems
                .flatMap { it.throwRecords }
                .filter { it.count in start..end }
                .sortedByDescending { it.count }

            // agrupa por count e conta quantas vezes aparece cada valor
            val groupedCounts: Map<Int, Int> = attemptsInBin
                .groupingBy { it.count }
                .eachCount()

            // converte para lista ordenada (podes inverter a ordenação se quiseres)
            val displayItems = groupedCounts.entries
                .sortedByDescending { it.key }

            Dialog(
                onDismissRequest = { selectedBin = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),  // preto semitransparente
                    elevation = 16.dp,
                    modifier = Modifier
                        .size(width = 100.dp, height = 140.dp)
                        .padding(0.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "$start–$end",
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "($binValue runs)",
                            fontSize = 10.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 120.dp)
                        ) {
                            items(displayItems) { (value, freq) ->
                                Text(
                                    text = "$value ($freq)",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun CountsHistogramWorkOut(
        throwRecords: List<ThrowRecord>,
        maxBins: Int = 20
    ) {
        // 1) extrai todos os counts
        val allCounts = throwRecords.map { it.count }
        if (allCounts.isEmpty()) {
            Text(
                text = "",
                color = MaterialTheme.colors.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            return
        }

        // 2) define lista de candidatos de binSize
        val candidates = listOf(10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000)

        // 3) encontra maxCount
        val maxCount = allCounts.maxOrNull() ?: 0

        // 4) escolhe o menor binSize tal que (maxCount/binSize +1) <= maxBins
        val effectiveBinSize = candidates.firstOrNull { size ->
            (maxCount / size) + 1 <= maxBins
        } ?: candidates.last()

        // 5) calcula número real de bins
        val nBins = (maxCount / effectiveBinSize) + 1

        // 6) agrupa frequências por bin
        val freqPerBin = (0 until maxBins).associateWith { binIdx ->
            val start = binIdx * effectiveBinSize
            val end = start + effectiveBinSize - 1
            allCounts.count { it in start..end }
        }

        /* ------------------------------------------------------------------ */
        /* 7) CCDF: quantas entradas ficam ESTRITAMENTE ACIMA do limite do bin */
        /* ------------------------------------------------------------------ */
        val sortedFreq = freqPerBin.toSortedMap()          // garante ordem crescente
        val total      = sortedFreq.values.sum()           // total de amostras

        var acumulada = 0
        val ccdfPerBin: Map<Int, Int> = sortedFreq.mapValues { (_, freq) ->
            acumulada += freq            // CDF parcial (≤ bin)
            total - acumulada            // CCDF ( > bin )
        }


        // estado para saber qual bin foi clicado
        var selectedBin by remember { mutableStateOf<Int?>(null) }

        // 7) desenha o gráfico
        val primaryColor = MaterialTheme.colors.primary.toArgb()
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                // monta entradas
                val entries = ccdfPerBin.map { (bin, freq) ->
                    BarEntry(bin.toFloat(), freq.toFloat())
                }
                chart.setOnTouchListener { v, event ->
                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                            v.performClick()  // ✅ necessário para acessibilidade
                        }
                    }
                    false  // deixa o gráfico continuar a tratar o toque
                }
                val dataSet = BarDataSet(entries, "Histogram").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 7f
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry) =
                            barEntry.y.toInt().toString()
                    }
                }
                chart.setMinOffset(0f)

                chart.axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setXOffset(0f)
                }

                chart.axisRight.isEnabled = false
                chart.axisLeft.isEnabled = false

                chart.setViewPortOffsets(
                    /* left  */ 0f,
                    /* top   */ 0f,
                    /* right */ 0f,
                    /* bottom*/ 0f
                )
                chart.data = BarData(dataSet)

                // configura o eixo X
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(true)
                    position = XAxis.XAxisPosition.BOTTOM
                    isGranularityEnabled = true
                    granularity = 1f
                    setLabelCount(maxBins+1, true)
                    axisMinimum = -0.5f
                    axisMaximum = (maxBins - 1) + 0.5f
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 7f
                    yOffset = 0f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            if(value<0){
                                return ""
                            }
                            else {
                                val bin = value.toInt()
                                val end = (bin + 1) * effectiveBinSize
                                return "$end"
                            }
                        }
                    }
                }

                chart.axisRight.isEnabled = false
                chart.isDragEnabled = true
                chart.setVisibleXRangeMaximum(10f)

                // listener de clique
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        e?.let { selectedBin = it.x.toInt() }
                    }
                    override fun onNothingSelected() {}
                })

                chart.invalidate()
            }
        )

        // 8) ao clicar numa barra, mostra diálogo com valores agrupados
        selectedBin?.let { binIdx ->
            val start = binIdx * effectiveBinSize
            val end = start + effectiveBinSize - 1

            // filtra e agrupa por count
            val grouped = throwRecords
                .filter { it.count in start..end }
                .groupingBy { it.count }
                .eachCount()
                .entries
                .sortedByDescending { it.key }

            Dialog(
                onDismissRequest = { selectedBin = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),
                    elevation = 16.dp,
                    modifier = Modifier
                        .size(width = 80.dp, height = 120.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "$start–$end",
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(6.dp))
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(grouped) { (value, freq) ->
                                Text(
                                    text = "$value ($freq)",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }




    @Composable
    fun AttemptsBarChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        val maxIndex = throwRecords
            .mapIndexed { idx, rec -> idx to rec.count }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex
        val totalEntries = throwRecords.size
        var isZoomed by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                CombinedChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(false)
                    setHighlightPerDragEnabled(false)
                }
            },
            update = { chart ->

                val labelFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        val idx = barEntry?.x?.toInt() ?: return ""
                        return if (isZoomed || idx == maxIndex) {
                            barEntry.y.toInt().toString()
                        } else {
                            ""
                        }
                    }
                }

                val entries = throwRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.count.toFloat())
                }

                val barDataSet = BarDataSet(entries, "Throws").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 12f
                    valueFormatter = labelFormatter
                }

                val progression = throwRecords.map { it.count.toFloat() }
                    .runningFoldIndexed(0f) { index, acc, value ->
                        if (index == 0) value else ((acc * index) + value) / (index + 1)
                    }
                    .drop(1)

                val lineEntries = progression.mapIndexed { i, avg -> Entry(i.toFloat(), avg) }

                val lineDataSet = LineDataSet(lineEntries, "avg accu").apply {
                    color = AndroidColor.rgb(255, 165, 0)
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                    mode = LineDataSet.Mode.LINEAR
                }

                val combinedData = CombinedData().apply {
                    setData(BarData(barDataSet))
                    setData(LineData(lineDataSet))
                }

                chart.data = combinedData
                chart.isDragEnabled = isZoomed
                chart.setMinOffset(0f)
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    axisMinimum = -1f
                    axisMaximum = (lastIndex) + 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    yOffset = 2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.roundToInt() + 1
                            return if (idx > 0 && idx < lastIndex + 2) "$idx" else ""
                        }
                    }
                }
                chart.isDragEnabled = true

                val maxY = throwRecords.maxOfOrNull { it.count }?.toFloat() ?: 0f
                val stepOptions = listOf(1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(maxY / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val axisMax = ceil(maxY / chosenStep).toInt() * chosenStep
                val labelCount = (axisMax / chosenStep).toInt() + 1
                val acumuladaFinal = throwRecords.map { it.count }
                    .runningFoldIndexed(0f) { idx, acc, valor ->
                        if (idx == 0) valor.toFloat()
                        else ((acc * idx) + valor) / (idx + 1)
                    }.last()

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawGridLines(true)
                    gridColor = AndroidColor.GRAY
                    textColor = primaryColor
                    textSize = 8f
                    setDrawAxisLine(false)
                    val mediaLine = LimitLine(acumuladaFinal, "").apply {
                        lineColor = AndroidColor.rgb(255, 165, 0)
                        lineWidth = 0.5f
                        textColor = AndroidColor.rgb(255, 165, 0)
                        textSize = 9f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }

                chart.setVisibleXRangeMaximum(1000f)
                chart.setMaxVisibleValueCount(1000)

                val gestureDetector = GestureDetector(chart.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        isZoomed = !isZoomed
                        if (isZoomed) {
                            chart.setVisibleXRangeMaximum(5f)
                            chart.moveViewToX((totalEntries - 5).coerceAtLeast(0).toFloat())
                        } else {
                            chart.fitScreen()
                        }
                        chart.invalidate()
                        return true
                    }
                })

                chart.setOnTouchListener { v, event ->
                    // ✅ Sempre processa o gestureDetector para apanhar double tap
                    gestureDetector.onTouchEvent(event)

                    if (isZoomed) {
                        // Só no zoom in impede que o parent intercepte
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                                v.performClick()
                            }
                        }
                    } else {
                        // Zoom out -> permite que o parent intercepte arrastos
                        (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                    }
                    false // não consumir totalmente, deixar seguir
                }


                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)

        )
    }

    @Composable
    fun AttemptsDurationBarChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        val maxIndex = throwRecords
            .mapIndexed { idx, rec -> idx to rec.durationSeconds }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex
        val totalEntries = throwRecords.size
        var isZoomed by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                CombinedChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(false)
                    setHighlightPerDragEnabled(false)
                }
            },
            update = { chart ->

                val labelFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        val idx = barEntry?.x?.toInt() ?: return ""
                        return if (isZoomed || idx == maxIndex) {
                            val value = barEntry.y
                            if (value >= 100f) {
                                "%.0f".format(value)
                            } else {
                                "%.1f".format(value)
                            }
                        } else {
                            ""
                        }
                    }
                }

                val entries = throwRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.durationSeconds.toFloat())
                }

                val barDataSet = BarDataSet(entries, "Duration (s)").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 12f
                    valueFormatter = labelFormatter
                }

                val progression = throwRecords.map { it.durationSeconds.toFloat() }
                    .runningFoldIndexed(0f) { index, acc, value ->
                        if (index == 0) value else ((acc * index) + value) / (index + 1)
                    }
                    .drop(1)

                val lineEntries = progression.mapIndexed { i, avg -> Entry(i.toFloat(), avg) }

                val lineDataSet = LineDataSet(lineEntries, "avg dur").apply {
                    color = AndroidColor.rgb(255, 165, 0)
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                    mode = LineDataSet.Mode.LINEAR
                }

                val combinedData = CombinedData().apply {
                    setData(BarData(barDataSet))
                    setData(LineData(lineDataSet))
                }

                chart.data = combinedData
                chart.isDragEnabled = isZoomed
                chart.setMinOffset(0f)
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    axisMinimum = -1f
                    axisMaximum = (lastIndex) + 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    yOffset = 2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.roundToInt() + 1
                            return if (idx > 0 && idx < lastIndex + 2) "$idx" else ""
                        }
                    }
                }
                chart.isDragEnabled = true

                // 🔥 Usa durationSeconds para calcular o max Y
                val maxY = throwRecords.maxOfOrNull { it.durationSeconds }?.toFloat() ?: 0f
                val stepOptions = listOf(1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(maxY / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val axisMax = ceil(maxY / chosenStep).toInt() * chosenStep
                val labelCount = (axisMax / chosenStep).toInt() + 1

                // 🔥 Calcula média acumulada com durationSeconds
                val acumuladaFinal = throwRecords.map { it.durationSeconds }
                    .runningFoldIndexed(0f) { idx, acc, valor ->
                        if (idx == 0) valor.toFloat()
                        else ((acc * idx) + valor) / (idx + 1)
                    }.last()

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawGridLines(true)
                    gridColor = AndroidColor.GRAY
                    textColor = primaryColor
                    textSize = 8f
                    setDrawAxisLine(false)
                    val mediaLine = LimitLine(acumuladaFinal, "").apply {
                        lineColor = AndroidColor.rgb(255, 165, 0)
                        lineWidth = 0.5f
                        textColor = AndroidColor.rgb(255, 165, 0)
                        textSize = 9f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }

                chart.setVisibleXRangeMaximum(1000f)
                chart.setMaxVisibleValueCount(1000)

                val gestureDetector = GestureDetector(chart.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        isZoomed = !isZoomed
                        if (isZoomed) {
                            chart.setVisibleXRangeMaximum(5f)
                            chart.moveViewToX((totalEntries - 5).coerceAtLeast(0).toFloat())
                        } else {
                            chart.fitScreen()
                        }
                        chart.invalidate()
                        return true
                    }
                })

                chart.setOnTouchListener { v, event ->
                    gestureDetector.onTouchEvent(event)

                    if (isZoomed) {
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                                v.performClick()
                            }
                        }
                    } else {
                        (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                    }
                    false
                }

                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )
    }


    @Composable
    fun AttemptsTpsBarChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        val maxIndex = throwRecords
            .mapIndexed { idx, rec -> idx to rec.frequency }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex
        val totalEntries = throwRecords.size
        var isZoomed by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                CombinedChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(false)
                    setHighlightPerDragEnabled(false)
                }
            },
            update = { chart ->

                val labelFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        val idx = barEntry?.x?.toInt() ?: return ""
                        return if (isZoomed || idx == maxIndex) {
                            val value = barEntry.y
                            "%.1f".format(value)
                        } else {
                            ""
                        }
                    }
                }

                val entries = throwRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.frequency.toFloat())
                }

                val barDataSet = BarDataSet(entries, "Throws per second (tps)").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 12f
                    valueFormatter = labelFormatter
                }

                val progression = throwRecords.map { it.frequency.toFloat() }
                    .runningFoldIndexed(0f) { index, acc, value ->
                        if (index == 0) value else ((acc * index) + value) / (index + 1)
                    }
                    .drop(1)

                val lineEntries = progression.mapIndexed { i, avg -> Entry(i.toFloat(), avg) }

                val lineDataSet = LineDataSet(lineEntries, "avg dur").apply {
                    color = AndroidColor.rgb(255, 165, 0)
                    lineWidth = 2f
                    setDrawCircles(false)
                    setDrawValues(false)
                    mode = LineDataSet.Mode.LINEAR
                }

                val combinedData = CombinedData().apply {
                    setData(BarData(barDataSet))
                    setData(LineData(lineDataSet))
                }

                chart.data = combinedData
                chart.isDragEnabled = isZoomed
                chart.setMinOffset(0f)
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    axisMinimum = -1f
                    axisMaximum = (lastIndex) + 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    yOffset = 2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.roundToInt() + 1
                            return if (idx > 0 && idx < lastIndex + 2) "$idx" else ""
                        }
                    }
                }
                chart.isDragEnabled = true

                val maxY = throwRecords.maxOfOrNull { it.frequency }?.toFloat() ?: 0f
                val stepOptions = listOf(0.2f, 0.5f, 1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(maxY / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val axisMax = ceil(maxY / chosenStep).toInt() * chosenStep
                val labelCount = (axisMax / chosenStep).toInt() + 1

                // 🔥 Calcula média acumulada com durationSeconds
                val acumuladaFinal = throwRecords.map { it.frequency }
                    .runningFoldIndexed(0f) { idx, acc, valor ->
                        if (idx == 0) valor.toFloat()
                        else ((acc * idx) + valor) / (idx + 1)
                    }.last()

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawGridLines(true)
                    gridColor = AndroidColor.GRAY
                    textColor = primaryColor
                    textSize = 8f
                    setDrawAxisLine(false)
                    val mediaLine = LimitLine(acumuladaFinal, "").apply {
                        lineColor = AndroidColor.rgb(255, 165, 0)
                        lineWidth = 0.5f
                        textColor = AndroidColor.rgb(255, 165, 0)
                        textSize = 9f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }

                chart.setVisibleXRangeMaximum(1000f)
                chart.setMaxVisibleValueCount(1000)

                val gestureDetector = GestureDetector(chart.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        isZoomed = !isZoomed
                        if (isZoomed) {
                            chart.setVisibleXRangeMaximum(5f)
                            chart.moveViewToX((totalEntries - 5).coerceAtLeast(0).toFloat())
                        } else {
                            chart.fitScreen()
                        }
                        chart.invalidate()
                        return true
                    }
                })

                chart.setOnTouchListener { v, event ->
                    gestureDetector.onTouchEvent(event)

                    if (isZoomed) {
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                                v.performClick()
                            }
                        }
                    } else {
                        (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                    }
                    false
                }

                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )
    }

    @Composable
    fun AttemptsBarSortChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()
        val sortedRecords = throwRecords.sortedByDescending { it.count }

        val maxIndex = sortedRecords
            .mapIndexed { idx, rec -> idx to rec.count }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex
        val totalEntries = throwRecords.size
        var isZoomed by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                CombinedChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(false)
                    setHighlightPerDragEnabled(false)
                }
            },
            update = { chart ->

                val labelFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        val idx = barEntry?.x?.toInt() ?: return ""
                        return if (isZoomed || idx == maxIndex) {
                            barEntry.y.toInt().toString()
                        } else {
                            ""
                        }
                    }
                }

                val entries = sortedRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.count.toFloat())
                }

                val mean = sortedRecords.map { it.count }.average().toFloat()

                val barColors = sortedRecords.map { rec ->
                    if (rec.count >= mean)
                        AndroidColor.rgb(255, 165, 0)
                    else
                        primaryColor
                }

                val barDataSet = BarDataSet(entries, "Throws").apply {
                    colors = barColors
                    valueTextColor = primaryColor
                    valueTextSize = 12f
                    valueFormatter = labelFormatter
                }

                val progression = sortedRecords.map { it.count.toFloat() }
                    .runningFoldIndexed(0f) { index, acc, value ->
                        if (index == 0) value else ((acc * index) + value) / (index + 1)
                    }
                    .drop(1)

                val lineEntries = progression.mapIndexed { i, avg -> Entry(i.toFloat(), avg) }

                val combinedData = CombinedData().apply {
                    setData(BarData(barDataSet))
                }

                chart.data = combinedData
                chart.isDragEnabled = isZoomed
                chart.setMinOffset(0f)
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    axisMinimum = -1f
                    axisMaximum = (lastIndex) + 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    yOffset = 2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.roundToInt() + 1
                            return if (idx > 0 && idx < lastIndex + 2) "$idx" else ""
                        }
                    }
                }
                chart.isDragEnabled = true

                val maxY = sortedRecords.maxOfOrNull { it.count }?.toFloat() ?: 0f
                val stepOptions = listOf(1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(maxY / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val axisMax = ceil(maxY / chosenStep).toInt() * chosenStep
                val labelCount = (axisMax / chosenStep).toInt() + 1
                val acumuladaFinal = sortedRecords.map { it.count }
                    .runningFoldIndexed(0f) { idx, acc, valor ->
                        if (idx == 0) valor.toFloat()
                        else ((acc * idx) + valor) / (idx + 1)
                    }.last()

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawGridLines(true)
                    gridColor = AndroidColor.GRAY
                    textColor = primaryColor
                    textSize = 8f
                    setDrawAxisLine(false)
                    val mediaLine = LimitLine(acumuladaFinal, "").apply {
                        lineColor = AndroidColor.rgb(255, 165, 0)
                        lineWidth = 0.5f
                        textColor = AndroidColor.rgb(255, 165, 0)
                        textSize = 9f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }

                chart.setVisibleXRangeMaximum(1000f)
                chart.setMaxVisibleValueCount(1000)

                val gestureDetector = GestureDetector(chart.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        isZoomed = !isZoomed
                        if (isZoomed) {
                            chart.setVisibleXRangeMaximum(5f)
                            chart.moveViewToX((0).coerceAtLeast(0).toFloat())
                        } else {
                            chart.fitScreen()
                        }
                        chart.invalidate()
                        return true
                    }
                })

                chart.setOnTouchListener { v, event ->
                    // ✅ Sempre processa o gestureDetector para apanhar double tap
                    gestureDetector.onTouchEvent(event)

                    if (isZoomed) {
                        // Só no zoom in impede que o parent intercepte
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                                v.performClick()
                            }
                        }
                    } else {
                        // Zoom out -> permite que o parent intercepte arrastos
                        (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                    }
                    false // não consumir totalmente, deixar seguir
                }


                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )
    }

    @Composable
    fun AttemptsDurationBarSortChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()
        val sortedRecords = throwRecords.sortedByDescending { it.durationSeconds }

        val maxIndex = sortedRecords
            .mapIndexed { idx, rec -> idx to rec.durationSeconds }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex
        val totalEntries = throwRecords.size
        var isZoomed by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                CombinedChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(false)
                    setHighlightPerDragEnabled(false)
                }
            },
            update = { chart ->

                val labelFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        val idx = barEntry?.x?.toInt() ?: return ""
                        return if (isZoomed || idx == maxIndex) {
                            val value = barEntry.y
                            if (value >= 100f) {
                                "%.0f".format(value)
                            } else {
                                "%.1f".format(value)
                            }
                        } else {
                            ""
                        }
                    }
                }

                val entries = sortedRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.durationSeconds.toFloat())
                }

                val mean = sortedRecords.map { it.durationSeconds }.average().toFloat()

                val barColors = sortedRecords.map { rec ->
                    if (rec.durationSeconds >= mean)
                        AndroidColor.rgb(255, 165, 0) // laranja
                    else
                        primaryColor               // cor primária
                }

                val barDataSet = BarDataSet(entries, "Duration").apply {
                    colors = barColors
                    valueTextColor = primaryColor
                    valueTextSize = 12f
                    valueFormatter = labelFormatter
                }


                val combinedData = CombinedData().apply {
                    setData(BarData(barDataSet))
                }

                chart.data = combinedData
                chart.isDragEnabled = isZoomed
                chart.setMinOffset(0f)

                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    axisMinimum = -1f
                    axisMaximum = (lastIndex) + 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    yOffset = 2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.roundToInt() + 1
                            return if (idx > 0 && idx < lastIndex + 2) "$idx" else ""
                        }
                    }
                }

                val maxY = sortedRecords.maxOfOrNull { it.durationSeconds }?.toFloat() ?: 0f
                val stepOptions = listOf(1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(maxY / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val axisMax = ceil(maxY / chosenStep).toInt() * chosenStep
                val labelCount = (axisMax / chosenStep).toInt() + 1

                val acumuladaFinal = sortedRecords.map { it.durationSeconds }
                    .runningFoldIndexed(0f) { idx, acc, valor ->
                        if (idx == 0) valor.toFloat()
                        else ((acc * idx) + valor) / (idx + 1)
                    }.last()

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawGridLines(true)
                    gridColor = AndroidColor.GRAY
                    textColor = primaryColor
                    textSize = 8f
                    setDrawAxisLine(false)
                    val mediaLine = LimitLine(acumuladaFinal, "").apply {
                        lineColor = AndroidColor.rgb(255, 165, 0)
                        lineWidth = 0.5f
                        textColor = AndroidColor.rgb(255, 165, 0)
                        textSize = 9f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }

                chart.setVisibleXRangeMaximum(1000f)
                chart.setMaxVisibleValueCount(1000)

                val gestureDetector = GestureDetector(chart.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        isZoomed = !isZoomed
                        if (isZoomed) {
                            chart.setVisibleXRangeMaximum(5f)
                            chart.moveViewToX(0f)
                        } else {
                            chart.fitScreen()
                        }
                        chart.invalidate()
                        return true
                    }
                })

                chart.setOnTouchListener { v, event ->
                    gestureDetector.onTouchEvent(event)

                    if (isZoomed) {
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                                v.performClick()
                            }
                        }
                    } else {
                        (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                    }
                    false
                }

                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )
    }

    @Composable
    fun AttemptsTpsBarSortChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()
        val sortedRecords = throwRecords.sortedByDescending { it.frequency }

        val maxIndex = sortedRecords
            .mapIndexed { idx, rec -> idx to rec.frequency }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex
        val totalEntries = throwRecords.size
        var isZoomed by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                CombinedChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(false)
                    setHighlightPerDragEnabled(false)
                }
            },
            update = { chart ->

                val labelFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry?): String {
                        val idx = barEntry?.x?.toInt() ?: return ""
                        return if (isZoomed || idx == maxIndex) {
                            val value = barEntry.y
                            "%.1f".format(value)
                        } else {
                            ""
                        }
                    }
                }

                val entries = sortedRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.frequency.toFloat())
                }

                val mean = sortedRecords.map { it.frequency }.average().toFloat()

                val barColors = sortedRecords.map { rec ->
                    if (rec.frequency >= mean)
                        AndroidColor.rgb(255, 165, 0) // laranja
                    else
                        primaryColor               // cor primária
                }

                val barDataSet = BarDataSet(entries, "TPS").apply {
                    colors = barColors
                    valueTextColor = primaryColor
                    valueTextSize = 12f
                    valueFormatter = labelFormatter
                }


                val combinedData = CombinedData().apply {
                    setData(BarData(barDataSet))
                }

                chart.data = combinedData
                chart.isDragEnabled = isZoomed
                chart.setMinOffset(0f)

                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    axisMinimum = -1f
                    axisMaximum = (lastIndex) + 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    yOffset = 2f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val idx = value.roundToInt() + 1
                            return if (idx > 0 && idx < lastIndex + 2) "$idx" else ""
                        }
                    }
                }

                val maxY = sortedRecords.maxOfOrNull { it.frequency }?.toFloat() ?: 0f
                val stepOptions = listOf(1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(maxY / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val axisMax = ceil(maxY / chosenStep).toInt() * chosenStep
                val labelCount = (axisMax / chosenStep).toInt() + 1

                val acumuladaFinal = sortedRecords.map { it.frequency }
                    .runningFoldIndexed(0f) { idx, acc, valor ->
                        if (idx == 0) valor.toFloat()
                        else ((acc * idx) + valor) / (idx + 1)
                    }.last()

                chart.axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawGridLines(true)
                    gridColor = AndroidColor.GRAY
                    textColor = primaryColor
                    textSize = 8f
                    setDrawAxisLine(false)
                    val mediaLine = LimitLine(acumuladaFinal, "").apply {
                        lineColor = AndroidColor.rgb(255, 165, 0)
                        lineWidth = 0.5f
                        textColor = AndroidColor.rgb(255, 165, 0)
                        textSize = 9f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }

                chart.setVisibleXRangeMaximum(1000f)
                chart.setMaxVisibleValueCount(1000)

                val gestureDetector = GestureDetector(chart.context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        isZoomed = !isZoomed
                        if (isZoomed) {
                            chart.setVisibleXRangeMaximum(5f)
                            chart.moveViewToX(0f)
                        } else {
                            chart.fitScreen()
                        }
                        chart.invalidate()
                        return true
                    }
                })

                chart.setOnTouchListener { v, event ->
                    gestureDetector.onTouchEvent(event)

                    if (isZoomed) {
                        when (event.actionMasked) {
                            MotionEvent.ACTION_DOWN -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                                v.performClick()
                            }
                        }
                    } else {
                        (v.parent as? ViewGroup)?.requestDisallowInterceptTouchEvent(false)
                    }
                    false
                }

                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        )
    }


    /*@Composable
    fun AttemptsBarChart(throwRecords: List<ThrowRecord>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        val maxIndex = throwRecords
            .mapIndexed { idx, rec -> idx to rec.count }
            .maxByOrNull { it.second }
            ?.first ?: 0

        val lastIndex = throwRecords.lastIndex

        val labeledIndices = setOf(maxIndex, lastIndex)

        val labelFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String {
                val idx = barEntry?.x?.toInt() ?: return ""
                return if (idx in labeledIndices) barEntry.y.toInt().toString() else ""
            }
        }
        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        AndroidView(
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    // desliga zoom e drag
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                // 1) monta dados
                val entries = throwRecords.mapIndexed { i, rec ->
                    BarEntry(i.toFloat(), rec.count.toFloat())
                }
                val dataSet = BarDataSet(entries, "Throws").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 8f
                    valueFormatter = labelFormatter
                }
                chart.data = BarData(dataSet)

                // listener de clique
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        selectedIndex = e?.x?.toInt()
                    }
                    override fun onNothingSelected() { }
                })

                // 2) eixo X (sem grid vertical)
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val runs = value.toInt()+1
                            return "$runs"
                        }
                    }
                }

                // 3) Y dinâmico: escolhe step para ≤10 linhas
                val maxY = throwRecords.maxOfOrNull { it.count }?.toFloat() ?: 0f
                val stepOptions = listOf(1f,2f,5f,10f, 20f, 50f, 100f, 200f, 500f, 1000f,2000f,5000f,10000f)
                val chosenStep = stepOptions.firstOrNull { step ->
                    // ceil(maxY/step) = número de secções
                    (ceil(maxY / step).toInt() + 1) <= 10
                } ?: stepOptions.last()
                val nSections = ceil(maxY / chosenStep).toInt()            // N tal que N*step >= maxY
                val axisMax = nSections * chosenStep                       // topo do eixo Y
                val labelCount = nSections + 1                             // linhas = 0..N

                chart.axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = AndroidColor.DKGRAY
                    gridLineWidth = 1f
                    axisMinimum = 0f
                    axisMaximum = axisMax
                    isGranularityEnabled = true
                    granularity = chosenStep
                    setLabelCount(labelCount, true)
                    setDrawAxisLine(false)
                    setDrawLabels(true) // <- mostra os rótulos
                    textColor = primaryColor
                    textSize = 8f
                    setXOffset(0f)
                    val media = throwRecords.map { it.count }.average().toFloat()
                    val mediaLine = LimitLine(media, "Média").apply {
                        lineColor = AndroidColor.RED
                        lineWidth = 1.5f
                        textColor = AndroidColor.RED
                        textSize = 10f
                        enableDashedLine(10f, 10f, 0f)
                        labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
                    }
                    removeAllLimitLines()
                    addLimitLine(mediaLine)
                }


                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false
                chart.description.isEnabled = false

                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)

        )
        // 9) mostra Dialog ao clicar fora de Compose “inline”
        selectedIndex?.let { idx ->
            val value = throwRecords.getOrNull(idx)?.count ?: 0
            Dialog(
                onDismissRequest = { selectedIndex = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),
                    elevation = 16.dp,
                    modifier = Modifier.size(width = 60.dp, height = 38.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "$value",
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }*/


    @Composable
    fun MaxThrowBarChart(
        historyItems: List<HistoryItem>,
        maxBars: Int = 30
    ) {
        // 1) extrai todos os counts
        val allCounts = historyItems
            .flatMap { it.throwRecords.map { tr -> tr.count.toFloat() } }
        if (allCounts.isEmpty()) {
            Text(
                text = "Sem lançamentos para mostrar",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            return
        }

        // 2) escolhe o tamanho do bloco em número de attempts (≤ maxBars barras)
        val blockSizeCandidates = listOf(10, 20, 50, 100, 200, 500)
        val effectiveBlockSize = blockSizeCandidates.firstOrNull { block ->
            ceil(allCounts.size.toFloat() / block).toInt() <= maxBars
        } ?: blockSizeCandidates.last()

        // 3) agrupa em blocos e calcula o máximo de cada
        val maxima = allCounts
            .chunked(effectiveBlockSize)
            .map { chunk -> chunk.maxOrNull() ?: 0f }

        // 4) índice do bloco com o valor máximo global
        val globalMaxIndex = remember(maxima) {
            maxima
                .mapIndexed { idx, v -> idx to v }
                .maxByOrNull { it.second }
                ?.first
        } ?: 0

        // 5) monta as entradas
        val entries = maxima.mapIndexed { idx, value ->
            BarEntry(idx.toFloat(), value)
        }

        // 6) formatter para mostrar só no bloco com o máximo global
        val maxFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String =
                barEntry
                    ?.takeIf { it.x.toInt() == globalMaxIndex }
                    ?.y
                    ?.let { String.format("%.0f", it) }
                    ?: ""
        }

        // 7) estado para saber que barra foi clicada
        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        // 8) cor principal
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 16.dp, end = 8.dp),
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                // aplica dados
                val ds = BarDataSet(entries, "Máximo a cada $effectiveBlockSize runs").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 8f
                    valueFormatter = maxFormatter
                }
                chart.data = BarData(ds)

                // listener de clique
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        selectedIndex = e?.x?.toInt()
                    }
                    override fun onNothingSelected() { }
                })

                // eixo X com label rotacionado
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val runs = (value.toInt() + 1) * effectiveBlockSize
                            return "$runs"
                        }
                    }
                }

                val maxY = maxima.maxOrNull() ?: 0f
                val minY = maxima.minOrNull() ?: 0f

                val stepOptions = listOf(1f, 2f, 5f, 10f, 20f, 50f, 100f, 200f, 500f, 1000f)
                val range = maxY - minY
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(range / step).toInt() + 1) <= 5
                } ?: stepOptions.last()

                val lowerIndex = floor(minY / chosenStep).toInt().coerceAtLeast(0)
                val axisMin = lowerIndex * chosenStep

                val upperIndex = ceil(maxY / chosenStep).toInt()
                val axisMax = upperIndex * chosenStep

                val labelCount = upperIndex - lowerIndex + 1


                chart.axisLeft.apply {
                    axisMinimum = axisMin
                    axisMaximum = axisMax

                    setDrawGridLines(true)
                    gridColor    = AndroidColor.DKGRAY
                    gridLineWidth = 1f

                    isGranularityEnabled = true
                    granularity          = chosenStep
                    setLabelCount(labelCount, false)

                    setDrawLabels(true)
                    textColor = primaryColor
                    textSize  = 8f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }

                    setDrawAxisLine(false)
                    setXOffset(0f)
                }
                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false

                chart.invalidate()
            }
        )

        // 9) mostra Dialog ao clicar fora de Compose “inline”
        selectedIndex?.let { idx ->
            val value = maxima.getOrNull(idx)?.toInt() ?: 0
            Dialog(
                onDismissRequest = { selectedIndex = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),
                    elevation = 16.dp,
                    modifier = Modifier.size(width = 60.dp, height = 38.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "$value",
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun AverageThrowBarChart(
        historyItems: List<HistoryItem>,
        maxBars: Int = 30
    ) {
        // 1) extrai todos os counts como Float
        val allCounts = historyItems
            .flatMap { it.throwRecords.map { tr -> tr.count.toFloat() } }
        if (allCounts.isEmpty()) {
            Text(
                text = "no data",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
            return
        }

        // 2) escolhe blockSize para caber em maxBars barras
        val blockSizeCandidates = listOf(10, 20, 50, 100, 200, 500)
        val effectiveBlockSize = blockSizeCandidates.firstOrNull { block ->
            ceil(allCounts.size.toFloat() / block).toInt() <= maxBars
        } ?: blockSizeCandidates.last()

        // 3) agrupa e calcula média de cada bloco
        val averages = allCounts
            .chunked(effectiveBlockSize)
            .map { it.average().toFloat() }

        // 4) índice da maior média para mostrar só nela o label
        val maxIndex = remember(averages) {
            averages
                .mapIndexed { i, v -> i to v }
                .maxByOrNull { it.second }
                ?.first
        } ?: 0

        // 5) monta as entradas
        val entries = averages.mapIndexed { i, avg ->
            BarEntry(i.toFloat(), avg)
        }

        // 6) formatter de label de barra (só no maxIndex)
        val avgFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String =
                barEntry
                    ?.takeIf { it.x.toInt() == maxIndex }
                    ?.y
                    ?.let { String.format("%.1f", it) }
                    ?: ""
        }

        // 7) estado para saber qual barra foi tocada
        var selectedIndex by remember { mutableStateOf<Int?>(null) }

        // 8) cor principal
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 16.dp, end = 8.dp),
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                // seta dados
                val ds = BarDataSet(entries, "Média a cada $effectiveBlockSize runs").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 8f
                    valueFormatter = avgFormatter
                }
                chart.data = BarData(ds)

                // click listener
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        selectedIndex = e?.x?.toInt()
                    }
                    override fun onNothingSelected() { }
                })

                // eixo X
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    labelRotationAngle = 90f
                    textColor = primaryColor
                    textSize = 8f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val runs = (value.toInt() + 1) * effectiveBlockSize
                            return "$runs"
                        }
                    }
                }

                // eixo Y dinâmico em [minY, maxY]
                val maxY = averages.maxOrNull() ?: 0f
                val minY = averages.minOrNull() ?: 0f
                val stepOptions = listOf(1f,2f,5f,10f,20f,50f,100f,200f,500f,1000f)
                val range = maxY - minY
                val chosenStep = stepOptions.firstOrNull { step ->
                    (ceil(range / step).toInt() + 1) <= 5
                } ?: stepOptions.last()
                val lowerIndex = floor(minY / chosenStep).toInt().coerceAtLeast(0)
                val upperIndex = ceil(maxY / chosenStep).toInt()
                val axisMin = lowerIndex * chosenStep
                val axisMax = upperIndex * chosenStep
                val labelCount = upperIndex - lowerIndex + 1

                chart.axisLeft.apply {
                    axisMinimum = axisMin
                    axisMaximum = axisMax

                    setDrawGridLines(true)
                    gridColor = AndroidColor.DKGRAY
                    gridLineWidth = 1f

                    isGranularityEnabled = true
                    granularity = chosenStep
                    setLabelCount(labelCount, false)

                    setDrawLabels(true)
                    textColor = primaryColor
                    textSize = 8f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }

                    setDrawAxisLine(false)
                    setXOffset(0f)
                }

                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false
                chart.invalidate()
            }
        )

        // 9) Dialog inline ao tocar na barra
        selectedIndex?.let { idx ->
            val value = averages.getOrNull(idx)?.toInt() ?: 0
            Dialog(
                onDismissRequest = { selectedIndex = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xCC363F4C),
                    elevation = 16.dp,
                    modifier = Modifier.size(width = 60.dp, height = 38.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = String.format("%.1f", value.toFloat()),
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    /*@Composable
    fun AverageThrowBarChart(historyItems: List<HistoryItem>) {
        val primaryColor = MaterialTheme.colors.primary.toArgb()

        // Top 5 valores de averageThrow para mostrar só neles o bar-label
        val bestIndices = remember(historyItems) {
            historyItems
                .mapIndexed { index, item -> index to item.averageThrow }
                .sortedByDescending { it.second }
                .take(5)
                .map { it.first }
                .toSet()
        }

        val floatValueFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String {
                val idx = barEntry?.x?.toInt() ?: return ""
                return if (idx in bestIndices) {
                    "%.1f".format(barEntry.y)
                } else ""
            }
        }

        AndroidView(
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setDrawValueAboveBar(true)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    setDoubleTapToZoomEnabled(false)
                    isDragEnabled = false
                    setHighlightPerTapEnabled(true)
                }
            },
            update = { chart ->
                // monta dados
                val entries = historyItems.mapIndexed { i, item ->
                    BarEntry(i.toFloat(), item.averageThrow)
                }
                val dataSet = BarDataSet(entries, "Avg Throw").apply {
                    color = primaryColor
                    valueTextColor = primaryColor
                    valueTextSize = 8f
                    valueFormatter = floatValueFormatter
                }
                chart.data = BarData(dataSet)

                // eixo X sem grid vertical
                chart.xAxis.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    isGranularityEnabled = true
                    granularity = 1f
                    position = XAxis.XAxisPosition.BOTTOM
                }

                // cálculo dinâmico do eixo Y
                val maxY = historyItems.maxOfOrNull { it.averageThrow } ?: 0f
                val steps = listOf(10f,20f,50f,100f,200f,500f,1000f)
                val chosenStep = steps.firstOrNull { step ->
                    (ceil(maxY/step).toInt()+1) <= 10
                } ?: steps.last()
                val sections = ceil(maxY/chosenStep).toInt()
                val axisMax = sections * chosenStep
                val labelCount = sections+1

                // eixo Y com grid e labels
                chart.axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = AndroidColor.DKGRAY
                    gridLineWidth = 1f

                    axisMinimum = 0f
                    axisMaximum = axisMax

                    isGranularityEnabled = true
                    granularity = chosenStep

                    setLabelCount(labelCount, true)

                    // *** mostrar labels no eixo Y ***
                    setDrawLabels(true)
                    textColor = primaryColor
                    textSize = 8f

                    // opcional: formatar casas decimais
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "%.0f".format(value)
                        }
                    }

                    setDrawAxisLine(false)
                    setXOffset(0f)
                }

                chart.axisRight.isEnabled = false
                chart.legend.isEnabled = false
                chart.description.isEnabled = false

                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }*/




    private fun vibrateHalfSecond() {
        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ (Wear OS 3+) usa VibratorManager
            val vm = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            // versões mais antigas
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // não procede se não houver motor de vibração
        if (!vib.hasVibrator()) return

        // sempre chamar na UI thread
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(
                    VibrationEffect.createOneShot(
                        150,  // meio segundo
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(250L)
            }
        }
    }

    // Registros do sensor e funções de gerenciamento

    override fun onResume() {
        super.onResume()

        accelerometer?.let { sensor ->
            // usa o delay mínimo suportado pelo hardware (µs)
            val minDelayUs = 10_000//sensor.minDelay.takeIf { it > 0 } ?: 50_000
            sensorManager.registerListener(
                this,              // SensorEventListener (MainActivity)
                sensor,            // o acelerómetro
                minDelayUs,        // samplingPeriodUs
                minDelayUs,        // maxReportLatencyUs
                sensorHandler      // callbacks em HandlerThread
            )
        }

        /*accelerometer?.also { sensor ->
            //sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            //sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }*/
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }



    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            //latestX = it.values[0]
            //latestY = it.values[1]
            latestZ = it.values[2]
            //zFiltered += alpha * (latestZ - zFiltered)
            zWindow.addLast(latestZ)
            while (zWindow.size > windowSize+1) {
                zWindow.removeFirst()
            }
            zFiltered = zWindow.sum() / zWindow.size
            // atualiza mínimos e máximos
            /*minX = minOf(minX, latestX);  maxX = maxOf(maxX, latestX)
            minY = minOf(minY, latestY);  maxY = maxOf(maxY, latestY)*/
            minZ = minOf(minZ, zFiltered);  maxZ = maxOf(maxZ, zFiltered)

            //sensorSamples.add(SensorSample(System.currentTimeMillis(), latestX, latestY, latestZ))
            // opcional: limite de buffer
            //if (sensorSamples.size > 200) sensorSamples.removeAt(0)

            val currentTime = System.currentTimeMillis()
            val diffTime=currentTime - lastSensorUpdateTime
            val lowth=-thresholdZ+2*thresholdZ*gapZ/100
            if (flankCount == 0) {
                seriesStartTime = currentTime
                arrayDiffs.clear()
            }
            /*if(swapZ==0){
                if (latestZ < -thresholdZ && !wasBelowZ && diffTime > 50) {
                    arrayDiffs.add(diffTime)
                    lastSensorUpdateTime = currentTime
                    flankCount += throwMultiplier
                    wasBelowZ = true
                }
                if (latestZ > thresholdZ-2*thresholdZ*gapZ/100) {
                    wasBelowZ = false
                }
            }
            else {
                if (latestZ > thresholdZ && !wasBelowZ && diffTime > 50) {
                    arrayDiffs.add(diffTime)
                    lastSensorUpdateTime = currentTime
                    flankCount += throwMultiplier
                    wasBelowZ = true
                }
                if (latestZ < -thresholdZ+2*thresholdZ*gapZ/100) {
                    wasBelowZ = false
                }
            }*/

            when (throwPhase) {
                0 -> {
                    if (zFiltered < 5f) {
                        throwPhase = 1
                    }
                }
                1 -> {
                    if (zFiltered > thresholdZ) {
                        throwPhase = 2
                    }
                }
                2 -> {
                    if (zFiltered < lowth) {
                        if (diffTime > 200) {
                            flankCount += throwMultiplier
                        }
                        lastSensorUpdateTime = currentTime
                        throwPhase = 1    // reset para fase 1, permitindo ciclo contínuo
                    }
                }
            }

            /*when (throwPhase) {
                0 -> {
                    if (latestZ > thresholdZ ) {
                        throwPhase = 1
                    }
                    if (latestZ < thresholdZ ) {
                        lastSensorUpdateTime = currentTime
                    }
                }
                1 -> {
                    if (latestZ < thresholdZ) {
                        throwPhase = 2
                    }
                }
                2 -> {
                    if (latestZ > thresholdZ ) {
                        throwPhase = 1
                        lastSensorUpdateTime = currentTime
                    }
                    if (latestZ < lowth) {
                        throwPhase = 3
                    }
                }
                3 -> {
                    if (latestZ > lowth) {

                        if(currentTime - lastSensorUpdateTime > 250){
                            flankCount += throwMultiplier
                        }
                        lastSensorUpdateTime = currentTime
                        throwPhase = 0
                    }
                }

            }*/
            val diffMs = lastAnnouncedTime.let { currentTime - it } ?: 0L
            val durationMs = seriesStartTime?.let { currentTime - it } ?: 0L
            val durationSec = ((durationMs) / 1000f)
            //accelerometerReading = "Tosses: ${flankCount}\nTime: ${"%.1f".format(durationSec)}"//"${"%.1f".format(minZ)}:${"%.1f".format(maxZ)}"
            accelerometerReading = "${flankCount}"//"${"%.1f".format(minZ)}:${"%.1f".format(maxZ)}"




            if (flankCount > 0){


                if (swapCount == 1){ //time
                    /*val diffMs = lastAnnouncedTime.let { currentTime - it } ?: 0L
                    val durationMs = seriesStartTime?.let { currentTime - it } ?: 0L
                    val durationSec = ((durationMs) / 1000f)*/
                    if (durationSec>1 && durationSec.toInt() % timeInterval == 0 && diffMs>1000
                    ){
                        lastAnnouncedTime = currentTime
                        if (swapAudio == 1){ //vibrate
                            vibrateHalfSecond()
                        }
                        if (swapAudio == 2) { //audio high
                            announceTime(durationSec.toInt())
                        }
                    }
                }
                if (swapCount == 0){ //throws
                    if (flankCount % throwsInterval == 0 && flankCount != lastAnnouncedCount
                    ) {
                        lastAnnouncedCount = flankCount
                        if (swapAudio == 1){ //vibrate
                            vibrateHalfSecond()
                        }
                        if (swapAudio == 2) { //audio high
                            announceFlankCount(flankCount)
                        }
                    }
                }
                if (swapCount == 2){ //tps
                    if (swapAudio == 2) { //audio high
                        if (!isMetronomeRunning) { //metronomo start
                            isMetronomeRunning = true
                            metronome.start((tpsInterval * 60).toInt())
                        }
                    }
                }



                //metronome
                if (swapMetronome==1 && !isMetronomeRunning) { //metronomo start
                    isMetronomeRunning = true
                    metronome.start(bpm)
                }
            }

            /*if (flankCount > 0
                && flankCount % 20 == 0
                && flankCount != lastAnnouncedCount
            ) {
                lastAnnouncedCount = flankCount

                //announceFlankCount(flankCount)
            }*/
            /*if (flankCount > 0){
                val durationMs = seriesStartTime?.let { currentTime - it } ?: 0L
                val durationSec = ((durationMs) / 1000f).toInt()
                if (durationSec>1 && durationSec % 5 == 0 && flankCount != lastAnnouncedCount
                ){
                    lastAnnouncedCount = flankCount
                    //announceTime(durationSec)
                    //vibrateHalfSecond()
                }
            }*/
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        if (sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            if (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                isAccelReliable=0;
            }
            if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
                isAccelReliable=1;
            }
            if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM){
                isAccelReliable=2;
            }
            if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH){
                isAccelReliable=3;
            }
            /*if (isAccelReliable<2){
                resetAccelerometer()
            }*/

        }
    }

    private fun resetAccelerometer() {
        sensorManager.unregisterListener(this)  // remove listener
        sensorHandler.postDelayed({
            accelerometer?.let { sensor ->
                val minDelayUs = 10_000
                sensorManager.registerListener(
                    this,
                    sensor,
                    minDelayUs,
                    minDelayUs,      // sem batching
                    sensorHandler
                )
            }
        }, 200L)
        Toast.makeText(this, "Sensor Reset!", Toast.LENGTH_SHORT).show()
    }

    private fun saveThrowMultiplier(newMultiplier: Int) {
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit()
            .putInt("throwMultiplier", newMultiplier)
            .apply()
        throwMultiplier = newMultiplier
    }

    private fun deleteHistoryItem(historyItem: HistoryItem, context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingJson = prefs.getString("history", "[]")
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableListOf()

        // Filtra todos os que NÃO têm a mesma data e nota
        val filteredHistory = existingHistory.filter {
            it.dateTime != historyItem.dateTime || it.note != historyItem.note
        }

        val newJson = gson.toJson(filteredHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }


    private fun deleteHistoryByNote(note: String, context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingJson = prefs.getString("history", "[]")
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableListOf()
        // Mantém apenas os items cuja nota seja diferente da selecionada
        val filteredHistory = existingHistory.filter { it.note != note }
        val newJson = gson.toJson(filteredHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }


    /*private fun saveHistoryItem(throwRecords: List<ThrowRecord>, note: String) {
        val dateTimeStr = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()).format(Date())
        val newItem = HistoryItem(dateTime = dateTimeStr, throwRecords = throwRecords, note = note)
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val gson = Gson()
        val existingJson = prefs.getString("history", "[]")
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableStateListOf()
        existingHistory.add(newItem)
        val newJson = gson.toJson(existingHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }*/

    /*private fun saveHistoryItem(throwRecords: List<ThrowRecord>, note: String) {
        // Calcula os valores agregados
        val attempts = throwRecords.size
        val totalThrows = throwRecords.sumOf { it.count }
        val maxThrow = throwRecords.maxOfOrNull { it.count } ?: 0
        val averageThrow = if (attempts > 0) totalThrows.toFloat() / attempts else 0f
        // Para calcular o TPS (lancamentos por segundo), converta os valores para Double
        val totalDuration = throwRecords.sumOf { it.durationSeconds.toDouble() }
        val tps = if (totalDuration > 0) totalThrows.toFloat() / totalDuration.toFloat() else 0f

        // Cria uma string com a data/hora atual
        val dateTimeStr = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()).format(Date())

        // Cria um novo HistoryItem com os valores agregados
        val newItem = HistoryItem(
            dateTime = dateTimeStr,
            throwRecords = throwRecords,
            note = note,
            attempts = attempts,
            totalThrows = totalThrows,
            maxThrow = maxThrow,
            averageThrow = averageThrow,
            tps = tps,
            jugglerName="PT"
        )

        // Salva o novo item no SharedPreferences
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val gson = Gson()
        val existingJson = prefs.getString("history", "[]")
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableStateListOf()
        existingHistory.add(newItem)
        val newJson = gson.toJson(existingHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }*/
    private fun decryptPass(encrypted: String): String {
        // offsets usados na encriptação: +7, +5, +3, +1
        val offsets = listOf(7, 5, 3, 1)
        // garanta que tem exatamente 4 chars; se não, retorne empty ou trate como quiser
        if (encrypted.length != 4) return ""
        return encrypted.mapIndexed { idx, c ->
            // pega o código, subtrai o offset, converte de volta
            (c.code - offsets[idx]).toChar()
        }.joinToString("")
    }


    private fun saveHistoryItem(throwRecords: List<ThrowRecord>, note: String) {
        val attempts = throwRecords.size
        val totalThrows = throwRecords.sumOf { it.count }
        val maxThrow = throwRecords.maxOfOrNull { it.count } ?: 0
        val averageThrow = if (attempts > 0) totalThrows.toFloat() / attempts else 0f
        val totalDuration = throwRecords.sumOf { it.durationSeconds.toDouble() }
        val tps = if (totalDuration > 0) totalThrows.toFloat() / totalDuration.toFloat() else 0f
        val dateTimeStr = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()).format(Date())

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val gson = Gson()
        val existingJson = prefs.getString("history", "[]")
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val existingHistory: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableListOf()

        // Obtém o jugglerName atual ou "Juggler" se ainda não existir histórico
        val jugglerName     = prefs.getString("juggler_name", "") ?: ""
        val jugglerNickName = prefs.getString("juggler_nickname", "")      ?: ""
        val jugglerPass     = prefs.getString("juggler_pass", "")          ?: ""
        //val jugglerNickName = existingHistory.firstOrNull()?.jugglerName ?: ""
        //val jugglerPass = existingHistory.firstOrNull()?.jugglerName ?: ""


        val durations = throwRecords.map { it.durationSeconds }
        val totalDur = durations.sum()
        val maxDur   = durations.maxOrNull() ?: 0f
        val avgDur   = if (durations.isNotEmpty()) durations.average().toFloat() else 0f

        val newItem = HistoryItem(
            dateTime = dateTimeStr,
            throwRecords = throwRecords,
            note = note,
            attempts = attempts,
            totalThrows = totalThrows,
            maxThrow = maxThrow,
            averageThrow = averageThrow,
            tps = tps,
            jugglerName = jugglerName,
            jugglerNickName = jugglerNickName,
            jugglerPass = jugglerPass,
            totalDuration = totalDur,
            maxDuration = maxDur,
            averageDuration = avgDur
        )

        existingHistory.add(newItem)
        val newJson = gson.toJson(existingHistory, type)
        prefs.edit().putString("history", newJson).apply()
    }



    fun loadHistoryItems(context: Context): List<HistoryItem> {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        val historyJson = prefs.getString("history", "[]") ?: "[]"
        return gson.fromJson(historyJson, type) ?: emptyList()
    }

    private fun resetCounter() {
        val endTime = System.currentTimeMillis()
        val durationMs = seriesStartTime?.let { endTime - it } ?: 0L
        val durationSec = (durationMs - 1600) / 1000f
        lastDiffs.clear()
        lastDiffs.addAll(arrayDiffs.map { (it / 10).toInt() })
        arrayDiffs.clear()
        zFiltered=0f;

        //Toast.makeText(this, "Sensor Status ${isAccelReliable}", Toast.LENGTH_SHORT).show()
        //showDiffsDialog = true
        throwPhase = 0
        val frequency = if (durationSec > 0) flankCount / durationSec else 0f
        recordedRecords.add(ThrowRecord(count = flankCount, durationSeconds = durationSec, frequency = frequency))
        saveRecordedRecords()

        // constrói dados do gráfico para a última sessão
        lastSessionSamples.clear()
        lastSessionSamples.addAll(sensorSamples)
        // agora reseta o buffer para a próxima série
        sensorSamples.clear()
        if(swapAudio==2) {
            if(swapCount==1) { //time
                announceFinalTime(durationSec)
            }
            if(swapCount==0) { //throws
                announceFinalFlankCount(flankCount)
            }
        }
        //announceFlankCount(flankCount)

        metronome.stop()
        isMetronomeRunning = false


        lastAnnouncedCount = 0

        flankCount = 0
        wasBelowZ = false
        accelerometerReading = "T: $flankCount"
        lastSensorUpdateTime = System.currentTimeMillis()
        seriesStartTime = null
        //resetAccelerometer()
    }

    private fun deleteLastRecord() {
        if (recordedRecords.isNotEmpty()) {
            recordedRecords.removeAt(recordedRecords.lastIndex)
            saveRecordedRecords()
        }
        /*thresholdZ = minZ * 0.7f
        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit()
            .putFloat("thresholdZ", thresholdZ)
            .apply()*/
        //minX = Float.MAX_VALUE;  maxX = Float.MIN_VALUE
        //minY = Float.MAX_VALUE;  maxY = Float.MIN_VALUE
        //minZ = Float.MAX_VALUE;  maxZ = Float.MIN_VALUE


        lastSessionSamples.clear()
        flankCount = 0
        wasBelowZ = false
        accelerometerReading = "T: $flankCount"
        lastSensorUpdateTime = System.currentTimeMillis()
        seriesStartTime = null
    }

    private fun resetList(note: String) {
        if (recordedRecords.isNotEmpty()) {
            saveHistoryItem(recordedRecords, note)
        }
        recordedRecords.clear()
        saveRecordedRecords()
        flankCount = 0
        wasBelowZ = false
        accelerometerReading = "T: $flankCount"
        lastSensorUpdateTime = System.currentTimeMillis()
        seriesStartTime = null
    }

    private fun saveLastEntry(note: String) {
        if (recordedRecords.isNotEmpty()) {
            val lastRecord = recordedRecords.last()
            saveHistoryItem(listOf(lastRecord), note)
            recordedRecords.removeAt(recordedRecords.lastIndex)
            saveRecordedRecords()
        }
        flankCount = 0
        wasBelowZ = false
        accelerometerReading = "T: $flankCount"
        lastSensorUpdateTime = System.currentTimeMillis()
        seriesStartTime = null
    }

    private fun saveSelectedEntries(
        selectedIndices: List<Int>,
        note: String
    ) {
        if (selectedIndices.isNotEmpty()) {

            val selectedRecords = selectedIndices
                .sorted()
                .map { index -> recordedRecords[index] }

            saveHistoryItem(selectedRecords, note)

            selectedIndices.sortedDescending().forEach { index ->
                recordedRecords.removeAt(index)
            }

            saveRecordedRecords()
        }
        flankCount = 0
        wasBelowZ = false
        accelerometerReading = "T: $flankCount"
        lastSensorUpdateTime = System.currentTimeMillis()
        seriesStartTime = null
    }

    private fun incrementLastRecord() {
        if (recordedRecords.isNotEmpty()) {
            val idx = recordedRecords.lastIndex
            val old = recordedRecords[idx]
            val newCount = old.count + throwMultiplier
            // recalcula frequência com base na mesma duração
            val newFreq = if (old.durationSeconds > 0f) newCount / old.durationSeconds else 0f
            recordedRecords[idx] = old.copy(count = newCount, frequency = newFreq)
            saveRecordedRecords()
        }
    }

    private fun decrementLastRecord() {
        if (recordedRecords.isNotEmpty()) {
            val idx = recordedRecords.lastIndex
            val old = recordedRecords[idx]
            if (old.count > 1) {
                val newCount = old.count - throwMultiplier
                val newFreq = if (old.durationSeconds > 0f) newCount / old.durationSeconds else 0f
                recordedRecords[idx] = old.copy(count = newCount, frequency = newFreq)
                saveRecordedRecords()
            }
        }
    }


    private fun sendMessageToPhone(path: String, message: String) {
        Wearable.getNodeClient(this)
            .connectedNodes
            .addOnSuccessListener { nodes: List<Node> ->
                for (node in nodes) {
                    Wearable.getMessageClient(this)
                        .sendMessage(node.id, path, message.toByteArray())
                        .addOnSuccessListener {
                            Log.d("WearMainAct", "Mensagem enviada para ${node.displayName}")
                            Toast.makeText(this, "Mensagem enviada para ${node.displayName}", Toast.LENGTH_SHORT).show()

                        }
                        .addOnFailureListener { e ->
                            Log.e("WearMainAct", "Erro ao enviar mensagem", e)
                            Toast.makeText(this, "Erro ao enviar mensagem", Toast.LENGTH_SHORT).show()

                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("WearMainAct", "Erro ao obter nós", e)
            }
    }

    @Composable
    fun RecordedValuesList(
        recordedRecords: List<ThrowRecord>,
        swapCount: Int,
        swapDisplay: Int,
        swapGraph: Int,
        saveAll: Boolean,
        onSaveAllChange: (Boolean) -> Unit,
        showNoteDialog: Boolean,
        onShowNoteDialogChange: (Boolean) -> Unit,
        onIncrementLast: () -> Unit,
        onDecrementLast: () -> Unit,
    ) {
        val reversedList = recordedRecords.reversed()

        /*Text(
            text = "",
            fontSize = 4.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp),
        )*/



        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(vertical = 0.dp,horizontal = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.DarkGray),
        ) {*/
        if (swapDisplay==1){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly, // espaços iguais antes, entre e depois
                horizontalAlignment = Alignment.CenterHorizontally // centraliza horizontalmente
            ) {


                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(18.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wos_attempts),
                        contentDescription = "Attempts",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }

                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(18.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wos_histogram),
                        contentDescription = "Attempts",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(18.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wos_progress),
                        contentDescription = "Attempts",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (reversedList.isNotEmpty()) {
                    AttemptsBarChart(recordedRecords)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly, // espaços iguais antes, entre e depois
                horizontalAlignment = Alignment.CenterHorizontally // centraliza horizontalmente
            ) {
                Button(
                    onClick = onIncrementLast,
                    modifier = Modifier
                        .size(20.dp)
                        .weight(0.5f),
                    shape = CircleShape,
                    enabled = !isScreenBlocked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "+", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                }
                Button(
                    onClick = onDecrementLast,
                    modifier = Modifier
                        .size(20.dp)
                        .weight(0.5f),
                    shape = CircleShape,
                    enabled = !isScreenBlocked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "-", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                }
                Button(
                    onClick = { },
                    modifier = Modifier
                        .size(20.dp)
                        .weight(0.5f),
                    shape = CircleShape,
                    enabled = !isScreenBlocked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "x", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                }
            }
        }

        if (swapDisplay==0){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly, // espaços iguais antes, entre e depois
                horizontalAlignment = Alignment.CenterHorizontally // centraliza horizontalmente
            ) {


                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(18.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wos_attempts),
                        contentDescription = "Attempts",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }

                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(18.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wos_histogram),
                        contentDescription = "Attempts",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .size(18.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wos_progress),
                        contentDescription = "Attempts",
                        modifier = Modifier.fillMaxSize(),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (reversedList.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 0.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(50))
                            .background(Color.DarkGray)
                            .clickable {
                                onSaveAllChange(false)
                                onShowNoteDialogChange(true)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${reversedList.size}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(1.3f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                        )
                        Text(
                            text = "${"%4d".format(reversedList[0].count)}t",
                            color = MaterialTheme.colors.primary,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                        )
                        Text(
                            text = if (reversedList[0].durationSeconds > 600) {
                                reversedList[0].durationSeconds.toTimeString()
                            } else {
                                "%.1f".format(reversedList[0].durationSeconds) + "s"
                            },
                            color = MaterialTheme.colors.primary,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                        )
                        Text(
                            text = buildAnnotatedString {
                                append("%.1f".format(reversedList[0].frequency))
                                withStyle(style = SpanStyle(fontSize = 6.sp)) {
                                    append("tps")
                                }
                            },
                            color = MaterialTheme.colors.primary,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.save_arrow),
                            contentDescription = "Save Run",
                            modifier = Modifier
                                .size(10.dp)
                                .weight(1.3f),
                            tint = MaterialTheme.colors.primary
                        )

                    }
                }
                reversedList.forEachIndexed { index, record ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 0.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (index>0) {
                            Text(
                                text = "${reversedList.size - index}",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1.3f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                ),

                                )
                            //if (swapCount == 1) { //throws
                            Text(
                                text = "${"%4d".format(record.count)}t",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(2f),
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                ),
                            )
                            //}
                            //if (swapCount == 0) { //time
                            Text(
                                text = if (record.durationSeconds > 600) {
                                    record.durationSeconds.toTimeString()
                                } else {
                                    "%.1f".format(record.durationSeconds) + "s"
                                },
                                color     = Color.White,
                                fontSize  = 10.sp,
                                modifier  = Modifier.weight(2f),
                                textAlign = TextAlign.Center,
                                style     = MaterialTheme.typography.body2.copy(
                                    fontSize    = 10.sp,
                                    lineHeight  = 12.sp
                                )
                            )
                            //}
                            //if (swapCount == 2) { //tps
                            Text(
                                text = buildAnnotatedString {
                                    append("%.1f".format(record.frequency))
                                    withStyle(style = SpanStyle(fontSize = 6.sp)) {
                                        append("tps")
                                    }
                                },
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(2f),
                                textAlign = TextAlign.Right,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                ),
                            )
                            //}

                            Text(
                                text="",
                                modifier= Modifier
                                    .size(10.dp)
                                    .weight(1.3f),
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly, // espaços iguais antes, entre e depois
                horizontalAlignment = Alignment.CenterHorizontally // centraliza horizontalmente
            ) {
                Button(
                    onClick = onIncrementLast,
                    modifier = Modifier
                        .size(20.dp)
                        .weight(0.5f),
                    shape = CircleShape,
                    enabled = !isScreenBlocked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "+", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                }
                Button(
                    onClick = onDecrementLast,
                    modifier = Modifier
                        .size(20.dp)
                        .weight(0.5f),
                    shape = CircleShape,
                    enabled = !isScreenBlocked,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "-", fontSize = 20.sp, color = MaterialTheme.colors.primary)
                }
            }
        }




        //}
        /*Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (reversedList.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 0.dp)
                        .fillMaxWidth(0.7f)
                        .clip(RoundedCornerShape(50))
                        .background(Color.DarkGray)
                        .clickable {
                            onSaveAllChange(false)
                            onShowNoteDialogChange(true)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${reversedList.size}",
                        color = MaterialTheme.colors.primary,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(1.3f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                    )
                    Text(
                        text = "${"%4d".format(reversedList[0].count)}t",
                        color = MaterialTheme.colors.primary,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                    )
                    Text(
                        text = if (reversedList[0].durationSeconds > 600) {
                            reversedList[0].durationSeconds.toTimeString()
                        } else {
                            "%.1f".format(reversedList[0].durationSeconds) + "s"
                        },
                        color = MaterialTheme.colors.primary,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("%.1f".format(reversedList[0].frequency))
                            withStyle(style = SpanStyle(fontSize = 6.sp)) {
                                append("tps")
                            }
                        },
                        color = MaterialTheme.colors.primary,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(2f),
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp, lineHeight = 12.sp),
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.save_arrow),
                        contentDescription = "Save Run",
                        modifier = Modifier
                            .size(10.dp)
                            .weight(1.3f),
                        tint = MaterialTheme.colors.primary
                    )

                }
            }
            reversedList.forEachIndexed { index, record ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 0.dp)
                        .fillMaxWidth(0.7f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (index>0) {
                        Text(
                            text = "${reversedList.size - index}",
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.weight(1.3f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2.copy(
                                fontSize = 10.sp,
                                lineHeight = 12.sp
                            ),

                            )
                        //if (swapCount == 1) { //throws
                            Text(
                                text = "${"%4d".format(record.count)}t",
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(2f),
                                textAlign = TextAlign.Left,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                ),
                            )
                        //}
                        //if (swapCount == 0) { //time
                            Text(
                                text = if (record.durationSeconds > 600) {
                                    record.durationSeconds.toTimeString()
                                } else {
                                    "%.1f".format(record.durationSeconds) + "s"
                                },
                                color     = Color.White,
                                fontSize  = 10.sp,
                                modifier  = Modifier.weight(2f),
                                textAlign = TextAlign.Center,
                                style     = MaterialTheme.typography.body2.copy(
                                    fontSize    = 10.sp,
                                    lineHeight  = 12.sp
                                )
                            )
                        //}
                        //if (swapCount == 2) { //tps
                            Text(
                                text = buildAnnotatedString {
                                    append("%.1f".format(record.frequency))
                                    withStyle(style = SpanStyle(fontSize = 6.sp)) {
                                        append("tps")
                                    }
                                },
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(2f),
                                textAlign = TextAlign.Right,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                ),
                            )
                        //}

                            Text(
                                text="",
                                modifier= Modifier
                                    .size(10.dp)
                                    .weight(1.3f),
                            )
                    }
                }
            }
        }*/
    }




    private fun loadDefaultHistoryJson(): String {
        return resources
            .openRawResource(R.raw.gouveia)   // substitui “default_history” pelo nome do teu ficheiro
            .bufferedReader()
            .use { it.readText() }
    }

    fun migrateHistoryWithNewFields(context: Context) {
        // 1) pega o SharedPreferences e o JSON antigo
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson  = Gson()
        val type  = object : TypeToken<MutableList<HistoryItem>>() {}.type
        val oldJson = prefs.getString("history", "[]") ?: "[]"

        // 2) desserializa para lista mutável
        val oldList: MutableList<HistoryItem> = gson.fromJson(oldJson, type)

        // 3) faz o map para adicionar os novos campos (aqui como vazio, ou você pode gerar valores)
        val newList = oldList.map { hi ->
            hi.copy(
                jugglerName = "",   // padrão ou buscar de algum outro lugar
                jugglerNickName = "",   // padrão ou buscar de algum outro lugar
                jugglerPass     = ""    // idem
            )
        }

        // 4) serializa de volta e grava no prefs
        val newJson = gson.toJson(newList, type)
        prefs.edit().putString("history", newJson).apply()
    }

    // 2) Migração no onCreate (antes de setContent)
    fun migrateHistoryWithDurationFields(context: Context) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type

        val existingJson = prefs.getString("history", "[]") ?: "[]"
        val list: MutableList<HistoryItem> = gson.fromJson(existingJson, type) ?: mutableListOf()

        var changed = false
        val migrated = list.map { hi ->
            // só migra quem ainda tiver totalDuration == 0 (assumindo que já tenha sido calculado antes)
            if (hi.totalDuration == 0f) {
                val durations = hi.throwRecords.map { it.durationSeconds }
                val totalDur = durations.sum()
                val maxDur   = durations.maxOrNull() ?: 0f
                val avgDur   = if (durations.isNotEmpty()) durations.average().toFloat() else 0f
                changed = true
                hi.copy(
                    totalDuration   = totalDur,
                    maxDuration     = maxDur,
                    averageDuration = avgDur
                )
            } else {
                hi
            }
        }

        if (changed) {
            prefs.edit()
                .putString("history", gson.toJson(migrated, type))
                .apply()
        }
    }

    private fun migrateDurationsSince(
        context: Context,
        sinceDateStr: String = "19-05-25",
        delta: Float = 0.3f
    ) {
        val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<HistoryItem>>() {}.type

        // 1) carrega a lista atual
        val json = prefs.getString("history", "[]") ?: "[]"
        val list: MutableList<HistoryItem> = gson.fromJson(json, type) ?: mutableListOf()

        // 2) prepara o formato para parse
        val parser = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault())
        val cutoff = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            .parse(sinceDateStr)
            ?.let { Date(it.time) }  // meio-dia do dia
            ?: return

        var changed = false

        // 3) itera e aplica delta
        val migrated = list.map { hi ->
            val hiDate = parser.parse(hi.dateTime) ?: return@map hi
            if (hiDate >= cutoff) {
                // atualiza cada throwRecord
                val newRecs = hi.throwRecords.map { tr ->
                    val newDur = tr.durationSeconds + delta
                    changed = true
                    ThrowRecord(
                        count            = tr.count,
                        durationSeconds  = newDur,
                        frequency        = if (newDur > 0f) tr.count / newDur else 0f
                    )
                }
                // constroi um novo HistoryItem com os throwRecords já atualizados
                hi.copy(throwRecords = newRecs)
            } else hi
        }

        // 4) se mudou, grava de volta
        if (changed) {
            prefs.edit()
                .putString("history", gson.toJson(migrated, type))
                .apply()
        }
    }


}

// Composables auxiliares



/*@Composable
fun AccelerometerDisplay(reading: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        fontSize = 14.sp,
        text = reading
    )
}*/





fun Float.toTimeString(): String {
    val totalTenths = (this * 10).toInt()
    val minutes     = floor(totalTenths / 600f).toInt()
    val secondsTenths = totalTenths % (600)
    val seconds     = round(secondsTenths / 10f).toInt()
    return "%02d:%02d".format(minutes, seconds)
}
fun Float.toTimeAnnotated(): AnnotatedString = buildAnnotatedString {
    val totalTenths   = (this@toTimeAnnotated * 10).toInt()
    val minutes       = floor(totalTenths / 600f).toInt()
    val secondsTenths = totalTenths % 600
    val secondsRound  = round(secondsTenths / 10f).toInt()
    val seconds       = floor(secondsTenths / 10f).toInt()
    val tenths        = (secondsTenths % 10).toInt()

    if(minutes>0) {
        append(minutes.toString())
        withStyle(SpanStyle(fontSize = 6.sp)) {
            append("m")
        }
        append(seconds.toString())
        withStyle(SpanStyle(fontSize = 6.sp)) {
            append("s")
        }
    } else {
        append("${seconds}.${tenths}")
        withStyle(SpanStyle(fontSize = 6.sp)) {
            append("s")
        } 
    }

}


/*ata class ThrowRecord(
    val count: Int,
    val timestamp: Long
)

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var thresholdZ = -10f
    private var wasBelowZ = false
    private var lastSensorUpdateTime = 0L

    private val throwRecords = mutableListOf<ThrowRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            JugglerPalTheme {
                val scope = rememberCoroutineScope()
                var currentCount by remember { mutableStateOf(0) }

                LaunchedEffect(throwRecords.size) {
                    if (throwRecords.isNotEmpty()) {
                        currentCount = throwRecords.last().count
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Lançamentos: $currentCount")
                    Button(onClick = {
                        scope.launch(Dispatchers.IO) {
                            sendThrowRecordsToPhone()
                        }
                    }) {
                        Text("Enviar Lançamentos")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val z = it.values[2]
            val currentTime = System.currentTimeMillis()

            if (thresholdZ < 0) {
                if (z < thresholdZ && !wasBelowZ && (currentTime - lastSensorUpdateTime) > 100) {
                    addThrowRecord()
                    lastSensorUpdateTime = currentTime
                    wasBelowZ = true
                }
                if (z >= 0.8 * thresholdZ) {
                    wasBelowZ = false
                }
            } else {
                if (z > thresholdZ && !wasBelowZ && (currentTime - lastSensorUpdateTime) > 100) {
                    addThrowRecord()
                    lastSensorUpdateTime = currentTime
                    wasBelowZ = true
                }
                if (z <= 0.8 * thresholdZ) {
                    wasBelowZ = false
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun addThrowRecord() {
        val newCount = if (throwRecords.isEmpty()) 2 else throwRecords.last().count + 2
        throwRecords.add(ThrowRecord(newCount, System.currentTimeMillis()))
    }

    private fun sendThrowRecordsToPhone() {
        val gson = Gson()
        val json = gson.toJson(throwRecords)

        sendMessageToPhone("/jugglerpal/throws", json)
    }

    private fun sendMessageToPhone(path: String, message: String) {
        Wearable.getNodeClient(this)
            .connectedNodes
            .addOnSuccessListener { nodes: List<Node> ->
                for (node in nodes) {
                    Wearable.getMessageClient(this)
                        .sendMessage(node.id, path, message.toByteArray())
                        .addOnSuccessListener {
                            Log.d("WearMainAct", "Mensagem enviada para ${node.displayName}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("WearMainAct", "Erro ao enviar mensagem", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("WearMainAct", "Erro ao obter nós", e)
            }
    }
}*/
