package com.brins.searchlib.activity


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_SEARCH_HOT
import com.brins.baselib.route.RouterHub.Companion.SEARCHACTIVITY
import com.brins.baselib.utils.KeyboardUtils
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.*
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.search.HotSearchResult
import com.brins.networklib.model.search.SearchSuggestResult
import com.brins.searchlib.R
import com.brins.searchlib.contract.SearchContract
import com.brins.searchlib.presenter.SearchPresenter
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_search.*

@Route(path = SEARCHACTIVITY)
class SearchActivity : BaseMvpActivity<SearchPresenter>(), SearchContract.View, TextWatcher {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mHis = arrayListOf<String>()
    private var mTagAdapter: TagAdapter<String>? = null
    private var mSearchSuggest: Array<String?> = emptyArray()

    override fun getLayoutResId(): Int {
        return R.layout.activity_search
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        et_search.addTextChangedListener(this)
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                KeyboardUtils.hideSoftInput(this@SearchActivity)
                val search = et_search.text?.trim()
                if (!search.isNullOrEmpty()) {
                    ToastUtils.show(search, Toast.LENGTH_SHORT)
                    mPresenter?.addHistorySearch(search.toString())
                    onAddHistorySearch(search.toString())
                    true
                } else {
                    false
                }
            }
            false


        }
        mTagAdapter = object : TagAdapter<String>(mHis) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                val tv: TextView = LayoutInflater.from(this@SearchActivity).inflate(
                    R.layout.base_search_tv,
                    id_flowlayout, false
                ) as TextView
                tv.text = t
                return tv
            }

        }
        iv_del.setOnClickListener {
            mPresenter?.clearHistorySearch()
        }
        id_flowlayout.adapter = mTagAdapter

        id_flowlayout.setOnTagClickListener { view, position, parent ->
            ToastUtils.show(mHis[position], Toast.LENGTH_SHORT)
            true
        }
        mAdapter = HotSearchAdapter()
        rv_hot_search.adapter = mAdapter
        rv_hot_search.layoutManager = GridLayoutManager(
            this, 2,
            RecyclerView.VERTICAL,
            false
        )
        launch({
            showLoading()
            mPresenter?.loadHotSearch()
            mPresenter?.loadHistorySearch()
        }, {})
    }

    override fun onResume() {
        super.onResume()
        et_search.postDelayed({
            KeyboardUtils.showSoftInput(et_search)
        }, 300)
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this@SearchActivity)

    }

    override fun onLoadHistorySearch(result: ArrayList<String>) {
        if (!result.isNullOrEmpty()) {
            mHis = result
            mTagAdapter?.addNewData(result)
        }

    }

    override fun onLoadHotSearch(result: HotSearchResult) {
        hideLoading()
        val list = mutableListOf<BaseData>()
        result.data?.let {
            it.forEach { hotSearchData ->
                list.add(hotSearchData)
            }
            mAdapter?.setNewData(list)
        }
    }

    override fun onAddHistorySearch(result: String) {
        mTagAdapter?.addNewData(result)
    }

    override fun onHistorySearchClear() {
        mTagAdapter?.clear()
    }

    override fun onSuggestSearchLoad(result: SearchSuggestResult) {
        result.result?.let {
            mSearchSuggest = arrayOfNulls(it.allMatch!!.size)
            for ((i, a) in it.allMatch!!.withIndex()) {
                mSearchSuggest[i] = a.keyword
            }
            list_item.visibility = View.VISIBLE
            list_item.adapter = ArrayAdapter<String>(
                this
                , android.R.layout.simple_list_item_1, mSearchSuggest
            )
        }
    }

    override fun afterTextChanged(s: Editable?) {
        s.let {
            if (it.toString().isNullOrEmpty()) {
                list_item.visibility = View.GONE
                mSearchSuggest = emptyArray()
            } else {
                launch({
                    mPresenter?.loadSearchSuggest(it.toString())
                }, {})
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    class HotSearchAdapter() : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_SEARCH_HOT, R.layout.search_item_hot)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            if (item.itemType == ITEM_SEARCH_HOT) {
                helper.setText(R.id.tv_search_num, "${helper.adapterPosition + 1}")
                if (helper.adapterPosition < 4) {
                    helper.setTextColor(R.id.tv_search_num, Color.RED)
                }
                helper.setText(
                    R.id.tv_search_name,
                    (item as HotSearchResult.HotSearchData).searchWord
                )
                helper.setText(
                    R.id.tv_search_decoration,
                    item.content
                )
                GlideHelper.setImageResource(helper.getView(R.id.iv_search_icon), item.iconUrl)
            }
        }

    }


}