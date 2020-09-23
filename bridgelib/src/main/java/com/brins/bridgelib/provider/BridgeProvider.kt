package com.brins.bridgelib.provider

import com.brins.bridgelib.BridgeInterface
import com.brins.bridgelib.factory.Factory
import com.brins.bridgelib.store.BridgeStore

class BridgeProvider(private val factory: Factory) {

    val bridgeStore = BridgeStore()

    companion object {
        private const val DEFAULT_KEY = "com.brins.bridgelib"
    }

    fun <T : BridgeInterface> get(key: String, bridgeClass: Class<T>): T {
        var componentBridge = bridgeStore.get(key)
        if (bridgeClass.isInstance(componentBridge)) {
            @Suppress("UNCHECKED_CAST")
            return componentBridge as T
        }
        componentBridge = factory.create(bridgeClass)
        bridgeStore.put(key, componentBridge)
        return componentBridge
    }

    fun <T : BridgeInterface> get(bridgeClass: Class<T>): T =
        get(DEFAULT_KEY + "@" + bridgeClass.canonicalName, bridgeClass)
}