package com.example.hivepoint.model

import java.util.UUID

enum class HiveStatus {
    STRONG,
    MEDIUM,
    WEAK,
    QUEENLESS,
    DEAD,
    UNKNOWN;

    fun toDisplayName(language: String = "Română"): String {
        return when (language) {
            "English" -> when (this) {
                STRONG -> "Strong"; MEDIUM -> "Medium"; WEAK -> "Weak"; QUEENLESS -> "Queenless"; DEAD -> "Dead"; UNKNOWN -> "Unknown"
            }
            "Français" -> when (this) {
                STRONG -> "Fort"; MEDIUM -> "Moyen"; WEAK -> "Faible"; QUEENLESS -> "Orpheline"; DEAD -> "Mort"; UNKNOWN -> "Inconnu"
            }
            "Magyar" -> when (this) {
                STRONG -> "Erős"; MEDIUM -> "Közepes"; WEAK -> "Gyenge"; QUEENLESS -> "Anyátlan"; DEAD -> "Elhullott"; UNKNOWN -> "Ismeretlen"
            }
            "Deutsch" -> when (this) {
                STRONG -> "Stark"; MEDIUM -> "Mittel"; WEAK -> "Schwach"; QUEENLESS -> "Weisellos"; DEAD -> "Tot"; UNKNOWN -> "Unbekannt"
            }
            "Ukrainian" -> when (this) {
                STRONG -> "Сильний"; MEDIUM -> "Середній"; WEAK -> "Слабкий"; QUEENLESS -> "Без матки"; DEAD -> "Загиблий"; UNKNOWN -> "Невідомо"
            }
            "Russian" -> when (this) {
                STRONG -> "Сильный"; MEDIUM -> "Средний"; WEAK -> "Слабый"; QUEENLESS -> "Без матки"; DEAD -> "Мертвый"; UNKNOWN -> "Неизвестно"
            }
            "Bulgarian" -> when (this) {
                STRONG -> "Силен"; MEDIUM -> "Среден"; WEAK -> "Слаб"; QUEENLESS -> "Без майка"; DEAD -> "Мъртъв"; UNKNOWN -> "Неизвестен"
            }
            "Portuguese" -> when (this) {
                STRONG -> "Forte"; MEDIUM -> "Médio"; WEAK -> "Fraco"; QUEENLESS -> "Órfã"; DEAD -> "Morto"; UNKNOWN -> "Desconhecido"
            }
            "Italian" -> when (this) {
                STRONG -> "Forte"; MEDIUM -> "Medio"; WEAK -> "Debole"; QUEENLESS -> "Orfana"; DEAD -> "Morto"; UNKNOWN -> "Sconosciuto"
            }
            "Spanish" -> when (this) {
                STRONG -> "Fuerte"; MEDIUM -> "Medio"; WEAK -> "Débil"; QUEENLESS -> "Huérfana"; DEAD -> "Muerto"; UNKNOWN -> "Desconocido"
            }
            else -> when (this) {
                STRONG -> "Puternic"; MEDIUM -> "Mediu"; WEAK -> "Slab"; QUEENLESS -> "Fără Matcă"; DEAD -> "Mort"; UNKNOWN -> "Necunoscut"
            }
        }
    }
}

enum class BeeRace(val scientificName: String) {
    CARPATICA("Apis mellifera carpatica"),
    CARNICA("Apis mellifera carnica"),
    BUCKFAST("Hybrid Buckfast"),
    LIGUSTICA("Apis mellifera ligustica"),
    CAUCASICA("Apis mellifera caucasica"),
    MELLIFERA("Apis mellifera mellifera"),
    UNKNOWN("Unknown");

