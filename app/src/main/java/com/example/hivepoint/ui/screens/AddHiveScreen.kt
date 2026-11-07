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
import com.example.hivepoint.util.AppLocalization
import com.example.hivepoint.util.HiveSerializer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHiveScreen(
    selectedLanguage: String = "Română",
    onSaveHive: (Beehive) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val context = LocalContext.current

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
                title = { Text(AppLocalization.get(selectedLanguage, "Adaugă Stup / Stupină", "Add Hive / Apiary", "Ajouter une Ruche / Rucher", "Kaptár / Méhészet Hozzáadása", "Bienenstock / Imkerei Hinzufügen", "Додати вулик / пасіку", "Добавить улей / пасеку", "Добавяне на кошер / пчелин", "Adicionar Colmeia / Apiário", "Aggiungi Arnia / Apiario", "Añadir Colmena / Colmenar")) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = AppLocalization.get(selectedLanguage, "Meniu", "Menu", "Menu", "Menü", "Menü", "Меню", "Меню", "Меню", "Menu", "Menu", "Menú"))
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
                    text = { Text(AppLocalization.get(selectedLanguage, "Stup Individual 🐝", "Individual Hive 🐝", "Ruche Individuelle 🐝", "Egyedi Kaptár 🐝", "Einzelner Bienenstock 🐝", "Окремий вулик 🐝", "Отдельный улей 🐝", "Отделен кошер 🐝", "Colmeia Individual 🐝", "Arnia Individuale 🐝", "Colmena Individual 🐝")) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text(AppLocalization.get(selectedLanguage, "Stupină Complexă 🏠", "Apiary Complex 🏠", "Rucher Complexe 🏠", "Komplex Méhészet 🏠", "Imkerei-Komplex 🏠", "Комплексна пасіка 🏠", "Комплексная пасека 🏠", "Комплексен пчелин 🏠", "Complexo de Apiário 🏠", "Complesso Apiario 🏠", "Complejo Colmenar 🏠")) }
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
                        AppLocalization.get(selectedLanguage, "Adăugare Rapidă prin Hash P2P (AP1|... / HP1|...)", "Quick Add via P2P Hash (AP1|... / HP1|...)", "Ajout Rapide via Hash P2P (AP1|... / HP1|...)", "Gyors Hozzáadás P2P Hash-sel (AP1|... / HP1|...)", "Schnell-Hinzufügen per P2P Hash (AP1|... / HP1|...)", "Швидке додавання через P2P Hash (AP1|... / HP1|...)", "Быстрое добавление через P2P Hash (AP1|... / HP1|...)", "Бързо добавяне чрез P2P Hash (AP1|... / HP1|...)", "Adição Rápida via Hash P2P (AP1|... / HP1|...)", "Aggiunta Rapida tramite Hash P2P (AP1|... / HP1|...)", "Añadido Rápido por Hash P2P (AP1|... / HP1|...)"),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        AppLocalization.get(selectedLanguage, "Lipește un cod de telemetrie P2P primit prin SMS pentru auto-completare instantanee", "Paste P2P telemetry code received via SMS for instant auto-fill", "Collez le code de télémétrie P2P reçu par SMS", "Illessze be az SMS-ben kapott P2P kódot", "Fügen Sie den per SMS empfangenen P2P-Code ein", "Вставте код телеметрії P2P з SMS", "Вставьте код телеметрии P2P из SMS", "Поставете P2P телеметричен код от SMS", "Cole o código de telemetria P2P recebido por SMS", "Incolla il codice di telemetria P2P ricevuto via SMS", "Pega el código de telemetría P2P recibido por SMS"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = p2pHashText,
                        onValueChange = { p2pHashText = it },
                        label = { Text(AppLocalization.get(selectedLanguage, "Lipește Hash Stup / Stupină (AP1|... / HP1|...)", "Paste Hash (AP1|... or HP1|...)", "Coller le Hash (AP1|... / HP1|...)", "Hash beillesztése (AP1|... / HP1|...)", "Hash einfügen (AP1|... / HP1|...)", "Вставити Hash (AP1|... / HP1|...)", "Вставить Hash (AP1|... / HP1|...)", "Поставете Hash (AP1|... / HP1|...)", "Colar Hash (AP1|... / HP1|...)", "Incolla Hash (AP1|... / HP1|...)", "Pegar Hash (AP1|... / HP1|...)")) },
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
                                            AppLocalization.get(selectedLanguage, "Stupină și ${apiaryPayload.hives.size} stupi importați prin P2P!", "Apiary & ${apiaryPayload.hives.size} hives imported via P2P!", "Rucher et ${apiaryPayload.hives.size} ruches importés!", "Méhészet és ${apiaryPayload.hives.size} kaptár importálva!", "Imkerei & ${apiaryPayload.hives.size} Stöcke importiert!", "Пасіку та ${apiaryPayload.hives.size} вуликів імпортовано!", "Пасека и ${apiaryPayload.hives.size} ульев импортированы!", "Пчелин и ${apiaryPayload.hives.size} кошера импортирани!", "Apiário e ${apiaryPayload.hives.size} colmeias importados!", "Apiario e ${apiaryPayload.hives.size} arnie importati!", "Colmenar y ${apiaryPayload.hives.size} colmenas importados!"),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            AppLocalization.get(selectedLanguage, "Format Stupină P2P nevalid!", "Invalid Apiary P2P format!", "Format Rucher P2P non valide!", "Érvénytelen P2P formátum!", "Ungültiges P2P-Format!", "Недійсний формат P2P!", "Недействительный формат P2P!", "Невалиден P2P формат!", "Formato P2P inválido!", "Formato P2P non valido!", "¡Formato P2P no válido!"),
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
                                            AppLocalization.get(selectedLanguage, "Date stup importate din Hash!", "Hive data imported from Hash!", "Données de ruche importées!", "Kaptáradatok importálva!", "Stockdaten importiert!", "Дані вулика імпортовано!", "Данные улья импортированы!", "Данните са импортирани!", "Dados importados do Hash!", "Dati arnia importati da Hash!", "¡Datos importados del Hash!"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            AppLocalization.get(selectedLanguage, "Format Hash P2P nevalid!", "Invalid P2P Hash format!", "Format Hash P2P non valide!", "Érvénytelen Hash formátum!", "Ungültiges Hash-Format!", "Недійсний формат Hash!", "Недействительный формат Hash!", "Невалиден Hash формат!", "Formato Hash inválido!", "Formato Hash non valido!", "¡Formato Hash no válido!"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        AppLocalization.get(selectedLanguage, "Prefix Hash necunoscut (se așteaptă AP1| sau HP1|)", "Unknown Hash prefix (expected AP1| or HP1|)", "Préfixe Hash inconnu", "Ismeretlen Hash előtag", "Unbekanntes Hash-Präfix", "Невідомий префікс Hash", "Неизвестный префикс Hash", "Неизвестен Hash префикс", "Prefixo Hash desconhecido", "Prefisso Hash sconosciuto", "Prefijo Hash desconocido"),
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
                        Text(AppLocalization.get(selectedLanguage, "Importă Date din Hash", "Import Data from Hash", "Importer depuis Hash", "Adatok Importálása Hash-ből", "Daten aus Hash Importieren", "Імпортувати дані з Hash", "Импортировать данные из Hash", "Импортиране от Hash", "Importar Dados do Hash", "Importa Dati da Hash", "Importar Datos de Hash"))
                    }
                }
            }

            HorizontalDivider()

            if (selectedTabIndex == 0) {
                // Individual Hive Form
                Text(
                    AppLocalization.get(selectedLanguage, "Formular Stup Individual", "Individual Hive Form", "Formulaire Ruche Individuelle", "Egyedi Kaptár Űrlap", "Formular Einzelner Bienenstock", "Форма окремого вулика", "Форма отдельного улья", "Форма за отделен кошер", "Formulário de Colmeia Individual", "Modulo Arnia Individuale", "Formulario Colmena Individual"),
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = hiveNumber,
                    onValueChange = { hiveNumber = it },
                    label = { Text(AppLocalization.get(selectedLanguage, "Număr Stup / Identificator (ex. Stup #12)", "Hive Number / ID (e.g. Hive #12)", "Numéro / Identifiant (ex. Ruche #12)", "Kaptár Száma / ID (pl. Kaptár #12)", "Bienenstock-Nummer / ID (z.B. Stock #12)", "Номер / ID вулика (напр. Вулик #12)", "Номер / ID улья (напр. Улей #12)", "Номер / ID на кошер (напр. Кошер #12)", "Número / ID da Colmeia (ex. Colmeia #12)", "Numero / ID Arnia (es. Arnia #12)", "Número / ID Colmena (ej. Colmena #12)")) },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Apiary Complex Form
                Text(
                    AppLocalization.get(selectedLanguage, "Formular Stupină Complexă", "Apiary Complex Form", "Formulaire Rucher Complexe", "Komplex Méhészet Űrlap", "Formular Imkerei-Komplex", "Форма комплексної пасіки", "Форма комплексной пасеки", "Форма за комплексен пчелин", "Formulário de Complexo de Apiário", "Modulo Complesso Apiario", "Formulario Complejo Colmenar"),
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = apiaryName,
                    onValueChange = { apiaryName = it },
                    label = { Text(AppLocalization.get(selectedLanguage, "Nume / ID Stupină (ex. Stupina Montană Brașov)", "Apiary Name / ID (e.g. Mountain Apiary)", "Nom du Rucher / ID (ex. Rucher Montagne)", "Méhészet Neve / ID (pl. Hegyi Méhészet)", "Imkerei-Name / ID (z.B. Berg-Imkerei)", "Назва / ID пасіки (напр. Гірська пасіка)", "Название / ID пасеки (напр. Горная пасека)", "Име / ID на пчелин (напр. Планински пчелин)", "Nome do Apiário / ID (ex. Apiário da Montanha)", "Nome Apiario / ID (es. Apiario di Montagna)", "Nombre Colmenar / ID (ej. Colmenar de Montaña)")) },
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
                    label = { Text(AppLocalization.get(selectedLanguage, "Latitudine", "Latitude", "Latitude", "Szélesség", "Breitengrad", "Широта", "Широта", "Ширина", "Latitude", "Latitudine", "Latitud")) },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = longitudeText,
                    onValueChange = { longitudeText = it },
                    label = { Text(AppLocalization.get(selectedLanguage, "Longitudine", "Longitude", "Longitude", "Hosszúság", "Längengrad", "Довгота", "Долгота", "Дължина", "Longitude", "Longitudine", "Longitud")) },
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
                        label = { Text(AppLocalization.get(selectedLanguage, "Stare Stup (Status)", "Hive Condition (Status)", "Condition de la Ruche", "Kaptár Állapota", "Bienenstock-Zustand", "Стан вулика", "Состояние улья", "Състояние на кошер", "Condição da Colmeia", "Condizione Arnia", "Condición de la Colmena")) },
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
                label = { Text(AppLocalization.get(selectedLanguage, "Note / Descriere teren", "Field Notes / Description", "Notes / Description de terrain", "Terepi Megjegyzések / Leírás", "Feldnotizen / Beschreibung", "Польові нотатки / Опис", "Полевые заметки / Описание", "Полеви бележки / Описание", "Notas de Campo / Descrição", "Note di Campo / Descrizione", "Notas de Campo / Descripción")) },
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
                        AppLocalization.get(selectedLanguage, "Salvează și Pune Marcaj Stupină 🏠", "Save & Place Apiary Marker on Map 🏠", "Enregistrer & Placer le Marqueur 🏠", "Mentés & Méhészet Jelölő Elhelyezése 🏠", "Speichern & Imkerei-Markierung Platzieren 🏠", "Зберегти та розмістити маркер пасіки 🏠", "Сохранить и разместить маркер пасеки 🏠", "Запазване и поставяне на маркер 🏠", "Salvar e Colocar Marcador de Apiário 🏠", "Salva e Posiziona Marcatore Apiario 🏠", "Guardar y Colocar Marcador de Colmenar 🏠")
                    } else {
                        AppLocalization.get(selectedLanguage, "Salvează și Pune Pin pe Hartă 🐝", "Save & Place Pin on Map 🐝", "Enregistrer & Placer la Puce sur la Carte 🐝", "Mentés & Kaptár Jelölő Elhelyezése 🐝", "Speichern & Pin auf Karte Platzieren 🐝", "Зберегти та розмістити шпильку на карті 🐝", "Сохранить и разместить пин на карте 🐝", "Запазване и поставяне на пин 🐝", "Salvar e Colocar Pin no Mapa 🐝", "Salva e Posiziona Pin sulla Mappa 🐝", "Guardar y Colocar Pin en el Mapa 🐝")
                    }
                )
            }
        }
    }
}
