package com.example.hivepoint.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.hivepoint.model.Beehive
import com.example.hivepoint.model.HiveStatus
import com.example.hivepoint.model.InspectionLog
import com.example.hivepoint.model.QueenState
import com.example.hivepoint.model.Temperament
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HiveDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "robeefields_hives.db"
        private const val DATABASE_VERSION = 2

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

        private const val TABLE_INSPECTION_LOGS = "inspection_logs"
        private const val COL_LOG_ID = "id"
        private const val COL_LOG_HIVE_ID = "hive_id"
        private const val COL_LOG_TIMESTAMP = "timestamp"
        private const val COL_LOG_QUEEN_STATE = "queen_state"
        private const val COL_LOG_BROOD_FRAMES = "brood_frames"
        private const val COL_LOG_HONEY_FRAMES = "honey_frames"
        private const val COL_LOG_ADDED_SUPER = "added_super"
        private const val COL_LOG_VARROA = "varroa_treatment"
        private const val COL_LOG_FED_SYRUP = "fed_syrup"
        private const val COL_LOG_FED_PATTY = "fed_patty"
        private const val COL_LOG_CHANGED_BOTTOM = "changed_bottom_board"
        private const val COL_LOG_TEMPERAMENT = "temperament"
        private const val COL_LOG_NOTES = "notes"
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

        val createLogsQuery = """
            CREATE TABLE $TABLE_INSPECTION_LOGS (
                $COL_LOG_ID TEXT PRIMARY KEY,
                $COL_LOG_HIVE_ID TEXT,
                $COL_LOG_TIMESTAMP INTEGER,
                $COL_LOG_QUEEN_STATE TEXT,
                $COL_LOG_BROOD_FRAMES INTEGER,
                $COL_LOG_HONEY_FRAMES INTEGER,
                $COL_LOG_ADDED_SUPER INTEGER,
                $COL_LOG_VARROA INTEGER,
                $COL_LOG_FED_SYRUP INTEGER,
                $COL_LOG_FED_PATTY INTEGER,
                $COL_LOG_CHANGED_BOTTOM INTEGER,
                $COL_LOG_TEMPERAMENT TEXT,
                $COL_LOG_NOTES TEXT
            )
        """.trimIndent()
        db.execSQL(createLogsQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HIVES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INSPECTION_LOGS")
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
        db.delete(TABLE_INSPECTION_LOGS, "$COL_LOG_HIVE_ID = ?", arrayOf(id))
    }

    suspend fun insertInspectionLog(log: InspectionLog) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_LOG_ID, log.id)
            put(COL_LOG_HIVE_ID, log.hiveId)
            put(COL_LOG_TIMESTAMP, log.timestamp)
            put(COL_LOG_QUEEN_STATE, log.queenState.name)
            put(COL_LOG_BROOD_FRAMES, log.broodFrames)
            put(COL_LOG_HONEY_FRAMES, log.honeyFrames)
            put(COL_LOG_ADDED_SUPER, if (log.addedSuper) 1 else 0)
            put(COL_LOG_VARROA, if (log.varroaTreatment) 1 else 0)
            put(COL_LOG_FED_SYRUP, if (log.fedSyrup) 1 else 0)
            put(COL_LOG_FED_PATTY, if (log.fedPatty) 1 else 0)
            put(COL_LOG_CHANGED_BOTTOM, if (log.changedBottomBoard) 1 else 0)
            put(COL_LOG_TEMPERAMENT, log.temperament.name)
            put(COL_LOG_NOTES, log.notes)
        }
        db.insertWithOnConflict(TABLE_INSPECTION_LOGS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    suspend fun getInspectionLogsForHive(hiveId: String): List<InspectionLog> = withContext(Dispatchers.IO) {
        val logList = mutableListOf<InspectionLog>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_INSPECTION_LOGS,
            null,
            "$COL_LOG_HIVE_ID = ?",
            arrayOf(hiveId),
            null,
            null,
            "$COL_LOG_TIMESTAMP DESC"
        )

        cursor.use { c ->
            val idIdx = c.getColumnIndexOrThrow(COL_LOG_ID)
            val hiveIdIdx = c.getColumnIndexOrThrow(COL_LOG_HIVE_ID)
            val timeIdx = c.getColumnIndexOrThrow(COL_LOG_TIMESTAMP)
            val queenIdx = c.getColumnIndexOrThrow(COL_LOG_QUEEN_STATE)
            val broodIdx = c.getColumnIndexOrThrow(COL_LOG_BROOD_FRAMES)
            val honeyIdx = c.getColumnIndexOrThrow(COL_LOG_HONEY_FRAMES)
            val superIdx = c.getColumnIndexOrThrow(COL_LOG_ADDED_SUPER)
            val varroaIdx = c.getColumnIndexOrThrow(COL_LOG_VARROA)
            val syrupIdx = c.getColumnIndexOrThrow(COL_LOG_FED_SYRUP)
            val pattyIdx = c.getColumnIndexOrThrow(COL_LOG_FED_PATTY)
            val bottomIdx = c.getColumnIndexOrThrow(COL_LOG_CHANGED_BOTTOM)
            val tempIdx = c.getColumnIndexOrThrow(COL_LOG_TEMPERAMENT)
            val notesIdx = c.getColumnIndexOrThrow(COL_LOG_NOTES)

            while (c.moveToNext()) {
                val queenEnum = try {
                    QueenState.valueOf(c.getString(queenIdx))
                } catch (e: Exception) {
                    QueenState.UNKNOWN
                }

                val tempEnum = try {
                    Temperament.valueOf(c.getString(tempIdx))
                } catch (e: Exception) {
                    Temperament.CALM
                }

                logList.add(
                    InspectionLog(
                        id = c.getString(idIdx),
                        hiveId = c.getString(hiveIdIdx),
                        timestamp = c.getLong(timeIdx),
                        queenState = queenEnum,
                        broodFrames = c.getInt(broodIdx),
                        honeyFrames = c.getInt(honeyIdx),
                        addedSuper = c.getInt(superIdx) == 1,
                        varroaTreatment = c.getInt(varroaIdx) == 1,
                        fedSyrup = c.getInt(syrupIdx) == 1,
                        fedPatty = c.getInt(pattyIdx) == 1,
                        changedBottomBoard = c.getInt(bottomIdx) == 1,
                        temperament = tempEnum,
                        notes = c.getString(notesIdx) ?: ""
                    )
                )
            }
        }
        logList
    }
}
