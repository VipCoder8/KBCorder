package bee.corp.kbcorder.utility

import android.view.View

class ViewPositionRetreiver {
    companion object {
        fun getX(view: View) : Int {
            val targetLocation = IntArray(2)
            view.getLocationOnScreen(targetLocation)
            val targetX = targetLocation[0]
            return targetX
        }
        fun getY(view: View) : Int {
            val targetLocation = IntArray(2)
            view.getLocationOnScreen(targetLocation)
            val targetY = targetLocation[1]
            return targetY
        }
    }
}