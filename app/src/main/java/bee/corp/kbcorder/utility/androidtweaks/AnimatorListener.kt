package bee.corp.kbcorder.utility.androidtweaks

import android.animation.Animator

abstract class AnimatorListener : Animator.AnimatorListener {
    //Left empty so implementation is not required for all methods in other classes for cleaner code.
    override fun onAnimationStart(animation: Animator) {

    }
    override fun onAnimationEnd(animation: Animator) {

    }
    override fun onAnimationCancel(animation: Animator) {

    }
    override fun onAnimationRepeat(animation: Animator) {

    }
}