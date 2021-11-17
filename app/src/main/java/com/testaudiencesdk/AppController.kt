package com.testaudiencesdk

import android.app.Application
import android.util.Log
import com.livebroadcasterapi.LiveBroadcast
import com.livebroadcasterapi.LiveBroadcastConfig
import com.livebroadcasterapi.util.LiveBroadcasterPreferences
import com.livebroadcasterui.LiveBroadcastUI
import com.livebroadcasterui.LiveBroadcastUIConfig
import com.livebroadcasterui.R
import com.livebroadcasterui.Utils
import com.livebroadcasterui.liveEvent.product.IProductInfo

class AppController : Application(),IProductInfo{

    companion object {
        @JvmField
        var appContext: AppController? = null

        fun setInstance(application: AppController) {
            appContext = application
        }

        @JvmStatic
        fun getInstance(): AppController {
            return appContext as AppController
        }
    }

    override fun onCreate() {
        super.onCreate()
        setInstance(this)
        initializeLiveBroadcast()
    }

    private fun initializeLiveBroadcast() {
        val liveBroadcasterConfig = LiveBroadcastConfig.Builder(this)
            .setAccessToken(LiveBroadcasterPreferences.getAccessToken(this)?:"")
            .setPublicKey("") //Add public key
            .setLoggingEnabled(true)
            .build()

        LiveBroadcast.initialize(liveBroadcasterConfig)

        val liveBroadcastUiConfig = LiveBroadcastUIConfig.Builder()
            .messageTextSize(resources.getDimensionPixelSize(R.dimen.dimen_14sp))
            .build()

        LiveBroadcastUI.initialize(liveBroadcastUiConfig)
    }

    override fun onProductClick(productId: String, productUrl: String) {
        Utils.showToastMessage(this,"productId: $productId , productUrl: $productUrl")
    }
}