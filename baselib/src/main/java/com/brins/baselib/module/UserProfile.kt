package com.brins.baselib.module

import java.io.Serializable
import java.lang.ref.WeakReference

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class UserProfile : BaseData(), Serializable {

    var province = 0

    var authStatus = 0

    var followed = false

    var avatarUrl = ""

    var accountStatus = 0

    /**
     * 1男2女
     */
    var gender = 1

    var city = 0

    var birthday = ""

    var userId = ""

    var userType = 0

    var nickname = ""

    var signature = ""

    var description = ""

    var detailDescription = ""

    var avatarImgId = ""

    var backgroundImgId = ""

    var backgroundUrl = ""

    var avatarDetail: AvatarDetail? = null

    @Transient
    var onFollowListener: WeakReference<OnFollowListener>? = null

    fun setFollowListener(followListener: OnFollowListener): UserProfile {
        this.onFollowListener = WeakReference(followListener)
        return this
    }

    class AvatarDetail : Serializable {
        var userType = 0

        var identityIconUrl = ""
    }

    override val itemType: Int
        get() = ITEM_SEARCH_USER

    interface OnFollowListener {
        fun onFollow(user: UserProfile, pos: Int)
    }
}