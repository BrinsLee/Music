package com.brins.picturedetaillib.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseActivity
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.route.RouterHub.Companion.DETAILPICTUREACTIVITY
import com.brins.networklib.model.event.EventData
import com.brins.picturedetaillib.R
import com.brins.picturedetaillib.adapter.PictureInfoAdapter
import kotlinx.android.synthetic.main.activity_detail_picture.*

@Route(path = DETAILPICTUREACTIVITY)
class DetailPictureActivity : BaseActivity() {

    //    @Autowired(name = KEY_EVENT_PICTURES)
    lateinit var mPicList: ArrayList<EventData.Image>

    @Autowired(name = KEY_EVENT_PICTURE_POS)
    @JvmField
    var mPos: Int = 0

    lateinit var mAdapter: PictureInfoAdapter

    override fun getLayoutResId(): Int {
        return R.layout.activity_detail_picture
    }

    override fun init(savedInstanceState: Bundle?) {
        supportPostponeEnterTransition()
        mPicList = intent.getSerializableExtra(KEY_EVENT_PICTURES) as ArrayList<EventData.Image>
        mAdapter = PictureInfoAdapter(mPicList, supportFragmentManager)
        details_picture_vp.adapter = mAdapter
        details_picture_vp.currentItem = mPos
        tv_pic_num.text = "$mPos/${mPicList.size}"
        details_picture_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                tv_pic_num.text = "$position/${mPicList.size}"
            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }
}