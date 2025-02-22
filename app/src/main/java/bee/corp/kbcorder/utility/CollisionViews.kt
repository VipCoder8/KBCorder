package bee.corp.kbcorder.utility

import android.graphics.Rect
import android.view.View

class CollisionViews {
    companion object {
        fun isColliding(view1: View, view2: View) : Boolean {
            return getViewBounds(view1).intersect(getViewBounds(view2))
        }

        fun isColliding(view1: Rect, view2: Rect) : Boolean {
            return view1.intersect(view2)
        }

        fun getViewBounds(view: View): Rect {
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