package com.example.hivepoint.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.util.HiveSerializer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHiveScreen(
    selectedLanguage: String = "Română",
    onSaveHive: (Beehive) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val context = LocalContext.current
    val isEnglish = (selectedLanguage == "English")

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Stup Individual, 1: Stupină Complexă

    // Form states
    var hiveNumber by remember { mutableStateOf("") }
    var apiaryName by remember { mutableStateOf("") }
    var latitudeText by remember { mutableStateOf("45.65737") }
    var longitudeText by remember { mutableStateOf("25.60589") }
    var selectedStatus by remember { mutableStateOf(HiveStatus.STRONG) }
    var notes by remember { mutableStateOf("") }
    var expandedStatus by remember { mutableStateOf(false) }

    var p2pHashText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEnglish) "Add Hive / Apiary" else "Adaugă Stup / Stupină") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Meniu")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mode Selector Tabs
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text(if (isEnglish) "Individual Hive 🐝" else "Stup Individual 🐝") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text(if (isEnglish) "Apiary Complex 🏠" else "Stupină Complexă 🏠") }
                )
            }

            // Hash Import Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        if (isEnglish) "Quick Add via P2P Hash (AP1|... / HP1|...)" else "Adăugare Rapidă prin Hash P2P (AP1|... / HP1|...)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        if (isEnglish) "Paste P2P telemetry code received via SMS for instant auto-fill" else "Lipește un cod de telemetrie P2P primit prin SMS pentru auto-completare instantanee",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = p2pHashText,
                        onValueChange = { p2pHashText = it },
                        label = { Text(if (isEnglish) "Paste Hash (AP1|... or HP1|...)" else "Lipește Hash Stup / Stupină (AP1|... / HP1|...)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    OutlinedButton(
                        onClick = {
                            val trimmedHash = p2pHashText.trim()
                            if (trimmedHash.isNotBlank()) {
                                if (trimmedHash.startsWith("AP1")) {
                                    val apiaryPayload = HiveSerializer.deserializeApiary(trimmedHash)
                                    if (apiaryPayload != null) {
                                        onSaveHive(apiaryPayload.apiaryMarker)
                                        apiaryPayload.hives.forEach { h -> onSaveHive(h) }
                                        Toast.makeText(
                                            context,
                                            if (isEnglish) "Apiary & ${apiaryPayload.hives.size} hives imported via P2P!" else "Stupină și ${apiaryPayload.hives.size} stupi importați prin P2P!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (isEnglish) "Invalid Apiary P2P format!" else "Format Stupină P2P nevalid!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else if (trimmedHash.startsWith("HP1")) {
                                    val deserialized = HiveSerializer.deserialize(trimmedHash)
                                    if (deserialized != null) {
                                        hiveNumber = deserialized.hiveNumber
                                        latitudeText = (deserialized.latitude ?: 45.65737).toString()
                                        longitudeText = (deserialized.longitude ?: 25.60589).toString()
                                        selectedStatus = deserialized.status
                                        notes = deserialized.notes
                                        Toast.makeText(
                                            context,
                                            if (isEnglish) "Hive data imported from Hash!" else "Date stup importate din Hash!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (isEnglish) "Invalid P2P Hash format!" else "Format Hash P2P nevalid!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        if (isEnglish) "Unknown Hash prefix (expected AP1| or HP1|)" else "Prefix Hash necunoscut (se așteaptă AP1| sau HP1|)",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(if (isEnglish) "Import Data from Hash" else "Importă Date din Hash")
                    }
                }
            }

            HorizontalDivider()

            if (selectedTabIndex == 0) {
                // Individual Hive Form
                Text(
                    if (isEnglish) "Individual Hive Form" else "Formular Stup Individual",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = hiveNumber,
                    onValueChange = { hiveNumber = it },
                    label = { Text(if (isEnglish) "Hive Number / ID (e.g. Hive #12)" else "Număr Stup / Identificator (ex. Stup #12)") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Apiary Complex Form
                Text(
                    if (isEnglish) "Apiary Complex Form" else "Formular Stupină Complexă",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = apiaryName,
                    onValueChange = { apiaryName = it },
                    label = { Text(if (isEnglish) "Apiary Name / ID (e.g. Mountain Apiary)" else "Nume / ID Stupină (ex. Stupina Montană Brașov)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = latitudeText,
                    onValueChange = { latitudeText = it },
                    label = { Text(if (isEnglish) "Latitude" else "Latitudine") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = longitudeText,
                    onValueChange = { longitudeText = it },
                    label = { Text(if (isEnglish) "Longitude" else "Longitudine") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Show Hive Condition (Status) picker ONLY when adding an Individual Hive
            if (selectedTabIndex == 0) {
                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        value = selectedStatus.toDisplayName(selectedLanguage),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isEnglish) "Hive Condition (Status)" else "Stare Stup (Status)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
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
                value = notes,
                onValueChange = { notes = it },
                label = { Text(if (isEnglish) "Field Notes / Description" else "Note / Descriere teren") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4
            )

            Button(
                onClick = {
                    val rawName = if (selectedTabIndex == 0) hiveNumber else apiaryName
                    val cleanName = rawName.removePrefix("🏠").trim()

                    if (cleanName.isNotBlank()) {
                        val newHive = Beehive(
                            apiaryId = if (selectedTabIndex == 1) "apiary_complex" else "apiary-01",
                            hiveNumber = cleanName,
                            latitude = latitudeText.toDoubleOrNull() ?: 45.65737,
                            longitude = longitudeText.toDoubleOrNull() ?: 25.60589,
                            status = if (selectedTabIndex == 1) HiveStatus.STRONG else selectedStatus,
                            notes = notes.trim()
                        )
                        onSaveHive(newHive)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (selectedTabIndex == 1) {
                        if (isEnglish) "Save & Place Apiary Marker on Map 🏠" else "Salvează și Pune Marcaj Stupină 🏠"
                    } else {
                        if (isEnglish) "Save & Place Pin on Map 🐝" else "Salvează și Pune Pin pe Hartă 🐝"
                    }
                )
            }
        }
    }
}
