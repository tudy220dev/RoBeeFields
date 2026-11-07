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
import androidx.compose.material.icons.filled.WbCloudy
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
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.hivepoint.model.BeeRace
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.model.InspectionLog
import com.example.hivepoint.model.QueenState
import com.example.hivepoint.model.SwarmingTendency
import com.example.hivepoint.model.Temperament
import com.example.hivepoint.util.AppLocalization
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
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", if (selectedLanguage == "English") Locale.US else Locale("ro", "RO")) }
    val lastLogText = if (latestLog != null) {
        val lastLogPrefix = AppLocalization.get(selectedLanguage, "Ultimul jurnal", "Last log", "Dernier journal", "Utolsó napló", "Letztes Protokoll", "Останній журнал", "Последний журнал", "Последен дневник", "Último registro", "Ultimo registro", "Último registro")
        val queenLabel = AppLocalization.get(selectedLanguage, "Regină", "Queen", "Reine", "Anya", "Königin", "Матка", "Матка", "Майка", "Rainha", "Regina", "Reina")
        "$lastLogPrefix: ${dateFormat.format(Date(latestLog.timestamp))} | $queenLabel: ${latestLog.queenState.toDisplayName(selectedLanguage)}"
    } else {
        AppLocalization.get(selectedLanguage, "Niciun jurnal înregistrat încă.", "No inspection logs recorded yet.", "Aucun journal d'inspection enregistré.", "Még nincs naplóbejegyzés.", "Noch keine Inspektionsprotokolle.", "Ще немає записів огляду.", "Записей осмотра пока нет.", "Все още няма записани дневници.", "Nenhum registro de inspeção salvo.", "Nessun registro ancora salvato.", "Sin registros de inspección aún.")
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
            Toast.makeText(context, AppLocalization.get(selectedLanguage, "${uris.size} poze adăugate cu succes!", "${uris.size} photos added successfully!", "${uris.size} photos ajoutées!", "${uris.size} kép sikeresen hozzáadva!", "${uris.size} Fotos hinzugefügt!", "${uris.size} фото успішно додано!", "${uris.size} фото успешно добавлено!", "${uris.size} снимки са добавени!", "${uris.size} fotos adicionadas!", "${uris.size} foto aggiunte!", "¡${uris.size} fotos añadidas!"), Toast.LENGTH_SHORT).show()
        }
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
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${AppLocalization.get(selectedLanguage, "🐝 Meniu Stup", "🐝 Hive Menu", "🐝 Menu Ruche", "🐝 Kaptár Menü", "🐝 Bienenstock-Menü", "🐝 Меню вулика", "🐝 Меню улья", "🐝 Меню кошер", "🐝 Menu Colmeia", "🐝 Menu Arnia", "🐝 Menú Colmena")}: ${hive.hiveNumber}",
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
                        Text(AppLocalization.get(selectedLanguage, "Vezi Jurnale (${logs.size})", "View Logs (${logs.size})", "Voir Journaux (${logs.size})", "Naplók (${logs.size})", "Protokolle (${logs.size})", "Журнали (${logs.size})", "Журналы (${logs.size})", "Дневници (${logs.size})", "Ver Registros (${logs.size})", "Vedi Registri (${logs.size})", "Ver Registros (${logs.size})"))
                    }
                    Button(
                        onClick = { showCreateLogDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text(AppLocalization.get(selectedLanguage, "Creează Jurnal", "Create Log", "Créer Journal", "Új Napló", "Neues Protokoll", "Створити журнал", "Создать журнал", "Нов дневник", "Criar Registro", "Crea Registro", "Crear Registro"))
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            ListItem(
                headlineContent = { Text(AppLocalization.get(selectedLanguage, "Mută Stupul (Locație GPS)", "Move Hive (GPS Location)", "Déplacer la Ruche (Position GPS)", "Kaptár Áthelyezése (GPS)", "Bienenstock Verschieben (GPS)", "Перемістити вулик (GPS)", "Переместить улей (GPS)", "Преместване на кошер (GPS)", "Mover Colmeia (Posição GPS)", "Sposta Arnia (Posizione GPS)", "Mover Colmena (Posición GPS)")) },
                supportingContent = { Text(AppLocalization.get(selectedLanguage, "Schimbă poziția GPS pe hartă sau prin coordonate", "Change GPS position on map or coordinates", "Changer la position GPS sur la carte", "GPS pozíció módosítása a térképen", "GPS-Position auf Karte ändern", "Змінити позицію GPS на карті", "Изменить позицию GPS на карте", "Промяна на GPS позиция", "Mudar posição GPS no mapa", "Cambia posizione GPS sulla mappa", "Cambiar posición GPS en el mapa")) },
                leadingContent = { Icon(Icons.Default.MyLocation, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showMoveDialog = true }) {
                        Text(AppLocalization.get(selectedLanguage, "Mută", "Move", "Déplacer", "Áthelyezés", "Verschieben", "Перемістити", "Переместить", "Преместване", "Mover", "Sposta", "Mover"))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(AppLocalization.get(selectedLanguage, "Prognoză Meteo (Ventusky 🌤️)", "Weather Forecast (Ventusky 🌤️)", "Météo Live (Ventusky 🌤️)", "Időjárás (Ventusky 🌤️)", "Wettervorhersage (Ventusky 🌤️)", "Прогноз погоди (Ventusky 🌤️)", "Прогноз погоды (Ventusky 🌤️)", "Прогноза за времето (Ventusky 🌤️)", "Previsão do Tempo (Ventusky 🌤️)", "Previsioni Meteo (Ventusky 🌤️)", "Pronóstico del Tiempo (Ventusky 🌤️)")) },
                supportingContent = { Text(AppLocalization.get(selectedLanguage, "Deschide prognoza meteo live pe hartă la coordonatele stupului", "Open live weather forecast at hive coordinates", "Ouvrir la météo aux coordonnées de la ruche", "Élő időjárás megnyitása a kaptár koordinátáinál", "Live-Wetter an den Bienenstock-Koordinaten öffnen", "Відкрити прогноз погоди за координатами вулика", "Открыть прогноз погоды по координатам улья", "Прогноза за времето по координати", "Abrir previsão do tempo nas coordenadas", "Apri previsioni meteo a coordinate arnia", "Abrir pronóstico del tiempo en coordenadas")) },
                leadingContent = { Icon(Icons.Default.WbCloudy, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    Button(
                        onClick = {
                            val lat = hive.latitude ?: appCurrentLat
                            val lon = hive.longitude ?: appCurrentLon
                            val url = String.format(Locale.US, "https://www.ventusky.com/?p=%.5f;%.5f;11", lat, lon)
                            val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(viewIntent)
                            } catch (_: Exception) {
                                Toast.makeText(context, AppLocalization.get(selectedLanguage, "Niciun browser găsit", "No browser found", "Aucun navigateur trouvé", "Böngésző nem található", "Kein Browser gefunden", "Браузер не знайдено", "Браузер не найден", "Не е намерен браузър", "Nenhum navegador encontrado", "Nessun browser trovato", "Navegador no encontrado"), Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text(AppLocalization.get(selectedLanguage, "Vreme", "Weather", "Météo", "Időjárás", "Wetter", "Погода", "Погода", "Време", "Tempo", "Meteo", "Tiempo"))
                    }
                }
            )

            if (hive.photoUris.isNotEmpty()) {
                ListItem(
                    headlineContent = { Text("${AppLocalization.get(selectedLanguage, "Galerie Foto", "Photo Gallery", "Galerie Photo", "Fotógaléria", "Fotogalerie", "Фотогалерея", "Фотогалерея", "Фотогалерия", "Galeria de Fotos", "Galleria Fotografica", "Galería de Fotos")} (${hive.photoUris.size}) 📸", color = MaterialTheme.colorScheme.primary) },
                    supportingContent = { Text(AppLocalization.get(selectedLanguage, "Vizualizează, adaugă sau șterge poze din galerie", "View, add or delete photos", "Voir, ajouter ou supprimer des photos", "Fotók megtekintése, hozzáadása vagy törlése", "Fotos anzeigen, hinzufügen oder löschen", "Переглянути, додати або видалити фото", "Просмотреть, добавить или удалить фото", "Преглед, добавяне или изтриване на снимки", "Ver, adicionar ou excluir fotos", "Visualizza, aggiungi o elimina foto", "Ver, añadir o eliminar fotos")) },
                    leadingContent = { Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingContent = {
                        Button(onClick = { showGalleryDialog = true }) {
                            Text("${AppLocalization.get(selectedLanguage, "Galerie", "Gallery", "Galerie", "Galéria", "Galerie", "Галерея", "Галерея", "Галерия", "Galeria", "Galleria", "Galería")} (${hive.photoUris.size})")
                        }
                    }
                )
            }

            ListItem(
                headlineContent = { Text(AppLocalization.get(selectedLanguage, "Vezi Stup / Detalii Complete", "View Hive / Full Details", "Voir la Ruche / Détails Complets", "Kaptár Részletei", "Bienenstock-Details", "Переглянути вулик / Повні деталі", "Просмотреть улей / Полные детали", "Преглед на кошер / Детайли", "Ver Colmeia / Detalhes Completos", "Vedi Arnia / Dettagli Completi", "Ver Colmena / Detalles Completos")) },
                supportingContent = { Text(AppLocalization.get(selectedLanguage, "Afișează detaliile complete ale stupului", "Display complete hive details", "Afficher tous les détails de la ruche", "Kaptár összes részletének megjelenítése", "Vollständige Bienenstock-Details anzeigen", "Відобразити повні деталі вулика", "Отобразить полные детали улья", "Показване на пълните детайли", "Exibir detalhes completos da colmeia", "Mostra dettagli completi dell'arnia", "Mostrar detalles completos de la colmena")) },
                leadingContent = { Icon(Icons.Default.Visibility, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showDetailsDialog = true }) {
                        Text(AppLocalization.get(selectedLanguage, "Detalii", "Details", "Détails", "Részletek", "Details", "Деталі", "Детали", "Детайли", "Detalhes", "Dettagli", "Detalles"))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(AppLocalization.get(selectedLanguage, "Editează Stup", "Edit Hive", "Modifier la Ruche", "Kaptár Szerkesztése", "Bienenstock Bearbeiten", "Редагувати вулик", "Редактировать улей", "Редактиране на кошер", "Editar Colmeia", "Modifica Arnia", "Editar Colmena")) },
                supportingContent = { Text(AppLocalization.get(selectedLanguage, "Schimbă denumirea, starea sau notele stupului", "Change hive number, status, or notes", "Changer le numéro, le statut ou les notes", "Szám, állapot vagy megjegyzések módosítása", "Nummer, Status oder Notizen ändern", "Змінити номер, статус або примітки", "Изменить номер, статус или заметки", "Промяна на номер, състояние или бележки", "Alterar número, status ou notas", "Modifica numero, stato o note", "Cambiar número, estado o notas")) },
                leadingContent = { Icon(Icons.Default.Edit, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { showEditDialog = true }) {
                        Text(AppLocalization.get(selectedLanguage, "Editează", "Edit", "Modifier", "Szerkesztés", "Bearbeiten", "Редагувати", "Редактировать", "Редактиране", "Editar", "Modifica", "Editar"))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(if (hive.photoUris.isNotEmpty()) AppLocalization.get(selectedLanguage, "Adaugă Mai Multe Poze", "Add More Photos", "Ajouter Plus de Photos", "Több Kép Hozzáadása", "Mehr Fotos Hinzufügen", "Додати більше фото", "Добавить больше фото", "Добавяне на още снимки", "Adicionar Mais Fotos", "Aggiungi Altre Foto", "Añadir Más Fotos") else AppLocalization.get(selectedLanguage, "Adaugă Poze Stup", "Add Photos", "Ajouter des Photos", "Fotók Hozzáadása", "Fotos Hinzufügen", "Додати фото вулика", "Добавить фото улья", "Добавяне на снимки", "Adicionar Fotos", "Aggiungi Foto", "Añadir Fotos")) },
                supportingContent = { Text(if (hive.photoUris.isNotEmpty()) AppLocalization.get(selectedLanguage, "${hive.photoUris.size} poze salvate.", "${hive.photoUris.size} saved photos.", "${hive.photoUris.size} photos enregistrées.", "${hive.photoUris.size} mentett kép.", "${hive.photoUris.size} gespeicherte Fotos.", "${hive.photoUris.size} збережених фото.", "${hive.photoUris.size} сохраненных фото.", "${hive.photoUris.size} запазени снимки.", "${hive.photoUris.size} fotos salvas.", "${hive.photoUris.size} foto salvate.", "${hive.photoUris.size} fotos guardadas.") else AppLocalization.get(selectedLanguage, "Selectează poze din galerie", "Choose photos from gallery", "Choisir des photos dans la galerie", "Válasszon képeket a galériából", "Fotos aus Galerie auswählen", "Виберіть фото з галереї", "Выберите фото из галереи", "Изберете снимки от галерията", "Escolha fotos da galeria", "Scegli foto dalla galleria", "Elegir fotos de la galería")) },
                leadingContent = { Icon(Icons.Default.AddAPhoto, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(onClick = { photoPickerLauncher.launch("image/*") }) {
                        Text(AppLocalization.get(selectedLanguage, "Alege Poze", "Choose Photos", "Choisir Photos", "Fotók Kiválasztása", "Fotos Wählen", "Вибрати фото", "Выбрать фото", "Избор на снимки", "Escolher Fotos", "Scegli Foto", "Elegir Fotos"))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(AppLocalization.get(selectedLanguage, "Trimite Hash (SMS / P2P)", "Share Hash (SMS / P2P)", "Partager Hash (SMS / P2P)", "Hash Megosztása (SMS / P2P)", "Hash Teilen (SMS / P2P)", "Поділитися Hash (SMS / P2P)", "Поделиться Hash (SMS / P2P)", "Споделяне на Hash (SMS / P2P)", "Compartilhar Hash (SMS / P2P)", "Condividi Hash (SMS / P2P)", "Compartir Hash (SMS / P2P)")) },
                supportingContent = { Text(AppLocalization.get(selectedLanguage, "Generează și trimite codul compact P2P al stupului", "Generate and share P2P compact code", "Générer et partager le code P2P compact", "Kompakt P2P kód generálása és megosztása", "Kompakten P2P-Code generieren und teilen", "Згенерувати та поділитися кодом P2P", "Сгенерировать и поделиться кодом P2P", "Генериране и споделяне на P2P код", "Gerar e compartilhar código P2P", "Genera e condividi codice P2P", "Generar y compartir código P2P")) },
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
                            context.startActivity(Intent.createChooser(shareIntent, AppLocalization.get(selectedLanguage, "Trimite Hash Stup P2P", "Share Hive Hash P2P", "Partager Hash P2P", "P2P Hash Megosztása", "P2P-Hash Teilen", "Поділитися P2P Hash", "Поделиться P2P Hash", "Споделяне на P2P Hash", "Compartilhar Hash P2P", "Condividi Hash P2P", "Compartir Hash P2P")))
                            Toast.makeText(context, AppLocalization.get(selectedLanguage, "Hash generat cu succes!", "Hash generated successfully!", "Hash généré avec succès!", "Hash sikeresen generálva!", "Hash erfolgreich generiert!", "Hash успішно створено!", "Hash успешно создан!", "Hash е генериран успешно!", "Hash gerado com sucesso!", "Hash generato con successo!", "¡Hash generado con éxito!"), Toast.LENGTH_SHORT).show()
                            onDismiss()
                        }
                    ) {
                        Text(AppLocalization.get(selectedLanguage, "Trimite Hash", "Share Hash", "Partager Hash", "Hash Megosztása", "Hash Teilen", "Поділитися", "Поделиться", "Споделяне", "Compartilhar", "Condividi", "Compartir"))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(AppLocalization.get(selectedLanguage, "Șterge Stup", "Delete Hive", "Supprimer la Ruche", "Kaptár Törlése", "Bienenstock Löschen", "Видалити вулик", "Удалить улей", "Изтриване на кошер", "Excluir Colmeia", "Elimina Arnia", "Eliminar Colmena"), color = MaterialTheme.colorScheme.error) },
                supportingContent = { Text(AppLocalization.get(selectedLanguage, "Elimină definitiv pinul de pe hartă și din baza de date", "Permanently remove pin from map and database", "Supprimer définitivement de la carte et de la base de données", "Végleges törlés a térképről és az adatbázisból", "Dauerhaft von Karte und Datenbank entfernen", "Остаточно видалити з карти та бази даних", "Окончательно удалить с карты и из базы данных", "Завинаги премахване от картата и базата данни", "Remover permanentemente do mapa e banco de dados", "Rimuovi definitivamente da mappa e database", "Eliminar permanentemente del mapa y la base de datos")) },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = {
                    TextButton(
                        onClick = {
                            onDeleteHive(hive.id)
                            onDismiss()
                        }
                    ) {
                        Text(AppLocalization.get(selectedLanguage, "Șterge", "Delete", "Supprimer", "Törlés", "Löschen", "Видалити", "Удалить", "Изтриване", "Excluir", "Elimina", "Eliminar"), color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    }

    if (showMoveDialog) {
        val elementPrefix = AppLocalization.get(selectedLanguage, "Stup", "Hive", "Ruche", "Kaptár", "Bienenstock", "Вулик", "Улей", "Кошер", "Colmeia", "Arnia", "Colmena")
        MoveLocationDialog(
            elementName = "$elementPrefix ${hive.hiveNumber}",
            currentLat = hive.latitude ?: appCurrentLat,
            currentLon = hive.longitude ?: appCurrentLon,
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
                    Toast.makeText(context, AppLocalization.get(selectedLanguage, "Jurnal de inspecție salvat cu succes!", "Inspection log saved successfully!", "Journal d'inspection enregistré avec succès!", "Ellenőrzési napló sikeresen elmentve!", "Inspektionsprotokoll erfolgreich gespeichert!", "Журнал огляду успішно збережено!", "Журнал осмотра успешно сохранен!", "Дневникът е запазен успешно!", "Registro de inspeção salvo com sucesso!", "Registro di ispezione salvato con successo!", "¡Registro de inspección guardado con éxito!"), Toast.LENGTH_SHORT).show()
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
    var selectedBeeRace by remember { mutableStateOf(hive.beeRace) }
    var selectedSwarming by remember { mutableStateOf(hive.swarmingTendency) }
    var expandedStatus by remember { mutableStateOf(false) }
    var expandedRace by remember { mutableStateOf(false) }
    var expandedSwarming by remember { mutableStateOf(false) }

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

                // Bee Race Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedRace,
                    onExpandedChange = { expandedRace = !expandedRace }
                ) {
                    OutlinedTextField(
                        value = "${selectedBeeRace.toDisplayName(selectedLanguage)} (${selectedBeeRace.scientificName})",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isEn) "Bee Race (Type)" else "Tip Albine / Rasă") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRace) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedRace,
                        onDismissRequest = { expandedRace = false }
                    ) {
                        BeeRace.entries.forEach { race ->
                            DropdownMenuItem(
                                text = { Text("${race.toDisplayName(selectedLanguage)} (${race.scientificName})") },
                                onClick = {
                                    selectedBeeRace = race
                                    expandedRace = false
                                }
                            )
                        }
                    }
                }

                // Swarming Tendency Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedSwarming,
                    onExpandedChange = { expandedSwarming = !expandedSwarming }
                ) {
                    OutlinedTextField(
                        value = selectedSwarming.toDisplayName(selectedLanguage),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isEn) "Swarming Tendency" else "Tendințe de Roit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSwarming) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSwarming,
                        onDismissRequest = { expandedSwarming = false }
                    ) {
                        SwarmingTendency.entries.forEach { tendency ->
                            DropdownMenuItem(
                                text = { Text(tendency.toDisplayName(selectedLanguage)) },
                                onClick = {
                                    selectedSwarming = tendency
                                    expandedSwarming = false
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
                        beeRace = selectedBeeRace,
                        swarmingTendency = selectedSwarming,
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
                Text(if (isEn) "Bee Race: ${hive.beeRace.toDisplayName(selectedLanguage)} (${hive.beeRace.scientificName})" else "Rasa de albine: ${hive.beeRace.toDisplayName(selectedLanguage)} (${hive.beeRace.scientificName})")
                Text(if (isEn) "Swarming Tendency: ${hive.swarmingTendency.toDisplayName(selectedLanguage)}" else "Tendințe de roit: ${hive.swarmingTendency.toDisplayName(selectedLanguage)}")
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

@Composable
fun MoveLocationDialog(
    elementName: String,
    currentLat: Double,
    currentLon: Double,
    appCurrentLat: Double,
    appCurrentLon: Double,
    selectedLanguage: String = "Română",
    onDismiss: () -> Unit,
    onUpdateCoords: (Double, Double) -> Unit,
    onStartMapPickMode: () -> Unit
) {
    val isEn = selectedLanguage == "English"
    var latText by remember { mutableStateOf(currentLat.toString()) }
    var lonText by remember { mutableStateOf(currentLon.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEn) "Move Location: $elementName" else "Mută Poziția: $elementName")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isEn) "Choose how you want to relocate $elementName:" else "Alege cum dorești să relochezi $elementName:",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Option 1: Current GPS Position
                Button(
                    onClick = {
                        onUpdateCoords(appCurrentLat, appCurrentLon)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        if (isEn) "Use Current GPS Location" else "Folosește Poz. GPS Curentă"
                    )
                }

                // Option 2: Pick on Map
                OutlinedButton(
                    onClick = onStartMapPickMode,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (isEn) "Pick Position on Map" else "Selectează Poziția pe Hartă"
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Option 3: Manual Input
                Text(
                    text = if (isEn) "Or enter coordinates manually:" else "Sau introdu coordonatele manual:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = latText,
                    onValueChange = { latText = it },
                    label = { Text("Latitude / Latitudine") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lonText,
                    onValueChange = { lonText = it },
                    label = { Text("Longitude / Longitudine") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val lat = latText.toDoubleOrNull()
                    val lon = lonText.toDoubleOrNull()
                    if (lat != null && lon != null) {
                        onUpdateCoords(lat, lon)
                    }
                }
            ) {
                Text(if (isEn) "Save Coordinates" else "Salvează Coordonatele")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isEn) "Cancel" else "Renunță")
            }
        }
    )
}

