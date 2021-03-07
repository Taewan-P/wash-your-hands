package page.chungjungsoo.cleanhands

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import page.chungjungsoo.cleanhands.LocationData

class LocationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val DB_NAME = "LocationDB"
        private val DB_VERSION = 1
        private val TABLE_NAME = "HomeLocation"
        private val ID = "id"
        private val LAT = "Latitude"
        private val LNG = "Longitude"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // Create Database
        val createTable =
                "CREATE TABLE $TABLE_NAME" +
                        "($ID INTEGER PRIMARY KEY," +
                        "$LAT FLOAT DEFAULT 37.57601," +
                        "$LNG FLOAT DEFAULT 126.97692"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) { }

    fun updateLocation(location: LocationData, position: Int) : Boolean {
        // Update to-do in database
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(LAT, location.latitude)
        values.put(LNG, location.longitude)

        val result = db.update(TABLE_NAME, values, "$ID IN(SELECT $ID FROM $TABLE_NAME LIMIT 1 OFFSET $position)", null) > 0
        db.close()

        return result
    }

    fun getAll() : MutableList<LocationData> {
        // Get all data from database and return MutableList for ListView.
        var locations = mutableListOf<LocationData>()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        var latitute : Float
        var longitute : Float

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    latitute = cursor.getString(cursor.getColumnIndex(LAT))
                    longitute = cursor.getString(cursor.getColumnIndex(LNG))

                    locations.add(LocationData(latitute, longitute))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return lacations
    }
}