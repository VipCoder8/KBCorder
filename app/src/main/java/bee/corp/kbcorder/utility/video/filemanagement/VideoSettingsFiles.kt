package bee.corp.kbcorder.utility.video.filemanagement

import android.content.Context
import bee.corp.kbcorder.utility.Constants
import java.io.File

open class VideoSettingsFiles(c: Context) {
    protected var settingsFolder: File =
        File(c.getExternalFilesDir(null)?.absolutePath!! + "/" +
                Constants.Video.FileNames.SETTINGS_FOLDER_NAME
        )

    protected var fpsFile: File = File(settingsFolder.absolutePath + "/" +
            Constants.Video.FileNames.FPS_SETTING_FILE_NAME
    )
    protected var bitrateFile: File = File(settingsFolder.absolutePath + "/" +
            Constants.Video.FileNames.BITRATE_SETTING_FILE_NAME
    )
    protected var encoderFile: File = File(settingsFolder.absolutePath + "/" +
            Constants.Video.FileNames.ENCODER_SETTING_FILE_NAME
    )
    protected var directoryFile: File = File(settingsFolder.absolutePath + "/" +
            Constants.Video.FileNames.DIRECTORY_SETTING_FILE_NAME
    )
    protected var outputFormatFile: File = File(settingsFolder.absolutePath + "/" +
            Constants.Video.FileNames.OUTPUT_FORMAT_SETTING_FILE_NAME
    )
    init {
        if(!settingsFolder.exists()) {
            settingsFolder.mkdirs()
        }
        if(!fpsFile.exists()) {
            fpsFile.createNewFile()
        }
        if(!bitrateFile.exists()) {
            bitrateFile.createNewFile()
        }
        if(!encoderFile.exists()) {
            encoderFile.createNewFile()
        }
        if(!directoryFile.exists()) {
            directoryFile.createNewFile()
        }
        if(!outputFormatFile.exists()) {
            outputFormatFile.createNewFile()
        }
    }
}