package com.example.hivepoint.ui.components

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hivepoint.db.HiveDbHelper
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.model.InspectionLog
import com.example.hivepoint.model.QueenState
import com.example.hivepoint.model.Temperament
import com.example.hivepoint.util.HiveSerializer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun rememberPhotoBitmap(photoUriStr: String?): ImageBitmap? {
    val context = LocalContext.current
    return remember(photoUriStr) {
        if (photoUriStr.isNullOrBlank()) return@remember null
        try {
            val uri = Uri.parse(photoUriStr)
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiveActionBottomSheet(
    hive: Beehive,
    appCurrentLat: Double,
    appCurrentLon: Double,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onUpdateHive: (Beehive) -> Unit,
    onDeleteHive: (String) -> Unit,
    onUpdateCoords: (Double, Double) -> Unit,
    onStartMapPickMode: () -> Unit
) {
    val context = LocalContext.current
    val isEn = selectedLanguage == "English"
    val dbHelper = remember { HiveDbHelper(context) }
    val scope = rememberCoroutineScope()

    var logs by remember { mutableStateOf<List<InspectionLog>>(emptyList()) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var showGalleryDialog by remember { mutableStateOf(false) }
    var showCreateLogDialog by remember { mutableStateOf(false) }
    var showViewLogsDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(hive.id) {
        logs = dbHelper.getInspectionLogsForHive(hive.id)
    }

    val latestLog = logs.firstOrNull()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", if (isEn) Locale.US else Locale("ro", "RO")) }
    val lastLogText = if (latestLog != null) {
        if (isEn)
            "Last log: ${dateFormat.format(Date(latestLog.timestamp))} | Queen: ${latestLog.queenState.toDisplayName(selectedLanguage)}"
        else
            "Ultimul jurnal: ${dateFormat.format(Date(latestLog.timestamp))} | Regină: ${latestLog.queenState.toDisplayName(selectedLanguage)}"
    } else {
        if (isEn) "No inspection logs recorded yet." else "Niciun jurnal înregistrat încă."
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val newPhotoUris = (hive.photoUris + uris.map { it.toString() }).distinct()
            val updated = hive.copy(
                photoUris = newPhotoUris,
                updatedAt = System.currentTimeMillis()
            )
            onUpdateHive(updated)
            Toast.makeText(context, if (isEn) "${uris.size} photos added successfully!" else "${uris.size} poze adăugate cu succes!", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (isEn) "🐝 Hive Menu: ${hive.hiveNumber}" else "🐝 Meniu Stup: ${hive.hiveNumber}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = lastLogText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showViewLogsDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text(if (isEn) "View Logs (${logs.size})" else "Vezi Jurnale (${logs.size})")
                    }
                    Button(
                        onClick = { showCreateLogDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text(if (isEn) "Create Log" else "Crează Jurnal")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Mută Stupul
            ListItem(
                headlineContent = { Text(if (isEn) "Move Hive (GPS Location)" else "Mută Stupul (Move Location)") },
                supportingContent = { Text(if (isEn) "Change GPS position on map or coordinates" else "Schimbă poziția GPS pe hartă sau prin coordonate") },
                leadingContent = { Icon(Icons.Default.MyLocation, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showMoveDialog = true }) {
                        Text(if (isEn) "Move" else "Mută")
                    }
                }
            )

            if (hive.photoUris.isNotEmpty()) {
                ListItem(
                    headlineContent = { Text(if (isEn) "Photo Gallery (${hive.photoUris.size}) 📸" else "Galerie Foto (${hive.photoUris.size} poze) 📸", color = MaterialTheme.colorScheme.primary) },
                    supportingContent = { Text(if (isEn) "View, add or delete photos" else "Vizualizează, adaugă sau șterge poze din galerie") },
                    leadingContent = { Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingContent = {
                        Button(onClick = { showGalleryDialog = true }) {
                            Text(if (isEn) "Gallery (${hive.photoUris.size})" else "Galerie (${hive.photoUris.size})")
                        }
                    }
                )
            }

            ListItem(
                headlineContent = { Text(if (isEn) "View Hive / Full Details" else "Vezi Stup / Detalii Complete") },
                supportingContent = { Text(if (isEn) "Display complete hive details" else "Afișează detaliile complete ale stupului") },
                leadingContent = { Icon(Icons.Default.Visibility, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showDetailsDialog = true }) {
                        Text(if (isEn) "Details" else "Detalii")
                    }
                }
            )

            ListItem(
                headlineContent = { Text(if (isEn) "Edit Hive" else "Editează Stup") },
                supportingContent = { Text(if (isEn) "Change hive number, status, or notes" else "Schimbă denumirea, starea sau notele stupului") },
                leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showEditDialog = true }) {
                        Text(if (isEn) "Edit" else "Editează")
                    }
                }
            )

            ListItem(
                headlineContent = { Text(if (isEn) "Add Photos" else if (hive.photoUris.isNotEmpty()) "Adaugă Mai Multe Poze" else "Adaugă Poze Stup") },
                supportingContent = { Text(if (isEn) "${hive.photoUris.size} saved photos" else if (hive.photoUris.isNotEmpty()) "${hive.photoUris.size} poze salvate." else "Selectează poze din galerie") },
                leadingContent = { Icon(Icons.Default.AddAPhoto, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { photoPickerLauncher.launch("image/*") }) {
                        Text(if (isEn) "Choose Photos" else "Alege Poze")
                    }
                }
            )

            ListItem(
                headlineContent = { Text(if (isEn) "Share Hash (SMS / P2P)" else "Trimite Hash (SMS / P2P)") },
                supportingContent = { Text(if (isEn) "Generate and share P2P compact code" else "Generează și trimite codul compact P2P al stupului") },
                leadingContent = { Icon(Icons.Default.Share, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(
                        onClick = {
                            val hash = HiveSerializer.serialize(hive)
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, hash)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, if (isEn) "Share Hive Hash P2P" else "Trimite Hash Stup P2P"))
                            Toast.makeText(context, if (isEn) "Hash generated successfully!" else "Hash generat cu succes!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                    ) {
                        Text(if (isEn) "Share Hash" else "Trimite Hash")
                    }
                }
            )

            ListItem(
                headlineContent = { Text(if (isEn) "Delete Hive" else "Șterge Stup", color = MaterialTheme.colorScheme.error) },
                supportingContent = { Text(if (isEn) "Permanently remove pin from map and database" else "Elimină definitiv pinul de pe hartă și din baza de date") },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(
                        onClick = {
                            onDeleteHive(hive.id)
                            onDismiss()
                        }
                    ) {
                        Text(if (isEn) "Delete" else "Șterge", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    }

    if (showMoveDialog) {
        MoveLocationDialog(
            elementName = if (isEn) "Hive ${hive.hiveNumber}" else "Stup ${hive.hiveNumber}",
            currentLat = hive.latitude,
            currentLon = hive.longitude,
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

    if (showCreateLogDialog) {
        CreateInspectionLogDialog(
            hive = hive,
            latestLog = latestLog,
            selectedLanguage = selectedLanguage,
            onDismiss = { showCreateLogDialog = false },
            onSave = { newLog ->
                scope.launch {
                    dbHelper.insertInspectionLog(newLog)
                    logs = dbHelper.getInspectionLogsForHive(hive.id)
                    showCreateLogDialog = false
                    Toast.makeText(context, if (isEn) "Inspection log saved successfully!" else "Jurnal de inspecție salvat cu succes!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    if (showViewLogsDialog) {
        ViewInspectionLogsDialog(
            hive = hive,
            logs = logs,
            selectedLanguage = selectedLanguage,
            onDismiss = { showViewLogsDialog = false }
        )
    }

    if (showEditDialog) {
        EditHiveDialog(
            hive = hive,
            selectedLanguage = selectedLanguage,
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                onUpdateHive(updated)
                showEditDialog = false
                onDismiss()
            }
        )
    }

    if (showDetailsDialog) {
        HiveDetailsDialog(
            hive = hive,
            selectedLanguage = selectedLanguage,
            onDismiss = { showDetailsDialog = false },
            onOpenGallery = {
                showDetailsDialog = false
                showGalleryDialog = true
            }
        )
    }

    if (showGalleryDialog) {
        HivePhotoGalleryDialog(
            hive = hive,
            selectedLanguage = selectedLanguage,
            onUpdateHive = onUpdateHive,
            onDismiss = { showGalleryDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInspectionLogDialog(
    hive: Beehive,
    latestLog: InspectionLog?,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onSave: (InspectionLog) -> Unit
) {
    val isEn = selectedLanguage == "English"
    var selectedQueenState by remember { mutableStateOf(latestLog?.queenState ?: QueenState.SEEN) }
    var broodFrames by remember { mutableStateOf(latestLog?.broodFrames ?: 5) }
    var honeyFrames by remember { mutableStateOf(latestLog?.honeyFrames ?: 7) }

    var addedSuper by remember { mutableStateOf(false) }
    var varroaTreatment by remember { mutableStateOf(false) }
    var fedSyrup by remember { mutableStateOf(false) }
    var fedPatty by remember { mutableStateOf(false) }
    var changedBottomBoard by remember { mutableStateOf(false) }

    var temperament by remember { mutableStateOf(latestLog?.temperament ?: Temperament.CALM) }
    var notes by remember { mutableStateOf("") }
    var showNotesField by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "Inspection Log: Hive ${hive.hiveNumber}" else "Jurnal Inspecție: Stup ${hive.hiveNumber}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Starea Reginei
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (isEn) "1. Queen Status" else "1. Starea Reginei", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        QueenState.entries.filter { it != QueenState.UNKNOWN }.forEach { state ->
                            val isSelected = selectedQueenState == state
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedQueenState = state }
                            ) {
                                Text(
                                    text = state.toDisplayName(selectedLanguage),
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Section 2: Puterea Familiei
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (isEn) "2. Colony Strength (Frames)" else "2. Puterea Familiei (Rame)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isEn) "Brood Frames" else "Rame Puiet", style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { if (broodFrames > 0) broodFrames-- }) {
                                Icon(Icons.Default.Remove, contentDescription = "Minus")
                            }
                            Text(text = "$broodFrames", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { broodFrames++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Plus")
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isEn) "Honey / Pollen Frames" else "Rame Miere / Păstură", style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = { if (honeyFrames > 0) honeyFrames-- }) {
                                Icon(Icons.Default.Remove, contentDescription = "Minus")
                            }
                            Text(text = "$honeyFrames", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { honeyFrames++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Plus")
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Section 3: Intervenții & Tratamente
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (isEn) "3. Interventions & Treatments" else "3. Intervenții & Tratamente", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(
                            selected = addedSuper,
                            onClick = { addedSuper = !addedSuper },
                            label = { Text(if (isEn) "+ Added Super" else "+ Adăugat Cată") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        FilterChip(
                            selected = varroaTreatment,
                            onClick = { varroaTreatment = !varroaTreatment },
                            label = { Text(if (isEn) "Varroa Treatment" else "Tratament Varroa") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        FilterChip(
                            selected = fedSyrup,
                            onClick = { fedSyrup = !fedSyrup },
                            label = { Text(if (isEn) "Fed (Syrup)" else "Hrănit (Sirop)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        FilterChip(
                            selected = fedPatty,
                            onClick = { fedPatty = !fedPatty },
                            label = { Text(if (isEn) "Fed (Patty)" else "Hrănit (Turtă)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        FilterChip(
                            selected = changedBottomBoard,
                            onClick = { changedBottomBoard = !changedBottomBoard },
                            label = { Text(if (isEn) "Replaced Bottom Board" else "Schimbat Fund/Pardoseală") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                HorizontalDivider()

                // Section 4: Comportament & Agresivitate
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (isEn) "4. Temperament & Aggressiveness" else "4. Comportament & Agresivitate", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Temperament.entries.filter { it != Temperament.UNKNOWN }.forEach { temp ->
                            val isSelected = temperament == temp
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { temperament = temp }
                            ) {
                                Text(
                                    text = temp.toDisplayName(selectedLanguage),
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Section 5: Notiță Rapidă
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (!showNotesField) {
                        OutlinedButton(
                            onClick = { showNotesField = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (isEn) "💬 Tap for quick note (e.g. replace queen)" else "💬 Apasă pentru notă rapidă (ex: de schimbat matca)")
                        }
                    } else {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text(if (isEn) "Quick note / Remarks" else "Notiță rapidă / Observații") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            maxLines = 3
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val log = InspectionLog(
                        hiveId = hive.id,
                        queenState = selectedQueenState,
                        broodFrames = broodFrames,
                        honeyFrames = honeyFrames,
                        addedSuper = addedSuper,
                        varroaTreatment = varroaTreatment,
                        fedSyrup = fedSyrup,
                        fedPatty = fedPatty,
                        changedBottomBoard = changedBottomBoard,
                        temperament = temperament,
                        notes = notes
                    )
                    onSave(log)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewInspectionLogsDialog(
    hive: Beehive,
    logs: List<InspectionLog>,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit
) {
    val isEn = selectedLanguage == "English"
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", if (isEn) Locale.US else Locale("ro", "RO")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "Inspection Logs: Hive ${hive.hiveNumber}" else "Jurnale Inspecție: Stup ${hive.hiveNumber}") },
        text = {
            if (logs.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(if (isEn) "No inspection logs recorded for this hive." else "Niciun jurnal înregistrat pentru acest stup.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(logs) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = dateFormat.format(Date(log.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (isEn)
                                        "👑 Queen: ${log.queenState.toDisplayName(selectedLanguage)} | 🐝 Brood: ${log.broodFrames} | 🍯 Honey: ${log.honeyFrames}"
                                    else
                                        "👑 Regină: ${log.queenState.toDisplayName(selectedLanguage)} | 🐝 Puiet: ${log.broodFrames} | 🍯 Miere: ${log.honeyFrames}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                val interventions = mutableListOf<String>()
                                if (log.addedSuper) interventions.add(if (isEn) "Super added" else "Cată adăugată")
                                if (log.varroaTreatment) interventions.add("Varroa")
                                if (log.fedSyrup) interventions.add(if (isEn) "Syrup" else "Sirop")
                                if (log.fedPatty) interventions.add(if (isEn) "Patty" else "Turtă")
                                if (log.changedBottomBoard) interventions.add(if (isEn) "Bottom board" else "Fund schimbat")

                                if (interventions.isNotEmpty()) {
                                    Text(
                                        text = if (isEn) "Interventions: ${interventions.joinToString(", ")}" else "Intervenții: ${interventions.joinToString(", ")}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = if (isEn) "Temperament: ${log.temperament.toDisplayName(selectedLanguage)}" else "Temperament: ${log.temperament.toDisplayName(selectedLanguage)}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                if (log.notes.isNotBlank()) {
                                    Text(
                                        text = if (isEn) "Note: ${log.notes}" else "Notă: ${log.notes}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(if (isEn) "Close" else "Închide")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHiveDialog(
    hive: Beehive,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onSave: (Beehive) -> Unit
) {
    val isEn = selectedLanguage == "English"
    var hiveNumber by remember { mutableStateOf(hive.hiveNumber) }
    var notes by remember { mutableStateOf(hive.notes) }
    var selectedStatus by remember { mutableStateOf(hive.status) }
    var expandedStatus by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "Edit Hive" else "Editează Stup") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = hiveNumber,
                    onValueChange = { hiveNumber = it },
                    label = { Text(if (isEn) "Hive Number/Name" else "Număr/Nume Stup") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        value = selectedStatus.toDisplayName(selectedLanguage),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isEn) "Hive Status" else "Stare Stup (Status)") },
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

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(if (isEn) "Description / Notes" else "Descriere / Note") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = hive.copy(
                        hiveNumber = hiveNumber.ifBlank { hive.hiveNumber },
                        status = selectedStatus,
                        notes = notes,
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(updated)
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
fun HiveDetailsDialog(
    hive: Beehive,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onOpenGallery: () -> Unit
) {
    val isEn = selectedLanguage == "English"
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", if (isEn) Locale.US else Locale("ro", "RO")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "Hive Details: ${hive.hiveNumber}" else "Detalii Stup: ${hive.hiveNumber}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(if (isEn) "Status: ${hive.status.toDisplayName(selectedLanguage)}" else "Stare: ${hive.status.toDisplayName(selectedLanguage)}")
                Text(if (isEn) "Coordinates: ${hive.latitude}, ${hive.longitude}" else "Coordonate: ${hive.latitude}, ${hive.longitude}")
                Text(if (isEn) "Last update: ${dateFormat.format(Date(hive.updatedAt))}" else "Ultima actualizare: ${dateFormat.format(Date(hive.updatedAt))}")
                if (hive.notes.isNotBlank()) {
                    Text(if (isEn) "Notes: ${hive.notes}" else "Note: ${hive.notes}")
                }
                if (hive.photoUris.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = onOpenGallery) {
                        Text(if (isEn) "View Gallery (${hive.photoUris.size} photos)" else "Vezi Galerie (${hive.photoUris.size} poze)")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(if (isEn) "Close" else "Închide")
            }
        }
    )
}

@Composable
fun HivePhotoGalleryDialog(
    hive: Beehive,
    selectedLanguage: String = "Română",
    onUpdateHive: (Beehive) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isEn = selectedLanguage == "English"
    var currentPhotos by remember { mutableStateOf(hive.photoUris) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEn) "Photo Gallery - Hive ${hive.hiveNumber}" else "Galerie Foto - Stup ${hive.hiveNumber}") },
        text = {
            if (currentPhotos.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(if (isEn) "No photos in gallery." else "Nicio poză în galerie.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentPhotos) { uriStr ->
                        val bitmap = rememberPhotoBitmap(uriStr)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Poză stupină / stup",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(if (isEn) "Image unavailable" else "Imagine indisponibilă")
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            currentPhotos = currentPhotos - uriStr
                                            val updated = hive.copy(
                                                photoUris = currentPhotos,
                                                updatedAt = System.currentTimeMillis()
                                            )
                                            onUpdateHive(updated)
                                            Toast.makeText(context, if (isEn) "Photo deleted" else "Poză ștearsă", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Text(if (isEn) "Delete photo" else "Șterge poză", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(if (isEn) "Close" else "Închide")
            }
        }
    )
}
