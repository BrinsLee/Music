package com.chad.library.adapter.base2.provider

import com.chad.library.adapter.base2.BaseNodeAdapter
import com.chad.library.adapter.base2.entity.node.BaseNode

abstract class BaseNodeProvider : BaseItemProvider<BaseNode>() {

    override fun getAdapter(): BaseNodeAdapter? {
        return super.getAdapter() as? BaseNodeAdapter
    }

}