package com.example.hivepoint.model

import com.example.hivepoint.util.AppLocalization

enum class ChangelogCategory(val label: String) {
    ALL("All"),
    FIELD("Field"),
    SYNC("Sync"),
    MAPS("Maps"),
    CORE("Core");

    fun toDisplayName(lang: String): String {
        return AppLocalization.get(
            lang,
            ro = when (this) { ALL -> "Toate"; FIELD -> "Teren"; SYNC -> "Sincronizare"; MAPS -> "Hărți"; CORE -> "Nucleu" },
            en = when (this) { ALL -> "All"; FIELD -> "Field"; SYNC -> "Sync"; MAPS -> "Maps"; CORE -> "Core" },
            fr = when (this) { ALL -> "Tous"; FIELD -> "Terrain"; SYNC -> "Synchro"; MAPS -> "Cartes"; CORE -> "Noyau" },
            hu = when (this) { ALL -> "Összes"; FIELD -> "Terep"; SYNC -> "Szinkron"; MAPS -> "Térképek"; CORE -> "Mag" },
            de = when (this) { ALL -> "Alle"; FIELD -> "Feld"; SYNC -> "Synchro"; MAPS -> "Karten"; CORE -> "Kern" },
            uk = when (this) { ALL -> "Всі"; FIELD -> "Поле"; SYNC -> "Синхронізація"; MAPS -> "Карти"; CORE -> "Ядро" },
            ru = when (this) { ALL -> "Все"; FIELD -> "Поле"; SYNC -> "Синхронизация"; MAPS -> "Карты"; CORE -> "Ядро" },
            bg = when (this) { ALL -> "Всички"; FIELD -> "Поле"; SYNC -> "Синхронизация"; MAPS -> "Карти"; CORE -> "Ядро" },
            pt = when (this) { ALL -> "Todos"; FIELD -> "Campo"; SYNC -> "Sincronização"; MAPS -> "Mapas"; CORE -> "Núcleo" },
            it = when (this) { ALL -> "Tutti"; FIELD -> "Campo"; SYNC -> "Sincronizzazione"; MAPS -> "Mappe"; CORE -> "Nucleo" },
            es = when (this) { ALL -> "Todos"; FIELD -> "Campo"; SYNC -> "Sincronización"; MAPS -> "Mapas"; CORE -> "Núcleo" },
        )
    }
}

data class ChangelogSection(
    val titleRo: String,
    val titleEn: String,
    val titleFr: String = titleEn,
    val titleHu: String = titleEn,
    val titleDe: String = titleEn,
    val titleUk: String = titleEn,
    val titleRu: String = titleEn,
    val titleBg: String = titleEn,
    val titlePt: String = titleEn,
    val titleIt: String = titleEn,
    val titleEs: String = titleEn,
    val itemsRo: List<String>,
    val itemsEn: List<String>,
    val itemsFr: List<String> = itemsEn,
    val itemsHu: List<String> = itemsEn,
    val itemsDe: List<String> = itemsEn,
    val itemsUk: List<String> = itemsEn,
    val itemsRu: List<String> = itemsEn,
    val itemsBg: List<String> = itemsEn,
    val itemsPt: List<String> = itemsEn,
    val itemsIt: List<String> = itemsEn,
    val itemsEs: List<String> = itemsEn
) {
    fun getTitle(lang: String): String = AppLocalization.get(lang, titleRo, titleEn, titleFr, titleHu, titleDe, titleUk, titleRu, titleBg, titlePt, titleIt, titleEs)
    fun getItems(lang: String): List<String> {
        return when (lang) {
            "Română" -> itemsRo
            "English" -> itemsEn
            "Français" -> itemsFr
            "Magyar" -> itemsHu
            "Deutsch" -> itemsDe
            "Ukrainian" -> itemsUk
            "Russian" -> itemsRu
            "Bulgarian" -> itemsBg
            "Portuguese" -> itemsPt
            "Italian" -> itemsIt
            "Spanish" -> itemsEs
            else -> itemsEn
        }
    }
}

