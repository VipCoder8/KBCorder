package bee.corp.kbcorder.utility

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import bee.corp.kbcorder.model.VideoTabData
import bee.corp.kbcorder.view.activities.VideoPlayerActivity

class ActivityNavigationManager {
    companion object {
        fun navigateToDirectorySelector(arl: ActivityResultLauncher<Intent>) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            arl.launch(intent)
        }
        fun navigateToVideoPlayer(a: Activity, video: VideoTabData) {
            val intent = Intent(a, VideoPlayerActivity::class.java)
            intent.putExtra(Constants.Intent.ExtrasNames.VIDEO_PLAYER_ACTIVITY_VIDEO_TAB_DATA_PATH_EXTRA_NAME,
                video.filePath)
            intent.putExtra(Constants.Intent.ExtrasNames.VIDEO_PLAYER_ACTIVITY_VIDEO_TAB_DATA_TITLE_EXTRA_NAME,
                video.title)
            a.startActivity(intent)
        }
    }
}