    fun toDisplayName(language: String = "Română"): String {
        return when (language) {
            "English" -> when (this) {
                CARPATICA -> "Carpathian"; CARNICA -> "Carnica"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italian"; CAUCASICA -> "Caucasian"; MELLIFERA -> "European Dark"; UNKNOWN -> "Unspecified"
            }
            "Français" -> when (this) {
                CARPATICA -> "Carpatique"; CARNICA -> "Carnica"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italienne"; CAUCASICA -> "Caucasienne"; MELLIFERA -> "Noire européenne"; UNKNOWN -> "Non spécifié"
            }
            "Magyar" -> when (this) {
                CARPATICA -> "Kárpáti"; CARNICA -> "Karni"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Olasz"; CAUCASICA -> "Kaukázusi"; MELLIFERA -> "Európai sötét"; UNKNOWN -> "Ismeretlen"
            }
            "Deutsch" -> when (this) {
                CARPATICA -> "Karpaten"; CARNICA -> "Carnica"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italienische"; CAUCASICA -> "Kaukasische"; MELLIFERA -> "Europäische dunkle"; UNKNOWN -> "Unbekannt"
            }
            "Ukrainian" -> when (this) {
                CARPATICA -> "Карпатська"; CARNICA -> "Країнка"; BUCKFAST -> "Бакфаст"; LIGUSTICA -> "Італійська"; CAUCASICA -> "Кавказька"; MELLIFERA -> "Європейська темна"; UNKNOWN -> "Не вказано"
            }
            "Russian" -> when (this) {
                CARPATICA -> "Карпатская"; CARNICA -> "Краинская"; BUCKFAST -> "Бакфаст"; LIGUSTICA -> "Итальянская"; CAUCASICA -> "Кавказская"; MELLIFERA -> "Среднерусская"; UNKNOWN -> "Не указано"
            }
            "Bulgarian" -> when (this) {
                CARPATICA -> "Карпатска"; CARNICA -> "Карника"; BUCKFAST -> "Бакфаст"; LIGUSTICA -> "Италианска"; CAUCASICA -> "Кавказка"; MELLIFERA -> "Европейска тъмна"; UNKNOWN -> "Неуказан"
            }
            "Portuguese" -> when (this) {
                CARPATICA -> "Cárpatos"; CARNICA -> "Carniola"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italiana"; CAUCASICA -> "Caucasiana"; MELLIFERA -> "Preta Europeia"; UNKNOWN -> "Não especificado"
            }
            "Italian" -> when (this) {
                CARPATICA -> "Carpatica"; CARNICA -> "Carnica"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italiana"; CAUCASICA -> "Caucasica"; MELLIFERA -> "Nera Europea"; UNKNOWN -> "Non specificato"
            }
            "Spanish" -> when (this) {
                CARPATICA -> "Cárpata"; CARNICA -> "Carniola"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italiana"; CAUCASICA -> "Caucásica"; MELLIFERA -> "Negra Europea"; UNKNOWN -> "No especificado"
            }
            else -> when (this) {
                CARPATICA -> "Carpatină"; CARNICA -> "Carnica"; BUCKFAST -> "Buckfast"; LIGUSTICA -> "Italiană"; CAUCASICA -> "Caucaziană"; MELLIFERA -> "Neagră Europeană"; UNKNOWN -> "Nespecificat"
            }
        }
    }
}

enum class SwarmingTendency {
    NONE,
    WEAK,
    STRONG,
    SUPERSEDURE,
    UNKNOWN;

