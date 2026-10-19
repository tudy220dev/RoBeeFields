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
        if (language == "English") return this.name
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

enum class QueenState(val displayName: String) {
    SEEN("Văzută"),
    EGGS("Semne (ouă proaspete)"),
    MISSING("Lipsă / Orfan"),
    QUEEN_CELLS("Botci prezente"),
    UNKNOWN("Nespecificat")
}

enum class Temperament(val displayName: String) {
    CALM("Calmă"),
    NORMAL("Normală"),
    AGGRESSIVE("Agresivă"),
    UNKNOWN("Necunoscut")
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
    val notes: String = "",
    val photoUris: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // Backwards compatibility property getter
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
