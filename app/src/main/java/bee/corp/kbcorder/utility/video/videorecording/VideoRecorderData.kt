package bee.corp.kbcorder.utility.video.videorecording

import android.content.Intent

open class VideoRecorderData {
    //To save media projection data.
    protected var screenDensity: Int = 0
    protected var screenWidth: Int = 0
    protected var screenHeight: Int = 0
    protected var resultCode: Int = 0
    protected lateinit var resultData: Intent

    //To save video recorder preparation data.
    protected var oldFps: Int = 0
    protected var oldBitrate: Int = 0
    protected var oldSource: Int = 0
    protected var oldOutput: Int = 0
    protected var oldEncoder: Int = 0
    protected var oldWidth: Int = 0
    protected var oldHeight: Int = 0

    protected fun saveVideoSettings(fps: Int, bitrate: Int, source: Int, output: Int,
                                    encoder: Int, width: Int, height: Int) {
        this.oldFps = fps
        this.oldBitrate = bitrate
        this.oldSource = source
        this.oldOutput = output
        this.oldEncoder = encoder
        this.oldWidth = width
        this.oldHeight = height
    }

    protected fun saveMediaProjectionData(density: Int, width: Int, height: Int, resultCode: Int, resultData: Intent) {
        screenDensity = density
        screenWidth = width
        screenHeight = height
        this.resultCode = resultCode
        this.resultData = resultData
    }

}