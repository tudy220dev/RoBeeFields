package com.example.hivepoint.util

import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus

object HiveSerializer {

    private const val PREFIX = "HP1"
    private const val APIARY_PREFIX = "AP1"
    private const val DELIMITER = "|"

    data class ApiaryP2PPayload(
        val apiaryMarker: Beehive,
        val hives: List<Beehive>
    )

    fun serialize(hive: Beehive): String {
        val lat = hive.latitude?.toString() ?: ""
        val lon = hive.longitude?.toString() ?: ""
        val safeNotes = hive.notes.replace(DELIMITER, " ").replace("\n", " ").trim()
        val safeNumber = hive.hiveNumber.replace(DELIMITER, " ").trim()
        val photosJoined = hive.photoUris.joinToString(";")

        return listOf(
            PREFIX,
            hive.id,
            hive.apiaryId,
            safeNumber,
            lat,
            lon,
            hive.status.name,
            hive.updatedAt.toString(),
            safeNotes,
            photosJoined
        ).joinToString(DELIMITER)
    }

    fun serializeApiary(apiaryName: String, lat: Double, lon: Double, hives: List<Beehive>): String {
        val sb = StringBuilder()
        sb.append(APIARY_PREFIX).append(DELIMITER)
            .append(apiaryName.replace(DELIMITER, " ").replace("\n", " "))
            .append(DELIMITER).append(lat).append(DELIMITER).append(lon)
            .append(DELIMITER).append(hives.size)

        for (hive in hives) {
            sb.append("\n").append(serialize(hive))
        }
        return sb.toString()
    }

    fun deserialize(text: String): Beehive? {
        return try {
            if (!text.startsWith(PREFIX)) return null
            val parts = text.split(DELIMITER)
            if (parts.size < 8) return null

            val id = parts[1]
            val apiaryId = parts[2]
            val hiveNumber = parts[3]
            val latitude = parts[4].toDoubleOrNull()
            val longitude = parts[5].toDoubleOrNull()
            val status = runCatching { HiveStatus.valueOf(parts[6]) }.getOrDefault(HiveStatus.UNKNOWN)
            val updatedAt = parts[7].toLongOrNull() ?: System.currentTimeMillis()
            val notes = if (parts.size > 8) parts[8] else ""
            val photoUris = if (parts.size > 9 && parts[9].isNotBlank()) {
                parts[9].split(";").filter { it.isNotBlank() }
            } else {
                emptyList()
            }

            Beehive(
                id = id,
                apiaryId = apiaryId,
                hiveNumber = hiveNumber,
                latitude = latitude,
                longitude = longitude,
                status = status,
                notes = notes,
                photoUris = photoUris,
                updatedAt = updatedAt
            )
        } catch (e: Exception) {
            null
        }
    }

    fun deserializeApiary(text: String): ApiaryP2PPayload? {
        return try {
            val lines = text.trim().lines()
            if (lines.isEmpty()) return null

            val headerLine = lines[0]
            if (!headerLine.startsWith(APIARY_PREFIX)) return null

            val headerParts = headerLine.split(DELIMITER)
            if (headerParts.size < 4) return null

            val apiaryName = headerParts[1].trim()
            val latitude = headerParts[2].toDoubleOrNull() ?: 45.65737
            val longitude = headerParts[3].toDoubleOrNull() ?: 25.60589

            val apiaryMarker = Beehive(
                apiaryId = "apiary_complex",
                hiveNumber = apiaryName,
                latitude = latitude,
                longitude = longitude,
                status = HiveStatus.STRONG,
                notes = "Importat prin P2P SMS"
            )

            val hivesList = mutableListOf<Beehive>()
            for (i in 1 until lines.size) {
                val line = lines[i].trim()
                if (line.startsWith(PREFIX)) {
                    val deserializedHive = deserialize(line)
                    if (deserializedHive != null) {
                        hivesList.add(
                            deserializedHive.copy(
                                apiaryId = apiaryMarker.id,
                                latitude = latitude,
                                longitude = longitude
                            )
                        )
                    }
                }
            }

            ApiaryP2PPayload(
                apiaryMarker = apiaryMarker,
                hives = hivesList
            )
        } catch (e: Exception) {
            null
        }
    }
}
