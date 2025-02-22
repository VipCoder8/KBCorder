package bee.corp.kbcorder.view.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import bee.corp.kbcorder.utility.Constants

class VideoDeleteDialogCreator(c: Context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(c)

    init {
        this.setTitle(Constants.Views.DefaultValues.DEFAULT_VIDEO_DELETE_DIALOG_TITLE_TEXT)
        this.setMessage(Constants.Views.DefaultValues.DEFAULT_VIDEO_DELETE_DIALOG_MESSAGE_TEXT)
        this.setNegativeButton{dialog, _ -> dialog.dismiss()}
    }

    fun setTitle(text: String) {
        builder.setTitle(text)
    }
    fun setMessage(text: String) {
        builder.setMessage(text)
    }
    fun setNegativeButton(cl: DialogInterface.OnClickListener) {
        builder.setNegativeButton(Constants.Views.DefaultValues.DEFAULT_VIDEO_DELETE_DIALOG_NEGATIVE_BUTTON_TEXT, cl)
    }
    fun setPositiveButton(cl: DialogInterface.OnClickListener) {
        builder.setPositiveButton(Constants.Views.DefaultValues.DEFAULT_VIDEO_DELETE_DIALOG_POSITIVE_BUTTON_TEXT, cl)
    }

    fun buildDialog() : AlertDialog {
        return builder.create()
    }
}