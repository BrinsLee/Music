package com.brins.baselib.route

import com.brins.baselib.config.EVENT

interface RouterHub {

    companion object {
        const val MAIN = "/main"
        const val HOME = "/home"
        const val LOGIN = "/login"
        const val MUSIC_LIST = "/musiclist"
        const val MUSIC_DETAIL = "/musicdetail"
        const val BRIDGE = "/bridge"
        const val MUSIC_SQUARE = "/musicsquare"
        const val MINE = "/mine"
        const val DAILY = "/daily"
        const val SEARCH = "/search"
        const val FIND = "/find"
        const val PICTURE_DETAIL = "/picturedetail"
        const val EVENT_DETAIL = "/eventdetail"
        const val VIDEO = "/video"
        const val RADIO = "/radio"



        //    ****************Activity路径配置***************************

        const val MUSICDETAILACTIVITY = "$MUSIC_DETAIL/MusicDetailActivity"

        const val COMMENTSACTIVITY = "$MUSIC_DETAIL/CommentsActivity"

        const val PLAYLISTACTIVITY = "$MUSIC_DETAIL/PlayListActivity"

        const val MUSICLISTACTIVITY = "$MUSIC_LIST/MusicListActivity"

        const val BROWSERACTIVITY = "$BRIDGE/BrowserActivity"

        const val MUSICLISTSQUAREACTIVITY = "$MUSIC_SQUARE/MusicListSquareActivity"

        const val ALBUMLISTACTIVITY = "$MUSIC_LIST/AlbumListActivity"

        const val LOGINSELECTACTIVITY = "$LOGIN/LoginSelectActivity"

        const val LOGINEMAILACTIVITY = "$LOGIN/LoginEmailActivity"

        const val LOGINPHONEACTIVITY = "$LOGIN/LoginPhoneActivity"

        const val DAILYMUSICACTIVITY = "$DAILY/DailyMusicActivity"

        const val SEARCHACTIVITY = "$SEARCH/SearchActivity"

        const val MINEINFOACTIVITY = "$MINE/MineInfoActivity"

        const val DETAILPICTUREACTIVITY = "$PICTURE_DETAIL/DetailPictureActivity"

        const val EVENTDETAILACTIVITY = "$EVENT_DETAIL/EventDetailActivity"

        const val VIDEODETAILACTIVITY = "${VIDEO}/VideoDetailActivity"

        const val RADIOACTIVITY = "${RADIO}/RadioActivity"

        const val RADIODETAILACTIVITY = "${RADIO}/RadioDetailActivity"


        //    ****************Fragment路径配置***************************
        const val HOMEFRAGMENT = "$HOME/HomeFragment"

        const val MINEFRAGMENT = "$MINE/MineFragment"

        const val FINDFRAGMENT = "$FIND/FindFragment"

        const val EVENTCOMMENTFRAGMENT = "${EVENT_DETAIL}/EventCommentFragment"

    }
}