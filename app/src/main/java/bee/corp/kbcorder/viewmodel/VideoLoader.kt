package bee.corp.kbcorder.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import bee.corp.kbcorder.R
import bee.corp.kbcorder.model.VideoTabData
import bee.corp.kbcorder.utility.video.VideoDataRetriever
import bee.corp.kbcorder.utility.video.VideoSettings
import bee.corp.kbcorder.utility.video.filemanagement.VideoFilesManipulation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class VideoLoader(app: Application) : AndroidViewModel(app) {
    private var videosList: ArrayList<VideoTabData> = ArrayList()

    private var _videosLoaded: MutableLiveData<ArrayList<VideoTabData>> = MutableLiveData()
    val getLoadedVideos: LiveData<ArrayList<VideoTabData>> get() = _videosLoaded

    fun loadVideos() {
        viewModelScope.launch {
            if (videosList.isEmpty()) {
                suspendedLoadVideos()
                _videosLoaded.postValue(videosList)
            }
        }
    }

    fun clearVideos() {
        if(videosList.size > 0) {
            videosList.clear()
        }
    }

    private suspend fun suspendedLoadVideos() {
        //Loading videos in suspend function not to delay main thread.
        withContext(Dispatchers.IO) {
            val rootFolder = File(VideoSettings.videoSaveDirectory + "/")
            for(file: File in rootFolder.listFiles()!!) {
                if(VideoFilesManipulation.isValidVideoFile(file)) {
                    //Retrieving video data for video tab.
                    val title: String = file.name
                    val extension: String = file.extension
                    var preview: Bitmap? = VideoDataRetriever.getVideoPreview(file.absolutePath)
                    //Applying default preview image if original one can't be found.
                    if(preview == null) {
                        preview = BitmapFactory.decodeResource(getApplication<Application>().resources,
                            R.drawable.video_preview_placeholder)
                    }
                    //Creating VideoTabData instance with retrieved data and adding it to the list.
                    val tabData = VideoTabData(title, preview!!, file.absolutePath, extension)
                    videosList += tabData
                }
            }
        }
    }
}