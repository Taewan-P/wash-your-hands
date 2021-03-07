package page.chungjungsoo.cleanhands

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_map.*

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {
    var dbHandler : LocationDatabaseHelper? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var defaultLocation = LatLng(37.57601, 126.97692) //Seoul, Gwanghwamoon
    private val locationPermissionGranted = false
    private val lastKnownLocation: Location? = null
    lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.activity_map)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val innerMapFragment = innerMap as SupportMapFragment
        innerMapFragment.getMapAsync(this)

        myLocationBtn.setOnClickListener {
            getLocation()
        }

        setHomeBtn.setOnClickListener {
            setLocation()
            Toast.makeText(activity, "Set Home Location.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        dbHandler = LocationDatabaseHelper(this.requireContext())
        mMap = googleMap
//        val marker = this.defaultLocation
        val marker = dbHandler!!.getAll()
        mMap.addMarker(MarkerOptions().position(marker).title("Home"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0F))
    }

    private fun setLocation() {
        dbHandler = LocationDatabaseHelper(this.requireContext())
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionCode
            )
        }
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        Log.d("CURRENT LOCATION", "Lat: ${location!!.latitude}, Lon: ${location.longitude}")
        mMap.clear()
        dbHandler!!.updateLocation(location.latitude, location.longitude)
    }


    private fun getLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        Log.d("CURRENT LOCATION", "Lat: ${location!!.latitude}, Lon: ${location.longitude}")
        mMap.clear()
        val mark = LatLng(location.latitude, location.longitude)
        defaultLocation = mark
        mMap.addMarker((MarkerOptions().position(mark).title("Home")))
        mMap.moveCamera((CameraUpdateFactory.newLatLng(mark)))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0F))
    }


    // For when location is changed
    override fun onLocationChanged(location: Location) {
        Log.d("LOCATION", "Latitude: " + location.latitude + " , Longitude: " + location.longitude)
    }
}