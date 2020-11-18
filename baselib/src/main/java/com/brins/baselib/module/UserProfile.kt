package com.brins.baselib.module

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class UserProfile : BaseData() {

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

    var avatarDetail : AvatarDetail? = null

    class AvatarDetail {
        var userType = 0

        var identityIconUrl = ""
    }

    override val itemType: Int
        get() = ITEM_SEARCH_USER
}