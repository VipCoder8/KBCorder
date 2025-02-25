package bee.corp.kbcorder.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.activity.result.ActivityResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.video.VideoSettings
import bee.corp.kbcorder.utility.video.filemanagement.VideoFilesManipulation
import bee.corp.kbcorder.utility.video.videorecording.VideoRecorder

class ScreenRecorder(app: Application) : AndroidViewModel(app) {
    private val videoRecorder: VideoRecorder = VideoRecorder()
    private val mediaProjectionManager: MediaProjectionManager =
        app.applicationContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

    private val _videoRecordingState: MutableLiveData<Int> = MutableLiveData()
    val getVideoRecordingState: LiveData<Int> get() = _videoRecordingState

    private val _startActivityForResult: MutableLiveData<Intent> = MutableLiveData()
    val shouldStartActivityForResult: LiveData<Intent> get() = _startActivityForResult

    private val _recordingRemoved: MutableLiveData<Boolean> = MutableLiveData()
    val isRecordingRemoved: LiveData<Boolean> get() = _recordingRemoved

    fun initializeVideoRecorderProjection(it: ActivityResult) {
        //Preparing media projection if user agreed on screen capture.
        if(it.resultCode == Activity.RESULT_OK) {
            videoRecorder.prepareMediaProjection(
                mediaProjectionManager,
                Constants.Core.getScreenDensity(getApplication()).toInt(),
                Constants.Core.getScreenWidth(getApplication()),
                Constants.Core.getScreenHeight(getApplication()), it.resultCode, it.data!!)
        }
    }

    fun requestRecordingStart() {
        //Final recorder preparation that will allow recording of screen.
        videoRecorder.prepareRecorder(
            VideoSettings.videoFps,
            VideoSettings.videoBitrate,
            VideoSettings.videoSource,
            VideoSettings.videoOutput,
            VideoSettings.videoEncoder,
            Constants.Core.getScreenWidth(getApplication()),
            Constants.Core.getScreenHeight(getApplication()))
        _startActivityForResult.postValue(mediaProjectionManager.createScreenCaptureIntent())
    }

    fun removeRecording() {
        _recordingRemoved.postValue(true)
    }

    fun startRecording() {
        _videoRecordingState.postValue(Constants.Video.VideoRecordingStates.RECORDING_STARTED)
        if(!isRecording() && !videoRecorder.isPaused) {
            videoRecorder.startRecording(VideoFilesManipulation.generateVideoFilePath(VideoSettings.videoSaveDirectory, VideoSettings.videoOutput))
        } else if(videoRecorder.isPaused) {
            resumeRecording()
        }
    }

    private fun resumeRecording() {
        videoRecorder.resumeRecording()
    }

    fun pauseRecording() {
        _videoRecordingState.postValue(Constants.Video.VideoRecordingStates.RECORDING_PAUSED)
        videoRecorder.pauseRecording()
    }

    fun stopRecording() {
        _videoRecordingState.postValue(Constants.Video.VideoRecordingStates.RECORDING_STOPPED)
        videoRecorder.stopRecording()
    }

    fun isRecording() : Boolean {
        return videoRecorder.isRecording
    }

    fun isPaused() : Boolean {
        return videoRecorder.isPaused
    }

}
