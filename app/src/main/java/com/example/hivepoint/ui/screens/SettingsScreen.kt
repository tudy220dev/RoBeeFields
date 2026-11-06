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
import com.example.hivepoint.util.AppLocalization
import com.example.hivepoint.util.HiveCsvExporter
import com.example.hivepoint.util.MapCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MapsforgeRegion(
    val id: String,
    val name: String,
    val flag: String,
    val sizeMb: String,
    val downloadUrl: String
)

val MAPSFORGE_REGIONS = listOf(
    MapsforgeRegion("romania", "România (Toată țara)", "🇷🇴", "45 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("romania_transylvania", "România - Transilvania & Brașov", "🏔️", "14 MB", "https://download.mapsforge.org/maps/v5/europe/romania.map"),
    MapsforgeRegion("moldova", "Republica Moldova", "🇲🇩", "12 MB", "https://download.mapsforge.org/maps/v5/europe/moldova.map"),
    MapsforgeRegion("bulgaria", "Bulgaria (Toată țara)", "🇧🇬", "32 MB", "https://download.mapsforge.org/maps/v5/europe/bulgaria.map"),
    MapsforgeRegion("hungary", "Ungaria (Toată țara)", "🇭🇺", "28 MB", "https://download.mapsforge.org/maps/v5/europe/hungary.map"),
    MapsforgeRegion("germany", "Germania (Toată țara)", "🇩🇪", "185 MB", "https://download.mapsforge.org/maps/v5/europe/germany.map"),
    MapsforgeRegion("france", "Franța (Toată țara)", "🇫🇷", "190 MB", "https://download.mapsforge.org/maps/v5/europe/france.map"),
    MapsforgeRegion("italy", "Italia (Toată țara)", "🇮🇹", "145 MB", "https://download.mapsforge.org/maps/v5/europe/italy.map"),
    MapsforgeRegion("spain", "Spania (Toată țara)", "🇪🇸", "130 MB", "https://download.mapsforge.org/maps/v5/europe/spain.map"),
    MapsforgeRegion("great_britain", "Marea Britanie", "🇬🇧", "110 MB", "https://download.mapsforge.org/maps/v5/europe/great_britain.map"),
    MapsforgeRegion("poland", "Polonia", "🇵🇱", "95 MB", "https://download.mapsforge.org/maps/v5/europe/poland.map"),
    MapsforgeRegion("ukraine", "Ucraina", "🇺🇦", "80 MB", "https://download.mapsforge.org/maps/v5/europe/ukraine.map"),
    MapsforgeRegion("austria", "Austria", "🇦🇹", "30 MB", "https://download.mapsforge.org/maps/v5/europe/austria.map"),
    MapsforgeRegion("switzerland", "Elveția", "🇨🇭", "25 MB", "https://download.mapsforge.org/maps/v5/europe/switzerland.map"),
    MapsforgeRegion("greece", "Grecia", "🇬🇷", "35 MB", "https://download.mapsforge.org/maps/v5/europe/greece.map")
)

fun getSettingString(lang: String, key: String): String {
    return when (lang) {
        "English" -> when (key) {
            "title" -> "Settings & Privacy"
            "dark_title" -> "Dark Mode"
            "dark_desc" -> "Apply dark theme for night vision and battery savings"
            "csv_title" -> "Export & Import Hives (CSV)"
            "csv_desc" -> "Backup or restore all beehives and inspection logs in CSV format, choosing where to save or open from."
            "csv_export" -> "Export CSV"
            "csv_import" -> "Import CSV"
            "map_title" -> "Mapsforge Vector Maps (.map)"
            "map_desc" -> "Search and download offline vector maps for countries and specific regions in Europe & North America"
            "map_select" -> "Select Country / Region"
            "map_search" -> "Search country or region..."
            "lang_title" -> "Language"
            "type_title" -> "Offline Map Type"
            "type_desc" -> "Switch basemap style for optimal visibility and battery life"
            "sync_title" -> "P2P SMS Sync"
            "sync_desc" -> "Automatic reception of field telemetry from team via SMS"
            else -> key
        }
        "Français" -> when (key) {
            "title" -> "Paramètres & Confidentialité"
            "dark_title" -> "Mode Sombre"
            "dark_desc" -> "Appliquer un thème sombre pour la vision nocturne et l'économie de batterie"
            "csv_title" -> "Exporter & Importer (CSV)"
            "csv_desc" -> "Sauvegarder ou restaurer toutes les données en format CSV."
            "csv_export" -> "Exporter CSV"
            "csv_import" -> "Importer CSV"
            "map_title" -> "Cartes vectorielles Mapsforge (.map)"
            "map_desc" -> "Rechercher et télécharger des cartes hors-ligne"
            "map_select" -> "Sélectionner Pays / Région"
            "map_search" -> "Rechercher un pays ou une région..."
            "lang_title" -> "Langue"
            "type_title" -> "Type de carte hors-ligne"
            "type_desc" -> "Changer le style de carte pour une visibilité optimale"
            "sync_title" -> "Synchronisation SMS P2P"
            "sync_desc" -> "Réception automatique de la télémétrie par SMS"
            else -> key
        }
        "Magyar" -> when (key) {
            "title" -> "Beállítások és Adatvédelem"
            "dark_title" -> "Sötét Mód"
            "dark_desc" -> "Sötét téma alkalmazása az éjszakai látás és akkumulátor-megtakarítás érdekében"
            "csv_title" -> "Kaptárak Exportálása és Importálása (CSV)"
            "csv_desc" -> "Biztonsági mentés vagy visszaállítás CSV formátumban."
            "csv_export" -> "CSV Export"
            "csv_import" -> "CSV Import"
            "map_title" -> "Mapsforge Vektoros Térképek (.map)"
            "map_desc" -> "Offline térképek keresése és letöltése"
            "map_select" -> "Ország / Régió kiválasztása"
            "map_search" -> "Ország vagy régió keresése..."
            "lang_title" -> "Nyelv"
            "type_title" -> "Offline Térkép Típusa"
            "type_desc" -> "Alaptérkép stílus váltása az optimális láthatóságért"
            "sync_title" -> "P2P SMS Szinkronizálás"
            "sync_desc" -> "Telemetriai adatok automatikus fogadása SMS-ben"
            else -> key
        }
        "Deutsch" -> when (key) {
            "title" -> "Einstellungen & Datenschutz"
            "dark_title" -> "Dunkelmodus"
            "dark_desc" -> "Dunkles Design für Nachtsicht und Akkusparen anwenden"
            "csv_title" -> "Bienenstöcke Export & Import (CSV)"
            "csv_desc" -> "Sicherung oder Wiederherstellung im CSV-Format."
            "csv_export" -> "CSV Export"
            "csv_import" -> "CSV Import"
            "map_title" -> "Mapsforge Vektorkarten (.map)"
            "map_desc" -> "Offline-Vektorkarten suchen und herunterladen"
            "map_select" -> "Land / Region auswählen"
            "map_search" -> "Land oder Region suchen..."
            "lang_title" -> "Sprache"
            "type_title" -> "Offline-Kartentyp"
            "type_desc" -> "Kartensstil für optimale Sichtbarkeit wechseln"
            "sync_title" -> "P2P-SMS-Synchronisation"
            "sync_desc" -> "Automatischer Empfang von Telemetriedaten per SMS"
            else -> key
        }
        "Ukrainian" -> when (key) {
            "title" -> "Налаштування та конфіденційність"
            "dark_title" -> "Темний режим"
            "dark_desc" -> "Темна тема для нічного бачення та економії батареї"
            "csv_title" -> "Експорт та імпорт вуликів (CSV)"
            "csv_desc" -> "Резервне копіювання або відновлення у форматі CSV."
            "csv_export" -> "Експорт CSV"
            "csv_import" -> "Імпорт CSV"
            "map_title" -> "Векторні карти Mapsforge (.map)"
            "map_desc" -> "Пошук та завантаження оффлайн-карт"
            "map_select" -> "Виберіть країну / регіон"
            "map_search" -> "Пошук країни чи регіону..."
            "lang_title" -> "Мова"
            "type_title" -> "Тип оффлайн-карти"
            "type_desc" -> "Зміна стилю карти pentru кращої видимості"
            "sync_title" -> "Синхронізація P2P SMS"
            "sync_desc" -> "Автоматичний прийом телеметрії через SMS"
            else -> key
        }
        "Russian" -> when (key) {
            "title" -> "Настройки и конфиденциальность"
            "dark_title" -> "Темный режим"
            "dark_desc" -> "Темная тема для ночного видения и экономии батареи"
            "csv_title" -> "Экспорт и импорт ульев (CSV)"
            "csv_desc" -> "Резервное копирование или восстановление в формате CSV."
            "csv_export" -> "Экспорт CSV"
            "csv_import" -> "Импорт CSV"
            "map_title" -> "Векторные карты Mapsforge (.map)"
            "map_desc" -> "Поиск и загрузка офлайн-карт"
            "map_select" -> "Выберите страну / регион"
            "map_search" -> "Поиск страны или региона..."
            "lang_title" -> "Язык"
            "type_title" -> "Тип офлайн-карты"
            "type_desc" -> "Изменение стиля карты для лучшей видимости"
            "sync_title" -> "Синхронизация P2P SMS"
            "sync_desc" -> "Автоматический прием телеметрии через SMS"
            else -> key
        }
        "Bulgarian" -> when (key) {
            "title" -> "Настройки и поверителност"
            "dark_title" -> "Тъмен режим"
            "dark_desc" -> "Прилагане на тъмна тема за нощно виждане и пестене на батерия"
            "csv_title" -> "Експортиране и импортиране на кошери (CSV)"
            "csv_desc" -> "Архивиране или възстановяване във формат CSV."
            "csv_export" -> "Експорт CSV"
            "csv_import" -> "Импорт CSV"
            "map_title" -> "Векторни карти Mapsforge (.map)"
            "map_desc" -> "Търсене и изтегляне на офлайн карти"
            "map_select" -> "Изберете държава / регион"
            "map_search" -> "Търсене на държава или регион..."
            "lang_title" -> "Език"
            "type_title" -> "Тип офлайн карта"
            "type_desc" -> "Промяна на стила на картата за оптимална видимост"
            "sync_title" -> "P2P SMS Синхронизация"
            "sync_desc" -> "Автоматично приемане на телеметрия чрез SMS"
            else -> key
        }
        "Portuguese" -> when (key) {
            "title" -> "Configurações e Privacidade"
            "dark_title" -> "Modo Escuro"
            "dark_desc" -> "Aplicar tema escuro para visão noturna e economia de bateria"
            "csv_title" -> "Exportar & Importar Colmeias (CSV)"
            "csv_desc" -> "Fazer backup sau restaurar dados em formato CSV."
            "csv_export" -> "Exportar CSV"
            "csv_import" -> "Importar CSV"
            "map_title" -> "Mapas Vetoriais Mapsforge (.map)"
            "map_desc" -> "Pesquisar e baixar mapas offline"
            "map_select" -> "Selecionar País / Região"
            "map_search" -> "Pesquisar país ou região..."
            "lang_title" -> "Idioma"
            "type_title" -> "Tipo de Mapa Offline"
            "type_desc" -> "Mudar o estilo do mapa base"
            "sync_title" -> "Sincronização SMS P2P"
            "sync_desc" -> "Recepção automática de telemetria por SMS"
            else -> key
        }
        "Italian" -> when (key) {
            "title" -> "Impostazioni e Privacy"
            "dark_title" -> "Modalità Scura"
            "dark_desc" -> "Applica tema scuro per visione notturna e risparmio batteria"
            "csv_title" -> "Esporta & Importa Arnie (CSV)"
            "csv_desc" -> "Esegui backup o ripristina i dati in formato CSV."
            "csv_export" -> "Esporta CSV"
            "csv_import" -> "Importa CSV"
            "map_title" -> "Mappe Vettoriali Mapsforge (.map)"
            "map_desc" -> "Cerca e scarica mappe offline"
            "map_select" -> "Seleziona Paese / Regione"
            "map_search" -> "Cerca paese o regione..."
            "lang_title" -> "Lingua"
            "type_title" -> "Tipo di Mappa Offline"
            "type_desc" -> "Cambia lo stile della mappa di base"
            "sync_title" -> "Sincronizzazione SMS P2P"
            "sync_desc" -> "Ricezione automatica della telemetria via SMS"
            else -> key
        }
        "Spanish" -> when (key) {
            "title" -> "Ajustes y Privacidad"
            "dark_title" -> "Modo Oscuro"
            "dark_desc" -> "Aplicar tema oscuro para visión nocturna y ahorro de batería"
            "csv_title" -> "Exportar e Importar Colmenas (CSV)"
            "csv_desc" -> "Copia de seguridad o restauración en formato CSV."
            "csv_export" -> "Exportar CSV"
            "csv_import" -> "Importar CSV"
            "map_title" -> "Mapas Vectoriales Mapsforge (.map)"
            "map_desc" -> "Buscar y descargar mapas offline"
            "map_select" -> "Seleccionar País / Región"
            "map_search" -> "Buscar país o región..."
            "lang_title" -> "Idioma"
            "type_title" -> "Tipo de Mapa Offline"
            "type_desc" -> "Cambiar el estilo del mapa base"
            "sync_title" -> "Sincronización SMS P2P"
            "sync_desc" -> "Recepción automática de telemetría por SMS"
            else -> key
        }
        else -> when (key) {
            "title" -> "Setări & Confidențialitate"
            "dark_title" -> "Mod Întunecat (Dark Mode)"
            "dark_desc" -> "Aplica o temă închisă la culoare pentru confort vizual nocturn și economie de baterie"
            "csv_title" -> "Export & Import Stupi și Jurnale (CSV)"
            "csv_desc" -> "Salvează sau restaurează toate datele despre stupi și jurnale în format CSV, alegând locația de salvare sau fișierul dorit."
            "csv_export" -> "Exportă CSV"
            "csv_import" -> "Importă CSV"
            "map_title" -> "Descărcare Hărți Vectoriale Mapsforge (.map)"
            "map_desc" -> "Caută și descarcă hărți vectoriale offline pentru țări și regiuni specifice din Europa și America de Nord"
            "map_select" -> "Selectează Țară / Regiune"
            "map_search" -> "Caută țară sau regiune..."
            "lang_title" -> "Limbă / Language"
            "type_title" -> "Tip Hartă Offline (Map Theme)"
            "type_desc" -> "Schimbă stilul hărții de fundal pentru vizibilitate optimă în teren sau economie de baterie"
            "sync_title" -> "Sincronizare SMS P2P"
            "sync_desc" -> "Recepționare automată a datelor de la echipă prin SMS offline"
            else -> key
        }
    }
}

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

    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                val success = HiveCsvExporter.exportToCsv(context, uri)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(context, "Export CSV reușit!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Eroare la export CSV.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    val importCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch(Dispatchers.IO) {
                val success = HiveCsvExporter.importFromCsv(context, uri)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(context, "Import CSV reușit!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Eroare la import CSV.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(getSettingString(selectedLanguage, "title")) },
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
                            Text(getSettingString(selectedLanguage, "dark_title"), style = MaterialTheme.typography.titleMedium)
                            Text(
                                getSettingString(selectedLanguage, "dark_desc"),
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
                            text = getSettingString(selectedLanguage, "csv_title"),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = getSettingString(selectedLanguage, "csv_desc"),
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
                                Text(getSettingString(selectedLanguage, "csv_export"))
                            }

                            OutlinedButton(
                                onClick = {
                                    importCsvLauncher.launch(arrayOf("text/*", "application/vnd.ms-excel", "text/comma-separated-values", "*/*"))
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(getSettingString(selectedLanguage, "csv_import"))
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
                            text = getSettingString(selectedLanguage, "map_title"),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = getSettingString(selectedLanguage, "map_desc"),
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
                                label = { Text(getSettingString(selectedLanguage, "map_select")) },
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
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text(getSettingString(selectedLanguage, "map_search")) },
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
                                        text = { Text("No regions found") },
                                        onClick = {}
                                    )
                                }
                            }
                        }

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
                                text = if (isOnline) "Online" else "Offline",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }

                        if (isDownloading) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                LinearProgressIndicator(
                                    progress = { downloadProgress },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "Download... ${(downloadProgress * 100).toInt()}%",
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
                                        Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show()
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
                                                        Toast.makeText(context, "Map downloaded successfully!", Toast.LENGTH_LONG).show()
                                                    } else {
                                                        Toast.makeText(context, "Download error.", Toast.LENGTH_SHORT).show()
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
                                Text("Download")
                            }

                            if (isRegionDownloaded) {
                                OutlinedButton(
                                    onClick = {
                                        MapCacheManager.deleteMapsforgeMap(context, selectedRegion.id)
                                        isRegionDownloaded = false
                                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text("Delete", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            // Language Selection Card with Flags (using AppLocalization.supportedLanguages)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val currentDisplay = AppLocalization.supportedLanguages.find { it.first == selectedLanguage }?.second ?: selectedLanguage

                        Text(getSettingString(selectedLanguage, "lang_title"), style = MaterialTheme.typography.titleMedium)
                        ExposedDropdownMenuBox(
                            expanded = expandedLang,
                            onExpandedChange = { expandedLang = !expandedLang }
                        ) {
                            OutlinedTextField(
                                value = currentDisplay,
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
                                AppLocalization.supportedLanguages.forEach { (code, display) ->
                                    DropdownMenuItem(
                                        text = { Text(display) },
                                        onClick = {
                                            onLanguageChange(code)
                                            expandedLang = false
                                        }
                                    )
                                }
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
                        Text(getSettingString(selectedLanguage, "type_title"), style = MaterialTheme.typography.titleMedium)
                        Text(
                            getSettingString(selectedLanguage, "type_desc"),
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
                            Text(getSettingString(selectedLanguage, "sync_title"), style = MaterialTheme.typography.titleMedium)
                            Text(
                                getSettingString(selectedLanguage, "sync_desc"),
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
