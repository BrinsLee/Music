package com.brins.eventdetaillib.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.eventdetaillib.contract.EventDetailContract
import com.brins.networklib.model.comment.CommentResult

/**
 * Created by lipeilin
 * on 2021/1/12
 */
class EventViewModel(application: Application) :
    EventDetailContract.ViewModel(application) {

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

    override suspend fun getEventComment(id: String) {
        val result = mModel?.getEventComment(id)
        result?.let {
            mCommentData = it
            mCommentLiveData.value = it.comments
            mHotCommentLiveData.value = it.hotComments

        }
    }

    override suspend fun getEventForward(id: String) {
    }

    override suspend fun likeOrUnLikeEventComment(id: String, cid: String, t: Int, type: Int) {
        TODO("Not yet implemented")
    }
}