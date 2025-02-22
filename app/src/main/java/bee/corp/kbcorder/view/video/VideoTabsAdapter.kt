package bee.corp.kbcorder.view.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbcorder.databinding.VideoTabLayoutBinding
import bee.corp.kbcorder.model.VideoTabData

class VideoTabsAdapter(arr: ArrayList<VideoTabData>) : RecyclerView.Adapter<VideoTab>() {
    private var videosList = arr
    private var originalVideosList = arr

    private var videoViewClickListener: OnVideoTabViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoTab {
        return VideoTab(VideoTabLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return videosList.size
    }

    override fun onBindViewHolder(holder: VideoTab, position: Int) {
        holder.setTitle(videosList[position].title)
        holder.setPreview(videosList[position].preview)
        if(this.videoViewClickListener != null) {
            holder.setClickListener(object: OnVideoTabViewBasicClickListener {
                override fun onClick(position: Int, viewType: Int, view: View) {
                    this@VideoTabsAdapter.videoViewClickListener?.onClick(position, videosList[position],
                        viewType, view)
                }
            })
        }
    }

    fun setVideoClickListener(cl: OnVideoTabViewClickListener) {
        this.videoViewClickListener = cl
    }

    fun filterVideos(filteredVideos: ArrayList<VideoTabData>) {
        videosList = filteredVideos
        notifyDataSetChanged()
    }

    fun revertFilter() {
        videosList = originalVideosList
        notifyDataSetChanged()
    }

    interface OnVideoTabViewBasicClickListener {
        fun onClick(position: Int, viewType: Int, view: View)
    }

}