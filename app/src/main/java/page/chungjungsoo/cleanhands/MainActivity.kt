package page.chungjungsoo.cleanhands

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mapFragment: Fragment = MapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.map, mapFragment, mapFragment.javaClass.simpleName).commit()

        setLocationBtn.setOnClickListener {
            // TODO: 집 위치 설정하기
        }

        sensitivityBtn.setOnClickListener { 
            // TODO: 감도 설정

        }

        notiSettingBtn.setOnClickListener {
            var intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            startActivity(intent)
        }



    }
}