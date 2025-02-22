package bee.corp.kbcorder.utility.video

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import bee.corp.kbcorder.utility.Parser
import java.io.File

class VideoDataRetriever {
    companion object {
        private val mediaDataRetriever = MediaMetadataRetriever()
        fun getVideoPreview(path: String) : Bitmap? {
            try {
                mediaDataRetriever.setDataSource(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mediaDataRetriever.frameAtTime
        }
        fun getVideoDuration(path: String) : String {
            try {
                mediaDataRetriever.setDataSource(path)
                return Parser.parseLongToDuration(
                    mediaDataRetriever.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )!!.toLong()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return "null"
            }
        }
        fun getVideoFileSize(path: String) : String {
            val file = File(path)
            if((file.length() / (1024 * 1024)) == 0L) {
                return "${(file.length() / 1024)} KB"
            }
            return "${(file.length() / (1024 * 1024))} MB"
        }
        fun getVideoWidth(path: String) : Int {
            try {
                mediaDataRetriever.setDataSource(path)
                return mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!!.toInt()
            } catch(e: Exception) {
                e.printStackTrace()
                return -1
            }
        }
        fun getVideoHeight(path: String) : Int {
            try {
                mediaDataRetriever.setDataSource(path)
                return mediaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!!.toInt()
            } catch(e: Exception) {
                e.printStackTrace()
                return -1
            }
        }
    }
}