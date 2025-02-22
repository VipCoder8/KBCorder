package bee.corp.kbcorder.utility

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract

class Parser {
    companion object {
        //Returning file extension based on index user selected from settings page.
        fun getVideoFormatStringFromIndex(index: Int) : String {
            return when(index) {
                MediaRecorder.OutputFormat.MPEG_4 -> {
                    Constants.Video.OutputFormats.MPEG_4_OUTPUT_FORMAT
                }
                MediaRecorder.OutputFormat.WEBM -> {
                    Constants.Video.OutputFormats.WEBM_OUTPUT_FORMAT
                }
                else -> {
                    Constants.Video.OutputFormats.NOT_LISTED_OUTPUT_FORMAT
                }
            }
        }
        //Converting Long data type time to String in such pattern: "00:00:00".
        fun parseLongToDuration(time: Long) : String {
            return String.format("%02d:%02d:%02d", time/3600000, time/60000, (time/1000)%60)
        }
        //Converting android path Uri to parsable for java path.
        fun getRealPathFromURI(context: Context?, uri: Uri?): String? {
            var filePath: String? = null
            if (DocumentsContract.isDocumentUri(context, uri)) {
                val documentId = DocumentsContract.getDocumentId(uri)
                val split = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    filePath = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }
            return filePath
        }
    }
}