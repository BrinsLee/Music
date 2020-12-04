package com.brins.baselib.config

const val BASEURL = "http://118.31.65.24/"

const val PHONE_NUMBER_LENGTH = 11
const val UMAPPKEY = "5e33c33c4ca357e8100000fa"

const val KEY_ID = "KEY_ID"
const val KEY_URL = "KEY_URL"
const val KEY_COMMEND_PATH = "KEY_COMMEND_PATH"
/*const val KEY_INTENT_FROM = "KEY_INTENT_FROM"
const val KEY_INTENT_FROM_INTELLIGENCE = "KEY_INTENT_FROM_INTELLIGENCE"
const val KEY_INTENT_FROM_MINE = "KEY_INTENT_FROM_MINE"*/

const val TRANSITION_NAME = "TRANSITION_NAME"

const val MAIN_PROCESS_NAME: String = "com.brins.lightmusic"

val API_NEED_COOKIE = arrayListOf<String>(
    "[recommend, songs]",
    "[like]",
    "[comment, like]",
    "[comment, music]",
    "[playmode, intelligence, list]",
    "[follow]",
    "[search]",
    "[likelist]",
    "[recommend, resource]",
    "[user, event]",
    "[user, follows]"
)

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

        /**
         * 喜欢/不喜欢音乐
         */
        const val MUSIC_LIKE = "/like"
    }
}

interface MUSIC_LIST {

    companion object {
        const val MUSIC_LIST_DETAIL = "/playlist/detail"

        const val MUSIC_USER_LIST = "/user/playlist"

        const val MUSIC_LIST_USER_LIKE = "/likelist"

        const val MUSIC_LIST_DAILY_RECOMMEND = "/recommend/resource"

        const val MUSIC_LIST_INTELLIGENCE = "/playmode/intelligence/list"
    }
}

interface MUSIC_COMMENT {

    companion object {
        const val MUSIC_COMMENT = "music"

        const val ALBUM_COMMENT = "album"

        const val COMMENT = "/comment/{commentPath}"

        const val LIKE_UNLIKE_MUSIC_COMMENT = "/comment/like"

        const val COMMENT_TYPE_MUSIC = 0

        const val COMMENT_TYPE_MV = 1

        const val COMMENT_TYPE_LIST = 2

        const val COMMENT_TYPE_ALBUM = 3

        const val COMMENT_TYPE_STATION = 4

        const val COMMENT_TYPE_VIDEO = 5

        const val COMMENT_TYPE_DYMAIC = 6

    }
}


interface MINE_MUSIC_LIST {
    companion object {
        const val MINE_MUSIC_LIST = "/user/playlist"

        const val MINE_EVENT_DATA = "/user/event"
    }
}

interface MINE_INFO {
    companion object {
        const val MINE_INFO_FOLLOW = "/user/follows"
    }
}

interface LOGIN {
    companion object {
        const val LOGIN_EMAIL_LOGIN = "/login"
    }
}

interface SEARCH {
    companion object {

        const val TYPE_MUSIC = 1

        const val TYPE_ALBUM = 10

        const val TYPE_ARTIST = 100

        const val TYPE_MUSIC_LIST = 1000

        const val TYPE_USER = 1002

        const val TYPE_MV = 1004

        const val TYPE_RADIO = 1009

        const val TYPE_VIDEO = 1014

        const val SEARCH_HOT = "/search/hot/detail"

        //搜索建议
        const val SEARCH_SUGGEST = "/search/suggest"

        //搜索接口
        const val SEARCH_CLOUD = "/search"

        const val SERACH_FOLLOW = "/follow"
    }
}