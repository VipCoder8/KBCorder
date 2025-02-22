package bee.corp.kbcorder.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import bee.corp.kbcorder.utility.Constants

class NotificationCreator {
    companion object {
        fun createNotificationWithButtons(context: Context, channelId: String, channelName: String,
                                          smallIcon: Int, contentTitle: String, importance: Int,
                                          priority: Int, actionTitle: String, vararg action: Intent) : Notification {
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            val channel: NotificationChannel?
            //Creating buttons for notifications that invoke specific actions - start.
            val pendingIntents: Array<PendingIntent?> = arrayOfNulls(action.size)
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                for(i in pendingIntents.indices) {
                    pendingIntents[i] = PendingIntent.getBroadcast(context,
                        Constants.RequestCodes.GET_BROADCAST_REQUEST_CODE,
                        action[i], PendingIntent.FLAG_UPDATE_CURRENT)
                    //Adding the action itself.
                    builder.addAction(android.R.drawable.ic_menu_close_clear_cancel,
                        actionTitle,
                        pendingIntents[i])
                }
            } else {
                for(i in pendingIntents.indices) {
                    pendingIntents[i] = PendingIntent.getBroadcast(context,
                        Constants.RequestCodes.GET_BROADCAST_REQUEST_CODE,
                        action[i], PendingIntent.FLAG_MUTABLE)
                    //Adding the action itself.
                    builder.addAction(android.R.drawable.ic_menu_close_clear_cancel,
                        actionTitle,
                        pendingIntents[i])
                }
            }
            //Creating buttons for notifications that invoke specific actions - end.

            //Initializing notification based on current android version - start.
            builder.setSmallIcon(smallIcon)
            builder.setContentTitle(contentTitle)
            builder.setSilent(true)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = NotificationChannel(channelId, channelName, importance)
                (context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager).createNotificationChannel(channel)
                return builder.build()
            } else {
                builder.setPriority(priority)
                return builder.build()
            }
            //Initializing notification based on current android version - end.
        }
    }
}