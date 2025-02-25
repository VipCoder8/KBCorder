package bee.corp.kbcorder.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import bee.corp.kbcorder.R
import bee.corp.kbcorder.databinding.RecordingWindowLayoutBinding
import bee.corp.kbcorder.databinding.RecordingWindowRemoverLayoutBinding
import bee.corp.kbcorder.utility.AnimationHelper
import bee.corp.kbcorder.utility.CollisionViews
import bee.corp.kbcorder.utility.ViewExpanding
import bee.corp.kbcorder.utility.ViewPositionRetreiver
import bee.corp.kbcorder.utility.androidtweaks.AnimatorListener
import bee.corp.kbcorder.viewmodel.RecordingWindowViewModel
import bee.corp.kbcorder.viewmodel.ScreenRecorder

class RecordingWindow(v: ViewModelStoreOwner, c: Context) : BasicRecordingWindow(c) {
    private var context: Context = c
    private var viewModelStoreOwner = v

    private var bindingRecording: RecordingWindowLayoutBinding =
        RecordingWindowLayoutBinding.inflate(LayoutInflater.from(c))

    private var bindingRemover: RecordingWindowRemoverLayoutBinding =
        RecordingWindowRemoverLayoutBinding.inflate(LayoutInflater.from(c))

    private val windowManager: WindowManager = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private lateinit var bindingRecordingParams: WindowManager.LayoutParams
    private lateinit var bindingRemoverParams: WindowManager.LayoutParams

    private lateinit var screenRecorder: ScreenRecorder
    private lateinit var recordingWindowVM: RecordingWindowViewModel

    private lateinit var animationMoveInListener: Animator.AnimatorListener
    private lateinit var animationMoveOutListener: Animator.AnimatorListener
    private lateinit var animationHelper: AnimationHelper

    init {
        initWindowManagerParameters()
        initViewModels()
        observeViewModels()
        initAnimationHelper()
        initListeners()
    }

