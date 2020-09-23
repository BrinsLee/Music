package com.brins.home.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.module.BaseData
import com.brins.home.R
import com.brins.home.adapter.PersonalizedMusicAdapter
import com.brins.networklib.model.musiclist.MusicList
import com.brins.networklib.model.recommend.RecommendResult
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base2.BaseQuickAdapter

class TopRecommendViewHolder(itemView: View) :
    BaseViewHolder<RecommendResult<MusicList>>(itemView) {

    private lateinit var mAdapter: PersonalizedMusicAdapter
    private var mRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_personalized_music)

    override fun setData(data: RecommendResult<MusicList>?) {
        super.setData(data)
        data?.let {
            if (it.playlists != null) {
                val list = mutableListOf<BaseData>()
                list.addAll(it.playlists!!)
                mAdapter = PersonalizedMusicAdapter(list)
                mAdapter.animationEnable = true
                mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
                mRecyclerView.layoutManager =
                    LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                mRecyclerView.adapter = mAdapter
            }

        }
    }
}