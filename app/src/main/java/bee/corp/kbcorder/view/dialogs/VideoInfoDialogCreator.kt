package bee.corp.kbcorder.view.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import bee.corp.kbcorder.R
import bee.corp.kbcorder.utility.Constants

class VideoInfoDialogCreator(c: Context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(c)
    private val context: Context = c

    init {
        this.setTitle(context.getString(R.string.video_info_dialog_title_text))
        this.setPositiveButton{dialog, _ -> dialog.dismiss()}
    }

    fun setTitle(text: String) {
        builder.setTitle(text)
    }
    fun setMessage(title: String, fileSize: String, videoDuration: String) {
        builder.setMessage(
            context.getString(R.string.video_info_dialog_message_name_text) + " " +
                title + "\n" +
                context.getString(R.string.video_info_dialog_message_size_text) +" " +
                fileSize
                +"\n" +
                context.getString(R.string.video_info_dialog_message_duration_text) +" " +
                videoDuration)
    }
    fun setPositiveButton(cl: DialogInterface.OnClickListener) {
        builder.setPositiveButton(context.getString(R.string.video_info_dialog_positive_button_text), cl)
    }

    fun buildDialog() : AlertDialog {
        return builder.create()
    }
}