package com.brins.bridgelib.home

import androidx.fragment.app.Fragment
import com.brins.bridgelib.BridgeInterface

interface HomeBridgeInterface :BridgeInterface{

    fun getHomeFragment() : Fragment
}