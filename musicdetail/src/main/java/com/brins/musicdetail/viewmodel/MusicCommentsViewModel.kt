package com.brins.musicdetail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.musicdetail.contract.MusicDetailContract
import com.brins.networklib.model.comment.CommentResult

class MusicCommentsViewModel(application: Application) :
    MusicDetailContract.ViewModel(application) {

    private lateinit var mCommentData: CommentResult
    private val mHotCommentLiveData: MutableLiveData<ArrayList<CommentResult.Comment>> =
        MutableLiveData()

    private val mCommentLiveData: MutableLiveData<ArrayList<CommentResult.Comment>> =
        MutableLiveData()

    fun getMutableHotCommentData(): MutableLiveData<ArrayList<CommentResult.Comment>> =
        mHotCommentLiveData

    fun getMutableCommentData(): MutableLiveData<ArrayList<CommentResult.Comment>> =
        mCommentLiveData

    fun getCommentData(): CommentResult = mCommentData

    override suspend fun loadComments(path: String, id: String) {
        val result = mModel?.loadComment(path, id)
        result?.let {
            mCommentData = it
            mCommentLiveData.value = it.comments
            mHotCommentLiveData.value = it.hotComments

        }
    }

    override suspend fun likeOrUnLikeMusicComment(id: String, cid: String, t: Int, type: Int) {
        mModel?.likeOrUnLikeComment(id, cid, t, type)
    }
}