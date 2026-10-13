package com.example.hivepoint.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.hivepoint.util.MapCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    var offlineSyncEnabled by remember { mutableStateOf(true) }

    var isDownloading by remember { mutableStateOf(false) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    val isOnline = remember { MapCacheManager.isNetworkAvailable(context) }
    var isCached by remember { mutableStateOf(MapCacheManager.isMapCached(context, currentMapType)) }

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
                                    .menuAnchor()
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

            // Online Map Sync / Caching Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(if (selectedLanguage == "English") "Offline Map Cache Update" else "Actualizare & Cache Hartă Offline", style = MaterialTheme.typography.titleMedium)

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
                                text = if (isOnline) (if (selectedLanguage == "English") "Active Connection (WiFi/Cellular)" else "Conexiune Internet Activă (WiFi/Cellular)") else (if (selectedLanguage == "English") "Offline Mode" else "Deconectat (Mod Offline)"),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        }

                        Text(
                            text = if (isCached) (if (selectedLanguage == "English") "Cache Status: Saved locally for offline use." else "Stare Cache: Hartă salvată local în cacheDir pentru operare offline.")
                            else (if (selectedLanguage == "English") "Cache Status: No tiles downloaded. Using vector maps." else "Stare Cache: Nicio hartă descărcată anterior. Se folosește harta vector locală."),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (isDownloading) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                LinearProgressIndicator(
                                    progress = { downloadProgress },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = "Descărcare & Salvare Tile-uri... ${(downloadProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        Button(
                            enabled = !isDownloading,
                            onClick = {
                                if (!MapCacheManager.isNetworkAvailable(context)) {
                                    Toast.makeText(
                                        context,
                                        "Fără conexiune la internet! Aplicația folosește hărțile din cache-ul local.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    isDownloading = true
                                    scope.launch(Dispatchers.IO) {
                                        MapCacheManager.downloadAndCacheMap(
                                            context = context,
                                            mapType = currentMapType,
                                            onProgress = { p -> downloadProgress = p },
                                            onComplete = { success ->
                                                isDownloading = false
                                                if (success) {
                                                    isCached = true
                                                    Toast.makeText(
                                                        context,
                                                        "Harta a fost salvată cu succes în cache-ul local!",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "A apărut o eroare la descărcarea hărții.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(if (selectedLanguage == "English") "Update Map Cache" else "Actualizează Harta (Online / WiFi)")
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
                                        isCached = MapCacheManager.isMapCached(context, type)
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
