package com.brins.eventdetaillib.activity

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.config.KEY_EVENT_DATA
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.config.TRANSITION_IMAGE
import com.brins.baselib.route.RouterHub.Companion.EVENTDETAILACTIVITY
import com.brins.baselib.utils.SpanUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.getDateToString
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.MultiImageView
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.eventdetaillib.R
import com.brins.eventdetaillib.presenter.EventDetailPresenter
import com.brins.networklib.model.event.EventData
import kotlinx.android.synthetic.main.activity_event_detail.*

@Route(path = EVENTDETAILACTIVITY)
class EventDetailActivity : BaseMvpActivity<EventDetailPresenter>() {

    @Autowired(name = KEY_EVENT_DATA)
    lateinit var mEvent: EventData

    override fun getLayoutResId(): Int {
        return R.layout.activity_event_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        if (::mEvent.isInitialized) {
            GlideHelper.setImageResource(
                iv_avatar,
                mEvent.user?.avatarUrl
            )

            tv_name.setText(createNickName(mEvent))
            tv_date.setText(getDateToString(mEvent.eventTime))
            et_root.setText(mEvent.jsonData?.msg)

            mEvent.pics?.let {
                val list = mutableListOf<String>()
                it.forEach { image ->
                    list.add(image.squareUrl)
                }
                mi_event_images.setList(list)
                mi_event_images.setOnItemClickListener(object : MultiImageView.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val bundle = Bundle()
                        bundle.putSerializable(KEY_EVENT_PICTURES, it)
                        bundle.putInt(KEY_EVENT_PICTURE_POS, position)
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@EventDetailActivity,
                                view,
                                TRANSITION_IMAGE
                            )
                        BridgeProviders.instance.getBridge(PictureDetailBridgeInterface::class.java)
                            .toDetailPictureActivity(bundle, optionsCompat)
                    }

                })
            }

        }

    }

    private fun createNickName(item: EventData): SpannableStringBuilder? {
        var sub = when (item.type) {
            18 -> getString(R.string.share_song)
            19 -> getString(R.string.share_album)
            17, 28 -> getString(R.string.share_radio)
            22 -> getString(R.string.forward)
            39 -> getString(R.string.publish_video)
            13 -> getString(R.string.share_music_list)
            24 -> getString(R.string.share_article)
            41, 21 -> getString(R.string.share_video)
            else -> ""
        }
        val strBuilder = SpanUtils().append(item.user?.nickname ?: "")
            .setForegroundColor(UIUtils.getColor(R.color.blue_4f7daf))
            .append(" ")
            .append(sub)
            .setForegroundColor(UIUtils.getColor(R.color.default_btn_text)).create()
        return strBuilder

    }


}