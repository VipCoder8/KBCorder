package bee.corp.kbcorder.view.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import bee.corp.kbcorder.databinding.EditDialogLayoutBinding

class VideoFragmentsDialogs(private val activity: Activity) {
    private lateinit var videoEditDialogPositiveButtonClickListener: DialogInterface.OnClickListener

    private lateinit var videoDeleteDialogPositiveButtonClickListener: DialogInterface.OnClickListener

    lateinit var videoEditDialogBinding: EditDialogLayoutBinding
    private lateinit var videoEditDialogCreator: VideoEditDialogCreator
    lateinit var videoEditDialog: AlertDialog

    private lateinit var videoDeleteDialogCreator: VideoDeleteDialogCreator
    lateinit var videoDeleteDialog: AlertDialog

    lateinit var videoInfoDialogCreator: VideoInfoDialogCreator
    lateinit var videoInfoDialog: AlertDialog

    fun initDialogCreators() {
        videoEditDialogCreator = VideoEditDialogCreator(activity)
        videoDeleteDialogCreator = VideoDeleteDialogCreator(activity)
        videoInfoDialogCreator = VideoInfoDialogCreator(activity)
    }

    fun initDialogs(inflater: LayoutInflater,
                    onDeleteDialogPositiveButtonClick: OnDeleteDialogPositiveButtonClick,
                    onEditDialogPositiveButtonClick: OnEditDialogPositiveButtonClick) {
        initDialogsListeners(onDeleteDialogPositiveButtonClick, onEditDialogPositiveButtonClick)
        initVideoEditDialog(inflater)
        initVideoDeleteDialog()
        initVideoInfoDialog()
    }

    private fun initDialogsListeners(onDeleteDialogPositiveButtonClick: OnDeleteDialogPositiveButtonClick,
                                     onEditDialogPositiveButtonClick: OnEditDialogPositiveButtonClick) {
        initVideoEditDialogListeners(onEditDialogPositiveButtonClick)
        initVideoDeleteDialogListeners(onDeleteDialogPositiveButtonClick)
    }

    private fun initVideoEditDialog(inflater: LayoutInflater) {
        videoEditDialogBinding = EditDialogLayoutBinding.inflate(inflater)
        videoEditDialogCreator.setView(videoEditDialogBinding.root)
        videoEditDialogCreator.setPositiveButton(videoEditDialogPositiveButtonClickListener)
        videoEditDialog = videoEditDialogCreator.buildDialog()
    }

    private fun initVideoDeleteDialog() {
        videoDeleteDialogCreator.setPositiveButton(videoDeleteDialogPositiveButtonClickListener)
        videoDeleteDialog = videoDeleteDialogCreator.buildDialog()
    }

    private fun initVideoInfoDialog() {
        videoInfoDialog = videoInfoDialogCreator.buildDialog()
    }

    private fun initVideoEditDialogListeners(onEditDialogPositiveButtonClick: OnEditDialogPositiveButtonClick) {
        //Editing video title(that is applied in recycler view and files) when positive button of edit dialog is pressed.
        videoEditDialogPositiveButtonClickListener = DialogInterface.OnClickListener { _, _ ->
            onEditDialogPositiveButtonClick.onClick()
        }
    }

    private fun initVideoDeleteDialogListeners(onDeleteDialogPositiveButtonClick: OnDeleteDialogPositiveButtonClick) {
        videoDeleteDialogPositiveButtonClickListener = DialogInterface.OnClickListener { dialog, _ ->
            onDeleteDialogPositiveButtonClick.onClick()
            dialog.dismiss()
        }
    }
    interface OnEditDialogPositiveButtonClick {
        fun onClick()
    }
    interface OnDeleteDialogPositiveButtonClick {
        fun onClick()
    }
}