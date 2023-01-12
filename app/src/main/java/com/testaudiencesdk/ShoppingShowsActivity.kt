package com.testaudiencesdk

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.livebroadcasterapi.LiveBroadcast
import com.livebroadcasterapi.LiveBroadcastConstants
import com.livebroadcasterapi.model.BroadcastItemResponse
import com.livebroadcasterapi.model.LiveBroadcastError
import com.livebroadcasterapi.model.LiveBroadcastListQueryBuilder
import com.livebroadcasterapi.network.response.CompletionHandler
import com.livebroadcasterui.Utils
import com.livebroadcasterui.databinding.ActivityShowReelsBinding
import com.livebroadcasterui.embeddedUi.ChannelizeShoppingView

private val TAG = ShoppingShowsActivity::class.java.simpleName
class ShoppingShowsActivity : FragmentActivity(){
    private lateinit var binding : ActivityShowReelsBinding
    private var eventListResponse = ArrayList<BroadcastItemResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowReelsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        // Call BroadcastList Api
        callBroadcastApi()
    }

    private fun callBroadcastApi() {
        val progress = Utils.progressDialog(this)
        progress.show()

        val liveBroadcastListQueryBuilder = LiveBroadcastListQueryBuilder.Builder()
            .setLimit(5)
            .setSkip(0)
            .setSort(LiveBroadcastConstants.DESC)
            .build()

        LiveBroadcast.getApiInstance().getBroadcasts(
            liveBroadcastListQueryBuilder,
            object : CompletionHandler<List<BroadcastItemResponse>, LiveBroadcastError> {
                override fun onSuccess(result: List<BroadcastItemResponse>) {
                    progress.dismiss()
                    eventListResponse.clear()
                    eventListResponse = result as ArrayList<BroadcastItemResponse>
                    if(eventListResponse.isNotEmpty()){
                        val adapter = ScreenVerticalSlidePagerAdapter(this@ShoppingShowsActivity)
                        binding.viewPagerShows.adapter = adapter
                    }else{
                        Toast.makeText(this@ShoppingShowsActivity,"No shows available!!!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(error: LiveBroadcastError?) {
                    Log.e(TAG, "errorMessage: ${error?.error?.message}")
                    progress.dismiss()
                }
            })
    }


    private inner class ScreenVerticalSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = eventListResponse.size


        override fun createFragment(position: Int): Fragment{
            return ChannelizeShoppingView.newInstance(
                    eventListResponse[position],
                enableLiveChat = true,
                enableReactions = true,
                enableLiveAudiencesCount = true,
                enableProducts = true,
            )
        }
    }
}