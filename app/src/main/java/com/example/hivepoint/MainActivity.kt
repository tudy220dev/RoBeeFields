package com.example.hivepoint

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hivepoint.db.HiveDbHelper
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.model.MapType
import com.example.hivepoint.ui.components.ApiaryDetailBottomSheet
import com.example.hivepoint.ui.components.HiveActionBottomSheet
import com.example.hivepoint.ui.map.OfflineMapScreen
import com.example.hivepoint.ui.navigation.Screen
import com.example.hivepoint.ui.screens.AboutScreen
import com.example.hivepoint.ui.screens.AddHiveScreen
import com.example.hivepoint.ui.screens.SettingsScreen
import com.example.hivepoint.ui.theme.HivePointTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

private const val BRASOV_LAT = 45.65737
private const val BRASOV_LON = 25.60589
private const val PREFS_NAME = "robeefields_settings"
private const val PREF_DARK_MODE = "pref_dark_mode"
private const val PREF_LANG = "pref_lang"
private const val BROWSER_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36"

private fun isLocationInRomania(lat: Double, lon: Double): Boolean {
    return (lat in 43.6..48.3) && (lon in 20.2..29.7)
}

class MainActivity : ComponentActivity() {

    private val sharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }

    private val dbHelper by lazy {
        HiveDbHelper(this)
    }

    private var selectedLanguage by mutableStateOf("Română")

    private var sampleHives by mutableStateOf(
        listOf(
            Beehive(
                id = "hive-brasov",
                apiaryId = "apiary-01",
                hiveNumber = "Stup Brașov (Centru)",
                latitude = 45.6579,
                longitude = 25.6012,
                status = HiveStatus.STRONG,
                notes = "Stupină montană, cules brad",
            ),
            Beehive(
                id = "hive-bucuresti",
                apiaryId = "apiary-01",
                hiveNumber = "Stup București (Sud)",
                latitude = 44.4322,
                longitude = 26.1062,
                status = HiveStatus.MEDIUM,
                notes = "Stupină câmpie, tei",
            ),
            Beehive(
                id = "hive-cluj",
                apiaryId = "apiary-01",
                hiveNumber = "Stup Cluj (Nord-Vest)",
                latitude = 46.7712,
                longitude = 23.6236,
                status = HiveStatus.STRONG,
                notes = "Familie foarte activă",
            )
        )
    )

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        selectedLanguage = sharedPreferences.getString(PREF_LANG, "Română") ?: "Română"

        lifecycleScope.launch(Dispatchers.IO) {
            val storedHives = dbHelper.getAllHives()
            if (storedHives.isNotEmpty()) {
                sampleHives = storedHives
            } else {
                sampleHives.forEach { dbHelper.insertOrUpdateHive(it) }
            }
        }

        val config = org.osmdroid.config.Configuration.getInstance()
        config.load(applicationContext, applicationContext.getSharedPreferences("osmdroid_prefs", MODE_PRIVATE))
        config.userAgentValue = BROWSER_USER_AGENT

        val basePath = File(cacheDir, "osmdroid")
        val tileCachePath = File(basePath, "tiles")
        if (!tileCachePath.exists()) tileCachePath.mkdirs()

        config.osmdroidBasePath = basePath
        config.osmdroidTileCache = tileCachePath

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS
            )
        )

        setContent {
            val systemDark = isSystemInDarkTheme()
            val initialDark = remember {
                if (sharedPreferences.contains(PREF_DARK_MODE)) {
                    sharedPreferences.getBoolean(PREF_DARK_MODE, false)
                } else {
                    systemDark
                }
            }
            var darkThemeState by remember { mutableStateOf(initialDark) }

            HivePointTheme(darkTheme = darkThemeState) {
                HivePointApp(
                    hives = sampleHives,
                    isDarkTheme = darkThemeState,
                    onDarkThemeChange = { enabled ->
                        darkThemeState = enabled
                        sharedPreferences.edit().putBoolean(PREF_DARK_MODE, enabled).apply()
                    },
                    selectedLanguage = selectedLanguage,
                    onLanguageChange = { lang ->
                        selectedLanguage = lang
                        sharedPreferences.edit().putString(PREF_LANG, lang).apply()
                    },
                    onAddHive = { newHive ->
                        sampleHives = sampleHives + newHive
                        lifecycleScope.launch(Dispatchers.IO) {
                            dbHelper.insertOrUpdateHive(newHive)
                        }
                    },
                    onUpdateHive = { updatedHive ->
                        sampleHives = sampleHives.map { if (it.id == updatedHive.id) updatedHive else it }
                        lifecycleScope.launch(Dispatchers.IO) {
                            dbHelper.insertOrUpdateHive(updatedHive)
                        }
                    },
                    onDeleteHive = { hiveId ->
                        sampleHives = sampleHives.filterNot { it.id == hiveId }
                        lifecycleScope.launch(Dispatchers.IO) {
                            dbHelper.deleteHive(hiveId)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HivePointApp(
    hives: List<Beehive>,
    isDarkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    onAddHive: (Beehive) -> Unit,
    onUpdateHive: (Beehive) -> Unit,
    onDeleteHive: (String) -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Map) }
    var currentMapType by remember { mutableStateOf(MapType.TOPOGRAPHIC) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val toggleDrawerAction = {
        scope.launch {
            if (drawerState.isClosed) drawerState.open() else drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "🐝 RoBee Fields",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Map, contentDescription = null) },
                        label = { Text(if (selectedLanguage == "English") "Main Map" else "Harta Principală") },
                        selected = currentScreen is Screen.Map,
                        onClick = {
                            currentScreen = Screen.Map
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.AddLocation, contentDescription = null) },
                        label = { Text(if (selectedLanguage == "English") "Add New Hive" else "Adaugă Stup Nou") },
                        selected = currentScreen is Screen.AddHive,
                        onClick = {
                            currentScreen = Screen.AddHive
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text(if (selectedLanguage == "English") "Settings" else "Setări") },
                        selected = currentScreen is Screen.Settings,
                        onClick = {
                            currentScreen = Screen.Settings
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        label = { Text(if (selectedLanguage == "English") "About" else "Despre") },
                        selected = currentScreen is Screen.About,
                        onClick = {
                            currentScreen = Screen.About
                            scope.launch { drawerState.close() }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    Text(
                        text = "RoBee Fields v0.2.0 (Field Edition)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 12.dp, bottom = 16.dp)
                    )
                }
            }
        }
    ) {
        when (currentScreen) {
            is Screen.Map -> MapScreenWithDrawer(
                hives = hives,
                mapType = currentMapType,
                onMapTypeChange = { currentMapType = it },
                isDarkTheme = isDarkTheme,
                selectedLanguage = selectedLanguage,
                onOpenDrawer = { toggleDrawerAction() },
                onUpdateHive = onUpdateHive,
                onDeleteHive = onDeleteHive,
                onAddHive = onAddHive
            )
            is Screen.AddHive -> AddHiveScreen(
                selectedLanguage = selectedLanguage,
                onSaveHive = { hive ->
                    onAddHive(hive)
                    currentScreen = Screen.Map
                },
                onOpenDrawer = { toggleDrawerAction() }
            )
            is Screen.Settings -> SettingsScreen(
                currentMapType = currentMapType,
                onMapTypeChange = { currentMapType = it },
                isDarkTheme = isDarkTheme,
                onDarkThemeChange = onDarkThemeChange,
                selectedLanguage = selectedLanguage,
                onLanguageChange = onLanguageChange,
                onOpenDrawer = { toggleDrawerAction() }
            )
            is Screen.About -> AboutScreen(
                onOpenDrawer = { toggleDrawerAction() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenWithDrawer(
    hives: List<Beehive>,
    mapType: MapType,
    onMapTypeChange: (MapType) -> Unit,
    isDarkTheme: Boolean,
    selectedLanguage: String,
    onOpenDrawer: () -> Unit,
    onUpdateHive: (Beehive) -> Unit,
    onDeleteHive: (String) -> Unit,
    onAddHive: (Beehive) -> Unit
) {
    val context = LocalContext.current
    var currentLat by remember { mutableStateOf(BRASOV_LAT) }
    var currentLon by remember { mutableStateOf(BRASOV_LON) }
    var selectedHiveForMenu by remember { mutableStateOf<Beehive?>(null) }
    var selectedApiaryForMenu by remember { mutableStateOf<Beehive?>(null) }
    var longClickCoords by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var showGpsAddDialog by remember { mutableStateOf(false) }

    fun updateLocation() {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            if (locationManager != null && (
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            )) {
                val gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val netLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val loc = gpsLoc ?: netLoc
                if (loc != null && isLocationInRomania(loc.latitude, loc.longitude)) {
                    currentLat = loc.latitude
                    currentLon = loc.longitude
                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        currentLat = BRASOV_LAT
        currentLon = BRASOV_LON
    }

    DisposableEffect(context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (isLocationInRomania(location.latitude, location.longitude)) {
                    currentLat = location.latitude
                    currentLon = location.longitude
                }
            }
            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }
        if (locationManager != null && (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000L, 10f, listener)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000L, 10f, listener)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        onDispose {
            locationManager?.removeUpdates(listener)
        }
    }

    LaunchedEffect(Unit) {
        updateLocation()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("RoBee Fields - ${mapType.displayName}") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Meniu")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val nextType = when (mapType) {
                                MapType.TOPOGRAPHIC -> MapType.SATELLITE
                                MapType.SATELLITE -> MapType.TACTICAL
                                MapType.TACTICAL -> MapType.TOPOGRAPHIC
                            }
                            onMapTypeChange(nextType)
                        }
                    ) {
                        Icon(Icons.Default.Layers, contentDescription = "Schimbă Tip Hartă")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OfflineMapScreen(
                modifier = Modifier.fillMaxSize(),
                hives = hives,
                userLocation = Pair(currentLat, currentLon),
                mapType = mapType,
                isDarkTheme = isDarkTheme,
                onHiveClick = { clickedHive ->
                    if (clickedHive.apiaryId == "apiary_complex" || clickedHive.hiveNumber.startsWith("🏠")) {
                        selectedApiaryForMenu = clickedHive
                    } else {
                        selectedHiveForMenu = clickedHive
                    }
                },
                onMapLongClick = { lat, lon ->
                    longClickCoords = Pair(lat, lon)
                }
            )

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "GPS",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = if (selectedLanguage == "English") "Current GPS Coordinates" else "Coordonate GPS Curente",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format(Locale.US, "Lat: %.5f  |  Lon: %.5f", currentLat, currentLon),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }

                        IconButton(onClick = { updateLocation() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Actualizează GPS")
                        }
                    }

                    Button(
                        onClick = {
                            updateLocation()
                            showGpsAddDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddLocationAlt,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(if (selectedLanguage == "English") "Add Hive or Apiary at GPS Location" else "Adaugă Stup / Stupină la Locația Curentă")
                    }
                }
            }

            selectedHiveForMenu?.let { selected ->
                HiveActionBottomSheet(
                    hive = selected,
                    onDismiss = { selectedHiveForMenu = null },
                    onUpdateHive = { updated ->
                        onUpdateHive(updated)
                        selectedHiveForMenu = null
                    },
                    onDeleteHive = { id ->
                        onDeleteHive(id)
                        selectedHiveForMenu = null
                    }
                )
            }

            selectedApiaryForMenu?.let { apiaryHive ->
                val rawApiaryName = apiaryHive.hiveNumber.removePrefix("🏠").trim()
                val hivesInApiary = hives.filter {
                    it.apiaryId == apiaryHive.id ||
                    it.apiaryId == rawApiaryName ||
                    (it.apiaryId == "apiary_complex" && it.id != apiaryHive.id)
                }

                ApiaryDetailBottomSheet(
                    apiaryId = apiaryHive.id,
                    apiaryName = rawApiaryName,
                    apiaryLatitude = apiaryHive.latitude ?: currentLat,
                    apiaryLongitude = apiaryHive.longitude ?: currentLon,
                    hives = hivesInApiary,
                    onDismiss = { selectedApiaryForMenu = null },
                    onAddHiveToApiary = { newHiveInApiary ->
                        onAddHive(newHiveInApiary)
                    },
                    onSelectHive = { hiveSelected ->
                        selectedHiveForMenu = hiveSelected
                        selectedApiaryForMenu = null
                    },
                    onRenameApiary = { newName ->
                        val updated = apiaryHive.copy(hiveNumber = newName)
                        onUpdateHive(updated)
                    },
                    onDeleteApiary = {
                        onDeleteHive(apiaryHive.id)
                        hivesInApiary.forEach { onDeleteHive(it.id) }
                        selectedApiaryForMenu = null
                    }
                )
            }

            longClickCoords?.let { (longLat, longLon) ->
                AddElementAtCoordsDialog(
                    latitude = longLat,
                    longitude = longLon,
                    selectedLanguage = selectedLanguage,
                    onDismiss = { longClickCoords = null },
                    onSaveHive = { newElement ->
                        onAddHive(newElement)
                        longClickCoords = null
                    }
                )
            }

            if (showGpsAddDialog) {
                AddElementAtCoordsDialog(
                    latitude = currentLat,
                    longitude = currentLon,
                    selectedLanguage = selectedLanguage,
                    onDismiss = { showGpsAddDialog = false },
                    onSaveHive = { newElement ->
                        onAddHive(newElement)
                        showGpsAddDialog = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddElementAtCoordsDialog(
    latitude: Double,
    longitude: Double,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onSaveHive: (Beehive) -> Unit
) {
    val isEnglish = (selectedLanguage == "English")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var nameText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf(if (isEnglish) "Added at GPS coordinates" else "Marcat la coordonatele GPS") }
    var selectedStatus by remember { mutableStateOf(HiveStatus.STRONG) }
    var expandedStatus by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (selectedTabIndex == 0) (if (isEnglish) "🐝 Add Individual Hive" else "🐝 Adaugă Stup Individual") else (if (isEnglish) "🏠 Add Apiary Complex" else "🏠 Adaugă Stupină Complexă")) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text(if (isEnglish) "Hive 🐝" else "Stup 🐝") }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text(if (isEnglish) "Apiary 🏠" else "Stupină 🏠") }
                    )
                }

                Text(
                    text = String.format(Locale.US, "GPS: Lat %.5f, Lon %.5f", latitude, longitude),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text(if (selectedTabIndex == 0) (if (isEnglish) "Hive Number/Name" else "Număr/Nume Stup") else (if (isEnglish) "Apiary Name" else "Nume Stupină")) },
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedTabIndex == 0) {
                    ExposedDropdownMenuBox(
                        expanded = expandedStatus,
                        onExpandedChange = { expandedStatus = !expandedStatus }
                    ) {
                        OutlinedTextField(
                            value = selectedStatus.toDisplayName(selectedLanguage),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isEnglish) "Hive Status" else "Stare Stup (Status)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedStatus,
                            onDismissRequest = { expandedStatus = false }
                        ) {
                            HiveStatus.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status.toDisplayName(selectedLanguage)) },
                                    onClick = {
                                        selectedStatus = status
                                        expandedStatus = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text(if (isEnglish) "Field Notes" else "Note / Descriere Teren") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleanName = nameText.removePrefix("🏠").trim()
                    if (cleanName.isNotBlank()) {
                        val newHive = Beehive(
                            apiaryId = if (selectedTabIndex == 1) "apiary_complex" else "apiary-01",
                            hiveNumber = cleanName,
                            latitude = latitude,
                            longitude = longitude,
                            status = if (selectedTabIndex == 1) HiveStatus.STRONG else selectedStatus,
                            notes = notesText.trim()
                        )
                        onSaveHive(newHive)
                    }
                }
            ) {
                Text(if (selectedTabIndex == 1) (if (isEnglish) "Add Apiary 🏠" else "Adaugă Stupină 🏠") else (if (isEnglish) "Add Hive 🐝" else "Adaugă Stup 🐝"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isEnglish) "Cancel" else "Anulează")
            }
        }
    )
}
