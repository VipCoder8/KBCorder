package bee.corp.kbcorder.view.video

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbcorder.R
import bee.corp.kbcorder.databinding.VideoTabLayoutBinding
import bee.corp.kbcorder.utility.Constants

@SuppressLint("ClickableViewAccessibility")
class VideoTab(b: VideoTabLayoutBinding) : RecyclerView.ViewHolder(b.root) {
    private val binding: VideoTabLayoutBinding = b

    private val scaleDownAnimation: Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.widgets_scale_down)
    private val scaleUpAnimation: Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.widgets_scale_up)

    private lateinit var viewClickListener: VideoTabsAdapter.OnVideoTabViewBasicClickListener

    init {
        this.binding.root.setOnTouchListener { v, event ->
            when(event.action) {
                //Applying animation based on touch actions - start.
                MotionEvent.ACTION_DOWN -> {
                    binding.root.startAnimation(scaleDownAnimation)
                }
                MotionEvent.ACTION_CANCEL -> {
                    binding.root.startAnimation(scaleUpAnimation)
                }
            }
            if(event.action == MotionEvent.ACTION_UP) {
                binding.root.startAnimation(scaleUpAnimation)
                viewClickListener.onClick(absoluteAdapterPosition,
                    Constants.Views.VideoTabViewTypes.ROOT_VIEW_TYPE, this.binding.root)
            }
            //Applying animation based on touch actions - end.
            true
        }
        //Invokes VideoTabsAdapter.OnVideoTabViewBasicClickListener's methods on VideoTab's buttons' presses.
        this.binding.videoInfoButton.setOnClickListener {
            viewClickListener.onClick(absoluteAdapterPosition,
                Constants.Views.VideoTabViewTypes.INFO_BUTTON_VIEW_TYPE, this.binding.videoInfoButton)
        }
        this.binding.editTitleButton.setOnClickListener {
            viewClickListener.onClick(absoluteAdapterPosition,
                Constants.Views.VideoTabViewTypes.EDIT_BUTTON_VIEW_TYPE, this.binding.editTitleButton)
        }
        this.binding.deleteVideoButton.setOnClickListener {
            viewClickListener.onClick(absoluteAdapterPosition,
                Constants.Views.VideoTabViewTypes.DELETE_BUTTON_VIEW_TYPE, this.binding.deleteVideoButton)
        }
    }

    fun setClickListener(cl: VideoTabsAdapter.OnVideoTabViewBasicClickListener) {
        this.viewClickListener = cl
    }

    fun setTitle(title: String) {
        binding.videoTitle.text = title
    }

    fun setPreview(bitmap: Bitmap) {
        binding.videoPreview.setImageBitmap(bitmap)
    }

}