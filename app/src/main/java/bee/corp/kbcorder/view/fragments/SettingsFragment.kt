package bee.corp.kbcorder.view.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import bee.corp.kbcorder.R
import bee.corp.kbcorder.utility.ActivityNavigationManager
import bee.corp.kbcorder.utility.Ads
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.video.VideoSettings
import bee.corp.kbcorder.view.ThemeChanger
import bee.corp.kbcorder.viewmodel.VideoSettingsModifier

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var activityResultCallback: ActivityResultCallback<ActivityResult>
    private lateinit var activityResultContract: ActivityResultContract<Intent, ActivityResult>

    private lateinit var outputDirectoryPref: Preference

    private lateinit var videoSettingsModifier: VideoSettingsModifier

    private lateinit var preferenceEditor: SharedPreferences.Editor

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_fragment_preferences)
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
        initViewModels()
        observeViewModels()
        initPreferences()
        initPreferencesListeners()
        initOutputDirectorySelectActivityLauncher()
    }

    private fun initPreferences() {
        //Initialized separately from other preferences because it's the only preference that has to be modified with it's instance.
        outputDirectoryPref = preferenceScreen.getPreference(0)

        preferenceEditor = preferenceScreen.sharedPreferences!!.edit()
    }

    private fun initViewModels() {
        videoSettingsModifier = ViewModelProvider(requireActivity())[VideoSettingsModifier::class.java]
    }

    private fun observeViewModels() {
        //Reads video settings data from settings files and applies them to preferences.
        videoSettingsModifier.isDataReadAndApplied.observe(this) {
            preferenceScreen.getPreference(0).summary = "Current Directory: " +
                    VideoSettings.videoSaveDirectory
            preferenceScreen.getPreference(1).setDefaultValue(VideoSettings.videoEncoder)
            preferenceScreen.getPreference(2).setDefaultValue(VideoSettings.videoBitrate)
            preferenceScreen.getPreference(3).setDefaultValue(VideoSettings.videoFps)
            preferenceScreen.getPreference(4).setDefaultValue(VideoSettings.videoOutput)
        }
        //Updates texts and default values of preferences when they are changed(in onSharedPreferenceChanged method).
        videoSettingsModifier.getUpdatedData.observe(this) {
            when(it.dataType) {
                Constants.Video.DataTypes.VIDEO_OUTPUT_DIRECTORY_DATA_TYPE -> {
                    preferenceScreen.getPreference(0).summary = "Current Directory: ${it.data}"
                }
                Constants.Video.DataTypes.VIDEO_ENCODER_DATA_TYPE -> {
                    (preferenceScreen.getPreference(1) as ListPreference).value = it.data.toString()
                }
                Constants.Video.DataTypes.VIDEO_BITRATE_DATA_TYPE -> {
                    (preferenceScreen.getPreference(2) as ListPreference).value = it.data.toString()
                }
                Constants.Video.DataTypes.VIDEO_FPS_DATA_TYPE -> {
                    (preferenceScreen.getPreference(3) as ListPreference).value = it.data.toString()
                }
                Constants.Video.DataTypes.VIDEO_OUTPUT_FORMAT_DATA_TYPE -> {
                    (preferenceScreen.getPreference(4) as ListPreference).value = it.data.toString()
                }
            }
        }
    }

    private fun initPreferencesListeners() {
        outputDirectoryPref.setOnPreferenceClickListener {
            ActivityNavigationManager.navigateToDirectorySelector(activityResultLauncher)
            true
        }
    }

    //Register activity result launcher that opens folder selector that is saved as output folder for video files.
    private fun initOutputDirectorySelectActivityLauncher() {
        activityResultCallback = ActivityResultCallback {
            if(it.resultCode == Activity.RESULT_OK) {
                videoSettingsModifier.setVideoDirectoryFromUri(
                    DocumentFile.fromTreeUri(this@SettingsFragment.requireContext(), it.data?.data!!)?.uri!!
                )
            }
        }
        activityResultContract = ActivityResultContracts.StartActivityForResult()
        activityResultLauncher = registerForActivityResult(activityResultContract, activityResultCallback)
    }

    //Changing video settings based on user input from preferences.
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val value: Int = sharedPreferences?.getString(key, "-1")?.toInt()!!
        when(key) {
            "video_encoder_pref" -> {
                videoSettingsModifier.setVideoEncoder(value)
            }
            "video_fps_pref" -> {
                videoSettingsModifier.setVideoFps(value)
            }
            "video_bitrate_pref" -> {
                videoSettingsModifier.setVideoBitrate(value)
            }
            "video_output_format_pref" -> {
                videoSettingsModifier.setVideoOutputFormat(value)
            }
            "app_theme_pref" -> {
                if(value == 0) {
                    ThemeChanger.setDarkTheme()
                } else if(value == 1) {
                    ThemeChanger.setLightTheme()
                }
            }
        }
    }


}