data class ChangelogEntry(
    val version: String,
    val dateRo: String,
    val dateEn: String,
    val titleRo: String,
    val titleEn: String,
    val titleFr: String = titleEn,
    val titleHu: String = titleEn,
    val titleDe: String = titleEn,
    val titleUk: String = titleEn,
    val titleRu: String = titleEn,
    val titleBg: String = titleEn,
    val titlePt: String = titleEn,
    val titleIt: String = titleEn,
    val titleEs: String = titleEn,
    val tagRo: String,
    val tagEn: String,
    val tagFr: String = tagEn,
    val tagHu: String = tagEn,
    val tagDe: String = tagEn,
    val tagUk: String = tagEn,
    val tagRu: String = tagEn,
    val tagBg: String = tagEn,
    val tagPt: String = tagEn,
    val tagIt: String = tagEn,
    val tagEs: String = tagEn,
    val category: ChangelogCategory = ChangelogCategory.CORE,
    val descriptionRo: String? = null,
    val descriptionEn: String? = null,
    val descriptionFr: String? = descriptionEn,
    val descriptionHu: String? = descriptionEn,
    val descriptionDe: String? = descriptionEn,
    val descriptionUk: String? = descriptionEn,
    val descriptionRu: String? = descriptionEn,
    val descriptionBg: String? = descriptionEn,
    val descriptionPt: String? = descriptionEn,
    val descriptionIt: String? = descriptionEn,
    val descriptionEs: String? = descriptionEn,
    val sections: List<ChangelogSection> = emptyList()
) {
    fun getDate(lang: String): String = if (lang == "English") dateEn else dateRo
    fun getTag(lang: String): String = AppLocalization.get(lang, tagRo, tagEn, tagFr, tagHu, tagDe, tagUk, tagRu, tagBg, tagPt, tagIt, tagEs)
    fun getTitle(lang: String): String = AppLocalization.get(lang, titleRo, titleEn, titleFr, titleHu, titleDe, titleUk, titleRu, titleBg, titlePt, titleIt, titleEs)
    fun getDescription(lang: String): String? {
        val d = AppLocalization.get(lang, descriptionRo ?: "", descriptionEn ?: "", descriptionFr ?: "", descriptionHu ?: "", descriptionDe ?: "", descriptionUk ?: "", descriptionRu ?: "", descriptionBg ?: "", descriptionPt ?: "", descriptionIt ?: "", descriptionEs ?: "")
        return if (d.isBlank()) descriptionEn else d
    }
}

