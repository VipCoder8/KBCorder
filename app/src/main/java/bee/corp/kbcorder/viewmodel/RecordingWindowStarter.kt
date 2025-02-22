package bee.corp.kbcorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RecordingWindowStarter(app: Application) : AndroidViewModel(app) {
    private var _open: MutableLiveData<ScreenRecorder> = MutableLiveData()
    val onOpen: LiveData<ScreenRecorder> get() = _open

    fun open(sr: ScreenRecorder) {
        _open.postValue(sr)
    }
}