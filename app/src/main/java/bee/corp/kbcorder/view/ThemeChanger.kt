package bee.corp.kbcorder.view

import androidx.appcompat.app.AppCompatDelegate

class ThemeChanger {
    companion object {
        fun setDarkTheme() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        fun setLightTheme() {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}