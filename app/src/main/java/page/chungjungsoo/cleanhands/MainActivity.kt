package page.chungjungsoo.cleanhands

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
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
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            startActivity(intent)
        }
    }
}