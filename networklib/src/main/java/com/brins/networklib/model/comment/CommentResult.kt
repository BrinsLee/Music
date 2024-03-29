package com.brins.networklib.model.comment

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_MUSIC_DETAIL_COMMENT

class CommentResult {

    var code: Int = 0

    var total: Long = 0

    var hotComments: ArrayList<Comment>? = null

    var comments: ArrayList<Comment>? = null

    var more: Boolean = false

    class Comment : BaseData() {
        var user: User? = null

        var beReplied: ArrayList<Replay>? = null

        var status = 0

        var commentId = ""

        var content = ""

        var time = 0L

        var likedCount = 0

        var liked = false

        override val itemType: Int
            get() = ITEM_MUSIC_DETAIL_COMMENT
    }

    class User {

        var avatarUrl = ""

        var userId = ""

        var nickname = ""
    }

    class Replay {
        var user: User? = null

        var beRepliedCommentId = ""

        var status = 0
    }
}