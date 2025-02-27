package bee.corp.kbcorder.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat.startActivity
import bee.corp.kbcorder.view.activities.MainActivity
import java.util.Locale

class LanguageChanger {
    companion object {
        fun setLocale(lang: String?, context: Context, activity: Activity) {
            val myLocale = Locale(lang)
            val res: Resources = context.getResources()
            val dm: DisplayMetrics = res.getDisplayMetrics()
            val conf: Configuration = res.getConfiguration()
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
            val refresh: Intent = Intent(activity, MainActivity::class.java)
            activity.finish()
            activity.startActivity(refresh)
        }
    }
}