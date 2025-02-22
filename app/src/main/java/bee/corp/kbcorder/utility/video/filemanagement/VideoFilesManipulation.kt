package bee.corp.kbcorder.utility.video.filemanagement

import android.icu.util.Calendar
import bee.corp.kbcorder.model.VideoTabData
import bee.corp.kbcorder.utility.Parser
import bee.corp.kbcorder.utility.video.VideoSettings
import java.io.File

class VideoFilesManipulation {
    companion object {
        private val calendar: Calendar = Calendar.getInstance()
        fun deleteVideoFile(video: VideoTabData) : Boolean {
            try {
                return File(video.filePath).delete()
            } catch(e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        fun editVideoFileName(video: VideoTabData, parentPath: String, newTitle: String) : String {
            val oldVideoFile = File(video.filePath)
            val finalPath = generateVideoFilePathWithTitle(parentPath, newTitle, VideoSettings.videoOutput)
            val newVideoFile = File(finalPath)
            oldVideoFile.renameTo(newVideoFile)
            return finalPath
        }
        fun isValidVideoFile(file: File) : Boolean {
            return !file.isDirectory && doesEndWithValidExtension(file.name)
        }
        //Checking if file path ends with a valid video file extension.
        private fun doesEndWithValidExtension(file: String) : Boolean {
            return file.endsWith(".mp4") || file.endsWith(".avi")
                    || file.endsWith(".mkv") || file.endsWith(".webm")
                    || file.endsWith(".mov") || file.endsWith(".m4v")
                    || file.endsWith(".flv") || file.endsWith(".wmv")
        }
        //Generating video file path in such pattern: "save/path/video_title.file_extension".
        //File extension is based on index chosen by user or default if user didn't change it.
        private fun generateVideoFilePathWithTitle(parentPath: String, title: String, output: Int) : String {
            return "${parentPath}/${title}${
                Parser.getVideoFormatStringFromIndex(
                    output
                )
            }"
        }
        //Generating video file path in such pattern: "save/path/VID_date.file_extension".
        //Acts as default video file path.
        fun generateVideoFilePath(parentPath: String, output: Int) : String {
            return "${parentPath}/VID_${calendar[Calendar.MONTH]}_${calendar[Calendar.DAY_OF_MONTH]}_${calendar[Calendar.HOUR_OF_DAY]}_${calendar[Calendar.MINUTE]}_${calendar[Calendar.SECOND]}${
                Parser.getVideoFormatStringFromIndex(
                    output
                )
            }"
        }
    }
}