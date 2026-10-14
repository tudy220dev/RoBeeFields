package com.example.hivepoint.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.hivepoint.model.MapType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object MapCacheManager {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun getCachedMapFile(context: Context, mapType: MapType): File {
        val cacheDir = File(context.cacheDir, "map_cache")
        if (!cacheDir.exists()) cacheDir.mkdirs()
        return File(cacheDir, "romania_map_${mapType.name.lowercase()}.png")
    }

    fun isMapCached(context: Context, mapType: MapType): Boolean {
        val file = getCachedMapFile(context, mapType)
        return file.exists() && file.length() > 0
    }

    fun getMapsforgeFile(context: Context, regionName: String = "romania"): File {
        val dir = File(context.filesDir, "mapsforge")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "$regionName.map")
    }

    fun isMapsforgeDownloaded(context: Context, regionName: String = "romania"): Boolean {
        val file = getMapsforgeFile(context, regionName)
        return file.exists() && file.length() > 1000
    }

    fun deleteMapsforgeMap(context: Context, regionName: String = "romania"): Boolean {
        val file = getMapsforgeFile(context, regionName)
        return if (file.exists()) file.delete() else false
    }

    suspend fun downloadMapsforgeVectorMap(
        context: Context,
        regionName: String = "romania",
        downloadUrl: String = "https://download.mapsforge.org/maps/v5/europe/romania.map",
        onProgress: (Float) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                if (!isNetworkAvailable(context)) {
                    withContext(Dispatchers.Main) { onComplete(false) }
                    return@withContext
                }

                val targetFile = getMapsforgeFile(context, regionName)
                val tempFile = File(targetFile.parentFile, "$regionName.tmp")

                val url = URL(downloadUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.connect()

                val totalSize = connection.contentLength
                var downloadedBytes = 0

                connection.inputStream.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloadedBytes += bytesRead
                            if (totalSize > 0) {
                                val progress = downloadedBytes.toFloat() / totalSize
                                withContext(Dispatchers.Main) { onProgress(progress) }
                            }
                        }
                    }
                }

                if (tempFile.exists() && tempFile.length() > 0) {
                    if (targetFile.exists()) targetFile.delete()
                    tempFile.renameTo(targetFile)
                    withContext(Dispatchers.Main) { onComplete(true) }
                } else {
                    withContext(Dispatchers.Main) { onComplete(false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { onComplete(false) }
            }
        }
    }

    suspend fun downloadAndCacheMap(
        context: Context,
        mapType: MapType,
        onProgress: (Float) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            if (!isNetworkAvailable(context)) {
                withContext(Dispatchers.Main) { onComplete(false) }
                return@withContext
            }

            try {
                for (step in 1..10) {
                    delay(120)
                    val progress = step / 10f
                    withContext(Dispatchers.Main) {
                        onProgress(progress)
                    }
                }

                val targetFile = getCachedMapFile(context, mapType)
                val width = 1200
                val height = 800
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                val bgPaint = Paint().apply {
                    color = when (mapType) {
                        MapType.TOPOGRAPHIC -> Color.rgb(230, 235, 220)
                        MapType.SATELLITE -> Color.rgb(35, 55, 30)
                        MapType.TACTICAL -> Color.rgb(15, 20, 25)
                    }
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

                val landPaint = Paint().apply {
                    color = when (mapType) {
                        MapType.TOPOGRAPHIC -> Color.rgb(215, 230, 195)
                        MapType.SATELLITE -> Color.rgb(55, 80, 40)
                        MapType.TACTICAL -> Color.rgb(25, 35, 45)
                    }
                    style = Paint.Style.FILL
                }
                canvas.drawRoundRect(100f, 80f, 1100f, 720f, 60f, 60f, landPaint)

                FileOutputStream(targetFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                withContext(Dispatchers.Main) {
                    onComplete(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        }
    }
}
