package bee.corp.kbcorder.view.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import bee.corp.kbcorder.utility.Constants

class VideoInfoDialogCreator(c: Context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(c)

    init {
        this.setTitle(Constants.Views.DefaultValues.DEFAULT_VIDEO_INFO_DIALOG_TITLE_TEXT)
        this.setPositiveButton{dialog, _ -> dialog.dismiss()}
        this.setNegativeButton{dialog, _ -> dialog.dismiss()}
    }

    fun setTitle(text: String) {
        builder.setTitle(text)
    }
    fun setMessage(title: String, fileSize: String, videoDuration: String) {
        builder.setMessage(
            Constants.Views.DefaultValues.DEFAULT_VIDEO_INFO_DIALOG_MESSAGE_NAME_TEXT +
                title + "\n" +
                Constants.Views.DefaultValues.DEFAULT_VIDEO_INFO_DIALOG_MESSAGE_SIZE_TEXT +
                fileSize
                +"\n" +
                Constants.Views.DefaultValues.DEFAULT_VIDEO_INFO_DIALOG_MESSAGE_DURATION_TEXT +
                videoDuration)
    }
    fun setNegativeButton(cl: DialogInterface.OnClickListener) {
        builder.setNegativeButton(Constants.Views.DefaultValues.DEFAULT_VIDEO_EDIT_DIALOG_NEGATIVE_BUTTON_TEXT, cl)
    }
    fun setPositiveButton(cl: DialogInterface.OnClickListener) {
        builder.setPositiveButton(Constants.Views.DefaultValues.DEFAULT_VIDEO_INFO_DIALOG_POSITIVE_BUTTON_TEXT, cl)
    }

    fun buildDialog() : AlertDialog {
        return builder.create()
    }
}