object ChangelogRepository {
    val entries = listOf(
        ChangelogEntry(
            version = "v0.6.1",
            dateRo = "07 Noi 2026",
            dateEn = "07 Nov 2026",
            titleRo = "RoBee Fields 0.6.1 (Sincronizare Multilingvă 100% în Toate Meniurile)",
            titleEn = "RoBee Fields 0.6.1 (100% Full Multilingual Sync Across All Menus)",
            titleFr = "RoBee Fields 0.6.1 (Synchronisation Multilingue 100% sur Tous les Menus)",
            titleHu = "RoBee Fields 0.6.1 (100% Teljes Multilingvális Szinkronizáció Minden Menüben)",
            titleDe = "RoBee Fields 0.6.1 (100% Vollständige Mehrsprachige Synchronisierung in Allen Menüs)",
            titleUk = "RoBee Fields 0.6.1 (100% Мультимовна синхронізація в усіх меню)",
            titleRu = "RoBee Fields 0.6.1 (100% Мультиязычная синхронизация во всех меню)",
            titleBg = "RoBee Fields 0.6.1 (100% Мултиезична синхронизация във всички менюта)",
            titlePt = "RoBee Fields 0.6.1 (Sincronização Multilíngue 100% em Todos os Menus)",
            titleIt = "RoBee Fields 0.6.1 (Sincronizzazione Multilingue 100% in Tutti i Menu)",
            titleEs = "RoBee Fields 0.6.1 (Sincronización Multilingüe 100% en Todos los Menús)",
            tagRo = "Nucleu",
            tagEn = "Core",
            tagFr = "Noyau",
            tagHu = "Mag",
            tagDe = "Kern",
            tagUk = "Ядро",
            tagRu = "Ядро",
            tagBg = "Ядро",
            tagPt = "Núcleo",
            tagIt = "Nucleo",
            tagEs = "Núcleo",
            category = ChangelogCategory.CORE,
            descriptionRo = "Versiunea 0.6.1 aduce integrarea multilingvă completă (100%) în toate meniurile aplicației: Meniul Lateral, Harta Principală, Formularul Adaugă Stup/Stupină, Setări, Despre, Meniul Stupilor și Meniul Stupinelor.",
            descriptionEn = "Version 0.6.1 brings 100% complete multilingual integration across all app menus: Navigation Drawer, Main Map, Add Hive/Apiary Form, Settings, About, Hive Action Menu, and Apiary Complex Menu.",
            descriptionFr = "La version 0.6.1 introduit l'intégration multilingue complète à 100 % sur tous les menus de l'application.",
            descriptionHu = "A 0.6.1-es verzió 100%-os teljes multilingvális integrációt vezet be az alkalmazás minden menüjében.",
            descriptionDe = "Version 0.6.1 führt eine 100% vollständige mehrsprachige Integration in allen App-Menüs ein.",
            descriptionUk = "Версія 0.6.1 запроваджує 100% повну мультимовну інтеграцію в усіх меню програми.",
            descriptionRu = "Версия 0.6.1 внедряет 100% полную мультиязычную интеграцию во всех меню приложения.",
            descriptionBg = "Версия 0.6.1 въвежда 100% пълна мултиезична интеграция във всички менюта на приложението.",
            descriptionPt = "A versão 0.6.1 introduz integração multilíngue 100% completa em todos os menus do aplicativo.",
            descriptionIt = "La versione 0.6.1 introduce l'integrazione multilingue completa al 100% in tutti i menu dell'app.",
            descriptionEs = "La versión 0.6.1 introduce una integración multilingüe 100% completa en todos los menús de la aplicación.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    titleFr = "Nouveau",
                    titleHu = "Új",
                    titleDe = "Neu",
                    titleUk = "Нове",
                    titleRu = "Новое",
                    titleBg = "Ново",
                    titlePt = "Novo",
                    titleIt = "Nuovo",
                    titleEs = "Nuevo",
                    itemsRo = listOf(
                        "Schimbarea dinamică a limbii afectează acum Meniul Lateral (Harta Principală, Adaugă Stup, Setări, Despre).",
                        "Tratarea bilingvă și multilingvă integrală a Hărții Principale (titluri marcaje stupi/stupine, pin locație live GPS, instrumente de zoom și adăugare).",
                        "Localizarea completă a Meniului Stupilor (Meteo Ventusky, Galerie Foto, Jurnale de Inspecție, Mutare GPS, Detalii, Trimite Hash P2P).",
                        "Localizarea completă a Meniului Stupinelor (Adăugare Stup în Stupină, Mutare GPS, Editează Nume, P2P SMS Share)."
                    ),
                    itemsEn = listOf(
                        "Dynamic language switching now affects the Navigation Drawer (Main Map, Add Hive, Settings, About).",
                        "Comprehensive multilingual handling for the Main Map (hive/apiary marker titles, live GPS location pin, zoom & add controls).",
                        "Full localization of the Hive Action Menu (Ventusky Weather, Photo Gallery, Inspection Logs, Relocate GPS, Details, Share Hash P2P).",
                        "Full localization of the Apiary Complex Menu (Add Hive to Apiary, Relocate GPS, Edit Name, P2P SMS Share)."
                    ),
                    itemsFr = listOf(
                        "Le changement dynamique de langue affecte le tiroir de navigation et tous les menus.",
                        "Traduction complète de la carte principale, du menu des ruches et du menu des ruchers en 11 langues."
                    ),
                    itemsHu = listOf(
                        "A dinamikus nyelvváltás mostantól érinti a navigációs menüt, a főtérképet és az összes menüt.",
                        "A kaptár és méhészet menük teljes lokalizációja mind a 11 nyelven."
                    ),
                    itemsDe = listOf(
                        "Dynamischer Sprachwechsel wirkt sich nun auf die Hauptnavigation, Karten und alle Menüs aus.",
                        "Vollständige Lokalisierung des Bienenstock- und Imkerei-Menüs in 11 Sprachen."
                    ),
                    itemsUk = listOf(
                        "Динамічне перемикання мови тепер впливає на бічне меню, головну карту та всі діалоги.",
                        "Повна локалізація меню вуликів та пасік на всі 11 мов."
                    ),
                    itemsRu = listOf(
                        "Динамическое переключение языка теперь влияет на боковое меню, главную карту и все диалоги.",
                        "Полная локализация меню ульев и пасек на все 11 языков."
                    ),
                    itemsBg = listOf(
                        "Динамичната промяна на езика засяга страничното меню, основната карта и всички диалози.",
                        "Пълна локализация на менютата на кошерите и пчелините на 11 езика."
                    ),
                    itemsPt = listOf(
                        "Mudar o idioma agora afeta o menu lateral, mapa principal e todos os menus.",
                        "Localização completa dos menus de colmeia e apiário nos 11 idiomas."
                    ),
                    itemsIt = listOf(
                        "Il cambio lingua influisce ora sul menu laterale, sulla mappa principale e su tutti i menu.",
                        "Localizzazione completa dei menu arnia e apiario in 11 lingue."
                    ),
                    itemsEs = listOf(
                        "El cambio de idioma ahora afecta al menú lateral, mapa principal y todos los menús.",
                        "Localización completa de los menús de colmena y colmenar en los 11 idiomas."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.6.0",
            dateRo = "06 Noi 2026",
            dateEn = "06 Nov 2026",
            titleRo = "RoBee Fields 0.6.0 (Ediție Globală: 11 Limbi & Optimizare UI Stupine)",
            titleEn = "RoBee Fields 0.6.0 (Global Edition: 11 Languages & Apiary UI Optimization)",
            titleFr = "RoBee Fields 0.6.0 (Édition Globale : 11 Langues & UI Stupines)",
            titleHu = "RoBee Fields 0.6.0 (Globális Kiadás: 11 Nyelv & UI)",
            titleDe = "RoBee Fields 0.6.0 (Globale Edition: 11 Sprachen & UI)",
            titleUk = "RoBee Fields 0.6.0 (Глобальне видання: 11 мов та UI)",
            titleRu = "RoBee Fields 0.6.0 (Глобальное издание: 11 языков и UI)",
            titleBg = "RoBee Fields 0.6.0 (Глобално издание: 11 езика и UI)",
            titlePt = "RoBee Fields 0.6.0 (Edição Global: 11 Idiomas & UI)",
            titleIt = "RoBee Fields 0.6.0 (Edizione Globale: 11 Lingue & UI)",
            titleEs = "RoBee Fields 0.6.0 (Edición Global: 11 Idiomas & UI)",
            tagRo = "Teren",
            tagEn = "Field",
            tagFr = "Terrain",
            tagHu = "Terep",
            tagDe = "Feld",
            tagUk = "Поле",
            tagRu = "Поле",
            tagBg = "Поле",
            tagPt = "Campo",
            tagIt = "Campo",
            tagEs = "Campo",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Versiunea 0.6.0 extinde suportul lingvistic la 11 limbi internaționale cu steaguri oficiale și optimizează interfața de utilizare în meniul stupinelor.",
            descriptionEn = "Version 0.6.0 expands language support to 11 international languages with official flags and optimizes the user interface in the apiary menu.",
            descriptionFr = "La version 0.6.0 étend le support linguistique à 11 langues internationales avec des drapeaux officiels et optimise l'interface utilisateur du rucher.",
            descriptionHu = "A 0.6.0-ás verzió 11 nemzetközi nyelvre terjeszti ki a nyelvi támogatást zászlókkal és optimalizálja a méhészet menü felületét.",
            descriptionDe = "Version 0.6.0 erweitert die Sprachunterstützung auf 11 internationale Sprachen mit Flaggen und optimiert das Imkerei-Menü.",
            descriptionUk = "Версія 0.6.0 розширює підтримку до 11 міжнародних мов з прапорцями та оптимізує інтерфейс меню пасіки.",
            descriptionRu = "Версия 0.6.0 расширяет поддержку до 11 международных языков с флагами и оптимизирует интерфейс меню пасеки.",
            descriptionBg = "Версия 0.6.0 разширява поддръжката до 11 международни езика със знамена и оптимизира интерфейса на пчелина.",
            descriptionPt = "A versão 0.6.0 expande o suporte para 11 idiomas com bandeiras e otimiza a interface do apiário.",
            descriptionIt = "La versione 0.6.0 espande il supporto a 11 lingue con bandiere e ottimizza l'interfaccia dell'apiario.",
            descriptionEs = "La versión 0.6.0 amplía el soporte a 11 idiomas con banderas y optimiza la interfaz del colmenar.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    titleFr = "Nouveau",
                    titleHu = "Új",
                    titleDe = "Neu",
                    titleUk = "Нове",
                    titleRu = "Новое",
                    titleBg = "Ново",
                    titlePt = "Novo",
                    titleIt = "Nuovo",
                    titleEs = "Nuevo",
                    itemsRo = listOf(
                        "Suport lingvistic extins la 11 limbi internaționale (Română, Engleză, Franceză, Magiară, Germană, Ucraineană, Rusă, Bulgară, Portugheză, Italiană și Spaniolă) cu steaguri dedicate în setări.",
                        "Localizarea completă a tuturor setărilor, stărilor, raselor de albine, tendințelor de roit și ferestrelor de dialog în cele 11 limbi.",
                        "Refacerea completă a layout-ului în meniul stupinelor cu butoane organizate pe două rânduri pentru o utilizare fără cusur pe teren."
                    ),
                    itemsEn = listOf(
                        "Language support expanded to 11 international languages (Romanian, English, French, Hungarian, German, Ukrainian, Russian, Bulgarian, Portuguese, Italian, and Spanish) with dedicated flags in settings.",
                        "Full localization of all settings, hive statuses, bee races, swarming tendencies, and dialog windows across all 11 languages.",
                        "Complete redesign of the apiary menu layout with two-row structured action buttons for seamless field usability."
                    ),
                    itemsFr = listOf(
                        "Prise en charge de 11 langues internationales (Roumain, Anglais, Français, Hongrois, Allemand, Ukrainien, Russe, Bulgare, Portugais, Italien, Espagnol) avec drapeaux.",
                        "Traduction complète de tous les paramètres, états des ruches, races d'abeilles et fenêtres de dialogue.",
                        "Refonte complète de la disposition du menu des ruchers avec des boutons sur deux rangées."
                    ),
                    itemsHu = listOf(
                        "Nyelvi támogatás kiterjesztése 11 nemzetközi nyelvre zászlókkal a beállításokban.",
                        "Minden beállítás, kaptárállomány, méhfajta, rajzási hajlam és párbeszédablak teljes lokalizációja.",
                        "A kaptár menü elrendezésének teljes újratervezése kétsoros gombokkal."
                    ),
                    itemsDe = listOf(
                        "Sprachunterstützung auf 11 internationale Sprachen erweitert mit Flaggen.",
                        "Vollständige Lokalisierung aller Einstellungen, Bienenrassen, Schwarmtendenzen und Dialoge.",
                        "Komplette Neugestaltung des Imkerei-Menüs mit zweireihigen Aktionsschaltflächen."
                    ),
                    itemsUk = listOf(
                        "Підтримка 11 міжнародних мов з прапорцями в налаштуваннях.",
                        "Повна локалізація всіх налаштувань, станів вуликів, порід бджіл та діалогових вікон.",
                        "Повна переробка меню пасіки з дворядними кнопками."
                    ),
                    itemsRu = listOf(
                        "Поддержка 11 международных языков с флагами в настройках.",
                        "Полная локализация всех настроек, статусов ульев, пород пчел и диалоговых окон.",
                        "Полная переработка меню пасеки с двухрядными кнопками."
                    ),
                    itemsBg = listOf(
                        "Поддръжка на 11 международни езика със знамена в настройките.",
                        "Пълна локализация на всички настройки, състояния на кошерите, породи пчели и диалогови прозорци.",
                        "Пълно преработване на менюто на пчелина с двуредови бутони."
                    ),
                    itemsPt = listOf(
                        "Suporte a 11 idiomas internacionais com bandeiras nas configurações.",
                        "Localização completa de todas as configurações, status, raças e diálogos.",
                        "Redesenho completo do menu do apiário com botões em duas linhas."
                    ),
                    itemsIt = listOf(
                        "Supporto a 11 lingue internazionali con bandiere nelle impostazioni.",
                        "Localizzazione completa di tutte le impostazioni, stati, razze e finestre di dialogo.",
                        "Riprogettazione completa del menu dell'apiario con pulsanti su due file."
                    ),
                    itemsEs = listOf(
                        "Soporte para 11 idiomas internacionales con banderas en los ajustes.",
                        "Localización completa de todos los ajustes, estados, razas y diálogos.",
                        "Rediseño completo del menú del colmenar con botones en dos filas."
                    )
                ),
                ChangelogSection(
                    titleRo = "Îmbunătățit",
                    titleEn = "Improved",
                    titleFr = "Amélioré",
                    titleHu = "Fejlesztve",
                    titleDe = "Verbessert",
                    titleUk = "Покращено",
                    titleRu = "Улучшено",
                    titleBg = "Подобрено",
                    titlePt = "Melhorado",
                    titleIt = "Migliorato",
                    titleEs = "Mejorado",
                    itemsRo = listOf(
                        "Optimizarea performanței aplicației și fluidizarea comutării dinamice a limbii în timp real."
                    ),
                    itemsEn = listOf(
                        "Optimized application performance and streamlined real-time dynamic language switching."
                    ),
                    itemsFr = listOf(
                        "Optimisation des performances de l'application et fluidité du changement de langue en temps réel."
                    ),
                    itemsHu = listOf(
                        "Az alkalmazás teljesítményének optimalizálása és a valós idejű dinamikus nyelvváltás."
                    ),
                    itemsDe = listOf(
                        "Optimierung der App-Leistung und reibungsloser Sprachwechsel in Echtzeit."
                    ),
                    itemsUk = listOf(
                        "Оптимізація продуктивності програми та плавне перемикання мови в реальному часі."
                    ),
                    itemsRu = listOf(
                        "Оптимизация производительности приложения и плавное переключение языка в реальном времени."
                    ),
                    itemsBg = listOf(
                        "Оптимизиране на производителността на приложението и плавно превключване на езика в реално време."
                    ),
                    itemsPt = listOf(
                        "Otimização do desempenho do aplicativo e troca de idioma em tempo real."
                    ),
                    itemsIt = listOf(
                        "Ottimizzazione delle prestazioni dell'app e cambio lingua in tempo reale."
                    ),
                    itemsEs = listOf(
                        "Optimización del rendimiento de la aplicación y cambio de idioma en tiempo real."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.5.1",
            dateRo = "05 Noi 2026",
            dateEn = "05 Nov 2026",
            titleRo = "RoBee Fields 0.5.1 (Integrare Meteo Ventusky & Remediere Meniu Stupine)",
            titleEn = "RoBee Fields 0.5.1 (Ventusky Weather Integration & Apiary Menu Fix)",
            tagRo = "Teren",
            tagEn = "Field",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Versiunea 0.5.1 adaugă prognoza meteo Ventusky în meniul fiecărui stup individual și remediază afișarea textului și layout-ul în meniul stupinelor.",
            descriptionEn = "Version 0.5.1 adds Ventusky live weather forecast to individual hive menus and fixes text formatting and layout issues in the apiary menu.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Integrarea prognozei meteo live Ventusky pentru fiecare stup individual și stupină, cu navigare directă pe hartă la nivel de zoom 11.",
                        "Refacerea completă a meniului de detaliu al stupinei cu structură organizată pe rânduri duble pentru butoane și remedierea suprapunerilor de text."
                    ),
                    itemsEn = listOf(
                        "Live Ventusky weather forecast integration for individual hives and apiaries with direct map navigation at zoom level 11.",
                        "Complete redesign of the apiary detail menu with structured two-row action buttons and fix for text overlapping issues."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.5.0",
            dateRo = "04 Noi 2026",
            dateEn = "04 Nov 2026",
            titleRo = "RoBee Fields 0.5.0 (Ediție Majoră: Inspecții, Rasă, Roit & Export CSV)",
            titleEn = "RoBee Fields 0.5.0 (Major Edition: Advanced Inspection, Bee Race, Swarming & CSV)",
            tagRo = "Teren",
            tagEn = "Field",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Lansarea versiunii 0.5.0 aduce un set masiv de funcționalități avansate: jurnale de inspecție cu 5 secțiuni, selecția rasei de albine, tendințe de roit, mutare avansată pe hartă/GPS, export/import CSV și suport bilingv complet.",
            descriptionEn = "Version 0.5.0 brings a massive set of advanced features: inspection logs with 5 sections, bee race selection, swarming tendencies, advanced map/GPS relocation, CSV export/import, and full bilingual support.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Jurnale de inspecție pe teren cu 5 secțiuni intuitive (Starea Reginei, Puterea Familiei cu controloare +/-, Intervenții & Tratamente, Comportament și Notițe rapide).",
                        "Selecția rasei de albine (Carpatină, Carnica, Buckfast, Italiană, Caucaziană, Neagră Europeană).",
                        "Selector pentru tendințele de roit (Fără tendințe, Tendințe slabe, Foarte puternice, Schimbare liniștită).",
                        "Funcționalitate de mutare a stupilor și stupinelor prin 3 metode: Click pe hartă, Locația curentă GPS sau Coordonate manuale.",
                        "Export și Import complet de date în format CSV, permițând alegerea locației de salvare și a fișierului dorit."
                    ),
                    itemsEn = listOf(
                        "Field inspection logs with 5 intuitive sections (Queen Status, Colony Strength with +/- counters, Interventions & Treatments, Temperament, Quick Notes).",
                        "Bee race / type selection (Carpathian, Carnica, Buckfast, Italian, Caucasian, European Dark).",
                        "Swarming tendency selector (None, Low tendencies, Strong tendencies, Quiet supersedure).",
                        "Hive and apiary relocation via 3 methods: Map tap, Current GPS location, or Manual coordinates.",
                        "Complete CSV Export & Import functionality allowing custom folder/location selection."
                    )
                )
            )
        ),
        ChangelogEntry(
            version = "v0.4.0",
            dateRo = "19 Oct 2026",
            dateEn = "19 Oct 2026",
            titleRo = "RoBee Fields 0.4.0 (Ediție Inspecție & Teren)",
            titleEn = "RoBee Fields 0.4.0 (Field & Inspection Edition)",
            tagRo = "Teren",
            tagEn = "Field",
            category = ChangelogCategory.FIELD,
            descriptionRo = "Sistem complet de inspecție pe teren cu 5 secțiuni dedicate, jurnale persistente și export/import CSV cu alegerea locației.",
            descriptionEn = "Complete field inspection system with 5 dedicated sections, persistent logs, and CSV export/import with location selection.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Jurnale de inspecție pe teren cu 5 secțiuni intuitive.",
                        "Istoric detaliat al inspecțiilor salvat persistent în baza de date SQLite."
                    ),
                    itemsEn = listOf(
                        "Field inspection logs with 5 intuitive sections.",
                        "Detailed inspection history persistently saved in SQLite database."
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
            descriptionRo = "Optimizări majore pentru navigarea offline pe teren cu Mapsforge și osmdroid.",
            descriptionEn = "Major optimizations for offline field navigation with Mapsforge and osmdroid.",
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
            descriptionRo = "Protocoale de comunicare directă între apicultori în zone fără acoperire GSM.",
            descriptionEn = "Direct communication protocols between beekeepers in areas without cellular coverage.",
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
                )
            )
        ),
        ChangelogEntry(
            version = "v0.1.0",
            dateRo = "10 Oct 2026",
            dateEn = "10 Oct 2026",
            titleRo = "Nucleul Aplicației",
            titleEn = "App Core",
            tagRo = "Nucleu",
            tagEn = "Core",
            category = ChangelogCategory.CORE,
            descriptionRo = "Prima versiune funcțională a aplicației pentru gestiunea stupinelor offline.",
            descriptionEn = "First functional version of the application for managing apiaries offline.",
            sections = listOf(
                ChangelogSection(
                    titleRo = "Nou",
                    titleEn = "New",
                    itemsRo = listOf(
                        "Bază de date locală SQLite (HiveDbHelper).",
                        "Vizualizare hărți de bază și adăugare pin-uri stupi."
                    ),
                    itemsEn = listOf(
                        "Local SQLite database (HiveDbHelper).",
                        "Basic map rendering and hive pin placement."
                    )
                )
            )
        )
    )
}
