package com.brins.home.presenter

import com.brins.home.contract.HomeContract
import javax.inject.Inject


/**
 * @author lipeilin
 * @date 2020/7/22
 */
class HomePresenter @Inject constructor() : HomeContract.Presenter() {


    override suspend fun loadPersonalizedMusicList() {
        val result = mModel?.loadPersonalizedMusicList()
        mView?.onPersonalizedMusicListLoad(result)
    }

    override suspend fun loadBannerData() {
        val result = mModel?.loadBanner()
        mView?.onBannerLoad(result)
    }

    override suspend fun loadPersonalizedMusic() {
        val result = mModel?.loadPersonalizedMusic()
        mView?.onPersonalizedMusicLoad(result)
    }

    override suspend fun loadHotOrNewRecommend(type: String) {
        val result = mModel?.loadHotOrNewRecommend(type)
        when (type) {
            "new" -> mView?.onNewRecommendLoad(result)

            "hot" -> mView?.onHotRecommendLoad(result)
        }
    }

    override fun subscribe() {
        super.subscribe()
    }

    override fun unsubscribe() {
        mView = null
    }
}