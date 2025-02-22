package bee.corp.kbcorder.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import bee.corp.kbcorder.R
import bee.corp.kbcorder.databinding.ActivityMainBinding
import bee.corp.kbcorder.utility.Ads
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.PermissionRequester
import bee.corp.kbcorder.utility.services.AppCloseForegroundService
import bee.corp.kbcorder.view.RecordingWindow
import bee.corp.kbcorder.view.fragments.HomeFragment
import bee.corp.kbcorder.view.fragments.SettingsFragment
import bee.corp.kbcorder.view.fragments.VideosFragment
import bee.corp.kbcorder.viewmodel.RecordingWindowStarter
import bee.corp.kbcorder.viewmodel.VideoSettingsModifier
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK

class MainActivity : AppCompatActivity() {

    private lateinit var recordingWindow: RecordingWindow
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var videosFragment: VideosFragment
    private lateinit var homeFragment: HomeFragment

    private lateinit var binding: ActivityMainBinding

    private lateinit var permissionRequester: PermissionRequester

    private lateinit var foregroundServiceIntent: Intent

    private lateinit var videoSettingsModifierViewModel: VideoSettingsModifier
    private val recordingWindowViewModel: RecordingWindowStarter by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coreSetup()
        initViews(LayoutInflater.from(this))
        setContentView(binding.root)
        initViewModels()
        observeViewModels()
        loadVideoSettings()
        initListeners()
    }

    //Method that should be called on the very start up of the program.
    private fun coreSetup() {
        requestPermissions()
        initServices()
        initAds()
        this.startService(foregroundServiceIntent)
    }

    private fun initServices() {
        foregroundServiceIntent = Intent(this, AppCloseForegroundService::class.java)
    }

    private fun initViewModels() {
        videoSettingsModifierViewModel = ViewModelProvider(this)[VideoSettingsModifier::class.java]
    }

    private fun loadVideoSettings() {
        videoSettingsModifierViewModel.readData()
    }

    private fun initAds() {
        StartAppSDK.init(this, "201294017", false)
        StartAppSDK.setUserConsent(this, "pas", System.currentTimeMillis(), true)
        StartAppAd.disableSplash()
        Ads.showBannerAd(this)
    }

    private fun observeViewModels() {
        recordingWindowViewModel.onOpen.observe(this) {
            recordingWindow.passScreenRecorderAndStartObservingVMs(it)
            recordingWindow.open()
        }
    }

    private fun initViews(inflater: LayoutInflater) {
        binding = ActivityMainBinding.inflate(inflater)
        recordingWindow = RecordingWindow(this, applicationContext)
        settingsFragment = SettingsFragment()
        videosFragment = VideosFragment()
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(binding.fragmentsContainer.id, homeFragment).commit()
    }

    private fun initListeners() {
        binding.bottomNavigationBar.setOnItemSelectedListener {
            switchFragments(it.itemId)
            true
        }
    }

    private fun switchFragments(itemId: Int) {
        when(itemId) {
            R.id.home_button -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentsContainer.id, homeFragment).commit()
            }
            R.id.videos_button -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentsContainer.id, videosFragment).commit()
            }
            R.id.settings_button -> {
                supportFragmentManager.beginTransaction().replace(binding.fragmentsContainer.id, settingsFragment).commit()
            }
        }
    }

    private fun requestPermissions() {
        permissionRequester = PermissionRequester()
        permissionRequester.requestPermissions(Constants.Core.PERMISSIONS, this, {

        }, ActivityResultContracts.RequestMultiplePermissions())
        permissionRequester.requestDrawOverOtherAppsPermission(this, {

        }, ActivityResultContracts.StartActivityForResult())

    }
}