package bee.corp.kbcorder.utility.video.filemanagement

import android.content.Context
import bee.corp.kbcorder.utility.Constants
import java.io.File

class VideoSettingsReader(c: Context) : VideoSettingsFiles(c) {
    fun readData() : Array<out File>? {
        return settingsFolder.listFiles()
    }

    fun readFps() : Int {
        if(fpsFile.exists()) {
            try {
                return fpsFile.readLines()[0].toInt()
            } catch(e: Exception) {
                e.printStackTrace()
                return Constants.Video.VideoSettings.DEFAULT_VIDEO_FPS
            }
        } else {
            return Constants.Video.VideoSettings.DEFAULT_VIDEO_FPS
        }
    }
    fun readBitrate() : Int {
        if(bitrateFile.exists()) {
            try {
                return bitrateFile.readLines()[0].toInt()
            } catch (e: Exception) {
                e.printStackTrace()
                return Constants.Video.VideoSettings.DEFAULT_VIDEO_BITRATE
            }
        } else {
            return Constants.Video.VideoSettings.DEFAULT_VIDEO_BITRATE
        }
    }
    fun readEncoder() : Int {
        if(encoderFile.exists()) {
            try {
                return encoderFile.readLines()[0].toInt()
            } catch (e: Exception) {
                e.printStackTrace()
                return Constants.Video.VideoSettings.DEFAULT_VIDEO_ENCODER
            }
        } else {
            return Constants.Video.VideoSettings.DEFAULT_VIDEO_ENCODER
        }
    }
    fun readDirectory() : String {
        if(directoryFile.exists()) {
            try {
                return directoryFile.readLines()[0]
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        } else {
            return ""
        }
    }
    fun readOutputFormat() : Int {
        if(outputFormatFile.exists()) {
            try {
                return outputFormatFile.readLines()[0].toInt()
            } catch (e: Exception) {
                e.printStackTrace()
                return Constants.Video.VideoSettings.DEFAULT_VIDEO_OUTPUT
            }
        } else {
            return Constants.Video.VideoSettings.DEFAULT_VIDEO_OUTPUT
        }
    }
}