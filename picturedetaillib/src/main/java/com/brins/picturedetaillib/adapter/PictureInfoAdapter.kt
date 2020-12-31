package com.brins.picturedetaillib.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.networklib.model.event.EventData

/**
 * Created by lipeilin
 * on 2020/12/27
 */
class PictureInfoAdapter(val list: ArrayList<EventData.Image>, fm: FragmentManager) :
    FragmentStatePagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putSerializable(KEY_EVENT_PICTURES, list)
        bundle.putInt(KEY_EVENT_PICTURE_POS, position)
        return BridgeProviders.instance.getBridge(PictureDetailBridgeInterface::class.java)
            .getDetailPictureFragment(bundle)

    }

    override fun getCount(): Int {
        return list.size
    }

}