    fun toDisplayName(language: String = "Română"): String {
        return when (language) {
            "English" -> when (this) {
                NONE -> "None"; WEAK -> "Low tendencies"; STRONG -> "Strong tendencies"; SUPERSEDURE -> "Quiet supersedure"; UNKNOWN -> "Unspecified"
            }
            "Français" -> when (this) {
                NONE -> "Aucune"; WEAK -> "Faibles"; STRONG -> "Fortes"; SUPERSEDURE -> "Remplacement calme"; UNKNOWN -> "Non spécifié"
            }
            "Magyar" -> when (this) {
                NONE -> "Nincs"; WEAK -> "Gyenge hajlam"; STRONG -> "Erős hajlam"; SUPERSEDURE -> "Csendes anyaváltás"; UNKNOWN -> "Ismeretlen"
            }
            "Deutsch" -> when (this) {
                NONE -> "Keine"; WEAK -> "Schwach"; STRONG -> "Stark"; SUPERSEDURE -> "Stille Umweiselung"; UNKNOWN -> "Unbekannt"
            }
            "Ukrainian" -> when (this) {
                NONE -> "Немає"; WEAK -> "Слабкі"; STRONG -> "Сильні"; SUPERSEDURE -> "Тиха заміна"; UNKNOWN -> "Не вказано"
            }
            "Russian" -> when (this) {
                NONE -> "Нет"; WEAK -> "Слабые"; STRONG -> "Сильные"; SUPERSEDURE -> "Тихая смена"; UNKNOWN -> "Не указано"
            }
            "Bulgarian" -> when (this) {
                NONE -> "Няма"; WEAK -> "Слаби"; STRONG -> "Силни"; SUPERSEDURE -> "Тиха смяна"; UNKNOWN -> "Неуказан"
            }
            "Portuguese" -> when (this) {
                NONE -> "Sem tendências"; WEAK -> "Tendências fracas"; STRONG -> "Muito fortes"; SUPERSEDURE -> "Substituição silenciosa"; UNKNOWN -> "Não especificado"
            }
            "Italian" -> when (this) {
                NONE -> "Nessuna"; WEAK -> "Tendenze deboli"; STRONG -> "Molto forti"; SUPERSEDURE -> "Sostituzione silenziosa"; UNKNOWN -> "Non specificato"
            }
            "Spanish" -> when (this) {
                NONE -> "Sin tendencias"; WEAK -> "Tendencias débiles"; STRONG -> "Muy fuertes"; SUPERSEDURE -> "Sustitución silenciosa"; UNKNOWN -> "No especificado"
            }
            else -> when (this) {
                NONE -> "Fără tendințe"; WEAK -> "Tendințe slabe"; STRONG -> "Foarte puternice"; SUPERSEDURE -> "Schimbare liniștită"; UNKNOWN -> "Nespecificat"
            }
        }
    }
}

enum class QueenState {
    SEEN,
    EGGS,
    MISSING,
    QUEEN_CELLS,
    UNKNOWN;

    fun toDisplayName(language: String = "Română"): String {
        return when (language) {
            "English" -> when (this) {
                SEEN -> "Queen Seen"; EGGS -> "Fresh Eggs Seen"; MISSING -> "Missing / Queenless"; QUEEN_CELLS -> "Queen Cells Present"; UNKNOWN -> "Unspecified"
            }
            "Français" -> when (this) {
                SEEN -> "Vue"; EGGS -> "Œufs frais"; MISSING -> "Absente"; QUEEN_CELLS -> "Cellules royales"; UNKNOWN -> "Non spécifié"
            }
            "Magyar" -> when (this) {
                SEEN -> "Látva"; EGGS -> "Friss peték"; MISSING -> "Hiányzik"; QUEEN_CELLS -> "Anyabölcsők"; UNKNOWN -> "Ismeretlen"
            }
            "Deutsch" -> when (this) {
                SEEN -> "Gesehen"; EGGS -> "Frische Eier"; MISSING -> "Fehlt"; QUEEN_CELLS -> "Weisenzellen"; UNKNOWN -> "Unbekannt"
            }
            "Ukrainian" -> when (this) {
                SEEN -> "Побачено"; EGGS -> "Свіжі яйця"; MISSING -> "Відсутня"; QUEEN_CELLS -> "Маточники"; UNKNOWN -> "Не вказано"
            }
            "Russian" -> when (this) {
                SEEN -> "Увидена"; EGGS -> "Свежие яйца"; MISSING -> "Отсутствует"; QUEEN_CELLS -> "Маточники"; UNKNOWN -> "Не указано"
            }
            "Bulgarian" -> when (this) {
                SEEN -> "Видяна"; EGGS -> "Пресни яйца"; MISSING -> "Липсва"; QUEEN_CELLS -> "Маточници"; UNKNOWN -> "Неуказан"
            }
            "Portuguese" -> when (this) {
                SEEN -> "Vista"; EGGS -> "Ovos frescos"; MISSING -> "Desaparecida"; QUEEN_CELLS -> "Realheiras presentes"; UNKNOWN -> "Não especificado"
            }
            "Italian" -> when (this) {
                SEEN -> "Vista"; EGGS -> "Uova fresche"; MISSING -> "Mancante"; QUEEN_CELLS -> "Celle reali presenti"; UNKNOWN -> "Non specificato"
            }
            "Spanish" -> when (this) {
                SEEN -> "Vista"; EGGS -> "Huevos frescos"; MISSING -> "Desaparecida"; QUEEN_CELLS -> "Celdas reales presentes"; UNKNOWN -> "No especificado"
            }
            else -> when (this) {
                SEEN -> "Văzută"; EGGS -> "Semne (ouă proaspete)"; MISSING -> "Lipsă / Orfan"; QUEEN_CELLS -> "Botci prezente"; UNKNOWN -> "Nespecificat"
            }
        }
    }
}

