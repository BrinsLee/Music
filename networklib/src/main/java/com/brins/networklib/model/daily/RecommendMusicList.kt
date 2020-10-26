package com.brins.networklib.model.daily

/**
 * Created by lipeilin
 * on 2020/10/26
 */
class RecommendMusicList {

    var name = ""

    var id = ""

    var album : Album? = null

    var type : Int = 0

    var copywriter = ""

    var picUrl = ""

    var playcount = 0

    var createTime = ""

    var trackCount = ""

    class Album {
        var name = ""

        var id = ""

        var type = ""

        var size = 0

        var blurPicUrl = ""

        var picUrl = ""

        var company = ""

        var publishTime = 0L

        var subType = ""

        var description = ""



    }
}