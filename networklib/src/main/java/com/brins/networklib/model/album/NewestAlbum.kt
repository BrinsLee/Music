package com.brins.networklib.model.album

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_NEWEST_ALBUM
import java.io.Serializable

class NewestAlbum : BaseData(), Serializable {

    var name: String = ""

    var id: String = ""

    var type: String = ""

    var size: Int = 0

    var picId: String = ""

    var blurPicUrl: String = ""

    var companyId = 0

    var picUrl: String = ""

    var publishTime: String = ""

    var description: String = ""

    var tags: String = ""

    var company: String = ""

    var artist: Artist? = null

    var artists: ArrayList<Artist>? = null


    override val itemType: Int
        get() = ITEM_HOME_NEWEST_ALBUM

    class Artist : Serializable {

        var name = ""

        var id: String = ""

        var picId: String = ""

        var img1v1Id: String = ""

        var picUrl: String = ""

        var img1v1Url: String = ""

        var albumSize = 0

        var alias: Array<String>? = null

        var musicSize = 0

    }
}