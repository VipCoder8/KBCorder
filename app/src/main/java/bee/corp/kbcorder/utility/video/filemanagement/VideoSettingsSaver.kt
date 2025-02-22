package bee.corp.kbcorder.utility.video.filemanagement

import android.content.Context

class VideoSettingsSaver(c: Context) : VideoSettingsFiles(c) {
    fun saveVideoFps(fps: Int) {
        fpsFile.writeBytes(fps.toString().toByteArray())
    }
    fun saveVideoBitrate(bitrate: Int) {
        bitrateFile.writeBytes(bitrate.toString().toByteArray())
    }
    fun saveVideoEncoder(encoder: Int) {
        encoderFile.writeBytes(encoder.toString().toByteArray())
    }
    fun saveVideoDirectory(dir: String) {
        directoryFile.writeBytes(dir.toByteArray())
    }
    fun saveVideoOutputFormat(output: Int) {
        outputFormatFile.writeBytes(output.toString().toByteArray())
    }
}