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

    val adsSettingsList: Array<Int> = arrayOf(1,2,3)
    val adsWatchedList: HashMap<Int, Boolean> = hashMapOf()

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
                    if(adsWatchedList.containsKey(2) && adsWatchedList[2] == true) {
                        VideoSettings.videoBitrate = videoSettingsReader.readBitrate()
                    } else {
                        VideoSettings.videoBitrate = Constants.Video.VideoSettings.DEFAULT_VIDEO_BITRATE
                    }
                }
                Constants.Video.FileNames.FPS_SETTING_FILE_NAME -> {
                    if(adsWatchedList.containsKey(3) && adsWatchedList[3] == true) {
                        VideoSettings.videoFps = videoSettingsReader.readFps()
                    } else {
                        VideoSettings.videoFps = Constants.Video.VideoSettings.DEFAULT_VIDEO_FPS
                    }
                }
                Constants.Video.FileNames.OUTPUT_FORMAT_SETTING_FILE_NAME -> {
                    VideoSettings.videoOutput = videoSettingsReader.readOutputFormat()
                }
                Constants.Video.FileNames.ENCODER_SETTING_FILE_NAME -> {
                    if(adsWatchedList.containsKey(1) && adsWatchedList[1] == true) {
                        VideoSettings.videoEncoder = videoSettingsReader.readEncoder()
                    } else {
                        VideoSettings.videoEncoder = Constants.Video.VideoSettings.DEFAULT_VIDEO_ENCODER
                    }
                }
                Constants.Video.FileNames.DIRECTORY_SETTING_FILE_NAME -> {
                    VideoSettings.videoSaveDirectory = videoSettingsReader.readDirectory()
                }
            }
        }
        _dataRead.postValue(true)
    }

    fun updateWatchedAds(key: Int, value: Boolean) {
        adsWatchedList.put(key, value)
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