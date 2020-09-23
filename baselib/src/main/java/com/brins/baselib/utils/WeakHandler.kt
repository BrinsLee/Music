package com.brins.baselib.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference


class WeakHandler : Handler {
    interface IHandler {
        fun handleMsg(msg: Message)
    }

    private val mRef: WeakReference<IHandler>

    constructor(handler: IHandler) {
        mRef = WeakReference(handler)
    }

    constructor(looper: Looper?, handler: IHandler) : super(looper) {
        mRef = WeakReference(handler)
    }

    override fun handleMessage(msg: Message) {
        val handler = mRef.get()
        if (handler != null && msg != null) {
            handler.handleMsg(msg)
        }
    }
}