enum class Temperament {
    CALM,
    NORMAL,
    AGGRESSIVE,
    UNKNOWN;

    fun toDisplayName(language: String = "Română"): String {
        return when (language) {
            "English" -> when (this) {
                CALM -> "Calm"; NORMAL -> "Normal"; AGGRESSIVE -> "Aggressive"; UNKNOWN -> "Unknown"
            }
            "Français" -> when (this) {
                CALM -> "Calme"; NORMAL -> "Normale"; AGGRESSIVE -> "Agressive"; UNKNOWN -> "Inconnu"
            }
            "Magyar" -> when (this) {
                CALM -> "Nyugodt"; NORMAL -> "Normális"; AGGRESSIVE -> "Agresszív"; UNKNOWN -> "Ismeretlen"
            }
            "Deutsch" -> when (this) {
                CALM -> "Ruhig"; NORMAL -> "Normal"; AGGRESSIVE -> "Aggressiv"; UNKNOWN -> "Unbekannt"
            }
            "Ukrainian" -> when (this) {
                CALM -> "Спокійна"; NORMAL -> "Нормальна"; AGGRESSIVE -> "Агресивна"; UNKNOWN -> "Невідомо"
            }
            "Russian" -> when (this) {
                CALM -> "Спокойная"; NORMAL -> "Нормальная"; AGGRESSIVE -> "Агрессивная"; UNKNOWN -> "Неизвестно"
            }
            "Bulgarian" -> when (this) {
                CALM -> "Спокойна"; NORMAL -> "Нормална"; AGGRESSIVE -> "Агресивна"; UNKNOWN -> "Неизвестен"
            }
            "Portuguese" -> when (this) {
                CALM -> "Calma"; NORMAL -> "Normal"; AGGRESSIVE -> "Agressiva"; UNKNOWN -> "Desconhecido"
            }
            "Italian" -> when (this) {
                CALM -> "Calma"; NORMAL -> "Normale"; AGGRESSIVE -> "Aggressiva"; UNKNOWN -> "Sconosciuto"
            }
            "Spanish" -> when (this) {
                CALM -> "Calma"; NORMAL -> "Normal"; AGGRESSIVE -> "Agresiva"; UNKNOWN -> "Desconocido"
            }
            else -> when (this) {
                CALM -> "Calmă"; NORMAL -> "Normală"; AGGRESSIVE -> "Agresivă"; UNKNOWN -> "Necunoscut"
            }
        }
    }
}

data class Apiary(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

data class Beehive(
    val id: String = UUID.randomUUID().toString(),
    val apiaryId: String,
    val hiveNumber: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: HiveStatus = HiveStatus.STRONG,
    val beeRace: BeeRace = BeeRace.CARPATICA,
    val swarmingTendency: SwarmingTendency = SwarmingTendency.NONE,
    val notes: String = "",
    val photoUris: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val photoUri: String?
        get() = photoUris.firstOrNull()
}

data class InspectionLog(
    val id: String = UUID.randomUUID().toString(),
    val hiveId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val queenState: QueenState = QueenState.UNKNOWN,
    val broodFrames: Int = 5,
    val honeyFrames: Int = 7,
    val addedSuper: Boolean = false,
    val varroaTreatment: Boolean = false,
    val fedSyrup: Boolean = false,
    val fedPatty: Boolean = false,
    val changedBottomBoard: Boolean = false,
    val temperament: Temperament = Temperament.CALM,
    val notes: String = ""
)
