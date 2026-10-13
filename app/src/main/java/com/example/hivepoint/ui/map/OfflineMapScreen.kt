package com.example.hivepoint.ui.map

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.model.MapType
import com.example.hivepoint.util.MarkerIconHelper
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.io.File

private const val BRASOV_LAT = 45.65737
private const val BRASOV_LON = 25.60589
private const val BROWSER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36"

// Esri World Topo Map (100% Free, ZERO API Key Required)
private val ESRI_WORLD_TOPO = object : OnlineTileSourceBase(
    "Esri_World_Topo",
    0, 19, 256, ".jpg",
    arrayOf("https://services.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/")
) {
    override fun getTileURLString(pMapTileIndex: Long): String {
        return baseUrl + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex)
    }
}

// Esri World Imagery Satellite (100% Free, ZERO API Key Required)
private val ESRI_SATELLITE = object : OnlineTileSourceBase(
    "Esri_Satellite",
    0, 19, 256, ".jpg",
    arrayOf("https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/")
) {
    override fun getTileURLString(pMapTileIndex: Long): String {
        return baseUrl + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex)
    }
}

// OpenStreetMap HOT (100% Free, ZERO API Key Required)
private val OSM_HOT = object : OnlineTileSourceBase(
    "OSM_HOT",
    0, 19, 256, ".png",
    arrayOf("https://a.tile.openstreetmap.fr/hot/", "https://b.tile.openstreetmap.fr/hot/")
) {
    override fun getTileURLString(pMapTileIndex: Long): String {
        return baseUrl + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + ".png"
    }
}

private val TACTICAL_DARK_FILTER = ColorMatrixColorFilter(
    ColorMatrix(
        floatArrayOf(
            0.25f, 0.25f, 0.25f, 0f, -50f,
            0.25f, 0.25f, 0.25f, 0f, -50f,
            0.25f, 0.25f, 0.25f, 0f, -50f,
            0f,    0f,    0f,    1f, 0f
        )
    )
)

