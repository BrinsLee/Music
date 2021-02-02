package com.brins.musicdetail.model

import com.brins.musicdetail.contract.MusicDetailContract
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.comment.CommentResult
import com.brins.networklib.model.like.LikeMusicResult
import com.brins.networklib.model.music.MusicLrcResult

class MusicDetailModel : MusicDetailContract.Model {

    override suspend fun loadMusicLrc(id: String): MusicLrcResult =
        ApiHelper.getMusicService().getMusicLrc(id).await()

    override suspend fun loadComment(path: String, id: String): CommentResult =
        ApiHelper.getMusicService().getComment(path, id).await()

    override suspend fun likeMusic(id: String): LikeMusicResult =
        ApiHelper.getMusicService().likeOrUnLikeMusic(id, true).await()

    override suspend fun unLikeMusic(id: String): LikeMusicResult =
        ApiHelper.getMusicService().likeOrUnLikeMusic(id, false).await()


    override suspend fun likeOrUnLikeComment(id: String, cid: String, t: Int, type: Int) {
        ApiHelper.getMusicService().likeOrUnLikeMusicComment(id, cid, t, type).await()
    }

    override fun onDestroy() {

    }
}