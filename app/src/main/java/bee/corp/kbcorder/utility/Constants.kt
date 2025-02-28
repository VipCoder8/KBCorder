package bee.corp.kbcorder.utility

import android.Manifest
import android.app.Application
import android.content.Context
import android.media.MediaRecorder
import android.util.DisplayMetrics
import android.view.WindowManager


class Constants {
    object Video {
        const val MEDIA_PROJECTION_NAME = "BCorder_Recording"
        object VideoSettings {
            const val DEFAULT_VIDEO_FPS = 30
            const val DEFAULT_VIDEO_BITRATE = 10000000
            const val DEFAULT_VIDEO_SOURCE = MediaRecorder.VideoSource.SURFACE
            const val DEFAULT_VIDEO_OUTPUT = MediaRecorder.OutputFormat.MPEG_4
            const val DEFAULT_VIDEO_ENCODER = MediaRecorder.VideoEncoder.DEFAULT
        }
        object VideoRecordingStates {
            const val RECORDING_STARTED = 0
            const val RECORDING_PAUSED = 1
            const val RECORDING_STOPPED = 2
        }
        object OutputFormats {
            const val MPEG_4_OUTPUT_FORMAT = ".mp4"
            const val WEBM_OUTPUT_FORMAT = ".webm"
            const val NOT_LISTED_OUTPUT_FORMAT = ".und"
        }
        object FileNames {
            const val SETTINGS_FOLDER_NAME = "settings"
            const val FPS_SETTING_FILE_NAME = "fps.dat"
            const val BITRATE_SETTING_FILE_NAME = "bitrate.dat"
            const val OUTPUT_FORMAT_SETTING_FILE_NAME = "output_format.dat"
            const val ENCODER_SETTING_FILE_NAME = "encoder.dat"
            const val DIRECTORY_SETTING_FILE_NAME = "directory.dat"
        }
        object DataTypes {
            const val VIDEO_OUTPUT_DIRECTORY_DATA_TYPE = 0
            const val VIDEO_ENCODER_DATA_TYPE = 1
            const val VIDEO_BITRATE_DATA_TYPE = 2
            const val VIDEO_FPS_DATA_TYPE = 3
            const val VIDEO_OUTPUT_FORMAT_DATA_TYPE = 4
        }
    }

    object Views {
        object VideoTabViewTypes {
            const val ROOT_VIEW_TYPE = 0
            const val DELETE_BUTTON_VIEW_TYPE = 1
            const val EDIT_BUTTON_VIEW_TYPE = 2
            const val INFO_BUTTON_VIEW_TYPE = 3
        }
        object DefaultValues {
            const val DEFAULT_VIDEO_ITEM_DECORATION_PADDING = 18
        }
    }

    object Errors {
        object VideoExceptionTexts {
            const val COULDNT_OPEN_INVALID_VIDEO_FILE_ERROR = "Couldn't play the video, reason: invalid or corrupted video file."
            const val COULDNT_DELETE_VIDEO_FILE_ERROR = "Couldn't delete the video file. Please, restart app or delete manually."
            const val COULDNT_EDIT_VIDEO_FILE_NAME_ERROR = "Couldn't edit the video file. Please, restart app or edit manually."
        }
    }

    object RequestCodes {
        const val GET_BROADCAST_REQUEST_CODE = 0
        const val FOREGROUND_SERVICE_APP_CLOSE_ON_NOTIFICATION_ID = 1
    }

    object Intent {
        object Actions {
            const val APP_CLOSE_FROM_NOTIFICATION_ACTION = "app_close_from_notification"
        }
        object ExtrasNames {
            const val VIDEO_PLAYER_ACTIVITY_VIDEO_TAB_DATA_PATH_EXTRA_NAME = "video_tab_path_for_video_player"
            const val VIDEO_PLAYER_ACTIVITY_VIDEO_TAB_DATA_TITLE_EXTRA_NAME = "video_tab_title_for_video_player"
        }
    }

    object Notifications {
        object ChannelsIDs {
            const val APP_CLOSE_FROM_NOTIFICATION_CHANNEL_ID = "app_cls_notif"
        }

        object ChannelsNames {
            const val APP_CLOSE_FROM_NOTIFICATION_CHANNEL_NAME = "Close App From Notification"
        }
        object ActionTitles {
            const val APP_CLOSE_FROM_NOTIFICATION_ACTION_TITLE = "Exit"
            const val APP_CLOSE_FROM_NOTIFICATION_CONTENT_TITLE = "Running..."
        }
    }

    object Settings {
        object Files {
            const val APP_LANGUAGE_FILE_NAME = "app_language"
            const val APP_THEME_FILE_NAME = "app_theme"
        }
        object Types {
            const val APP_LANGUAGE_DATA_TYPE = 0
            const val APP_THEME_DATA_TYPE = 1
        }
    }

    object Core {

        fun getNavigationBarHeight(a: Application): Int {
            val metrics = DisplayMetrics()
            a.getSystemService(WindowManager::class.java).getDefaultDisplay().getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            a.getSystemService(WindowManager::class.java).getDefaultDisplay().getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight) realHeight - usableHeight
            else 0
        }
        fun getScreenWidth(a: Application) : Int {
            return a.getSystemService(WindowManager::class.java).defaultDisplay.width
        }
        fun getScreenHeight(a: Application) : Int {
            val metrics = DisplayMetrics()
            a.getSystemService(WindowManager::class.java).getDefaultDisplay().getMetrics(metrics)
            return metrics.heightPixels + getNavigationBarHeight(a)
        }
        fun getScreenDensity(c: Context): Int {
            return c.resources.displayMetrics.densityDpi
        }
        val PERMISSIONS = arrayOf(Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION)
    }
}