package com.brins.picturedetaillib.fragment

import android.os.Bundle
import android.view.View
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.fragment.BaseFragment
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.networklib.model.event.EventData
import com.brins.picturedetaillib.R
import kotlinx.android.synthetic.main.fragment_detail_picture.*

class DetailPictureFragment : BaseFragment() {

    override fun getLayoutResID(): Int {
        return R.layout.fragment_detail_picture
    }

    override fun reLoad() {
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            val list = it.getSerializable(KEY_EVENT_PICTURES) as ArrayList<EventData.Image>
            val pos = it.getInt(KEY_EVENT_PICTURE_POS)
            GlideHelper.setImageResource(
                full_image_iv,
                list[pos].originUrl
            )
        }
    }

    companion object {

        fun newInstance(bundle: Bundle): DetailPictureFragment {
            val fragment = DetailPictureFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    fun getSharedElement(): View {
        return full_image_iv
    }
}