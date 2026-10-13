package com.example.hivepoint.util

import android.content.Context
import org.osmdroid.tileprovider.modules.OfflineTileProvider
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.views.MapView
import java.io.File
import java.io.FileOutputStream

object OfflineTileArchiveManager {

    /**
     * Checks for offline map archive files (.sqlite, .mbtiles, .zip) in local app storage
     * and initializes the osmdroid MapView to load tiles strictly offline from these archives.
     */
    fun setupOfflineTileProvider(context: Context, mapView: MapView): Boolean {
        try {
            val archiveDir = File(context.filesDir, "osmdroid")
            if (!archiveDir.exists()) archiveDir.mkdirs()

            // Copy sample offline archive from assets if available and not yet copied
            copyAssetArchiveIfNeeded(context, archiveDir, "romania_map.sqlite")

            val archiveFiles = archiveDir.listFiles { file ->
                file.isFile && (
                    file.extension.equals("sqlite", true) ||
                    file.extension.equals("mbtiles", true) ||
                    file.extension.equals("zip", true)
                )
            }

            if (!archiveFiles.isNullOrEmpty()) {
                val registerReceiver = SimpleRegisterReceiver(context)
                val tileSource = XYTileSource(
                    "Mapnik",
                    0, 19, 256, ".png",
                    arrayOf("http://localhost/")
                )
                val offlineProvider = OfflineTileProvider(registerReceiver, archiveFiles)
                mapView.tileProvider = offlineProvider
                mapView.setTileSource(tileSource)
                mapView.setUseDataConnection(false)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun copyAssetArchiveIfNeeded(context: Context, targetDir: File, fileName: String) {
        val targetFile = File(targetDir, fileName)
        if (targetFile.exists() && targetFile.length() > 0) return

        try {
            context.assets.open(fileName).use { input ->
                FileOutputStream(targetFile).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            // Asset file optional if user provides external file
        }
    }
}
