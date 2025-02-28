package bee.corp.kbcorder.utility.video.filemanagement

import java.io.File

class FilesManipulation {
    fun createFile(path: String) {
        File(path).createNewFile()
    }
    fun writeToFile(path: String, data: String) {
        File(path).writeText(data)
    }
    fun getData(path: String) : String {
        return File(path).readText()
    }
}