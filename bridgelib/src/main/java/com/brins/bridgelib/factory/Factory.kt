package com.brins.bridgelib.factory

import com.brins.bridgelib.BridgeInterface

interface Factory {
    fun <T : BridgeInterface> create(bridgeClazz: Class<T>) : T
}