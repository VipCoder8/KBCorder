package bee.corp.kbcorder.view.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import bee.corp.kbcorder.utility.Constants

class VideoEditDialogCreator(c: Context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(c)

    init {
        this.setTitle(Constants.Views.DefaultValues.DEFAULT_VIDEO_EDIT_DIALOG_TITLE_TEXT)
        this.setNegativeButton{dialog, _ -> dialog.dismiss()}
    }

    fun setTitle(text: String) {
        builder.setTitle(text)
    }
    fun setView(view: View) {
        builder.setView(view)
    }
    fun setNegativeButton(cl: DialogInterface.OnClickListener) {
        builder.setNegativeButton(Constants.Views.DefaultValues.DEFAULT_VIDEO_EDIT_DIALOG_NEGATIVE_BUTTON_TEXT, cl)
    }
    fun setPositiveButton(cl: DialogInterface.OnClickListener) {
        builder.setPositiveButton(Constants.Views.DefaultValues.DEFAULT_VIDEO_EDIT_DIALOG_POSITIVE_BUTTON_TEXT, cl)
    }

    fun buildDialog() : AlertDialog {
        return builder.create()
    }

}