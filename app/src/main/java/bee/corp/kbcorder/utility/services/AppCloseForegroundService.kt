package bee.corp.kbcorder.utility.services

import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import bee.corp.kbcorder.R
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.view.NotificationCreator
import kotlin.system.exitProcess

class AppCloseForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Creating foreground service notification with a button that exits the program on press.
        val appCloseOnNotification = NotificationCreator.createNotificationWithButtons(this,
            Constants.Notifications.ChannelsIDs.APP_CLOSE_FROM_NOTIFICATION_CHANNEL_ID,
            Constants.Notifications.ChannelsNames.APP_CLOSE_FROM_NOTIFICATION_CHANNEL_NAME,
            R.mipmap.ic_launcher_adaptive_fore,
            Constants.Notifications.ActionTitles.APP_CLOSE_FROM_NOTIFICATION_CONTENT_TITLE,
            NotificationManager.IMPORTANCE_HIGH, NotificationCompat.PRIORITY_HIGH,
            Constants.Notifications.ActionTitles.APP_CLOSE_FROM_NOTIFICATION_ACTION_TITLE,
            Intent(applicationContext, AppCloseFromNotification::class.java).apply {
                action = Constants.Intent.Actions.APP_CLOSE_FROM_NOTIFICATION_ACTION
            }
        )
        startForeground(
            Constants.RequestCodes.FOREGROUND_SERVICE_APP_CLOSE_ON_NOTIFICATION_ID,
            appCloseOnNotification)
        return super.onStartCommand(intent, flags, startId)
    }

    class AppCloseFromNotification : BroadcastReceiver() {
        //Receiving action sent from the exit button from foreground service notification.
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == Constants.Intent.Actions.APP_CLOSE_FROM_NOTIFICATION_ACTION) {
                //Closing the app.
                val serviceIntent = Intent(
                    context,
                    AppCloseForegroundService::class.java
                )
                context!!.stopService(serviceIntent)
                exitProcess(0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}