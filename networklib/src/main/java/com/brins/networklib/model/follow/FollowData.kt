package com.brins.networklib.model.follow

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_FIND_FOLLOW

/**
 * Created by lipeilin
 * on 2020/12/1
 */
class FollowData : BaseData(){

    var py = ""

    var time = 0

    var followed = true

    var userId = ""

    var userType = 0

    var vipType = 0

    var gender = 1

    var followeds = 0

    var follows = 0

    var avatarUrl = ""

    var nickname = ""

    var accountStatus = 0

    var signature = ""

    var vipRights: VipRight? = null

    var eventCount = 0

    var playlistCount = 0

    class VipRight {

        var redVipAnnualCount = 0

        var redVipLevel = 0
    }

    override val itemType: Int
        get() = ITEM_FIND_FOLLOW
}