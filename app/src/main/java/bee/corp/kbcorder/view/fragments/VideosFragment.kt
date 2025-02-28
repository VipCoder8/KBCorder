package bee.corp.kbcorder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bee.corp.kbcorder.databinding.FragmentVideosBinding
import bee.corp.kbcorder.model.VideoTabData
import bee.corp.kbcorder.utility.ActivityNavigationManager
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.video.VideoDataRetriever
import bee.corp.kbcorder.view.dialogs.VideoFragmentsDialogs
import bee.corp.kbcorder.view.video.OnVideoTabViewClickListener
import bee.corp.kbcorder.view.video.VideoItemDecoration
import bee.corp.kbcorder.view.video.VideoTabsAdapter
import bee.corp.kbcorder.viewmodel.VideoLoader
import bee.corp.kbcorder.viewmodel.VideosManagement

class VideosFragment : Fragment() {
    private lateinit var binding: FragmentVideosBinding

    private lateinit var videoLoaderViewModel: VideoLoader
    private lateinit var videosManagementViewModel: VideosManagement

    private lateinit var videosAdapter: VideoTabsAdapter

    private lateinit var videoClickListener: OnVideoTabViewClickListener

    private lateinit var dialogs: VideoFragmentsDialogs
    private lateinit var onDeleteDialogPositiveButtonClick: VideoFragmentsDialogs.OnDeleteDialogPositiveButtonClick
    private lateinit var onEditDialogPositiveButtonClick: VideoFragmentsDialogs.OnEditDialogPositiveButtonClick

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews(inflater)
        initViewModels()
        initListeners()
        initDialogs(inflater)
        observeViewModels()
        return binding.root
    }
    private fun initViews(inflater: LayoutInflater) {
        binding = FragmentVideosBinding.inflate(inflater)
    }

    private fun initViewModels() {
        videoLoaderViewModel = ViewModelProvider(requireActivity())[VideoLoader::class.java]
        videosManagementViewModel = ViewModelProvider(requireActivity())[VideosManagement::class.java]
    }

    private fun initDialogs(inflater: LayoutInflater) {
        dialogs = VideoFragmentsDialogs(requireActivity())
        dialogs.initDialogCreators()
        dialogs.initDialogs(inflater, onDeleteDialogPositiveButtonClick, onEditDialogPositiveButtonClick)
    }

    private fun initListeners() {
        videoClickListener = object: OnVideoTabViewClickListener {
            override fun onClick(position: Int, data: VideoTabData, viewType: Int, view: View) {
                videosManagementViewModel.setCurrentVideoTab(position, data)
                showTypeBasedDialogs(viewType, data)
            }
        }
        onEditDialogPositiveButtonClick = object: VideoFragmentsDialogs.OnEditDialogPositiveButtonClick {
            override fun onClick() {
                editVideoTitle()
            }
        }
        onDeleteDialogPositiveButtonClick = object: VideoFragmentsDialogs.OnDeleteDialogPositiveButtonClick {
            override fun onClick() {
                videosManagementViewModel.deleteVideo(
                    videosManagementViewModel.currentVideoTabPosition,
                    videosManagementViewModel.currentVideoTab
                )
            }
        }
        binding.searchVideosView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return true }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == null) {
                    return true
                }
                if(newText.isEmpty()) {
                    videosAdapter.revertFilter()
                    return true
                }
                videosManagementViewModel.findVideos(newText)
                return true
            }
        })
    }

    private fun showTypeBasedDialogs(viewType: Int, data: VideoTabData) {
        when (viewType) {
            Constants.Views.VideoTabViewTypes.EDIT_BUTTON_VIEW_TYPE -> {
                dialogs.videoEditDialog.show()
            }
            Constants.Views.VideoTabViewTypes.DELETE_BUTTON_VIEW_TYPE -> {
                dialogs.videoDeleteDialog.show()
            }
            Constants.Views.VideoTabViewTypes.INFO_BUTTON_VIEW_TYPE -> {
                updateVideoInfoDialogMessageText()
                dialogs.videoInfoDialog.show()
            }
            Constants.Views.VideoTabViewTypes.ROOT_VIEW_TYPE -> {
                ActivityNavigationManager.navigateToVideoPlayer(requireActivity(), data)
            }
        }
    }

    private fun editVideoTitle() {
        videosManagementViewModel.editVideoTitle(dialogs.videoEditDialogBinding.newTitleView.text.toString(),
            videosManagementViewModel.currentVideoTab, videosManagementViewModel.currentVideoTabPosition)
    }

    private fun observeViewModels() {
        //Loading videos.
        videoLoaderViewModel.getLoadedVideos.observe(viewLifecycleOwner) {
            applyLoadedVideos(it)
        }
        //Modifying video items based on view model's video modifications results.
        videosManagementViewModel.getEditedVideo.observe(viewLifecycleOwner) {
            videosAdapter.notifyItemChanged(it)
        }
        videosManagementViewModel.getDeletedVideo.observe(viewLifecycleOwner) {
            videosAdapter.notifyItemRemoved(it)
        }
        videosManagementViewModel.getFoundVideos.observe(viewLifecycleOwner) {
            videosAdapter.filterVideos(it)
        }
    }

    private fun updateVideoInfoDialogMessageText() {
        dialogs.videoInfoDialogCreator.setMessage(
            videosManagementViewModel.currentVideoTab.title,
            VideoDataRetriever.getVideoFileSize(videosManagementViewModel.currentVideoTab.filePath),
            VideoDataRetriever.getVideoDuration(videosManagementViewModel.currentVideoTab.filePath))
        dialogs.videoInfoDialog = dialogs.videoInfoDialogCreator.buildDialog()

    }

    private fun applyLoadedVideos(arr: ArrayList<VideoTabData>) {
        videosManagementViewModel.setLoadedVideos(arr)

        videosAdapter = VideoTabsAdapter(arr)
        videosAdapter.setVideoClickListener(videoClickListener)
        binding.videosView.adapter = videosAdapter
        binding.videosView.addItemDecoration(
            VideoItemDecoration(Constants.Views.DefaultValues.DEFAULT_VIDEO_ITEM_DECORATION_PADDING))
    }

    override fun onStart() {
        super.onStart()
        videoLoaderViewModel.loadVideos()
    }
    override fun onStop() {
        super.onStop()
        videoLoaderViewModel.clearVideos()
    }
}