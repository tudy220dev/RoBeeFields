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
import com.example.hivepoint.util.AppLocalization
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
    var currentApiaryTitle by remember { mutableStateOf(apiaryName.removePrefix("🏠").trim()) }
    var showAddHiveDialog by remember { mutableStateOf(false) }
    var showEditApiaryDialog by remember { mutableStateOf(false) }
    var showMoveDialog by remember { mutableStateOf(false) }

    val individualHives = hives.filterNot { hive ->
        hive.hiveNumber.removePrefix("🏠").trim().equals(currentApiaryTitle, ignoreCase = true)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val apiaryPrefix = AppLocalization.get(selectedLanguage, "Stupină", "Apiary", "Rucher", "Méhészet", "Imkerei", "Пасіка", "Пасека", "Пчелин", "Apiário", "Apiario", "Colmenar")
    val hivesCountLabel = AppLocalization.get(selectedLanguage, "Stupi în stupină", "Hives count", "Nombre de ruches", "Kaptárak száma", "Anzahl Bienenstöcke", "Кількість вуликів", "Количество ульев", "Брой кошери", "Quantidade de colmeias", "Numero arnie", "Número de colmenas")

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
                    text = "🏠 $apiaryPrefix: $currentApiaryTitle",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = String.format(
                        if (selectedLanguage == "English") Locale.US else Locale("ro", "RO"),
                        "GPS: %.5f, %.5f | $hivesCountLabel: %d",
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
                    Text(AppLocalization.get(selectedLanguage, "Adaugă Stup", "Add Hive", "Ajouter Ruche", "Kaptár Hozzáadása", "Stock Hinzufügen", "Додати вулик", "Добавить улей", "Добавяне на кошер", "Adicionar Colmeia", "Aggiungi Arnia", "Añadir Colmena"), style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = {
                        val url = String.format(Locale.US, "https://www.ventusky.com/?p=%.5f;%.5f;11", apiaryLatitude, apiaryLongitude)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, AppLocalization.get(selectedLanguage, "Niciun browser găsit", "No browser found", "Aucun navigateur trouvé", "Böngésző nem található", "Kein Browser gefunden", "Браузер не знайдено", "Браузер не найден", "Не е намерен браузър", "Nenhum navegador encontrado", "Nessun browser trovato", "Navegador no encontrado"), Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.WbCloudy, contentDescription = null, modifier = Modifier.padding(end = 4.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(AppLocalization.get(selectedLanguage, "Vreme (Ventusky 🌤️)", "Weather (Ventusky)", "Météo (Ventusky 🌤️)", "Időjárás (Ventusky 🌤️)", "Wetter (Ventusky 🌤️)", "Погода (Ventusky 🌤️)", "Погода (Ventusky 🌤️)", "Време (Ventusky 🌤️)", "Tempo (Ventusky 🌤️)", "Meteo (Ventusky 🌤️)", "Tiempo (Ventusky 🌤️)"), style = MaterialTheme.typography.labelMedium)
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
                    Text(AppLocalization.get(selectedLanguage, "Mută GPS", "Move", "Déplacer", "Áthelyezés", "Verschieben", "Перемістити", "Переместить", "Преместване", "Mover", "Sposta", "Mover"), style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = { showEditApiaryDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text(AppLocalization.get(selectedLanguage, "Editează", "Edit Name", "Modifier", "Szerkesztés", "Bearbeiten", "Редагувати", "Редактировать", "Редактиране", "Editar", "Modifica", "Editar"), style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = {
                        val payload = HiveSerializer.serializeApiary(currentApiaryTitle, apiaryLatitude, apiaryLongitude, individualHives)
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, payload)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, AppLocalization.get(selectedLanguage, "Trimite Stupină P2P", "Share Apiary P2P", "Partager Rucher P2P", "Méhészet Megosztása P2P", "Imkerei Teilen P2P", "Поділитися пасікою P2P", "Поделиться пасекой P2P", "Споделяне на пчелин P2P", "Compartilhar Apiário P2P", "Condividi Apiario P2P", "Compartir Colmenar P2P")))
                        Toast.makeText(context, AppLocalization.get(selectedLanguage, "Telemetrie Stupină P2P generată!", "Apiary P2P data generated!", "Données P2P générées!", "P2P adatok generálva!", "P2P-Daten generiert!", "Телеметрію P2P згенеровано!", "Телеметрия P2P сгенерирована!", "P2P данни са генерирани!", "Dados P2P gerados!", "Dati P2P generati!", "¡Datos P2P generados!"), Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text("P2P SMS", style = MaterialTheme.typography.labelSmall)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = AppLocalization.get(selectedLanguage, "Lista Stupilor din această Stupină:", "Hives in this Apiary:", "Ruches dans ce Rucher :", "A méhészet kaptárai:", "Bienenstöcke in dieser Imkerei:", "Вулики в цій пасіці:", "Ульи в этой пасеке:", "Кошери в този пчелин:", "Colmeias neste Apiário:", "Arnie in questo Apiario:", "Colmenas en este Colmenar:"),
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
                                        Text(AppLocalization.get(selectedLanguage, "Opțiuni", "Options", "Options", "Opciók", "Optionen", "Опції", "Опции", "Опции", "Opções", "Opzioni", "Opciones"))
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
                        text = AppLocalization.get(selectedLanguage, "Niciun stup adăugat încă în această stupină. Apasă pe 'Adaugă Stup' mai sus pentru a înregistra primul stup!", "No hives added yet in this apiary. Click 'Add Hive' above to register the first hive!", "Aucune ruche ajoutée. Cliquez sur 'Ajouter Ruche' pour commencer !", "Még nincs kaptár. Kattintson a 'Kaptár Hozzáadása' gombra!", "Noch keine Bienenstöcke hinzugefügt. Klicken Sie oben auf 'Stock Hinzufügen'!", "Ще немає вуликів. Натисніть 'Додати вулик' вище!", "Еще нет ульев. Нажмите 'Добавить улей' выше!", "Все още няма добавени кошери. Натиснете 'Добавяне на кошер'!", "Nenhuma colmeia adicionada ainda. Clique em 'Adicionar Colmeia'!", "Nessuna arnia ancora aggiunta. Clicca 'Aggiungi Arnia'!", "¡Sin colmenas aún. Haz clic en 'Añadir Colmena' arriba!"),
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
                Text(AppLocalization.get(selectedLanguage, "Șterge Toată Stupina și Stupii", "Delete Entire Apiary and Hives", "Supprimer Tout le Rucher", "Minden Kaptár és Méhészet Törlése", "Gesamte Imkerei Löschen", "Видалити всю пасіку та вулики", "Удалить всю пасеку и ульи", "Изтриване на целия пчелин", "Excluir Apiário Inteiro e Colmeias", "Elimina Intero Apiario e Arnie", "Eliminar Colmenar Entero y Colmenas"), color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (showMoveDialog) {
        MoveLocationDialog(
            elementName = "$apiaryPrefix $currentApiaryTitle",
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
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(AppLocalization.get(selectedLanguage, "Editează Stupină", "Edit Apiary", "Modifier le Rucher", "Méhészet Szerkesztése", "Imkerei Bearbeiten", "Редагувати пасіку", "Редактировать пасеку", "Редактиране на пчелин", "Editar Apiário", "Modifica Apiario", "Editar Colmenar")) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(AppLocalization.get(selectedLanguage, "Nume NOU Stupină", "NEW Apiary Name", "Nouveau Nom du Rucher", "Új Méhészet Neve", "Neuer Imkerei-Name", "Нова назва пасіки", "Новое название пасеки", "Ново име на пчелин", "Novo Nome do Apiário", "Nuovo Nome Apiario", "Nuevo Nombre Colmenar")) },
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
                Text(AppLocalization.get(selectedLanguage, "Salvează", "Save", "Enregistrer", "Mentés", "Speichern", "Зберегти", "Сохранить", "Запазване", "Salvar", "Salva", "Guardar"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AppLocalization.get(selectedLanguage, "Anulează", "Cancel", "Annuler", "Mégse", "Abbrechen", "Скасувати", "Отмена", "Отказ", "Cancelar", "Annulla", "Cancelar"))
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
    var hiveNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val dialogTitle = AppLocalization.get(selectedLanguage, "🐝 Adaugă Stup în $apiaryName", "🐝 Add Hive in $apiaryName", "🐝 Ajouter Ruche dans $apiaryName", "🐝 Kaptár Hozzáadása itt: $apiaryName", "🐝 Stock Hinzufügen in $apiaryName", "🐝 Додати вулик у $apiaryName", "🐝 Добавить улей в $apiaryName", "🐝 Добавяне на кошер в $apiaryName", "🐝 Adicionar Colmeia em $apiaryName", "🐝 Aggiungi Arnia in $apiaryName", "🐝 Añadir Colmena en $apiaryName")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = hiveNumber,
                    onValueChange = { hiveNumber = it },
                    label = { Text(AppLocalization.get(selectedLanguage, "Număr Stup (ex. Stup #1)", "Hive Number (e.g. Hive #1)", "Numéro de Ruche (ex. Ruche #1)", "Kaptár Száma (pl. Kaptár #1)", "Stock-Nummer (z.B. Stock #1)", "Номер вулика (напр. Вулик #1)", "Номер улья (напр. Улей #1)", "Номер на кошер (напр. Кошер #1)", "Número da Colmeia (ex. Colmeia #1)", "Numero Arnia (es. Arnia #1)", "Número de Colmena (ej. Colmena #1)")) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(AppLocalization.get(selectedLanguage, "Note / Stare Familie", "Notes / Colony Status", "Notes / Statut Colonie", "Megjegyzések / Család Állapota", "Notizen / Volkszustand", "Примітки / Стан сім'ї", "Заметки / Состояние семьи", "Бележки / Състояние", "Notas / Status da Colônia", "Note / Stato Famiglia", "Notas / Estado de Colonia")) },
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
                Text(AppLocalization.get(selectedLanguage, "Adaugă în Stupină", "Add to Apiary", "Ajouter au Rucher", "Hozzáadás a Méhészethez", "Zur Imkerei Hinzufügen", "Додати до пасіки", "Добавить в пасеку", "Добавяне към пчелина", "Adicionar ao Apiário", "Aggiungi all'Apiario", "Añadir al Colmenar"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AppLocalization.get(selectedLanguage, "Anulează", "Cancel", "Annuler", "Mégse", "Abbrechen", "Скасувати", "Отмена", "Отказ", "Cancelar", "Annulla", "Cancelar"))
            }
        }
    )
}
