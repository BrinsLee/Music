package com.brins.baselib.route

interface RouterHub {

    companion object {
        const val MAIN = "/main"
        const val HOME = "/home"
        const val LOGIN = "/login"
        const val MUSIC_LIST = "/musiclist"
        const val MUSIC_DETAIL = "/musicdetail"
        const val BRIDGE = "/bridge"
        const val MUSIC_SQUARE = "/musicsquare"


        //    ****************Activity路径配置***************************

        const val MUSICDETAILACTIVITY = "$MUSIC_DETAIL/MusicDetailActivity"

        const val COMMENTSACTIVITY = "$MUSIC_DETAIL/CommentsActivity"

        const val PLAYLISTACTIVITY = "$MUSIC_DETAIL/PlayListActivity"

        const val MUSICLISTACTIVITY = "$MUSIC_LIST/MusicListActivity"

        const val BROWSERACTIVITY = "$BRIDGE/BrowserActivity"

        const val MUSICLISTSQUAREACTIVITY = "$MUSIC_SQUARE/MusicListSquareActivity"


        //    ****************Fragment路径配置***************************
        const val HOMEFRAGMENT = "$HOME/HomeFragment"
    }
}