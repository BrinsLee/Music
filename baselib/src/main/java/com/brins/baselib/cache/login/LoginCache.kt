package com.brins.baselib.cache.login

import com.brins.baselib.cache.cacheinterface.CacheInterface
import com.brins.baselib.module.like.UserLikeMusicResult
import com.brins.baselib.module.userlogin.UserAccountBean
import com.brins.baselib.module.userlogin.UserProfileBean


object LoginCache : CacheInterface {

    var isLogin = false


    /**
     * 用户信息，缓存，方便调用
     */
    var userAccount: UserAccountBean? = null

    /**
     * 用户画像，缓存，方便调用
     */
    var userProfile: UserProfileBean? = null

    var UserCookie = ""

    var likeResult: UserLikeMusicResult? = null

    override fun clear() {
        isLogin = false
        userAccount = null
        userProfile = null
        UserCookie = ""
        likeResult = null
    }
}