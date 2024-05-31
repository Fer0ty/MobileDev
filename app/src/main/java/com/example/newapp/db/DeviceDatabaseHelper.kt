package com.example.newapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Device(val id: Long, val name: String, val type: String, val room: String)

class DeviceDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "smart_home.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_DEVICES = "devices"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_ROOM = "room"

        private const val SQL_CREATE_TABLE_DEVICES = """
            CREATE TABLE $TABLE_DEVICES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_ROOM TEXT NOT NULL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_DEVICES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DEVICES")
        onCreate(db)
    }

    fun getAllDevices(): List<Device> {
        val devices = mutableListOf<Device>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_DEVICES", null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val type = getString(getColumnIndexOrThrow(COLUMN_TYPE))
                val room = getString(getColumnIndexOrThrow(COLUMN_ROOM))
                devices.add(Device(id, name, type, room))
            }
        }
        cursor.close()
        return devices
    }

    fun getDeviceById(id: Long): Device? {
        val db = readableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM $TABLE_DEVICES WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
        var device: Device? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
            val room = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROOM))
            device = Device(id, name, type, room)
        }
        cursor.close()
        return device
    }

    fun updateDeviceById(id: Long, newName: String, newType: String, newRoom: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
            put(COLUMN_TYPE, newType)
            put(COLUMN_ROOM, newRoom)
        }
        return db.update(TABLE_DEVICES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun deleteDeviceById(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_DEVICES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun addDevice(name: String, type: String, room: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_TYPE, type)
            put(COLUMN_ROOM, room)
        }
        return db.insert(TABLE_DEVICES, null, values)
    }

    fun getDevicesByRoom(roomName: String): List<Device> {
        val devices = mutableListOf<Device>()
        val db = readableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM $TABLE_DEVICES WHERE $COLUMN_ROOM = ?", arrayOf(roomName))
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val type = getString(getColumnIndexOrThrow(COLUMN_TYPE))
                val room = getString(getColumnIndexOrThrow(COLUMN_ROOM))
                devices.add(Device(id, name, type, room))
            }
        }
        cursor.close()
        return devices
    }
}
