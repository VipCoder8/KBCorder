package bee.corp.kbcorder.utility

import android.animation.Animator
import android.view.View

class AnimationHelper {
    fun startMoveAnimation(view: View, toX: Float, toY: Float, duration: Long,
                           listener: Animator.AnimatorListener? = null) {
        if(listener != null) {
            view.animate().translationX(toX).translationY(toY)
                .setDuration(duration).setListener(listener).start()
        } else {
            view.animate().translationX(toX).translationY(toY)
                .setDuration(duration).start()
        }
    }
}