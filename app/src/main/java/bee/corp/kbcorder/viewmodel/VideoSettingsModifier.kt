package bee.corp.kbcorder.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bee.corp.kbcorder.model.UpdatedVideoData
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.Parser
import bee.corp.kbcorder.utility.video.VideoSettings
import bee.corp.kbcorder.utility.video.filemanagement.VideoSettingsReader
import bee.corp.kbcorder.utility.video.filemanagement.VideoSettingsSaver

class VideoSettingsModifier(app: Application) : AndroidViewModel(app) {
    private val videoSettingsSaver: VideoSettingsSaver = VideoSettingsSaver(app.applicationContext)
    private val videoSettingsReader: VideoSettingsReader = VideoSettingsReader(app.applicationContext)

    private var _dataRead: MutableLiveData<Boolean> = MutableLiveData()
    val isDataReadAndApplied: LiveData<Boolean> get() = _dataRead

    private var updatedVideoData: UpdatedVideoData = UpdatedVideoData()
    private var _dataUpdated: MutableLiveData<UpdatedVideoData> = MutableLiveData()
    val getUpdatedData: LiveData<UpdatedVideoData> get() = _dataUpdated

    init {
        //Reading and saving video directory on initialize for other parts of program
        //that uses video save path to work properly(it's being read after save).
        if(videoSettingsReader.readDirectory().isBlank()) {
            videoSettingsSaver.saveVideoDirectory(
                getApplication<Application>().applicationContext.getExternalFilesDir(null)?.absolutePath!!)
        }
    }

    fun readData() {
        for(data in videoSettingsReader.readData()!!) {
            when(data.name) {
                Constants.Video.FileNames.BITRATE_SETTING_FILE_NAME -> {
                    VideoSettings.videoBitrate = videoSettingsReader.readBitrate()
                }
                Constants.Video.FileNames.FPS_SETTING_FILE_NAME -> {
                    VideoSettings.videoFps = videoSettingsReader.readFps()
                }
                Constants.Video.FileNames.OUTPUT_FORMAT_SETTING_FILE_NAME -> {
                    VideoSettings.videoOutput = videoSettingsReader.readOutputFormat()
                }
                Constants.Video.FileNames.ENCODER_SETTING_FILE_NAME -> {
                    VideoSettings.videoEncoder = videoSettingsReader.readEncoder()
                }
                Constants.Video.FileNames.DIRECTORY_SETTING_FILE_NAME -> {
                    VideoSettings.videoSaveDirectory = videoSettingsReader.readDirectory()
                }
            }
        }
        _dataRead.postValue(true)
    }

    fun setVideoFps(fps: Int) {
        VideoSettings.videoFps = fps
        videoSettingsSaver.saveVideoFps(fps)
        passUpdatedData(Constants.Video.DataTypes.VIDEO_FPS_DATA_TYPE, fps)
    }
    fun setVideoBitrate(bitrate: Int) {
        VideoSettings.videoBitrate = bitrate
        videoSettingsSaver.saveVideoBitrate(bitrate)
        passUpdatedData(Constants.Video.DataTypes.VIDEO_BITRATE_DATA_TYPE, bitrate)
    }
    fun setVideoOutputFormat(format: Int) {
        VideoSettings.videoOutput = format
        videoSettingsSaver.saveVideoOutputFormat(format)
        passUpdatedData(Constants.Video.DataTypes.VIDEO_OUTPUT_FORMAT_DATA_TYPE, format)
    }
    fun setVideoEncoder(encoder: Int) {
        VideoSettings.videoEncoder = encoder
        videoSettingsSaver.saveVideoEncoder(encoder)
        passUpdatedData(Constants.Video.DataTypes.VIDEO_ENCODER_DATA_TYPE, encoder)
    }
    fun setVideoDirectoryFromUri(dir: Uri) {
        VideoSettings.videoSaveDirectory = Parser.getRealPathFromURI(getApplication(), dir)!!
        videoSettingsSaver.saveVideoDirectory(VideoSettings.videoSaveDirectory)
        passUpdatedData(Constants.Video.DataTypes.VIDEO_OUTPUT_DIRECTORY_DATA_TYPE,
            VideoSettings.videoSaveDirectory)
    }

    private fun passUpdatedData(dataType: Int, data: Any) {
        updatedVideoData.dataType = dataType
        updatedVideoData.data = data
        _dataUpdated.postValue(updatedVideoData)
    }
}