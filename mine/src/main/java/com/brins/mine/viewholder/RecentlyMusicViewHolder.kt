package com.brins.mine.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.subscribeDbResult
import com.brins.mine.R
import com.brins.networklib.model.music.RecentlyMusic
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class RecentlyMusicViewHolder(view: View) : BaseViewHolder<RecentlyMusic>(view) {

    private val mCover: ImageView = view.findViewById(R.id.cover)
    private val mName: TextView = view.findViewById(R.id.name)
    private val mCoverMask: RoundedImageView = view.findViewById(R.id.cover_mask)
    override fun setData(data: RecentlyMusic?) {
        super.setData(data)
        mCoverMask.visibility = View.VISIBLE
        DatabaseFactory.getRecentlyMusics().subscribeDbResult({
            val list = it
            if (list.isNotEmpty()) {
                val cover = if (list[list.lastIndex].picUrl.isEmpty()) {
                    list[list.lastIndex].song?.picUrl
                } else {
                    list[list.lastIndex].picUrl
                }
                GlideHelper.setRoundImageResource(mCover, cover, 10)
            }
            mName.setText("已播放歌曲\n${list.size}")
        }, {
        })
    }
}