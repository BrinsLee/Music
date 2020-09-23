package com.brins.playerlib.model

import android.content.Intent
import android.content.ServiceConnection
import com.brins.baselib.utils.UIUtils
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.playerlib.contract.PlayerContract

class PlayerModel : PlayerContract.Model {


    override fun bindPlaybackService(
        intent: Intent,
        mConnection: ServiceConnection,
        bindAutoCreate: Int
    ) {
        UIUtils.getContext().bindService(intent, mConnection, bindAutoCreate)
    }

    override fun unbindPlaybackService(mConnection: ServiceConnection) {
        UIUtils.getContext().unbindService(mConnection)
    }

    override suspend fun checkMusicUsable(id: String): Boolean {
        val result = ApiHelper.getMusicService().getMusicUseable(id).await()
        return result.success
    }

    override suspend fun loadMusicUrl(id: String): String {
        if (checkMusicUsable(id)) {
            val result = ApiHelper.getMusicService().getMusicUrl(id).await()
            return result.data[0].url
        }
        return ""
    }

    override suspend fun loadMusicComment(id: String) {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {

    }
}