package bee.corp.kbcorder.utility

import android.content.Context
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class Ads {
    companion object {
        fun showRewardedAd(context: Context) {
            val rewardedAd: StartAppAd = StartAppAd(context)
            rewardedAd.loadAd(object: AdEventListener {
                override fun onReceiveAd(p0: Ad) {
                    rewardedAd.showAd()
                }
                override fun onFailedToReceiveAd(p0: Ad?) {}
            })
        }
        fun showBannerAd(context: Context) {
            val bannerAd: StartAppAd = StartAppAd(context)
            bannerAd.loadAd(object: AdEventListener {
                override fun onReceiveAd(p0: Ad) {
                    bannerAd.showAd()
                }
                override fun onFailedToReceiveAd(p0: Ad?) {}
            })
        }
    }
}