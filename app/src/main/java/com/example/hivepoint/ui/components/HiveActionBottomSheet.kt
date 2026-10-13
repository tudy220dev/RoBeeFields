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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.util.HiveSerializer
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
    onDismiss: () -> Unit,
    onUpdateHive: (Beehive) -> Unit,
    onDeleteHive: (String) -> Unit
) {
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var showGalleryDialog by remember { mutableStateOf(false) }

    // Multi-photo picker launcher
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
            Toast.makeText(context, "${uris.size} poze adăugate cu succes!", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🐝 Meniu Stup: ${hive.hiveNumber}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = String.format(
                        Locale.US,
                        "GPS: %.5f, %.5f | Status: %s | Poze: %d",
                        hive.latitude ?: 0.0,
                        hive.longitude ?: 0.0,
                        hive.status.toDisplayName("Română"),
                        hive.photoUris.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            // 1. Vezi Galeria Foto (If photos attached)
            if (hive.photoUris.isNotEmpty()) {
                ListItem(
                    headlineContent = { Text("Galerie Foto (${hive.photoUris.size} poze) 📸", color = MaterialTheme.colorScheme.primary) },
                    supportingContent = { Text("Vizualizează, adaugă sau șterge poze din galerie") },
                    leadingContent = { Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingContent = {
                        Button(onClick = { showGalleryDialog = true }) {
                            Text("Galerie (${hive.photoUris.size})")
                        }
                    }
                )
            }

            // 2. Vezi Stup (Inspector Complete)
            ListItem(
                headlineContent = { Text("Vezi Stup / Inspector Stupină") },
                supportingContent = { Text("Afișează detaliile complete ale stupului și istoricul") },
                leadingContent = { Icon(Icons.Default.Visibility, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showDetailsDialog = true }) {
                        Text("Detalii")
                    }
                }
            )

            // 3. Edit
            ListItem(
                headlineContent = { Text("Editează Stup") },
                supportingContent = { Text("Schimbă denumirea, starea sau notele stupului") },
                leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showEditDialog = true }) {
                        Text("Editează")
                    }
                }
            )

            // 4. Add Multiple Photos
            ListItem(
                headlineContent = { Text(if (hive.photoUris.isNotEmpty()) "Adaugă Mai Multe Poze" else "Adaugă Poze Stup") },
                supportingContent = { Text(if (hive.photoUris.isNotEmpty()) "${hive.photoUris.size} poze salvate. Atinge pentru a adăuga altele." else "Selectează una sau mai multe poze din galerie") },
                leadingContent = { Icon(Icons.Default.AddAPhoto, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { photoPickerLauncher.launch("image/*") }) {
                        Text("Alege Poze")
                    }
                }
            )

            // 5. Share Hash P2P
            ListItem(
                headlineContent = { Text("Trimite Hash (SMS / P2P)") },
                supportingContent = { Text("Generează și trimite codul compact P2P al stupului") },
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
                            context.startActivity(Intent.createChooser(shareIntent, "Trimite Hash Stup P2P"))
                            Toast.makeText(context, "Hash generat cu succes!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                    ) {
                        Text("Trimite Hash")
                    }
                }
            )

            // 6. Remove
            ListItem(
                headlineContent = { Text("Șterge Stup", color = MaterialTheme.colorScheme.error) },
                supportingContent = { Text("Elimină definitiv pinul de pe hartă și din baza de date") },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(
                        onClick = {
                            onDeleteHive(hive.id)
                            onDismiss()
                        }
                    ) {
                        Text("Șterge", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    }

    if (showEditDialog) {
        EditHiveDialog(
            hive = hive,
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
            onUpdateHive = onUpdateHive,
            onDismiss = { showGalleryDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHiveDialog(
    hive: Beehive,
    onDismiss: () -> Unit,
    onSave: (Beehive) -> Unit
) {
    var hiveNumber by remember { mutableStateOf(hive.hiveNumber) }
    var notes by remember { mutableStateOf(hive.notes) }
    var selectedStatus by remember { mutableStateOf(hive.status) }
    var expandedStatus by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editează Stup") },
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
                    label = { Text("Număr/Nume Stup") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        value = selectedStatus.toDisplayName("Română"),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Stare Stup (Status)") },
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
                                text = { Text(status.toDisplayName("Română")) },
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
                    label = { Text("Descriere / Note") },
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
                Text("Salvează")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anulează")
            }
        }
    )
}

@Composable
fun HiveDetailsDialog(
    hive: Beehive,
    onDismiss: () -> Unit,
    onOpenGallery: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🐝 Inspector Stup: ${hive.hiveNumber}") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (hive.photoUris.isNotEmpty()) {
                    Text("Galerie Poze (${hive.photoUris.size}):", style = MaterialTheme.typography.labelMedium)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hive.photoUris) { photoUriStr ->
                            val bitmap = rememberPhotoBitmap(photoUriStr)
                            if (bitmap != null) {
                                Card(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(120.dp)
                                        .clickable { onOpenGallery() },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Poză Stup",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }

                Text("Stare / Status: ${hive.status.toDisplayName("Română")}", style = MaterialTheme.typography.titleMedium)
                Text(String.format(Locale.US, "Coordonate Latitudine: %.5f", hive.latitude ?: 0.0))
                Text(String.format(Locale.US, "Coordonate Longitudine: %.5f", hive.longitude ?: 0.0))
                if (hive.notes.isNotBlank()) {
                    Text("Descriere / Note Teren: ${hive.notes}")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Închide")
            }
        }
    )
}

@Composable
fun HivePhotoGalleryDialog(
    hive: Beehive,
    onUpdateHive: (Beehive) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Add additional photos launcher from inside the gallery
    val addMorePhotosLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val newUris = (hive.photoUris + uris.map { it.toString() }).distinct()
            val updated = hive.copy(
                photoUris = newUris,
                updatedAt = System.currentTimeMillis()
            )
            onUpdateHive(updated)
            Toast.makeText(context, "${uris.size} poze adăugate!", Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📸 Galerie Foto: ${hive.hiveNumber} (${hive.photoUris.size} poze)") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedButton(
                    onClick = { addMorePhotosLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text("Adaugă Alte Poze în Galerie")
                }

                if (hive.photoUris.isNotEmpty()) {
                    hive.photoUris.forEachIndexed { index, photoUriStr ->
                        val bitmap = rememberPhotoBitmap(photoUriStr)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Poză Stup ${index + 1}",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                    )
                                } else {
                                    Text("Fotografia #${index + 1} nu a putut fi încărcată.")
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            val remainingUris = hive.photoUris.filterNot { it == photoUriStr }
                                            val updated = hive.copy(
                                                photoUris = remainingUris,
                                                updatedAt = System.currentTimeMillis()
                                            )
                                            onUpdateHive(updated)
                                            Toast.makeText(context, "Poză ștearsă!", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                        Text("Șterge Poză", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text("Nicio poză în galerie.")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Închide")
            }
        }
    )
}
