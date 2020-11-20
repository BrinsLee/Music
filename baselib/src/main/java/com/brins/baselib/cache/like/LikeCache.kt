package com.brins.baselib.cache.like

import com.brins.baselib.cache.cacheinterface.CacheInterface
import com.brins.baselib.module.MusicList

object LikeCache : CacheInterface {

    var likeMusicList: MusicList? = null

    override fun clear() {
        likeMusicList = null
    }
}