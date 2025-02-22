package bee.corp.kbcorder.utility.video

import bee.corp.kbcorder.utility.Constants

class VideoSettings {
    companion object {
        var videoFps: Int = Constants.Video.VideoSettings.DEFAULT_VIDEO_FPS
        var videoBitrate: Int = Constants.Video.VideoSettings.DEFAULT_VIDEO_BITRATE
        var videoSource: Int = Constants.Video.VideoSettings.DEFAULT_VIDEO_SOURCE
        var videoOutput: Int = Constants.Video.VideoSettings.DEFAULT_VIDEO_OUTPUT
        var videoEncoder: Int = Constants.Video.VideoSettings.DEFAULT_VIDEO_ENCODER
        var videoSaveDirectory: String = "null"
    }
}