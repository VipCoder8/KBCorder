package bee.corp.kbcorder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bee.corp.kbcorder.model.SettingsData
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.video.VideoSettings
import bee.corp.kbcorder.utility.video.filemanagement.FilesManipulation
import java.io.File

class SettingsSaver(application: Application) : AndroidViewModel(application) {
    val settingsFolder = File(application.getExternalFilesDir(null)?.absolutePath!! + "/" +
            Constants.Video.FileNames.SETTINGS_FOLDER_NAME
    )

    private val filesManipulation: FilesManipulation = FilesManipulation()

    private val _readData: MutableLiveData<SettingsData> = MutableLiveData()
    val getReadData: LiveData<SettingsData> get() = _readData

    init {
        readData(settingsFolder.absolutePath + "/")
    }

    private fun readData(file: String) {
        if(File(file).name.equals(Constants.Settings.Files.APP_LANGUAGE_FILE_NAME)) {
            val settingsData = SettingsData(Constants.Settings.Types.APP_LANGUAGE_DATA_TYPE, filesManipulation.getData(file))
            _readData.value = settingsData
        }
        if(File(file).name.equals(Constants.Settings.Files.APP_THEME_FILE_NAME)) {
            val settingsData = SettingsData(Constants.Settings.Types.APP_THEME_DATA_TYPE, filesManipulation.getData(file))
            _readData.value = settingsData
        }
    }
    fun saveData(file: String, data: String) {
        filesManipulation.createFile(file)
        filesManipulation.writeToFile(file, data)
    }
}