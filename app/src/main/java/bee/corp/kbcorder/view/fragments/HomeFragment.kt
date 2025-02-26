package bee.corp.kbcorder.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import bee.corp.kbcorder.databinding.FragmentHomeBinding
import bee.corp.kbcorder.viewmodel.RecordingWindowStarter
import bee.corp.kbcorder.viewmodel.ScreenRecorder

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recordingRequestActivityResultLauncher: ActivityResultLauncher<Intent>

    private val recordingWindowViewModel: RecordingWindowStarter by activityViewModels()
    private lateinit var screenRecorderViewModel: ScreenRecorder
    private var wasRecordingRequested: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews(inflater)
        initRecordingRequestActivityResultLauncher()
        initViewModels()
        observeViewModels()
        initListeners()
        return binding.root
    }

    private fun initViews(inflater: LayoutInflater) {
        binding = FragmentHomeBinding.inflate(inflater)
    }

    private fun initRecordingRequestActivityResultLauncher() {
        //Registering result activity launcher that will request for screen capture intent from user and pass result to ScreenRecorder class.
        recordingRequestActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode != Activity.RESULT_CANCELED) {
                screenRecorderViewModel.initializeVideoRecorderProjection(it)
                recordingWindowViewModel.open(screenRecorderViewModel)
            } else {
                wasRecordingRequested = false
            }
        }
    }

    private fun initViewModels() {
        screenRecorderViewModel = ViewModelProvider(requireActivity())[ScreenRecorder::class.java]
    }

    private fun observeViewModels() {
        screenRecorderViewModel.shouldStartActivityForResult.observe(viewLifecycleOwner) {
            if(!wasRecordingRequested) {
                recordingRequestActivityResultLauncher.launch(it)
                wasRecordingRequested = true
            }
        }
        screenRecorderViewModel.isRecordingRemoved.observe(viewLifecycleOwner) {
            wasRecordingRequested = false
        }
    }

    private fun initListeners() {
        binding.recordingRequestButton.setOnClickListener {
            screenRecorderViewModel.requestRecordingStart()
        }
    }

}