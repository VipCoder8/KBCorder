package bee.corp.kbcorder.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.widget.ImageButton
import bee.corp.kbcorder.R

open class BasicRecordingWindow(c: Context) {

    private var controllerCameraInitialX = 0f
    private var controllerCameraInitialY = 0f
    private var controllerCameraInitialTouchX = 0f
    private var controllerCameraInitialTouchY = 0f

    protected var widgetsMoveInToX: Float = 0f
    protected var widgetsMoveOutToX: Float = 0f

    protected var startStopWidgetMoveInToY: Float = 0f
    protected var startStopWidgetMoveOutToY: Float = 0f

    protected var pauseWidgetMoveInToY: Float = 0f
    protected var pauseWidgetMoveOutToY: Float = 0f

    protected var widgetsMoveDuration: Long = 0L

    protected var isControllerCaptured: Boolean = false

    init {
        startStopWidgetMoveInToY = c.resources.getInteger(R.integer.recording_window_start_stop_widget_move_in_to_y).toFloat()
        startStopWidgetMoveOutToY = c.resources.getInteger(R.integer.recording_window_start_stop_widget_move_out_to_y).toFloat()
        widgetsMoveDuration = c.resources.getInteger(R.integer.recording_window_widget_move_duration).toLong()
        widgetsMoveOutToX = c.resources.getInteger(R.integer.recording_window_widgets_move_out_to_x).toFloat()
        widgetsMoveInToX = c.resources.getInteger(R.integer.recording_window_widgets_move_in_to_x).toFloat()
        pauseWidgetMoveInToY = c.resources.getInteger(R.integer.recording_window_pause_widget_move_in_to_y).toFloat()
        pauseWidgetMoveOutToY = c.resources.getInteger(R.integer.recording_window_pause_widget_move_out_to_y).toFloat()
    }

    protected fun setControllerCameraPositions(initialX: Float, initialY: Float, initialTouchX: Float, initialTouchY: Float) {
        controllerCameraInitialX = initialX
        controllerCameraInitialY = initialY
        controllerCameraInitialTouchX = initialTouchX
        controllerCameraInitialTouchY = initialTouchY
    }
    protected fun getCameraMoveX(rawX: Float) : Int {
        return (controllerCameraInitialX + (rawX - controllerCameraInitialTouchX).toInt()).toInt()
    }
    protected fun getCameraMoveY(rawY: Float) : Int {
        return (controllerCameraInitialY + (rawY - controllerCameraInitialTouchY).toInt()).toInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    protected fun addRecordingCameraButtonEvents(cameraEvent: RecordingCameraEvents, button: ImageButton) {
        button.setOnTouchListener{ _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cameraEvent.onDown(event)
                }
                MotionEvent.ACTION_MOVE -> {
                    cameraEvent.onMove(event)
                }
                MotionEvent.ACTION_UP -> {
                    cameraEvent.onRelease(event)
                }
            }
            false
        }
        button.setOnClickListener {
            cameraEvent.onClick()
        }
    }

    protected interface RecordingCameraEvents {
        fun onMove(event: MotionEvent)
        fun onClick()
        fun onDown(event: MotionEvent)
        fun onRelease(event: MotionEvent)
    }

}