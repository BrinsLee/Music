package com.brins.networklib.model.personal

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_HOME_PERSONALIZED_MUSIC

class PersonalizedMusic : BaseMusic() {


    override val itemType: Int
        get() = ITEM_HOME_PERSONALIZED_MUSIC
}