package com.brins.picturedetaillib.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.fragment.BaseFragment
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.networklib.model.event.EventData
import com.brins.picturedetaillib.R
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
            showLoading()
            GlideHelper.setImageResource(
                full_image_iv,
                list[pos].originUrl, object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ToastUtils.show(getString(R.string.load_fail), Toast.LENGTH_SHORT)
                        hideLoading()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        hideLoading()
                        resource?.let { drawable ->
                            full_image_iv.setImageDrawable(drawable)
                        }
                        return true
                    }

                }
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