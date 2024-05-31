package com.example.newapp.db


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Room(val id: Long, val name: String)

class RoomDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "smart_home2.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_ROOMS = "rooms"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"

        private const val SQL_CREATE_TABLE_ROOMS = """
            CREATE TABLE $TABLE_ROOMS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_ROOMS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROOMS")
        onCreate(db)
    }

    fun getAllRooms(): List<Room> {
        val rooms = mutableListOf<Room>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_ROOMS", null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                rooms.add(Room(id, name))
            }
        }
        cursor.close()
        return rooms
    }

    fun getRoomById(id: Long): Room? {
        val db = readableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM $TABLE_ROOMS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
        var room: Room? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            room = Room(id, name)
        }
        cursor.close()
        return room
    }

    fun updateRoomNameById(id: Long, newName: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
        }
        return db.update(TABLE_ROOMS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteRoomById(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_ROOMS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun addRoom(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        return db.insert(TABLE_ROOMS, null, values)
    }


}