package com.brins.home.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.module.BaseData
import com.brins.home.R
import com.brins.home.adapter.PersonalizedMusicAdapter
import com.brins.networklib.model.personal.PersonalizedMusicList
import com.brins.networklib.model.personal.PersonalizedResult
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base2.BaseQuickAdapter

class PersonalizedViewHolder(itemView: View) :
    BaseViewHolder<PersonalizedResult<PersonalizedMusicList>>(itemView) {

    private lateinit var mAdapter: PersonalizedMusicAdapter
    private var mRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_personalized_music)

    override fun setData(data: PersonalizedResult<PersonalizedMusicList>?) {
        super.setData(data)
        data?.let {
            if (it.result != null) {
                val list = mutableListOf<BaseData>()
                list.addAll(it.result!!)
                mAdapter = PersonalizedMusicAdapter(list)
                mAdapter.animationEnable = true
                mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
                mAdapter.isAnimationFirstOnly = false
                mRecyclerView.layoutManager =
                    LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                mRecyclerView.adapter = mAdapter
            }

        }
    }
}