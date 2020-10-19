package com.brins.mine.viewholder

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brins.baselib.database.factory.DataBaseFactory
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.subscribeDbResult
import com.brins.mine.R
import com.brins.networklib.model.music.RecentlyMusic
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class RecentlyMusicViewHolder(view: View) : BaseViewHolder<RecentlyMusic>(view) {

    private val mCover: ImageView = view.findViewById(R.id.cover)
    private val mName: TextView = view.findViewById(R.id.name)

    override fun setData(data: RecentlyMusic?) {
        super.setData(data)
        DataBaseFactory.getRecentlyMusics().subscribeDbResult({
            val list = it
            if (list.isNotEmpty()) {
                GlideHelper.setRoundImageResource(mCover, list[list.lastIndex].picUrl, 10)
            }
            mName.setText("已播放歌曲\n${list.size}")
        }, {
        })
    }
}