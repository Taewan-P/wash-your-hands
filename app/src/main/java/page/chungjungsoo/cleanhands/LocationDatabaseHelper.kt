package page.chungjungsoo.cleanhands

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.maps.model.LatLng

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
                        "$LAT REAL," +
                        "$LNG REAL)"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) { }

    fun updateLocation(lati: Double, lngi: Double) : Boolean {
        // Update to-do in database
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(LAT, lati)
        values.put(LNG, lngi)
        val result = db.update(TABLE_NAME, values, null, null) > 0
        db.close()

        return result
    }

    fun getAll() : LatLng {
        // Get all data from database and return MutableList for ListView.
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        var latitute : Double = 37.57601
        var longitute : Double = 126.97692

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    latitute = cursor.getDouble(cursor.getColumnIndex(LAT))
                    longitute = cursor.getDouble(cursor.getColumnIndex(LNG))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return LatLng(latitute, longitute)
    }
}