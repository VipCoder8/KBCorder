package bee.corp.kbcorder.utility

import android.graphics.Rect
import android.view.View

class ViewExpanding {
    companion object {
        fun expandView(view: View, top: Int, left: Int, bottom: Int, right: Int) : Rect {
            val rect: Rect = getViewBounds(view)
            rect.top -= top
            rect.left -= left
            rect.bottom += bottom
            rect.right += right
            return rect
        }

        private fun getViewBounds(view: View): Rect {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val left = location[0]
            val top = location[1]
            val right = left + view.width
            val bottom = top + view.height
            return Rect(left, top, right, bottom)
        }
    }
}