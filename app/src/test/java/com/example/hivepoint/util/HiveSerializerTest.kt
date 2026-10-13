package com.example.hivepoint.util

import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class HiveSerializerTest {

    @Test
    fun testSerializationAndDeserialization() {
        val original = Beehive(
            id = "hive-123",
            apiaryId = "apiary-456",
            hiveNumber = "Stup #7",
            latitude = 44.4322,
            longitude = 26.1062,
            status = HiveStatus.STRONG,
            notes = "Familie foarte activă, miere în plus",
            photoUris = emptyList(),
            updatedAt = 1700000000000L
        )

        val serialized = HiveSerializer.serialize(original)
        assertNotNull(serialized)

        val deserialized = HiveSerializer.deserialize(serialized)
        assertNotNull(deserialized)
        assertEquals(original, deserialized)
    }

    @Test
    fun testDeserializeInvalidPrefix() {
        val invalid = "INVALID|hive-123|apiary-456|Stup #7"
        assertNull(HiveSerializer.deserialize(invalid))
    }
}
