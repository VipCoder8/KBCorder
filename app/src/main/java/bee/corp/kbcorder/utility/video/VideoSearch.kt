package bee.corp.kbcorder.utility.video

import bee.corp.kbcorder.model.VideoTabData

class VideoSearch {
    companion object {
        fun searchVideo(videos: ArrayList<VideoTabData>, target: String) : ArrayList<VideoTabData> {
            val output: ArrayList<VideoTabData> = ArrayList()
            for(video in videos) {
                if(video.title.contains(target)) {
                    output += video
                }
            }
            return output
        }
    }
}