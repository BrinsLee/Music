package com.brins.bridgelib.store

import com.brins.bridgelib.BridgeInterface

class BridgeStore {
    private val mMap = HashMap<String, BridgeInterface>()


    fun put(key: String, bridge: BridgeInterface) {
        mMap.put(key, bridge)
    }

    fun get(key: String): BridgeInterface? = mMap[key]

    fun clear() {
        for (item in mMap.values) {
            item.onClear()
        }
        mMap.clear()
    }
}