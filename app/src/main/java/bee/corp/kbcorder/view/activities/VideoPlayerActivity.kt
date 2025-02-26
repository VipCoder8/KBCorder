package bee.corp.kbcorder.view.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import bee.corp.kbcorder.databinding.ActivityVideoPlayerBinding
import bee.corp.kbcorder.utility.Constants
import bee.corp.kbcorder.utility.video.VideoDataRetriever

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding

    private lateinit var exoPlayerBuilder: ExoPlayer.Builder
    private lateinit var exoPlayer: ExoPlayer

    private lateinit var videoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews(LayoutInflater.from(this))
        setContentView(binding.root)
        initListeners()
        extractStringExtras()
        initVideoPlayer(videoPath)
        adjustOrientation(VideoDataRetriever.getVideoHeight(videoPath), VideoDataRetriever.getVideoWidth(videoPath))
    }

    private fun extractStringExtras() {
        videoPath = intent.getStringExtra(Constants.Intent.ExtrasNames.VIDEO_PLAYER_ACTIVITY_VIDEO_TAB_DATA_PATH_EXTRA_NAME)!!
    }

    private fun initViews(inflater: LayoutInflater) {
        binding = ActivityVideoPlayerBinding.inflate(inflater)
    }

    private fun initVideoPlayer(path: String) {
        exoPlayerBuilder = ExoPlayer.Builder(this)
        exoPlayer = exoPlayerBuilder.build()
        exoPlayer.setMediaItem(MediaItem.fromUri(path))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        binding.videoPlayerView.player = exoPlayer
    }

    private fun initListeners() {

    }

    private fun adjustOrientation(height: Int, width: Int) {
        //If height or width are less than 0, it means that file is invalid
        //as MediaMetadataRetriever#setDataSource method in VideoDataRetriever#getVideoWidth
        //and/or VideoDataRetriever#getVideoHeight methods throws exception,
        //so we have to close the activity and notify user about this error.
        if(height < 0 || width < 0) {
            Toast.makeText(this, Constants.Errors.VideoExceptionTexts.COULDNT_OPEN_INVALID_VIDEO_FILE_ERROR, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        //Change screen orientation based on width and height of video file.
        requestedOrientation = if(height > width) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

}