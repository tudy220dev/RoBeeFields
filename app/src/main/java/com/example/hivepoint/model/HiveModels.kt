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
        if (language == "English") {
            return when (this) {
                STRONG -> "Strong"
                MEDIUM -> "Medium"
                WEAK -> "Weak"
                QUEENLESS -> "Queenless"
                DEAD -> "Dead"
                UNKNOWN -> "Unknown"
            }
        }
        return when (this) {
            STRONG -> "Puternic"
            MEDIUM -> "Mediu"
            WEAK -> "Slab"
            QUEENLESS -> "Fără Matcă"
            DEAD -> "Mort"
            UNKNOWN -> "Necunoscut"
        }
    }
}

enum class BeeRace(val displayNameRo: String, val displayNameEn: String, val scientificName: String) {
    CARPATICA("Carpatină", "Carpathian", "Apis mellifera carpatica"),
    CARNICA("Carnica", "Carnica", "Apis mellifera carnica"),
    BUCKFAST("Buckfast", "Buckfast", "Hybrid Buckfast"),
    LIGUSTICA("Italiană", "Italian", "Apis mellifera ligustica"),
    CAUCASICA("Caucaziană", "Caucasian", "Apis mellifera caucasica"),
    MELLIFERA("Neagră Europeană", "European Dark", "Apis mellifera mellifera"),
    UNKNOWN("Nespecificat", "Unspecified", "Unknown");

    fun toDisplayName(language: String = "Română"): String {
        return if (language == "English") displayNameEn else displayNameRo
    }
}

enum class SwarmingTendency(val displayNameRo: String, val displayNameEn: String) {
    NONE("Fără tendințe", "None"),
    WEAK("Tendințe slabe", "Low tendencies"),
    STRONG("Foarte puternice", "Strong tendencies"),
    SUPERSEDURE("Schimbare liniștită", "Quiet supersedure"),
    UNKNOWN("Nespecificat", "Unspecified");

    fun toDisplayName(language: String = "Română"): String {
        return if (language == "English") displayNameEn else displayNameRo
    }
}

enum class QueenState {
    SEEN,
    EGGS,
    MISSING,
    QUEEN_CELLS,
    UNKNOWN;

    fun toDisplayName(language: String = "Română"): String {
        val isEn = language == "English"
        return when (this) {
            SEEN -> if (isEn) "Queen Seen" else "Văzută"
            EGGS -> if (isEn) "Fresh Eggs Seen" else "Semne (ouă proaspete)"
            MISSING -> if (isEn) "Missing / Queenless" else "Lipsă / Orfan"
            QUEEN_CELLS -> if (isEn) "Queen Cells Present" else "Botci prezente"
            UNKNOWN -> if (isEn) "Unspecified" else "Nespecificat"
        }
    }
}

enum class Temperament {
    CALM,
    NORMAL,
    AGGRESSIVE,
    UNKNOWN;

    fun toDisplayName(language: String = "Română"): String {
        val isEn = language == "English"
        return when (this) {
            CALM -> if (isEn) "Calm" else "Calmă"
            NORMAL -> if (isEn) "Normal" else "Normală"
            AGGRESSIVE -> if (isEn) "Aggressive" else "Agresivă"
            UNKNOWN -> if (isEn) "Unknown" else "Necunoscut"
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
