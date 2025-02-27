package bee.corp.kbcorder.view.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import bee.corp.kbcorder.R
import bee.corp.kbcorder.utility.Constants

class VideoDeleteDialogCreator(c: Context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(c)
    private val context: Context = c

    init {
        this.setTitle(context.getString(R.string.video_delete_dialog_title_text))
        this.setMessage(context.getString(R.string.video_delete_dialog_message_text))
        this.setNegativeButton{dialog, _ -> dialog.dismiss()}
    }

    fun setTitle(text: String) {
        builder.setTitle(text)
    }
    fun setMessage(text: String) {
        builder.setMessage(text)
    }
    fun setNegativeButton(cl: DialogInterface.OnClickListener) {
        builder.setNegativeButton(context.getString(R.string.video_delete_dialog_negative_button_text), cl)
    }
    fun setPositiveButton(cl: DialogInterface.OnClickListener) {
        builder.setPositiveButton(context.getString(R.string.video_delete_dialog_positive_button_text), cl)
    }

    fun buildDialog() : AlertDialog {
        return builder.create()
    }
}