    private fun initWindowManagerParameters() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            bindingRecordingParams = WindowManager.LayoutParams(
                context.resources.getDimension(R.dimen.recording_window_layout_width).toInt(),
                context.resources.getDimension(R.dimen.recording_window_layout_height).toInt(),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            bindingRemoverParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        } else {
            bindingRecordingParams = WindowManager.LayoutParams(
                context.resources.getDimension(R.dimen.recording_window_layout_width).toInt(),
                context.resources.getDimension(R.dimen.recording_window_layout_height).toInt(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            bindingRemoverParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        }
        bindingRecordingParams.gravity = Gravity.TOP or Gravity.START
        bindingRemoverParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        bindingRemoverParams.y = 64
    }

    fun passScreenRecorderAndStartObservingVMs(sc: ScreenRecorder) {
        screenRecorder = sc
        observeScreenRecorderViewModel()
    }

    private fun initViewModels() {
        recordingWindowVM = ViewModelProvider(viewModelStoreOwner)[RecordingWindowViewModel::class.java]
    }

    private fun observeViewModels() {
        recordingWindowVM.onStartStopControlButtonStateChange.observeForever {
            updateStartStopRecordingStateButtonImage(it)
        }
        recordingWindowVM.onPauseControlButtonStateChange.observeForever {
            updatePauseRecordingStateButtonImage(it)
        }
    }

    private fun initAnimationHelper() {
        animationHelper = AnimationHelper()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        animationMoveInListener = object: AnimatorListener() {
            override fun onAnimationStart(animation: Animator) {
                updateButtonsOnMoveInStart()
            }
        }
        animationMoveOutListener = object: AnimatorListener() {
            override fun onAnimationEnd(animation: Animator) {
                updateButtonsOnMoveOutEnd()
            }
        }
        addRecordingCameraButtonEvents(object: RecordingCameraEvents {
            override fun onMove(event: MotionEvent) {
                updateControllerOnRemoverCollision(event.rawX, event.rawY)
                if(!isControllerCaptured) {
                    bindingRemover.root.visibility = View.VISIBLE
                    bindingRecordingParams.x = getCameraMoveX(event.rawX)
                    bindingRecordingParams.y = getCameraMoveY(event.rawY)
                    windowManager.updateViewLayout(bindingRecording.root, bindingRecordingParams)
                }
            }

            override fun onClick() {
                if(bindingRecording.startStopRecordingButton.visibility == View.INVISIBLE) {
                    moveInButtons()
                } else {
                    moveOutButtons()
                }
            }

            override fun onDown(event: MotionEvent) {
                setControllerCameraPositions(bindingRecordingParams.x.toFloat(), bindingRecordingParams.y.toFloat(), event.rawX, event.rawY)
            }

            override fun onRelease(event: MotionEvent) {
                if(!isControllerCaptured) {
                    bindingRemover.root.visibility = View.INVISIBLE
                } else {
                    screenRecorder.removeRecording()
                    windowManager.removeView(bindingRecording.root)
                    windowManager.removeView(bindingRemover.root)
                }
            }

        }, bindingRecording.controllerCamera)

        bindingRecording.startStopRecordingButton.setOnClickListener {
            if(!screenRecorder.isRecording()) {
                screenRecorder.startRecording()
            } else {
                screenRecorder.stopRecording()
            }
        }
        bindingRecording.pauseRecordingButton.setOnClickListener {
            if(screenRecorder.isRecording()) {
                screenRecorder.pauseRecording()
            } else if(screenRecorder.isPaused()) {
                screenRecorder.startRecording()
            }
        }
    }

    private fun updateControllerOnRemoverCollision(touchX: Float, touchY: Float) {
        if(CollisionViews.isColliding(CollisionViews.getViewBounds(bindingRecording.controllerCamera),
                ViewExpanding.expandView(bindingRemover.recordingRemover, 50, 50, 50, 50))) {
            bindingRecordingParams.x = ViewPositionRetreiver.getX(bindingRemover.root) + bindingRecording.controllerCamera.width/2
            bindingRecordingParams.y = ViewPositionRetreiver.getY(bindingRemover.root) - bindingRemover.recordingRemover.height + bindingRecording.controllerCamera.height/2
            isControllerCaptured = true
        } else {
            isControllerCaptured = false
        }
    }

    private fun observeScreenRecorderViewModel() {
        screenRecorder.getVideoRecordingState.observeForever {
            recordingWindowVM.updateState(it)
        }
    }

    private fun updateStartStopRecordingStateButtonImage(it: Int) {
        bindingRecording.startStopRecordingButton.setImageResource(it)
    }
    private fun updatePauseRecordingStateButtonImage(it: Int) {
        bindingRecording.pauseRecordingButton.setImageResource(it)
    }

    //Moving animation that shows buttons up.
    private fun moveInButtons() {
        animationHelper.startMoveAnimation(bindingRecording.startStopRecordingButton,
            widgetsMoveInToX,
            startStopWidgetMoveInToY,
            widgetsMoveDuration,
            animationMoveInListener)

        animationHelper.startMoveAnimation(bindingRecording.pauseRecordingButton,
            widgetsMoveInToX,
            pauseWidgetMoveInToY,
            widgetsMoveDuration,
            animationMoveInListener)
    }

    //Moving animation that hides buttons.
    private fun moveOutButtons() {
        animationHelper.startMoveAnimation(bindingRecording.startStopRecordingButton,
            widgetsMoveOutToX,
            startStopWidgetMoveOutToY,
            widgetsMoveDuration,
            animationMoveOutListener)

        animationHelper.startMoveAnimation(bindingRecording.pauseRecordingButton,
            widgetsMoveOutToX,
            pauseWidgetMoveOutToY,
            widgetsMoveDuration,
            animationMoveOutListener)
    }

    //Updating visibility on moving animations end - start.
    private fun updateButtonsOnMoveInStart() {
        bindingRecording.startStopRecordingButton.visibility = View.VISIBLE
        bindingRecording.pauseRecordingButton.visibility = View.VISIBLE
    }

    private fun updateButtonsOnMoveOutEnd() {
        bindingRecording.startStopRecordingButton.visibility = View.INVISIBLE
        bindingRecording.pauseRecordingButton.visibility = View.INVISIBLE
    }
    //Updating visibility on moving animations end - end.

    //Adding the view onto phone screen.
    fun open() {
        try {
            windowManager.addView(bindingRemover.root, bindingRemoverParams)
            windowManager.addView(bindingRecording.root, bindingRecordingParams)
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

}