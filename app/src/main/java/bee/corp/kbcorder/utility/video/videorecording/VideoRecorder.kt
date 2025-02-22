package bee.corp.kbcorder.utility.video.videorecording

import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import bee.corp.kbcorder.utility.Constants
import java.io.IOException

class VideoRecorder {
    private var mediaRecorder: MediaRecorder? = MediaRecorder()
    private var mediaProjection: MediaProjection? = null
    private var mediaProjectionManager: MediaProjectionManager? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val videoRecorderData = VideoRecorderData()

    private var isPrepared = false
    private var _isRecording = false
    private var _isPaused = false
    private var recordingIndicator = 0

    val isRecording: Boolean get() = _isRecording
    val isPaused: Boolean get() = _isPaused

    fun prepareMediaProjection(manager: MediaProjectionManager, density: Int, width: Int, height: Int, code: Int, data: Intent) {
        mediaProjectionManager = manager
        videoRecorderData.saveMediaProjectionData(density, width, height, code, data)
    }

    private fun createVirtualDisplay() {
        mediaProjection = mediaProjectionManager?.getMediaProjection(videoRecorderData.resultCode, videoRecorderData.resultData)
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            Constants.Video.MEDIA_PROJECTION_NAME,
            videoRecorderData.screenWidth, videoRecorderData.screenHeight, videoRecorderData.screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder?.surface, null, null
        )
    }

    fun prepareRecorder(fps: Int, bitrate: Int, source: Int, output: Int, encoder: Int, width: Int, height: Int) {
        videoRecorderData.saveVideoSettings(fps, bitrate, source, output, encoder, width, height)
    }

    private fun setupRecorder(videoFile: String) {
        if (isPrepared) return

        mediaRecorder?.apply {
            setVideoSource(videoRecorderData.oldSource)
            setOutputFormat(videoRecorderData.oldOutput)
            setVideoEncoder(videoRecorderData.oldEncoder)
            setVideoSize(videoRecorderData.oldWidth, videoRecorderData.oldHeight)
            setVideoFrameRate(videoRecorderData.oldFps)
            setOutputFile(videoFile)
            setVideoEncodingBitRate(videoRecorderData.oldBitrate)

            try {
                prepare()
                isPrepared = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun startRecording(videoFile: String) {
        if (_isRecording) return

        try {
            setupRecorder(videoFile)
            createVirtualDisplay()
            mediaRecorder?.start()
            _isRecording = true
            _isPaused = false
            recordingIndicator++
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        if (_isRecording) {
            try {
                resetRecording()
            } catch (e: RuntimeException) {
                e.printStackTrace()
                mediaRecorder?.reset()
            }
        }
    }

    fun pauseRecording() {
        if (_isRecording && !_isPaused) {
            mediaRecorder?.pause()
            _isRecording = false
            _isPaused = true
        }
    }

    fun resumeRecording() {
        if (_isPaused) {
            mediaRecorder?.resume()
            _isPaused = false
            _isRecording = true
        }
    }

    private fun resetRecording() {
        mediaRecorder?.apply {
            stop()
            reset()
        }
        mediaProjection?.stop()
        virtualDisplay?.release()

        isPrepared = false
        _isRecording = false
        _isPaused = false
        recordingIndicator = 0
    }
    class VideoRecorderData {
        var screenDensity = 0
        var screenWidth = 0
        var screenHeight = 0
        var resultCode = 0
        lateinit var resultData: Intent

        var oldFps = 0
        var oldBitrate = 0
        var oldSource = 0
        var oldOutput = 0
        var oldEncoder = 0
        var oldWidth = 0
        var oldHeight = 0

        fun saveVideoSettings(
            fps: Int,
            bitrate: Int,
            source: Int,
            output: Int,
            encoder: Int,
            width: Int,
            height: Int
        ) {
            oldFps = fps
            oldBitrate = bitrate
            oldSource = source
            oldOutput = output
            oldEncoder = encoder
            oldWidth = width
            oldHeight = height
        }

        fun saveMediaProjectionData(
            density: Int,
            width: Int,
            height: Int,
            resultCode: Int,
            resultData: Intent
        ) {
            screenDensity = density
            screenWidth = width
            screenHeight = height
            this.resultCode = resultCode
            this.resultData = resultData
        }
    }
}
