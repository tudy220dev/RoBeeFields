package com.example.hivepoint.model

enum class ChangelogCategory(val label: String) {
    ALL("All"),
    FIELD("Field"),
    SYNC("Sync"),
    MAPS("Maps"),
    CORE("Core")
}

data class ChangelogSection(
    val title: String,
    val items: List<String>
) {
    fun getTitle(lang: String): String = title
    fun getItems(lang: String): List<String> = items
}

data class ChangelogEntry(
    val version: String,
    val date: String,
    val title: String,
    val tag: String,
    val category: ChangelogCategory = ChangelogCategory.CORE,
    val description: String? = null,
    val sections: List<ChangelogSection> = emptyList()
) {
    fun getDate(lang: String): String = date
    fun getTag(lang: String): String = tag
    fun getTitle(lang: String): String = title
    fun getDescription(lang: String): String? = description
}

object ChangelogRepository {
    val entries = listOf(
        ChangelogEntry(
            version = "v0.4.0",
            date = "19 Oct 2026",
            title = "RoBee Fields 0.4.0 (Field & Inspection Edition)",
            tag = "Field",
            category = ChangelogCategory.FIELD,
            description = "Sistem complet de inspecție pe teren cu 5 secțiuni dedicate, jurnale persistente și export/import CSV cu alegerea locației.",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Jurnale de inspecție pe teren cu 5 secțiuni intuitive (Starea Reginei, Puterea Familiei, Intervenții & Tratamente, Comportament și Notițe rapide).",
                        "Funcționalitate de Export și Import în format CSV, permițând alegerea locației de salvare și a fișierului dorit.",
                        "Istoric detaliat al inspecțiilor salvat persistent în baza de date SQLite."
                    )
                ),
                ChangelogSection(
                    title = "Improved",
                    items = listOf(
                        "Optimizarea fluxului de lucru pentru apicultori pe teren fără conexiune internet."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.3.2",
            dateRo = "18 Oct 2026",
            dateEn = "18 Oct 2026",
            titleRo = "RoBee Fields 0.3.2 (Suport Multilingvistic pentru Changelog)",
            titleEn = "RoBee Fields 0.3.2 (Multilingual Support for Changelog)",
            tagRo = "Teren",
            tagEn = "Field",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Suport extins pentru comutarea limbii (Română/Engleză) în secțiunea Changelog și ecranul Despre, împreună cu remedierea erorilor de localizare.",
            descriptionEn = "Extended language switching support (Romanian/English) in the Changelog section and About screen, along with localization bug fixes.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Suport bilingv (Română și Engleză) pentru toate intrările din istoricul Changelog.",
                        "Tratarea dinamică a schimbării limbii în ecranul Despre.",
                        "Actualizare versiune aplicație la v0.3.2."
                    ),
                    itemsEn = listOf(
                        "Bilingual support (Romanian and English) for all Changelog history entries.",
                        "Dynamic language switching handling on the About screen.",
                        "App version updated to v0.3.2."
                    )
                ),
                ChangelogSection(
                    titleRo = "Rezolvat",
                    titleEn = "Fixed",
                    itemsRo = listOf(
                        "Remedierea problemei de sincronizare a limbii în ecranul Changelog la schimbarea setărilor."
                    ),
                    itemsEn = listOf(
                        "Fixed language synchronization issue in the Changelog screen when changing settings."
                    )
                ),
                ChangelogSection(
                    titleRo = "Îmbunătățit",
                    titleEn = "Improved",
                    itemsRo = listOf(
                        "Ajustări de layout și vizualizare pentru suportul multilingv în interfața utilizator."
                    ),
                    itemsEn = listOf(
                        "Layout and visualization adjustments for multilingual support in the UI."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.3.1",
            dateRo = "16 Oct 2026",
            dateEn = "16 Oct 2026",
            titleRo = "RoBee Fields 0.3.1 (Field Edition)",
            titleEn = "RoBee Fields 0.3.1 (Field Edition)",
            tagRo = "Teren",
            tagEn = "Field",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Ajustări de interfață utilizator, integrarea siglei oficiale RoBee Fields și implementarea istoricului interactiv Changelog.",
            descriptionEn = "UI adjustments, integration of the official RoBee Fields logo, and implementation of the interactive Changelog history.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Secțiune nouă Changelog în ecranul Despre cu istoric detaliat al versiunilor.",
                        "Integrare siglă oficială RoBee Fields ca logo al aplicației."
                    ),
                    itemsEn = listOf(
                        "New Changelog section in the About screen with detailed version history.",
                        "Integration of official RoBee Fields logo as the app icon."
                    )
                ),
                ChangelogSection(
                    titleRo = "Îmbunătățit",
                    titleEn = "Improved",
                    itemsRo = listOf(
                        "Optimizări ale interfeței grafice (UI) pentru o utilizare mai facilă pe teren."
                    ),
                    itemsEn = listOf(
                        "UI optimizations for easier usage in the field."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.3.0",
            dateRo = "13 Oct 2026",
            dateEn = "13 Oct 2026",
            titleRo = "RoBee Fields 0.3.0 (Field Edition)",
            titleEn = "RoBee Fields 0.3.0 (Field Edition)",
            tagRo = "Teren",
            tagEn = "Field",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Această actualizare aduce optimizări majore pentru navigarea offline pe teren, gestiunea avansată a stupinelor și o interfață modernizată.",
            descriptionEn = "This update brings major optimizations for offline field navigation, advanced apiary management, and a modernized interface.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Integrare motor hărți vectoriale offline cu Mapsforge și osmdroid.",
                        "Suport pentru descărcare și arhivare pachete regionale de hărți offline."
                    ),
                    itemsEn = listOf(
                        "Offline vector map engine integration with Mapsforge and osmdroid.",
                        "Support for downloading and archiving regional offline map packages."
                    )
                ),
                ChangelogSection(
                    titleRo = "Îmbunătățit",
                    titleEn = "Improved",
                    itemsRo = listOf(
                        "Acuratețe crescută a localizării GPS și optimizare consum baterie pe teren.",
                        "Sistem P2P SMS/mesh îmbunătățit pentru schimb descentralizat de date."
                    ),
                    itemsEn = listOf(
                        "Increased GPS localization accuracy and battery consumption optimization in the field.",
                        "Improved P2P SMS/mesh system for decentralized data exchange."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.2.1",
            dateRo = "12 Oct 2026",
            dateEn = "12 Oct 2026",
            titleRo = "Gestiune Stupine și Stupi",
            titleEn = "Apiary and Hive Management",
            tagRo = "Sincronizare",
            tagEn = "Sync",
            category = ChangelogCategory.SYNC,
            descriptionRo = "Adăugare și gestiune extinsă a stupinelor pe hartă, cu suport pentru inspecții și evidență individuală.",
            descriptionEn = "Addition and extended management of apiaries on the map, with support for inspections and individual record keeping.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Adăugare stupine noi și poziționare GPS pe harta offline.",
                        "Afișare fișă detaliată pentru fiecare stupină."
                    ),
                    itemsEn = listOf(
                        "Adding new apiaries and GPS positioning on the offline map.",
                        "Displaying detailed record sheets for each apiary."
                    )
                ),
                ChangelogSection(
                    titleRo = "Actualizat",
                    titleEn = "Updated",
                    itemsRo = listOf(
                        "Ajustări de compatibilitate pentru sincronizarea datelor despre stupi."
                    ),
                    itemsEn = listOf(
                        "Compatibility adjustments for syncing hive data."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.2.0",
            dateRo = "12 Oct 2026",
            dateEn = "12 Oct 2026",
            titleRo = "Sincronizare Descentralizată P2P",
            titleEn = "Decentralized P2P Sync",
            tagRo = "Sincronizare",
            tagEn = "Sync",
            category = ChangelogCategory.SYNC,
            descriptionRo = "Introducerea protocoalelor de comunicare directă între apicultori în zone fără acoperire GSM.",
            descriptionEn = "Introduction of direct communication protocols between beekeepers in areas without cellular coverage.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Serializator HiveSerializer pentru compresie extremă a mesajelor text.",
                        "SmsReceiver & SmsSender pentru import/export automat de stupi via SMS."
                    ),
                    itemsEn = listOf(
                        "HiveSerializer for extreme text message compression.",
                        "SmsReceiver & SmsSender for automated hive import/export via SMS."
                    )
                ),
                ChangelogSection(
                    titleRo = "Actualizat",
                    titleEn = "Updated",
                    itemsRo = listOf(
                        "Reducerea dimensiunii datelor cu peste 70% pentru transmisii SMS rapide."
                    ),
                    itemsEn = listOf(
                        "Reduced data payload size by over 70% for rapid SMS transmissions."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.1.1",
            dateRo = "11 Oct 2026",
            dateEn = "11 Oct 2026",
            titleRo = "Persistență Date și Stocare Locală",
            titleEn = "Data Persistence & Local Storage",
            tagRo = "Nucleu",
            tagEn = "Core",
            category = ChangelogCategory.CORE,
            descriptionRo = "Salvarea permanentă a datelor în baza de date locală pe dispozitiv (offline-first).",
            descriptionEn = "Permanent data persistence in the device local database (offline-first).",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Persistență completă a datelor în stocare locală pe durată lungă.",
                        "Optimizări și ajustări la interfața cu utilizatorul."
                    ),
                    itemsEn = listOf(
                        "Complete long-term data persistence in local storage.",
                        "UI optimizations and tweaks."
                    )
                ),
                ChangelogSection(
                    titleRo = "Actualizat",
                    titleEn = "Updated",
                    itemsRo = listOf(
                        "Stocarea sigură a datelor între sesiunile aplicației."
                    ),
                    itemsEn = listOf(
                        "Secure data retention between application sessions."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.1.0",
            dateRo = "10 Oct 2026",
            dateEn = "10 Oct 2026",
            titleRo = "Nucleul Aplicației (Arhitectură Inițială)",
            titleEn = "App Core (Initial Architecture)",
            tagRo = "Nucleu",
            tagEn = "Core",
            category = ChangelogCategory.CORE,
            descriptionRo = "Prima versiune funcțională a aplicației pentru gestiunea stupinelor fără internet.",
            descriptionEn = "First functional version of the application for managing apiaries without internet access.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Bază de date locală SQLite (HiveDbHelper).",
                        "Vizualizare hărți de bază și adăugare pin-uri stupi.",
                        "Meniu de navigare și setări de bază."
                    ),
                    itemsEn = listOf(
                        "Local SQLite database (HiveDbHelper).",
                        "Basic map rendering and hive pin placement.",
                        "Navigation menu and basic settings."
                    )
                )
            )
        )
    )
}