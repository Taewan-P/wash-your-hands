package page.chungjungsoo.cleanhands

import android.content.Intent
import android.provider.Settings
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
//class MainActivity : AppCompatActivity(){

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val defaultLocation = LatLng(37.57601, 126.97692) //Seoul, Gwanghwamoon
    private val locationPermissionGranted = false
    private val lastKnownLocation: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLocationBtn.setOnClickListener {
            // TODO: 집 위치 설정하기
        }

        sensitivityBtn.setOnClickListener { 
            // TODO: 감도 설정

        }

        notiSettingBtn.setOnClickListener {
            // TODO: 노티 설정으로 이동 -> 환경절정 - 앱 - 우리앱에 노티설정
            var intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker = this.defaultLocation
        mMap.addMarker(MarkerOptions().position(marker).title("Home"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0F))
    }
}