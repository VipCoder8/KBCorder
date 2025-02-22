package bee.corp.kbcorder.view.video

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VideoItemDecoration(p: Int) : RecyclerView.ItemDecoration() {
    val padding: Int = p
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = padding
    }
}