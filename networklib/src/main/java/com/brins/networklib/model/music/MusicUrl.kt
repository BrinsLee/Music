package com.brins.networklib.model.music

class MusicUrl {

    var data: ArrayList<MusicUrlDta> = arrayListOf()

    class MusicUrlDta {
        var url = ""

        var id = ""

        var size = 0

        var type = ""
    }

}