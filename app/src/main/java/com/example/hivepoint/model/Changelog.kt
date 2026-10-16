package com.example.hivepoint.model

enum class ChangelogCategory(val label: String) {
    ALL("All"),
    FIELD("Field"),
    SYNC("Sync"),
    MAPS("Maps"),
    CORE("Core")
}

data class ChangelogSection(
    val title: String, // e.g., "New", "Updated", "Improved", "Fixed"
    val items: List<String>
)

data class ChangelogEntry(
    val version: String,
    val date: String,
    val title: String,
    val tag: String,
    val category: ChangelogCategory = ChangelogCategory.CORE,
    val description: String? = null,
    val sections: List<ChangelogSection> = emptyList()
)

object ChangelogRepository {
    val entries = listOf(
        ChangelogEntry(
            version = "v0.3.1",
            date = "16 Oct 2026",
            title = "RoBee Fields 0.3.1 (Field Edition)",
            tag = "Field",
            category = ChangelogCategory.FIELD,
            description = "Ajustări de interfață utilizator, integrarea siglei oficiale RoBee Fields și implementarea istoricului interactiv Changelog.",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Secțiune nouă Changelog în ecranul Despre cu istoric detaliat al versiunilor.",
                        "Integrare siglă oficială RoBee Fields ca logo al aplicației."
                    )
                ),
                ChangelogSection(
                    title = "Improved",
                    items = listOf(
                        "Optimizări ale interfeței grafice (UI) pentru o utilizare mai facilă pe teren."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.3.0",
            date = "13 Oct 2026",
            title = "RoBee Fields 0.3.0 (Field Edition)",
            tag = "Field",
            category = ChangelogCategory.FIELD,
            description = "Această actualizare aduce optimizări majore pentru navigarea offline pe teren, gestiunea avansată a stupinelor și o interfață modernizată.",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Integrare motor hărți vectoriale offline cu Mapsforge și osmdroid.",
                        "Suport pentru descărcare și arhivare pachete regionale de hărți offline."
                    )
                ),
                ChangelogSection(
                    title = "Improved",
                    items = listOf(
                        "Acuratețe crescută a localizării GPS și optimizare consum baterie pe teren.",
                        "Sistem P2P SMS/mesh îmbunătățit pentru schimb descentralizat de date."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.2.1",
            date = "12 Oct 2026",
            title = "Gestiune Stupine și Stupi",
            tag = "Sync",
            category = ChangelogCategory.SYNC,
            description = "Adăugare și gestiune extinsă a stupinelor pe hartă, cu suport pentru inspecții și evidență individuală.",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Adăugare stupine noi și poziționare GPS pe harta offline.",
                        "Afișare fișă detaliată pentru fiecare stupină."
                    )
                ),
                ChangelogSection(
                    title = "Updated",
                    items = listOf(
                        "Ajustări de compatibilitate pentru sincronizarea datelor despre stupi."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.2.0",
            date = "12 Oct 2026",
            title = "Sincronizare Descentralizată P2P",
            tag = "Sync",
            category = ChangelogCategory.SYNC,
            description = "Introducerea protocoalelor de comunicare directă între apicultori în zone fără acoperire GSM.",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Serializator HiveSerializer pentru compresie extremă a mesajelor text.",
                        "SmsReceiver & SmsSender pentru import/export automat de stupi via SMS."
                    )
                ),
                ChangelogSection(
                    title = "Updated",
                    items = listOf(
                        "Reducerea dimensiunii datelor cu peste 70% pentru transmisii SMS rapide."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.1.1",
            date = "11 Oct 2026",
            title = "Persistență Date și Stocare Locală",
            tag = "Core",
            category = ChangelogCategory.CORE,
            description = "Salvarea permanentă a datelor în baza de date locală pe dispozitiv (offline-first).",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Persistență completă a datelor în stocare locală pe durată lungă.",
                        "Optimizări și ajustări la interfața cu utilizatorul."
                    )
                ),
                ChangelogSection(
                    title = "Updated",
                    items = listOf(
                        "Stocarea sigură a datelor între sesiunile aplicației."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.1.0",
            date = "10 Oct 2026",
            title = "Nucleul Aplicației (Arhitectură Inițială)",
            tag = "Core",
            category = ChangelogCategory.CORE,
            description = "Prima versiune funcțională a aplicației pentru gestiunea stupinelor fără internet.",
            sections = listOf(
                ChangelogSection(
                    title = "New",
                    items = listOf(
                        "Bază de date locală SQLite (HiveDbHelper).",
                        "Vizualizare hărți de bază și adăugare pin-uri stupi.",
                        "Meniu de navigare și setări de bază."
                    )
                )
            )
        )
    )
}
