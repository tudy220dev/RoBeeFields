package com.example.hivepoint.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.util.HiveSerializer
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiaryDetailBottomSheet(
    apiaryId: String = "apiary_complex",
    apiaryName: String,
    apiaryLatitude: Double,
    apiaryLongitude: Double,
    appCurrentLat: Double,
    appCurrentLon: Double,
    hives: List<Beehive>,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onAddHiveToApiary: (Beehive) -> Unit,
    onSelectHive: (Beehive) -> Unit,
    onRenameApiary: (String) -> Unit,
    onDeleteApiary: () -> Unit,
    onUpdateCoords: (Double, Double) -> Unit,
    onStartMapPickMode: () -> Unit
) {
    val context = LocalContext.current
    val isEn = selectedLanguage == "English"
    var currentApiaryTitle by remember { mutableStateOf(apiaryName.removePrefix("🏠").trim()) }
    var showAddHiveDialog by remember { mutableStateOf(false) }
    var showEditApiaryDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }

    val individualHives = hives.filterNot { hive ->
        hive.hiveNumber.removePrefix("🏠").trim().equals(currentApiaryTitle, ignoreCase = true)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.padding(top = 84.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header Info
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (isEn) "🏠 Apiary: $currentApiaryTitle" else "🏠 Stupină: $currentApiaryTitle",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = String.format(
                        if (isEn) Locale.US else Locale("ro", "RO"),
                        if (isEn) "GPS: %.5f, %.5f | Hives count: %d" else "GPS: %.5f, %.5f | Stupi în stupină: %d",
                        apiaryLatitude,
                        apiaryLongitude,
                        individualHives.size
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Action Buttons - Row 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showAddHiveDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text(if (isEn) "Add Hive" else "Adaugă Stup", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = {
                        val url = String.format(Locale.US, "https://www.ventusky.com/?p=%.5f;%.5f;11", apiaryLatitude, apiaryLongitude)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, if (isEn) "No browser found" else "Niciun browser găsit", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.WbCloudy, contentDescription = null, modifier = Modifier.padding(end = 4.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(if (isEn) "Weather (Ventusky)" else "Vreme (Ventusky 🌤️)", style = MaterialTheme.typography.labelMedium)
                }
            }

            // Action Buttons - Row 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showMoveDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text(if (isEn) "Move" else "Mută GPS", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = { showEditApiaryDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text(if (isEn) "Edit Name" else "Editează", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = {
                        val payload = HiveSerializer.serializeApiary(currentApiaryTitle, apiaryLatitude, apiaryLongitude, individualHives)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, payload)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, if (isEn) "Share Apiary P2P" else "Trimite Stupină P2P"))
                        Toast.makeText(context, if (isEn) "Apiary P2P data generated!" else "Telemetrie Stupină P2P generată!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text("P2P SMS", style = MaterialTheme.typography.labelSmall)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = if (isEn) "Hives in this Apiary:" else "Lista Stupilor din această Stupină:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Scrollable Hive List
            if (individualHives.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    items(individualHives) { hive ->
                        val cleanHiveName = hive.hiveNumber.removePrefix("🏠").trim()
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            ListItem(
                                headlineContent = { Text("🐝 $cleanHiveName", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                                supportingContent = {
                                    Text("Status: ${hive.status.toDisplayName(selectedLanguage)} ${if (hive.notes.isNotBlank()) "| " + hive.notes else ""}")
                                },
                                trailingContent = {
                                    Button(onClick = { onSelectHive(hive) }) {
                                        Text(if (isEn) "Options" else "Opțiuni")
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = if (isEn) "No hives added yet in this apiary. Click 'Add Hive' above to register the first hive!" else "Niciun stup adăugat încă în această stupină. Apasă pe 'Adaugă Stup' mai sus pentru a înregistra primul stup!",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            TextButton(
                onClick = {
                    onDeleteApiary()
                    onDismiss()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(if (isEn) "Delete Entire Apiary and Hives" else "Șterge Toată Stupina și Stupii", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (showMoveDialog) {
        MoveLocationDialog(
            elementName = if (isEn) "Apiary $currentApiaryTitle" else "Stupină $currentApiaryTitle",
            currentLat = apiaryLatitude,
            currentLon = apiaryLongitude,
            appCurrentLat = appCurrentLat,
            appCurrentLon = appCurrentLon,
            selectedLanguage = selectedLanguage,
            onDismiss = { showMoveDialog = false },
            onUpdateCoords = { lat: Double, lon: Double ->
                onUpdateCoords(lat, lon)
                showMoveDialog = false
                onDismiss()
            },
            onStartMapPickMode = {
                showMoveDialog = false
                onDismiss()
                onStartMapPickMode()
            }
        )
    }

    if (showAddHiveDialog) {
        AddHiveToApiaryDialog(
            parentApiaryId = apiaryId,
            apiaryName = currentApiaryTitle,
            latitude = apiaryLatitude,
            longitude = apiaryLongitude,
            selectedLanguage = selectedLanguage,
            onDismiss = { showAddHiveDialog = false },
            onSaveHive = { newHive ->
                onAddHiveToApiary(newHive)
                showAddHiveDialog = false
            }
        )
    }

    if (showEditApiaryDialog) {
        EditApiaryDialog(
            currentName = currentApiaryTitle,
            selectedLanguage = selectedLanguage,
            onDismiss = { showEditApiaryDialog = false },
            onSave = { newName ->
                currentApiaryTitle = newName
                onRenameApiary(newName)
                showEditApiaryDialog = false
            }
        )
    }
}

@Composable
fun EditApiaryDialog(
    currentName: String,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val isEn = selectedLanguage == "English"
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "Edit Apiary" else "Editează Stupină") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isEn) "NEW Apiary Name" else "Nume NOU Stupină") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(name.removePrefix("🏠").trim())
                    }
                }
            ) {
                Text(if (isEn) "Save" else "Salvează")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isEn) "Cancel" else "Anulează")
            }
        }
    )
}

@Composable
fun AddHiveToApiaryDialog(
    parentApiaryId: String,
    apiaryName: String,
    latitude: Double,
    longitude: Double,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onSaveHive: (Beehive) -> Unit
) {
    val isEn = selectedLanguage == "English"
    var hiveNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "🐝 Add Hive in $apiaryName" else "🐝 Adaugă Stup în $apiaryName") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = hiveNumber,
                    onValueChange = { hiveNumber = it },
                    label = { Text(if (isEn) "Hive Number (e.g. Hive #1)" else "Număr Stup (ex. Stup #1)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(if (isEn) "Notes / Colony Status" else "Note / Stare Familie") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (hiveNumber.isNotBlank()) {
                        val newHive = Beehive(
                            apiaryId = parentApiaryId,
                            hiveNumber = hiveNumber.removePrefix("🏠").trim(),
                            latitude = latitude,
                            longitude = longitude,
                            status = HiveStatus.STRONG,
                            notes = notes.trim()
                        )
                        onSaveHive(newHive)
                    }
                }
            ) {
                Text(if (isEn) "Add to Apiary" else "Adaugă în Stupină")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isEn) "Cancel" else "Anulează")
            }
        }
    )
}
