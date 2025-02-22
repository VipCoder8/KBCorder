package bee.corp.kbcorder.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bee.corp.kbcorder.model.VideoTabData
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.video.VideoSettings
import bee.corp.kbcorder.utility.video.filemanagement.VideoFilesManipulation

class VideosManagement(app: Application) : AndroidViewModel(app) {
    lateinit var currentVideoTab: VideoTabData
    var currentVideoTabPosition: Int = 0

    private lateinit var loadedVideos: ArrayList<VideoTabData>

    private val _videoEdited: MutableLiveData<Int> = MutableLiveData()
    val getEditedVideo: LiveData<Int> get() = _videoEdited

    private val _videoDeleted: MutableLiveData<Int> = MutableLiveData()
    val getDeletedVideo: LiveData<Int> get() = _videoDeleted

    fun setLoadedVideos(arr: ArrayList<VideoTabData>) {
        loadedVideos = arr
    }

    fun setCurrentVideoTab(position: Int, data: VideoTabData) {
        currentVideoTab = data
        currentVideoTabPosition = position
    }

    fun editVideoTitle(newTitle: String, data: VideoTabData, position: Int) {
        data.title = newTitle + "." + data.fileExtension
        data.filePath = VideoFilesManipulation.editVideoFileName(data, VideoSettings.videoSaveDirectory, newTitle)
        _videoEdited.postValue(position)
    }

    fun deleteVideo(position: Int, data: VideoTabData) {
        if(VideoFilesManipulation.deleteVideoFile(data)) {
            loadedVideos -= data
            _videoDeleted.postValue(position)
        } else {
            Toast.makeText(this.getApplication(), Constants.Errors.VideoExceptionTexts.COULDNT_DELETE_VIDEO_FILE_ERROR, Toast.LENGTH_LONG).show()
        }
    }

}