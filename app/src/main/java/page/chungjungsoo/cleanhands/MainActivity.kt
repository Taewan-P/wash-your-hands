package page.chungjungsoo.cleanhands

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var locationManager: LocationManager
    private var home = Location("point A")
    private var pos = Location("point B")
    private var stay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, BackGround::class.java)
        startService(intent)
      
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.prefFragment, Preference())
            .commit()


        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }

        var mapFragment: Fragment = MapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.map, mapFragment, mapFragment.javaClass.simpleName).commit()


        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val pref : SharedPreferences = getSharedPreferences("page.chungjungsoo.cleanhands_preferences", MODE_PRIVATE)
        val sensitivity = pref.getInt("sensitivity", 50).toFloat()
        val lat = pref.getFloat("latitude", 0.0F)
        val lon = pref.getFloat("longitude", 0.0F)

        home.latitude = lat.toDouble()
        home.longitude = lon.toDouble()

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                location.let {
                    pos.latitude = it.latitude
                    pos.longitude = it.longitude
                }
                if (pos.distanceTo(home) < sensitivity) {
                    if (!stay) {
                        stay = true
                        val channel = NotificationChannel("2", "푸시 알람", NotificationManager.IMPORTANCE_DEFAULT)

                        val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.createNotificationChannel(channel)

                        val title = "You are Home"
                        val content = "Wash your Hands"

                        val builder = NotificationCompat.Builder(applicationContext, "2")
                            .setContentTitle(title)
                            .setContentText(content)
                            .setAutoCancel(true)
                            .setOngoing(false)
                            .setSmallIcon(R.drawable.ic_baseline_wash_24)
                            .setShowWhen(true)
                            .setContentIntent(PendingIntent.getActivity(applicationContext, 0, Intent(), 0))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        NotificationManagerCompat.from(applicationContext).notify(2, builder.build())
                    }
                } else {
                    stay = false
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1f, locationListener)



        notiSettingBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            startActivity(intent)
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val alert = AlertDialog.Builder(this)
                        .setTitle("Location Access Needed")
                        .setPositiveButton("OK") { _, _ ->
                            finish()
                        }
                        .create()
                    alert.setCancelable(false)
                    alert.show()
                }
                return
            }
        }
    }
}