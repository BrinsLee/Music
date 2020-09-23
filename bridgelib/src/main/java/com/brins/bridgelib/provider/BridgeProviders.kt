package com.brins.bridgelib.provider

import com.brins.bridgelib.BridgeInterface
import com.brins.bridgelib.factory.Factory
import com.brins.bridgelib.factory.NewInstanceFactory

class BridgeProviders {

    private val mProvidersMap = HashMap<Class<*>, BridgeProvider>()
    private val mBridgeMap = HashMap<Class<*>, Class<*>>()
    private val mDefaultBridgeProvider = BridgeProvider(NewInstanceFactory.intance)


    companion object {
        val instance: BridgeProviders by lazy { BridgeProviders() }
    }


    /**
     *
     * 组件注册，缓存各个组件暴露的接口
     * @param T
     * @param clazz
     * @param factory
     * @param replace
     */
    fun <T : BridgeInterface> register(
        clazz: Class<T>,
        factory: Factory? = null,
        replace: Boolean = false
    ) = apply{

        if (clazz.interfaces.isEmpty() || !clazz.interfaces[0].interfaces.contains(BridgeInterface::class.java)) {
            throw RuntimeException("$clazz must implement BridgeInterface")
        }

        clazz.interfaces[0].let {
            if (mProvidersMap[it] == null || replace) {
                mBridgeMap[it] = clazz
                mProvidersMap[it] = if (factory == null) {
                    mDefaultBridgeProvider
                } else {
                    BridgeProvider(factory)
                }
            }
        }
    }


    /**
     * 通过key获取BridgeInterface
     *
     * @param T
     * @param clazz
     * @return
     */
    fun <T : BridgeInterface> getBridge(clazz: Class<T>): T {
        mProvidersMap[clazz]?.let {
            return it.get(mBridgeMap[clazz] as Class<T>)
        }
        throw RuntimeException("$clazz subClass is not register")

    }

    fun clear() {
        mProvidersMap.clear()
        mBridgeMap.clear()
        mDefaultBridgeProvider.bridgeStore.clear()
    }
}