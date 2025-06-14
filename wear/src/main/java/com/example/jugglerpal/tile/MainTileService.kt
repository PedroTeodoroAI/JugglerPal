package com.example.jugglerpal.tile

import android.content.Context
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.LayoutElement
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val RESOURCES_VERSION = "0"

@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService() {

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ) = resources(requestParams)

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ) = tile(requestParams, this)
}

private fun resources(
    requestParams: RequestBuilders.ResourcesRequest
): ResourceBuilders.Resources {
    return ResourceBuilders.Resources.Builder()
        .setVersion(RESOURCES_VERSION)
        .build()
}

private fun tile(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): TileBuilders.Tile {
    val singleTileTimeline = TimelineBuilders.Timeline.Builder()
        .addTimelineEntry(
            TimelineBuilders.TimelineEntry.Builder()
                .setLayout(
                    LayoutElementBuilders.Layout.Builder()
                        .setRoot(tileLayout(requestParams, context))
                        .build()
                )
                .build()
        )
        .build()

    return TileBuilders.Tile.Builder()
        .setResourcesVersion(RESOURCES_VERSION)
        .setTileTimeline(singleTileTimeline)
        .build()
}

private fun tileLayout(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
): LayoutElementBuilders.LayoutElement {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val json = prefs.getString("history", "[]")
    val type = object : TypeToken<List<HistoryItem>>() {}.type
    val history: List<HistoryItem> = Gson().fromJson(json, type) ?: emptyList()

    // Função para formatar duração como HH:MM:SS
    fun formatSeconds(seconds: Float): String {
        val totalSecs = seconds.toInt()
        val h = totalSecs / 3600
        val m = (totalSecs % 3600) / 60
        val s = totalSecs % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    val latest = history.maxByOrNull {
        SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()).parse(it.dateTime)?.time ?: 0L
    }

    val (title, dateStr, line1, line2, line3) = latest?.let { item ->
        val throws = item.throwRecords
        val attempts = throws.size
        val totalThrows = throws.sumOf { it.count }
        val maxThrow = throws.maxOfOrNull { it.count } ?: 0
        val averageThrow = if (attempts > 0) totalThrows.toFloat() / attempts else 0f
        val totalDuration = throws.sumOf { it.durationSeconds.toDouble() }

        val parsedDate = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.getDefault()).parse(item.dateTime)
        val formattedDate = parsedDate?.let {
            SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(it)
        } ?: ""

        listOf(
            item.note.takeIf { it.isNotBlank() } ?: "Unnamed Session",
            formattedDate,
            "Attempts: $attempts",
            "Max: $maxThrow • Avg: %.1f".format(averageThrow),
            "Throws: $totalThrows • Dur: ${formatSeconds(totalDuration.toFloat())}"
        )
    } ?: listOf("No data", "", "", "", "")

    return PrimaryLayout.Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setContent(
            LayoutElementBuilders.Column.Builder()
                .addContent(
                    Text.Builder(context, title)
                        .setTypography(Typography.TYPOGRAPHY_TITLE3)
                        .setColor(argb(Colors.DEFAULT.primary))
                        .build()
                )
                .addContent(
                    Text.Builder(context, dateStr)
                        .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                        .setColor(argb(Colors.DEFAULT.onSurface))
                        .build()
                )
                .addContent(
                    Text.Builder(context, line1)
                        .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                        .setColor(argb(Colors.DEFAULT.onSurface))
                        .build()
                )
                .addContent(
                    Text.Builder(context, line2)
                        .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                        .setColor(argb(Colors.DEFAULT.onSurface))
                        .build()
                )
                .addContent(
                    Text.Builder(context, line3)
                        .setTypography(Typography.TYPOGRAPHY_CAPTION2)
                        .setColor(argb(Colors.DEFAULT.onSurface))
                        .build()
                )
                .build()
        )
        .build()
}




@Preview(device = WearDevices.SMALL_ROUND)
@Preview(device = WearDevices.LARGE_ROUND)
fun tilePreview(context: Context) = TilePreviewData(::resources) {
    tile(it, context)
}

// Certifica-te de que esta data class está visível no módulo do relógio:
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
