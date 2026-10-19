package com.example.hivepoint.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hivepoint.model.MapType
import com.example.hivepoint.util.HiveCsvExporter
import com.example.hivepoint.util.MapCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withContext

data class MapsforgeRegion(
    val id: String,
    val name: String,
    val flag: String,
    val sizeMb: String,
    val downloadUrl: String
)

val MAPSFORGE_REGIONS = listOf(
    // 🇷🇴 România & Regiuni
    MapsforgeRegion("romania", "România (Toată țara)", "🇷🇴", "45 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_banat", "România - Banat", "🇷🇴", "8 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_crisana", "România - Crișana", "🇷🇴", "7 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_dobrogea", "România - Dobrogea", "🇷🇴", "6 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_maramures", "România - Maramureș", "🇷🇴", "6 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_moldova", "România - Moldova", "🇷🇴", "10 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_muntenia", "România - Muntenia", "🇷🇴", "12 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_oltenia", "România - Oltenia", "🇷🇴", "9 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_transylvania", "România - Transilvania & Brașov", "🏔️", "14 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),

    // 🇲🇩 Republica Moldova
    MapsforgeRegion("moldova", "Republica Moldova", "🇲🇩", "12 MB", "https://download.mapsforge.org/maps/v5/europe/moldova.map"),

    // 🇧🇬 Bulgaria
    MapsforgeRegion("bulgaria", "Bulgaria (Toată țara)", "🇧🇬", "32 MB", "https://download.mapsforge.org/maps/v5/europe/bulgaria.map"),

    // 🇭🇺 Ungaria
    MapsforgeRegion("hungary", "Ungaria (Toată țara)", "🇭🇺", "28 MB", "https://download.mapsforge.org/maps/v5/europe/hungary.map"),

    // 🇩🇪 Germania & Regiuni (Länder)
    MapsforgeRegion("germany", "Germania (Toată țara)", "🇩🇪", "185 MB", "https://download.mapsforge.org/maps/v5/europe/germany.map"),
    MapsforgeRegion("germany_bayern", "Germania - Bayern (Bavaria)", "🇩🇪", "38 MB", "https://download.mapsforge.org/maps/v5/europe/germany/bayern.map"),
    MapsforgeRegion("germany_baden_wuerttemberg", "Germania - Baden-Württemberg", "🇩🇪", "28 MB", "https://download.mapsforge.org/maps/v5/europe/germany/baden-wuerttemberg.map"),
    MapsforgeRegion("germany_berlin", "Germania - Berlin & Brandenburg", "🇩🇪", "18 MB", "https://download.mapsforge.org/maps/v5/europe/germany/berlin.map"),
    MapsforgeRegion("germany_hessen", "Germania - Hessen", "🇩🇪", "16 MB", "https://download.mapsforge.org/maps/v5/europe/germany/hessen.map"),
    MapsforgeRegion("germany_nordrhein_westfalen", "Germania - Nordrhein-Westfalen", "🇩🇪", "35 MB", "https://download.mapsforge.org/maps/v5/europe/germany/nordrhein-westfalen.map"),
    MapsforgeRegion("germany_sachsen", "Germania - Sachsen", "🇩🇪", "14 MB", "https://download.mapsforge.org/maps/v5/europe/germany/sachsen.map"),

    // 🇫🇷 Franța & Regiuni
    MapsforgeRegion("france", "Franța (Toată țara)", "🇫🇷", "190 MB", "https://download.mapsforge.org/maps/v5/europe/france.map"),
    MapsforgeRegion("france_alsace", "Franța - Alsace", "🇫🇷", "12 MB", "https://download.mapsforge.org/maps/v5/europe/france/alsace.map"),
    MapsforgeRegion("france_aquitaine", "Franța - Aquitaine", "🇫🇷", "22 MB", "https://download.mapsforge.org/maps/v5/europe/france/aquitaine.map"),
    MapsforgeRegion("france_bretagne", "Franța - Bretagne", "🇫🇷", "18 MB", "https://download.mapsforge.org/maps/v5/europe/france/bretagne.map"),
    MapsforgeRegion("france_ile_de_france", "Franța - Île-de-France (Paris)", "🇫🇷", "25 MB", "https://download.mapsforge.org/maps/v5/europe/france/ile-de-france.map"),
    MapsforgeRegion("france_paca", "Franța - Provence-Alpes-Côte d'Azur", "🇫🇷", "24 MB", "https://download.mapsforge.org/maps/v5/europe/france/paca.map"),
    MapsforgeRegion("france_rhone_alpes", "Franța - Rhône-Alpes", "🇫🇷", "28 MB", "https://download.mapsforge.org/maps/v5/europe/france/rhone-alpes.map"),

    // 🇮🇹 Italia & Regiuni
    MapsforgeRegion("italy", "Italia (Toată țara)", "🇮🇹", "145 MB", "https://download.mapsforge.org/maps/v5/europe/italy.map"),
    MapsforgeRegion("italy_lazio", "Italia - Lazio (Roma)", "🇮🇹", "18 MB", "https://download.mapsforge.org/maps/v5/europe/italy/lazio.map"),
    MapsforgeRegion("italy_lombardia", "Italia - Lombardia (Milano)", "🇮🇹", "25 MB", "https://download.mapsforge.org/maps/v5/europe/italy/lombardia.map"),
    MapsforgeRegion("italy_piemonte", "Italia - Piemonte", "🇮🇹", "20 MB", "https://download.mapsforge.org/maps/v5/europe/italy/piemonte.map"),
    MapsforgeRegion("italy_sicilia", "Italia - Sicilia", "🇮🇹", "16 MB", "https://download.mapsforge.org/maps/v5/europe/italy/sicilia.map"),
    MapsforgeRegion("italy_toscana", "Italia - Toscana", "🇮🇹", "18 MB", "https://download.mapsforge.org/maps/v5/europe/italy/toscana.map"),
    MapsforgeRegion("italy_veneto", "Italia - Veneto (Venezia)", "🇮🇹", "19 MB", "https://download.mapsforge.org/maps/v5/europe/italy/veneto.map"),

    // 🇪🇸 Spania & Regiuni
    MapsforgeRegion("spain", "Spania (Toată țara)", "🇪🇸", "130 MB", "https://download.mapsforge.org/maps/v5/europe/spain.map"),
    MapsforgeRegion("spain_andalucia", "Spania - Andalucía", "🇪🇸", "28 MB", "https://download.mapsforge.org/maps/v5/europe/spain/andalucia.map"),
    MapsforgeRegion("spain_catalunya", "Spania - Catalunya (Barcelona)", "🇪🇸", "22 MB", "https://download.mapsforge.org/maps/v5/europe/spain/catalunya.map"),
    MapsforgeRegion("spain_madrid", "Spania - Madrid", "🇪🇸", "12 MB", "https://download.mapsforge.org/maps/v5/europe/spain/madrid.map"),
    MapsforgeRegion("spain_valencia", "Spania - Valencia", "🇪🇸", "18 MB", "https://download.mapsforge.org/maps/v5/europe/spain/valencia.map"),

    // 🇬🇧 Marea Britanie & Regiuni
    MapsforgeRegion("great_britain", "Marea Britanie (Toată țara)", "🇬🇧", "110 MB", "https://download.mapsforge.org/maps/v5/europe/great_britain.map"),
    MapsforgeRegion("gb_england", "Marea Britanie - Anglia", "🇬🇧", "75 MB", "https://download.mapsforge.org/maps/v5/europe/great_britain/england.map"),
    MapsforgeRegion("gb_scotland", "Marea Britanie - Scoția", "🇬🇧", "28 MB", "https://download.mapsforge.org/maps/v5/europe/great_britain/scotland.map"),
    MapsforgeRegion("gb_wales", "Marea Britanie - Țara Galilor", "🇬🇧", "15 MB", "https://download.mapsforge.org/maps/v5/europe/great_britain/wales.map"),

    // 🇵🇱 Polonia & Regiuni
    MapsforgeRegion("poland", "Polonia (Toată țara)", "🇵🇱", "95 MB", "https://download.mapsforge.org/maps/v5/europe/poland.map"),
    MapsforgeRegion("poland_malopolskie", "Polonia - Małopolskie (Kraków)", "🇵🇱", "16 MB", "https://download.mapsforge.org/maps/v5/europe/poland/malopolskie.map"),
    MapsforgeRegion("poland_mazowieckie", "Polonia - Mazowieckie (Warszawa)", "🇵🇱", "20 MB", "https://download.mapsforge.org/maps/v5/europe/poland/mazowieckie.map"),
    MapsforgeRegion("poland_slaskie", "Polonia - Śląskie", "🇵🇱", "18 MB", "https://download.mapsforge.org/maps/v5/europe/poland/slaskie.map"),

    // 🇺̂ Ucraina, 🇦🇹 Austria, 🇨🇭 Elveția, 🇬🇷 Grecia
    MapsforgeRegion("ukraine", "Ucraina (Toată țara)", "🇺🇦", "80 MB", "https://download.mapsforge.org/maps/v5/europe/ukraine.map"),
    MapsforgeRegion("austria", "Austria", "🇦🇹", "30 MB", "https://download.mapsforge.org/maps/v5/europe/austria.map"),
    MapsforgeRegion("switzerland", "Elveția", "🇨🇭", "25 MB", "https://download.mapsforge.org/maps/v5/europe/switzerland.map"),
    MapsforgeRegion("greece", "Grecia", "🇬🇷", "35 MB", "https://download.mapsforge.org/maps/v5/europe/greece.map"),

    // 🇸🇪 Suedia, 🇳🇴 Norvegia, 🇫🇮 Finlanda, 🇩🇰 Danemarca
    MapsforgeRegion("sweden", "Suedia", "🇸🇪", "40 MB", "https://download.mapsforge.org/maps/v5/europe/sweden.map"),
    MapsforgeRegion("norway", "Norvegia", "🇳🇴", "35 MB", "https://download.mapsforge.org/maps/v5/europe/norway.map"),
    MapsforgeRegion("finland", "Finlanda", "🇫🇮", "30 MB", "https://download.mapsforge.org/maps/v5/europe/finland.map"),
    MapsforgeRegion("denmark", "Danemarca", "🇩🇰", "16 MB", "https://download.mapsforge.org/maps/v5/europe/denmark.map"),

    // 🇳🇱 Olanda, 🇧🇪 Belgia, 🇱🇺 Luxemburg, 🇵🇹 Portugalia, 🇮🇪 Irlanda, 🇮🇸 Islanda
    MapsforgeRegion("netherlands", "Olanda", "🇳🇱", "20 MB", "https://download.mapsforge.org/maps/v5/europe/netherlands.map"),
    MapsforgeRegion("belgium", "Belgia", "🇧🇪", "18 MB", "https://download.mapsforge.org/maps/v5/europe/belgium.map"),
    MapsforgeRegion("luxembourg", "Luxemburg", "🇱🇺", "6 MB", "https://download.mapsforge.org/maps/v5/europe/luxembourg.map"),
    MapsforgeRegion("portugal", "Portugalia", "🇵🇹", "25 MB", "https://download.mapsforge.org/maps/v5/europe/portugal.map"),
    MapsforgeRegion("ireland", "Irlanda", "🇮🇪", "22 MB", "https://download.mapsforge.org/maps/v5/europe/ireland.map"),
    MapsforgeRegion("iceland", "Islanda", "🇮🇸", "10 MB", "https://download.mapsforge.org/maps/v5/europe/iceland.map"),

    // 🇨🇿 Cehia, 🇸🇰 Slovacia, 🇸🇮 Slovenia, 🇭🇷 Croația, 🇷🇸 Sârbia, 🇧🇦 Bosnia, 🇦🇱 Albania
    MapsforgeRegion("czech_republic", "Cehia", "🇨🇿", "28 MB", "https://download.mapsforge.org/maps/v5/europe/czech_republic.map"),
    MapsforgeRegion("slovakia", "Slovacia", "🇸🇰", "22 MB", "https://download.mapsforge.org/maps/v5/europe/slovakia.map"),
    MapsforgeRegion("slovenia", "Slovenia", "🇸🇮", "15 MB", "https://download.mapsforge.org/maps/v5/europe/slovenia.map"),
    MapsforgeRegion("croatia", "Croația", "🇭🇷", "20 MB", "https://download.mapsforge.org/maps/v5/europe/croatia.map"),
    MapsforgeRegion("serbia", "Sârbia", "🇷🇸", "24 MB", "https://download.mapsforge.org/maps/v5/europe/serbia.map"),
    MapsforgeRegion("bosnia_herzegovina", "Bosnia și Herțegovina", "🇧🇦", "18 MB", "https://download.mapsforge.org/maps/v5/europe/bosnia-herzegovina.map"),
    MapsforgeRegion("albania", "Albania", "🇦🇱", "14 MB", "https://download.mapsforge.org/maps/v5/europe/albania.map"),

    // 🇪🇪 Estonia, 🇱🇻 Letonia, 🇱🇹 Lituania
    MapsforgeRegion("estonia", "Estonia", "🇪🇪", "12 MB", "https://download.mapsforge.org/maps/v5/europe/estonia.map"),
    MapsforgeRegion("latvia", "Letonia", "🇱🇻", "14 MB", "https://download.mapsforge.org/maps/v5/europe/latvia.map"),
    MapsforgeRegion("lithuania", "Lituania", "🇱🇹", "16 MB", "https://download.mapsforge.org/maps/v5/europe/lithuania.map"),

    // America de Nord & Regiuni
    MapsforgeRegion("us_central", "SUA - Regiunea Centrală", "🇺🇸", "110 MB", "https://download.mapsforge.org/maps/v5/north-america/us-central.map"),
    MapsforgeRegion("us_east", "SUA - Coasta de Est", "🇺🇸", "120 MB", "https://download.mapsforge.org/maps/v5/north-america/us-east.map"),
    MapsforgeRegion("us_west", "SUA - Coasta de Vest", "🇺🇸", "105 MB", "https://download.mapsforge.org/maps/v5/north-america/us-west.map"),
    MapsforgeRegion("canada", "Canada (Toată țara)", "🇨🇦", "110 MB", "https://download.mapsforge.org/maps/v5/north-america/canada.map"),
    MapsforgeRegion("mexico", "Mexic", "🇲🇽", "65 MB", "https://download.mapsforge.org/maps/v5/north-america/mexico.map")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentMapType: MapType = MapType.TOPOGRAPHIC,
    onMapTypeChange: (MapType) -> Unit = {},
    isDarkTheme: Boolean = false,
    onDarkThemeChange: (Boolean) -> Unit = {},
    selectedLanguage: String = "Română",
    onLanguageChange: (String) -> Unit = {},
    onOpenDrawer: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var expandedLang by remember { mutableStateOf(false) }
    var expandedRegion by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf(MAPSFORGE_REGIONS[0]) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredRegions = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            MAPSFORGE_REGIONS
        } else {
            MAPSFORGE_REGIONS.filter { region ->
                region.name.contains(searchQuery, ignoreCase = true) ||
                region.id.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    var offlineSyncEnabled by remember { mutableStateOf(true) }
    val isOnline = remember { MapCacheManager.isNetworkAvailable(context) }

    // CSV Export Launcher
    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                val success = HiveCsvExporter.exportToCsv(context, uri)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(context, "Export CSV reușit cu succes!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Eroare la exportul CSV.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // CSV Import Launcher
    val importCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                val success = HiveCsvExporter.importFromCsv(context, uri)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(context, "Import CSV reușit cu succes!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Eroare la importul CSV.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedLanguage == "English") "Settings & Privacy" else "Setări & Confidențialitate") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Meniu")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Dark Mode Switch Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (selectedLanguage == "English") "Dark Mode" else "Mod Întunecat (Dark Mode)", style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (selectedLanguage == "English") "Apply dark theme for night vision and battery savings" else "Aplica o temă închisă la culoare pentru confort vizual nocturn și economie de baterie",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onDarkThemeChange(it) }
                        )
                    }
                }
            }

            // CSV Export / Import Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (selectedLanguage == "English") "Export & Import Hives (CSV)" else "Export & Import Stupi și Jurnale (CSV)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (selectedLanguage == "English")
                                "Backup or restore all beehives and inspection logs in CSV format, choosing where to save or open from."
                            else
                                "Salvează sau restaurează toate datele despre stupi și jurnale în format CSV, alegând locația de salvare sau fișierul dorit.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val filename = "robeefields_hives_export_${System.currentTimeMillis()}.csv"
                                    exportCsvLauncher.launch(filename)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (selectedLanguage == "English") "Export CSV" else "Exportă CSV")
                            }

                            OutlinedButton(
                                onClick = {
                                    importCsvLauncher.launch(arrayOf("text/*", "application/vnd.ms-excel", "text/comma-separated-values", "*/*"))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (selectedLanguage == "English") "Import CSV" else "Importă CSV")
                            }
                        }
                    }
                }
            }

            // Mapsforge Offline Vector Map Downloader Card with Search Bar
            item {
                var isRegionDownloaded by remember(selectedRegion) {
                    mutableStateOf(MapCacheManager.isMapsforgeDownloaded(context, selectedRegion.id))
                }
                var isDownloading by remember { mutableStateOf(false) }
                var downloadProgress by remember { mutableFloatStateOf(0f) }

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (selectedLanguage == "English") "Mapsforge Vector Maps (.map)" else "Descărcare Hărți Vectoriale Mapsforge (.map)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (selectedLanguage == "English")
                                "Search and download offline vector maps for countries and specific regions in Europe & North America"
                            else
                                "Caută și descarcă hărți vectoriale offline pentru țări și regiuni specifice din Europa și America de Nord",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Region Selector Dropdown with Search Bar
                        ExposedDropdownMenuBox(
                            expanded = expandedRegion,
                            onExpandedChange = { expandedRegion = !expandedRegion }
                        ) {
                            OutlinedTextField(
                                value = "${selectedRegion.flag} ${selectedRegion.name} (${selectedRegion.sizeMb})",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(if (selectedLanguage == "English") "Select Country / Region" else "Selectează Țară / Regiune") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedRegion,
                                onDismissRequest = {
                                    expandedRegion = false
                                    searchQuery = ""
                                },
                                modifier = Modifier.heightIn(max = 320.dp)
                            ) {
                                // Search Input Field
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text(if (selectedLanguage == "English") "Search country or region (e.g. Bavaria, Transilvania)..." else "Caută țară sau regiune (ex. Transilvania, Bavaria)...") },
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                )

                                if (filteredRegions.isNotEmpty()) {
                                    filteredRegions.forEach { region ->
                                        DropdownMenuItem(
                                            text = { Text("${region.flag} ${region.name} (${region.sizeMb})") },
                                            onClick = {
                                                selectedRegion = region
                                                isRegionDownloaded = MapCacheManager.isMapsforgeDownloaded(context, region.id)
                                                expandedRegion = false
                                                searchQuery = ""
                                            }
                                        )
                                    }
                                } else {
                                    DropdownMenuItem(
                                        text = { Text(if (selectedLanguage == "English") "No countries or regions found" else "Nicio țară sau regiune găsită") },
                                        onClick = {}
                                    )
                                }
                            }
                        }

                        // Network status indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                                contentDescription = null,
                                tint = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = if (isOnline)
                                    (if (selectedLanguage == "English") "Active WiFi/Cellular connection" else "Conexiune Internet Activă")
                                else
                                    (if (selectedLanguage == "English") "Offline Mode" else "Fără conexiune la internet"),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }

                        Text(
                            text = if (isRegionDownloaded)
                                (if (selectedLanguage == "English") "Status: ${selectedRegion.name} saved locally 🟢" else "Stare: ${selectedRegion.name} salvată local (.map) 🟢")
                            else
                                (if (selectedLanguage == "English") "Status: ${selectedRegion.name} not downloaded 🔴" else "Stare: ${selectedRegion.name} nedescărcată 🔴"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isRegionDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )

                        if (isDownloading) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                LinearProgressIndicator(
                                    progress = { downloadProgress },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "Descărcare ${selectedRegion.name}... ${(downloadProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                enabled = !isDownloading,
                                onClick = {
                                    if (!MapCacheManager.isNetworkAvailable(context)) {
                                        Toast.makeText(context, "Fără conexiune la internet!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        isDownloading = true
                                        scope.launch(Dispatchers.IO) {
                                            MapCacheManager.downloadMapsforgeVectorMap(
                                                context = context,
                                                regionName = selectedRegion.id,
                                                downloadUrl = selectedRegion.downloadUrl,
                                                onProgress = { p -> downloadProgress = p },
                                                onComplete = { success ->
                                                    isDownloading = false
                                                    if (success) {
                                                        isRegionDownloaded = true
                                                        Toast.makeText(context, "Hartă ${selectedRegion.name} descărcată cu succes!", Toast.LENGTH_LONG).show()
                                                    } else {
                                                        Toast.makeText(context, "A apărut o eroare la descărcare.", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(if (selectedLanguage == "English") "Download" else "Descarcă")
                            }

                            if (isRegionDownloaded) {
                                OutlinedButton(
                                    onClick = {
                                        MapCacheManager.deleteMapsforgeMap(context, selectedRegion.id)
                                        isRegionDownloaded = false
                                        Toast.makeText(context, "Fișier șters!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text("Șterge", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            // Language Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(if (selectedLanguage == "English") "Language" else "Limbă / Language", style = MaterialTheme.typography.titleMedium)
                        ExposedDropdownMenuBox(
                            expanded = expandedLang,
                            onExpandedChange = { expandedLang = !expandedLang }
                        ) {
                            OutlinedTextField(
                                value = selectedLanguage,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLang) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            )
                            ExposedDropdownMenu(
                                expanded = expandedLang,
                                onDismissRequest = { expandedLang = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Română") },
                                    onClick = {
                                        onLanguageChange("Română")
                                        expandedLang = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("English") },
                                    onClick = {
                                        onLanguageChange("English")
                                        expandedLang = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Map Type Selector Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(if (selectedLanguage == "English") "Offline Map Type" else "Tip Hartă Offline (Map Theme)", style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (selectedLanguage == "English") "Switch basemap style for optimal visibility and battery life" else "Schimbă stilul hărții de fundal pentru vizibilitate optimă în teren sau economie de baterie",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        MapType.entries.forEach { type ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = (type == currentMapType),
                                    onClick = {
                                        onMapTypeChange(type)
                                    }
                                )
                                Text(
                                    text = type.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // P2P Sync Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (selectedLanguage == "English") "P2P SMS Sync" else "Sincronizare SMS P2P", style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (selectedLanguage == "English") "Automatic reception of field telemetry from team via SMS" else "Recepționare automată a datelor de la echipă prin SMS offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = offlineSyncEnabled,
                            onCheckedChange = { offlineSyncEnabled = it }
                        )
                    }
                }
            }
        }
    }
}
