package com.brins.bridgelib.factory

import com.brins.bridgelib.BridgeInterface
import java.lang.Exception
import java.lang.RuntimeException

class NewInstanceFactory : Factory {

    companion object {
        val intance: NewInstanceFactory by lazy { NewInstanceFactory() }
    }

    override fun <T : BridgeInterface> create(bridgeClazz: Class<T>): T = try {
        bridgeClazz.newInstance()
    } catch (e: Exception) {
        throw RuntimeException("Cannot create an instance of $bridgeClazz", e)
    }
}