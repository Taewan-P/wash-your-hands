package page.chungjungsoo.cleanhands

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BackGround : Service() {

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel("1234", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT)

        val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)


        val builder = NotificationCompat.Builder(this,"1234")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentText("test")
        startForeground(1, builder.build())
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}