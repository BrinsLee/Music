package com.brins.baselib.config

const val BASEURL = "http://118.31.65.24/"

const val PHONE_NUMBER_LENGTH = 11
const val UMAPPKEY = "5e33c33c4ca357e8100000fa"

const val KEY_ID = "KEY_ID"
const val KEY_URL = "KEY_URL"
const val KEY_COMMEND_PATH = "KEY_COMMEND_PATH"
const val KEY_EVENT_PICTURES = "KEY_EVENT_PICTURES"
const val KEY_EVENT_PICTURE_POS = "KEY_EVENT_PICTURE_POS"
const val KEY_EVENT_DATA = "KEY_EVENT_DATA"
const val KEY_EVENT_THREADID = "KEY_EVENT_THREADID"

const val TRANSITION_NAME = "TRANSITION_NAME"
const val TRANSITION_IMAGE = "image"

const val MAIN_PROCESS_NAME: String = "com.brins.lightmusic"

const val CODE_MAIN = 0
const val CODE_CLOSE = 1
const val CODE_PRV = 2
const val CODE_PAUSE = 3
const val CODE_PLAY = 4
const val CODE_NEXT = 5

const val ACTION_PRV = "com.brins.lightmusic.ACTION.PLAY_LAST"
const val ACTION_NEXT = "com.brins.lightmusic.ACTION.PLAY_NEXT"
const val ACTION_PAUSE = "com.brins.lightmusic.ACTION.PAUSE"
const val ACTION_PLAY = "com.brins.lightmusic.ACTION.PLAY"
const val ACTION_EXIST = "com.brins.lightmusic.ACTION.EXIST"


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
    "[user, follows]",
    "[event]",
    "[resource, like]"
)

val TYPE_EVENT = arrayListOf<Int>(
    18,
    19,
    17, 28,
    22,
    39,
    35, 13,
    24,
    41, 21
)

val MAINLAND = "内地"
val HONGKONG_TAIWAN = "港台"
val EUROPE_AMERICA = "欧美"
val JAPAN = "日本"
val KOREA = "韩国"

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

        const val LOGIN_PHONE_LOGIN = "/login/cellphone"
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

interface FIND {
    companion object {
        const val FIND_EVENT = "/event"
    }
}

interface LIKE {
    companion object {
        //1 点赞 ，2 取消点赞
        const val LIKE_OR_UNLIKE = "/resource/like"
    }
}

interface EVENT {
    companion object {
        const val EVENT_COMMENT = "/comment/event"
    }
}

interface VIDEO {
    companion object {
        /**
         * 最新MV
         */
        const val LASTEST_MUSIC_VIDEO = "/mv/first"

        /**
         * MV播放地址
         */
        const val MVURL = "/mv/url"

        /**
         * 详情
         */
        const val MVDETAIL = "/mv/detail"

        /**
         * 所有MV
         */
        const val MVALL = "/mv/all"

        /**
         * 获取评论
         */
        const val MVCOMMENTS = "/comment/mv"

    }
}

interface RADIO {
    companion object {
        const val RECOMMEND_RADIO = "/dj/hot"

        const val PERSONALIZED_RADIO = "/personalized/djprogram"
    }
}