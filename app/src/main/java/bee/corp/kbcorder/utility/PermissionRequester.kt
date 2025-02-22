package bee.corp.kbcorder.utility
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PermissionRequester {
    fun requestPermissions(perms: Array<String>, a: AppCompatActivity,
                           activityResultCallback: ActivityResultCallback<Map<String, Boolean>>,
                           activityResultContract: ActivityResultContracts.RequestMultiplePermissions) {
        val activityResultLauncher: ActivityResultLauncher<Array<String>> =
            a.registerForActivityResult(activityResultContract, activityResultCallback)
        if(!isPermissionGranted(perms, a)) {
            activityResultLauncher.launch(perms)
        }
    }

    //Requesting draw over other apps permission for RecordingWindow to work properly.
    fun requestDrawOverOtherAppsPermission(
        a: AppCompatActivity,
        activityResultCallback: ActivityResultCallback<ActivityResult>,
        activityResultContract: ActivityResultContract<Intent, ActivityResult>) {
        val activityResultLauncher: ActivityResultLauncher<Intent> =
            a.registerForActivityResult(activityResultContract, activityResultCallback)

        if (!Settings.canDrawOverlays(a)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + a.packageName)
            )
            activityResultLauncher.launch(intent)
        }
    }

    private fun isPermissionGranted(perms: Array<String>, c: Context) : Boolean {
        for(perm: String in perms) {
            return c.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

}