package bee.corp.kbcorder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bee.corp.kbcorder.R
import bee.corp.kbcorder.utility.Constants

class RecordingWindowViewModel : ViewModel() {
    //Responsible for changing control buttons' image when recording state is changed.
    private var _controlPauseButtonsStateLiveData: MutableLiveData<Int> = MutableLiveData()
    val onPauseControlButtonStateChange: LiveData<Int> get() = _controlPauseButtonsStateLiveData

    private var _controlStartStopButtonsStateLiveData: MutableLiveData<Int> = MutableLiveData()
    val onStartStopControlButtonStateChange: LiveData<Int> get() = _controlStartStopButtonsStateLiveData

    fun updateState(state: Int) {
        when(state) {
            Constants.Video.VideoRecordingStates.RECORDING_STARTED -> {
                _controlStartStopButtonsStateLiveData.postValue(R.drawable.recording_window_controller_stop_recording)
                _controlPauseButtonsStateLiveData.postValue(R.drawable.recording_window_controller_pause_recording)
            }
            Constants.Video.VideoRecordingStates.RECORDING_STOPPED -> {
                _controlStartStopButtonsStateLiveData.postValue(R.drawable.recording_window_controller_start_recording)
                _controlPauseButtonsStateLiveData.postValue(R.drawable.recording_window_controller_pause_recording)
            }
            Constants.Video.VideoRecordingStates.RECORDING_PAUSED -> {
                _controlPauseButtonsStateLiveData.postValue(R.drawable.recording_window_controller_resume_recording)
            }
        }
    }
}