@Composable
fun OfflineMapScreen(
    modifier: Modifier = Modifier,
    hives: List<Beehive> = emptyList(),
    userLocation: Pair<Double, Double>? = null,
    mapType: MapType = MapType.TOPOGRAPHIC,
    isDarkTheme: Boolean = false,
    onHiveClick: (Beehive) -> Unit = {},
    onMapLongClick: (latitude: Double, longitude: Double) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current

    val googleBlueDotDrawable = remember(context) {
        MarkerIconHelper.createGoogleBlueDotDrawable(context)
    }

    // Initialize real GIS MapView with Esri World Topo (100% Free, Zero API Key Needed)
    val mapView = remember {
        val config = Configuration.getInstance()
        config.load(context, context.getSharedPreferences("osmdroid_prefs", Context.MODE_PRIVATE))
        config.userAgentValue = BROWSER_USER_AGENT

        val basePath = File(context.cacheDir, "osmdroid")
        val tileCachePath = File(basePath, "tiles")
        if (!tileCachePath.exists()) tileCachePath.mkdirs()

        config.osmdroidBasePath = basePath
        config.osmdroidTileCache = tileCachePath

        MapView(context).apply {
            setTileSource(ESRI_WORLD_TOPO)
            setMultiTouchControls(true)
            setDestroyMode(false)
            setUseDataConnection(true)
            controller.setZoom(14.0)
            controller.setCenter(GeoPoint(BRASOV_LAT, BRASOV_LON))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { map ->
                val selectedTileSource = when (mapType) {
                    MapType.TOPOGRAPHIC -> ESRI_WORLD_TOPO
                    MapType.SATELLITE -> ESRI_SATELLITE
                    MapType.TACTICAL -> OSM_HOT
                }

                if (map.tileProvider.tileSource.name() != selectedTileSource.name()) {
                    map.setTileSource(selectedTileSource)
                }

                // Apply dark filter ONLY when Dark Theme or Tactical mode is on AND map is NOT in Satellite mode
                if ((isDarkTheme || mapType == MapType.TACTICAL) && mapType != MapType.SATELLITE) {
                    map.overlayManager.tilesOverlay.setColorFilter(TACTICAL_DARK_FILTER)
                } else {
                    map.overlayManager.tilesOverlay.setColorFilter(null)
                }

                map.overlays.clear()

                // Long Press Event Overlay for adding beehives on long press
                val mapEventsReceiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean = false
                    override fun longPressHelper(p: GeoPoint): Boolean {
                        onMapLongClick(p.latitude, p.longitude)
                        return true
                    }
                }
                map.overlays.add(MapEventsOverlay(mapEventsReceiver))

                // 1. Render Google Maps style Blue Dot for Live User Location
                if (userLocation != null) {
                    val (userLat, userLon) = userLocation
                    val userMarker = Marker(map).apply {
                        position = GeoPoint(userLat, userLon)
                        title = "Locația Mea Live (GPS)"
                        snippet = "Lat: %.5f | Lon: %.5f".format(userLat, userLon)
                        icon = googleBlueDotDrawable
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    }
                    map.overlays.add(userMarker)
                }

                // 2. Render Beehive Markers & Apiary Markers
                val topLevelMarkers = hives.filter { hive ->
                    val isApiary = hive.apiaryId == "apiary_complex" || hive.hiveNumber.startsWith("🏠")
                    val isStandaloneHive = hive.apiaryId == "apiary-01" || hive.apiaryId.isBlank()
                    isApiary || isStandaloneHive
                }

                for (hive in topLevelMarkers) {
                    val lat = hive.latitude ?: continue
                    val lon = hive.longitude ?: continue

                    val isApiary = hive.apiaryId == "apiary_complex" || hive.hiveNumber.startsWith("🏠")

                    val markerDrawable = if (isApiary) {
                        MarkerIconHelper.createApiaryMarkerDrawable(context, hive.hiveNumber)
                    } else {
                        val statusHex = when (hive.status) {
                            HiveStatus.STRONG -> "#FBC02D"
                            HiveStatus.MEDIUM -> "#FFA000"
                            HiveStatus.WEAK -> "#F57C00"
                            HiveStatus.QUEENLESS -> "#E53935"
                            HiveStatus.DEAD -> "#546E7A"
                            HiveStatus.UNKNOWN -> "#FBC02D"
                        }
                        MarkerIconHelper.createBeehiveMarkerDrawable(context, statusHex)
                    }

                    val hiveMarker = Marker(map).apply {
                        position = GeoPoint(lat, lon)
                        title = if (isApiary) "Stupină: ${hive.hiveNumber}" else "Stup: ${hive.hiveNumber}"
                        snippet = "Status: ${hive.status.toDisplayName("Română")} | Note: ${hive.notes}"
                        icon = markerDrawable
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        setOnMarkerClickListener { _, _ ->
                            onHiveClick(hive)
                            true
                        }
                    }
                    map.overlays.add(hiveMarker)
                }

                map.invalidate()
            }
        )

        // Stacked Control FABs for Field Navigation (Top Right)
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Button 1: Zoom In (+)
            FloatingActionButton(
                onClick = { mapView.controller.zoomIn() },
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Mărește (Zoom In)",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Button 2: Zoom Out (-)
            FloatingActionButton(
                onClick = { mapView.controller.zoomOut() },
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Micșorează (Zoom Out)",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Button 3: Center on Live GPS Location
            FloatingActionButton(
                onClick = {
                    if (userLocation != null) {
                        val (userLat, userLon) = userLocation
                        mapView.controller.animateTo(GeoPoint(userLat, userLon))
                        mapView.controller.setZoom(15.0)
                    } else {
                        mapView.controller.animateTo(GeoPoint(BRASOV_LAT, BRASOV_LON))
                        mapView.controller.setZoom(14.0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Centrează pe Locația Mea Live",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Button 4: Overview of Romania
            FloatingActionButton(
                onClick = {
                    mapView.controller.animateTo(GeoPoint(BRASOV_LAT, BRASOV_LON))
                    mapView.controller.setZoom(7.5)
                },
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ) {
                Icon(
                    imageVector = Icons.Default.CenterFocusStrong,
                    contentDescription = "Vedere de Ansamblu România",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
