package com.example.hivepoint.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HiveDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "robeefields_hives.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_HIVES = "beehives"
        private const val COL_ID = "id"
        private const val COL_APIARY_ID = "apiary_id"
        private const val COL_HIVE_NUMBER = "hive_number"
        private const val COL_LATITUDE = "latitude"
        private const val COL_LONGITUDE = "longitude"
        private const val COL_STATUS = "status"
        private const val COL_UPDATED_AT = "updated_at"
        private const val COL_NOTES = "notes"
        private const val COL_PHOTO_URI = "photo_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_HIVES (
                $COL_ID TEXT PRIMARY KEY,
                $COL_APIARY_ID TEXT,
                $COL_HIVE_NUMBER TEXT,
                $COL_LATITUDE REAL,
                $COL_LONGITUDE REAL,
                $COL_STATUS TEXT,
                $COL_UPDATED_AT INTEGER,
                $COL_NOTES TEXT,
                $COL_PHOTO_URI TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HIVES")
        onCreate(db)
    }

    suspend fun getAllHives(): List<Beehive> = withContext(Dispatchers.IO) {
        val hiveList = mutableListOf<Beehive>()
        val db = readableDatabase
        val cursor = db.query(TABLE_HIVES, null, null, null, null, null, "$COL_UPDATED_AT DESC")

        cursor.use { c ->
            val idIdx = c.getColumnIndexOrThrow(COL_ID)
            val apiaryIdx = c.getColumnIndexOrThrow(COL_APIARY_ID)
            val numIdx = c.getColumnIndexOrThrow(COL_HIVE_NUMBER)
            val latIdx = c.getColumnIndexOrThrow(COL_LATITUDE)
            val lonIdx = c.getColumnIndexOrThrow(COL_LONGITUDE)
            val statusIdx = c.getColumnIndexOrThrow(COL_STATUS)
            val updatedIdx = c.getColumnIndexOrThrow(COL_UPDATED_AT)
            val notesIdx = c.getColumnIndexOrThrow(COL_NOTES)
            val photoIdx = c.getColumnIndexOrThrow(COL_PHOTO_URI)

            while (c.moveToNext()) {
                val statusStr = c.getString(statusIdx)
                val statusEnum = try {
                    HiveStatus.valueOf(statusStr)
                } catch (e: Exception) {
                    HiveStatus.STRONG
                }

                val photoRaw = c.getString(photoIdx)
                val photoUris = if (!photoRaw.isNullOrBlank()) {
                    photoRaw.split(";").filter { it.isNotBlank() }
                } else {
                    emptyList()
                }

                hiveList.add(
                    Beehive(
                        id = c.getString(idIdx),
                        apiaryId = c.getString(apiaryIdx),
                        hiveNumber = c.getString(numIdx),
                        latitude = if (c.isNull(latIdx)) null else c.getDouble(latIdx),
                        longitude = if (c.isNull(lonIdx)) null else c.getDouble(lonIdx),
                        status = statusEnum,
                        updatedAt = c.getLong(updatedIdx),
                        notes = c.getString(notesIdx) ?: "",
                        photoUris = photoUris
                    )
                )
            }
        }
        hiveList
    }

    suspend fun insertOrUpdateHive(hive: Beehive) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID, hive.id)
            put(COL_APIARY_ID, hive.apiaryId)
            put(COL_HIVE_NUMBER, hive.hiveNumber)
            put(COL_LATITUDE, hive.latitude)
            put(COL_LONGITUDE, hive.longitude)
            put(COL_STATUS, hive.status.name)
            put(COL_UPDATED_AT, hive.updatedAt)
            put(COL_NOTES, hive.notes)
            put(COL_PHOTO_URI, hive.photoUris.joinToString(";"))
        }
        db.insertWithOnConflict(TABLE_HIVES, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    suspend fun deleteHive(id: String) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        db.delete(TABLE_HIVES, "$COL_ID = ?", arrayOf(id))
    }
}
