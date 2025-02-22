package bee.corp.kbcorder.view.video

import android.view.View
import bee.corp.kbcorder.model.VideoTabData

interface OnVideoTabViewClickListener {
    fun onClick(position: Int, data: VideoTabData, viewType: Int, view: View)
}