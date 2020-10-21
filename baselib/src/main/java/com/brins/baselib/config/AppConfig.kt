package com.brins.baselib.config

const val BASEURL = "http://118.31.65.24/"

const val PHONE_NUMBER_LENGTH = 11
const val UMAPPKEY = "5e33c33c4ca357e8100000fa"

const val KEY_ID = "KEY_ID"
const val KEY_URL = "KEY_URL"
const val TRANSITION_NAME = "TRANSITION_NAME"

const val MAIN_PROCESS_NAME: String = "com.brins.lightmusic"

var UserCookie = ""

interface RECOMMEND {
    companion object {
        const val RECOMMEND_MV = "/personalized/mv"

        /***
         * 推荐歌单
         */
        const val RECOMMEND_MUSIC_LIST = "/personalized"

        /**
         * 推荐新音乐
         */
        const val RECOMMEND_NEW_MUSIC = "/personalized/newsong"

        /**
         * 推荐新专辑
         */
        const val RECOMMEND_NEWEST_ALBUM = "/album/newest"

        /**
         * 专辑详情
         */
        const val RECOMMEND_ALBUM_DETAIL = "/album"

        /**
         * 全部新碟
         */
        const val RECOMMEND_NEW_ALBUM = "/album/new"

        /**
         * 新歌速递
         */
        const val RECOMMEND_TOP_SONG = "/top/song"

        /**
         * 推荐网友精选
         */
        const val RECOMMEND_ALBUM = "/top/playlist"

        /**
         * 推荐高品质
         */
        const val RECOMMEND_HIGHTQUALITY = "/top/playlist/highquality"

        /**
         * 每日推荐
         */
        const val RECOMMEND_DAILY_MUSIC = "/recommend/songs"
    }
}

interface BANNER {
    companion object {
        /**
         * 轮播图
         */
        const val BANNER = "/banner"
    }
}

interface MUSIC {

    companion object {

        /**
         * 音乐链接
         */
        const val MUSIC_URL = "/song/url"

        /**
         * 音乐是否可用
         */
        const val MUSIC_USEABLE = "/check/music"

        /**
         * 歌词
         */
        const val MUSIC_LRC = "/lyric"

        /**
         * 音乐详情
         */
        const val MUSIC_DETAIL = "/song/detail"
    }
}

interface MUSIC_LIST {

    companion object {
        const val MUSIC_LIST_DETAIL = "/playlist/detail"

        const val MUSIC_USER_LIST = "/user/playlist"
    }
}

interface MUSIC_COMMENT {

    companion object {
        const val MUSIC_COMMENT = "/comment/music"
    }
}


interface MINE_MUSIC_LIST {
    companion object {
        const val MINE_MUSIC_LIST = "/user/playlist"
    }
}

interface LOGIN {
    companion object{
        const val LOGIN_EMAIL_LOGIN = "/login"
    }
}