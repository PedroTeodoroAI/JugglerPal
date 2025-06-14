package com.example.jugglerpal.complication

import android.content.ComponentName
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.ln
import kotlin.math.pow
import android.app.PendingIntent
import android.content.Intent
import com.example.jugglerpal.presentation.MainActivity
import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.MonochromaticImage
import com.example.jugglerpal.R


class MainComplicationService : SuspendingComplicationDataSourceService() {
    private fun createTapAction(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            this,  /* context */
            0,     /* requestCode */
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? =
        when (type) {
            ComplicationType.RANGED_VALUE ->
                createRangedComplication(totalThrows = 6800)
            ComplicationType.SHORT_TEXT ->
                createShortComplication(totalThrows = 6800)
            else -> null
        }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val historyJson = prefs.getString("history", "[]")
        val listType = object : TypeToken<List<HistoryItem>>() {}.type
        val history: List<HistoryItem> = Gson().fromJson(historyJson, listType) ?: emptyList()

        // calcula total e nível
        val totalThrows = history.sumOf { it.totalThrows }
        val level = calculateLevel(totalThrows)

        return when (request.complicationType) {
            ComplicationType.RANGED_VALUE ->
                createRangedComplication(totalThrows)
            ComplicationType.SHORT_TEXT ->
                createShortComplication(totalThrows)
            else ->
                getPreviewData(request.complicationType)
                    ?: throw IllegalArgumentException("Type not supported")
        }
    }

    /*private fun createRangedComplication(level: Int, totalThrows: Int): ComplicationData {
        val nextTarget = throwsForLevel(level + 1)
        val prevTarget = throwsForLevel(level)
        val progress = totalThrows.toFloat().coerceIn(prevTarget.toFloat(), nextTarget.toFloat())
        val formatted = formatThrows(totalThrows)

        return RangedValueComplicationData.Builder(
            value = progress,
            min = prevTarget.toFloat(),
            max = nextTarget.toFloat(),
            contentDescription = PlainComplicationText.Builder("Level $level with $formatted throws").build()
        )
            .setTitle(PlainComplicationText.Builder("L-$level").build())
            .setText(PlainComplicationText.Builder(formatted).build())
            //.setTitle(PlainComplicationText.Builder(formatted).build())
            .setTapAction(createTapAction())
            .build()
    }*/

    private fun createRangedComplication(totalThrows: Int): ComplicationData {
        val bs          = binSize(totalThrows)
        val prevTarget  = (totalThrows / bs) * bs
        val nextTarget  = prevTarget + bs
        val value       = totalThrows.toFloat().coerceIn(prevTarget.toFloat(), nextTarget.toFloat())
        val formatted   = formatThrows(totalThrows)

        return RangedValueComplicationData.Builder(
            value = value,
            min   = prevTarget.toFloat(),
            max   = nextTarget.toFloat(),
            contentDescription = PlainComplicationText.Builder(
                "$formatted throws"
            ).build()
        )
            .setText( PlainComplicationText.Builder(formatted).build() )
            .setTapAction(createTapAction())
            .build()
    }


    private fun createShortComplication(totalThrows: Int): ComplicationData {
        val formatted = formatThrows(totalThrows)

        // cria um MonochromaticImage a partir de um drawable (cor de acentuação do watch face)
        val heartImage = MonochromaticImage.Builder(
            Icon.createWithResource(this, R.drawable.club2)
        ).build()

        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(formatted).build(),
            contentDescription = PlainComplicationText.Builder("$formatted throws").build()
        )
            .setMonochromaticImage(heartImage)    // imagem acima do texto
            .setTapAction(createTapAction())
            .build()
    }

    private fun calculateLevel(totalThrows: Int): Int {
        val base = 1.00695555
        val factor = 0.00695555
        val result = ln((totalThrows * factor / 680.0) + 1) / ln(base) - 1
        return result.toInt().coerceAtLeast(0)
    }

    /*private fun throwsForLevel(level: Int): Int {
        val base = 1.00695555
        val factor = 0.00695555
        return (680 * (base.pow(level + 1) - 1) / factor).toInt()
    }*/

    private fun binSize(totalThrows: Int): Int = when {
        totalThrows < 1_000_000    -> 1_000
        totalThrows < 10_000_000   -> 10_000
        totalThrows < 100_000_000  -> 100_000
        else                       -> 1_000_000
    }

    private fun formatThrows(throws: Int): String {
        return when {
            throws >= 100_000_000 -> "%dM".format(throws / 1_000_000)
            throws >= 10_000_000  -> "%.1fM".format(throws / 1_000_000f)
            throws >= 1_000_000   -> "%.2fM".format(throws / 1_000_000f)
            else                  -> "%dK".format(throws / 1_000)
        }.trimEnd('0').trimEnd('.')
    }
}

// --- Models ---

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
    val jugglerName: String = "",
    val jugglerNickName: String = "",
    val jugglerPass: String = "",
    val totalDuration: Float = 0f,
    val maxDuration: Float = 0f,
    val averageDuration: Float = 0f
)
