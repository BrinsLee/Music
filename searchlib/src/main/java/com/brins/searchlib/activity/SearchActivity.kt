package com.brins.searchlib.activity


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_SEARCH_HOT
import com.brins.baselib.route.RouterHub.Companion.SEARCHACTIVITY
import com.brins.baselib.utils.KeyboardUtils
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.AlphaLinearLayout
import com.brins.baselib.widget.CommonHeaderView
import com.brins.baselib.widget.FlowLayout
import com.brins.baselib.widget.TagAdapter
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.search.HotSearchResult
import com.brins.networklib.model.search.SearchSuggestResult
import com.brins.searchlib.R
import com.brins.searchlib.adapter.SearchViewPagerAdapter
import com.brins.searchlib.contract.SearchContract
import com.brins.searchlib.fragment.SearchResultFragment
import com.brins.searchlib.presenter.SearchPresenter
import com.brins.searchlib.widget.ScaleTransitionPagerTitleView
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import javax.inject.Inject

@Route(path = SEARCHACTIVITY)
@AndroidEntryPoint
class SearchActivity : BaseMvpActivity<SearchPresenter>(), SearchContract.View, TextWatcher,
    AdapterView.OnItemClickListener {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mHis = arrayListOf<String>()
    private var mTagAdapter: TagAdapter<String>? = null
    private var mSearchSuggest: Array<String?> = emptyArray()
    private var mTitleList: ArrayList<String> =
        arrayListOf("单曲", "专辑", "歌手", "歌单", "mv", "电台", "用户")

    @Inject
    lateinit var mSearchAdapter: SearchViewPagerAdapter

    companion object {
        val STATUS_SHOW_HOT = 1
        val STATUS_SHOW_SUGGEST = 2
        val STATUS_SHOW_RESULT = 3
    }

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
                val search = et_search.text?.trim()
                startSearch(search.toString())

            }
            true


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
            startSearch(mHis[position])
            true
        }
        list_item.onItemClickListener = this
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
        initViewPager()
    }

    private fun initViewPager() {
        vp_search.offscreenPageLimit = 5
        vp_search.adapter = mSearchAdapter
        val commonNavigator = CommonNavigator(this)
        commonNavigator.setScrollPivotX(0.25f)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = mTitleList[index]
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.textSize = 18f
                simplePagerTitleView.setOnClickListener {
                    vp_search.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int {
                return mTitleList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_EXACTLY
                linePagerIndicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                linePagerIndicator.setColors(Color.BLACK)
                return linePagerIndicator

            }

        }
        magic_indicator.navigator = commonNavigator
        val titleContainer =
            commonNavigator.titleContainer
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerPadding = UIUtil.dip2px(this, 15.0)
        titleContainer.dividerDrawable = UIUtils.getDrawable(R.drawable.base_bg_simple_divider)
        ViewPagerHelper.bind(magic_indicator, vp_search)

    }

    /**
     * 开始搜索
     *
     * @param search
     * @return
     */
    private fun startSearch(search: String): Boolean {
        KeyboardUtils.hideSoftInput(this@SearchActivity)
        return if (!search.isNullOrEmpty()) {
            mPresenter?.addHistorySearch(search)
            onAddHistorySearch(search)
            et_search.removeTextChangedListener(this)
            et_search.setText(search)
            et_search.setSelection(search.length)
            et_search.addTextChangedListener(this)
            changeVisibility(STATUS_SHOW_RESULT)
            mSearchAdapter.getList()?.forEach {
                (it as SearchResultFragment).keyWords = search

            }

            true
        } else {
            false
        }
    }

    private fun changeVisibility(status: Int) {
        when (status) {
            STATUS_SHOW_HOT -> {
                rl_history_search.visibility = View.VISIBLE
                rl_hot_search.visibility = View.VISIBLE
                rv_hot_search.visibility = View.VISIBLE
                list_item.visibility = View.GONE
                ll_magic.visibility = View.GONE
                vp_search.visibility = View.GONE
            }
            STATUS_SHOW_SUGGEST -> {
                rl_history_search.visibility = View.GONE
                rl_hot_search.visibility = View.GONE
                rv_hot_search.visibility = View.GONE
                ll_magic.visibility = View.GONE
                vp_search.visibility = View.GONE
                list_item.visibility = View.VISIBLE
            }
            STATUS_SHOW_RESULT -> {
                rl_history_search.visibility = View.GONE
                rl_hot_search.visibility = View.GONE
                rv_hot_search.visibility = View.GONE
                list_item.visibility = View.GONE
                vp_search.visibility = View.VISIBLE
                ll_magic.visibility = View.VISIBLE
            }
        }
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
        mHis.add(0, result)
        mTagAdapter?.addNewData(result)
    }

    override fun onHistorySearchClear() {
        mHis.clear()
        mTagAdapter?.clear()
    }

    override fun onSuggestSearchLoad(result: SearchSuggestResult) {
        result.result?.let {
            mSearchSuggest = arrayOfNulls(it.allMatch!!.size)
            for ((i, a) in it.allMatch!!.withIndex()) {
                mSearchSuggest[i] = a.keyword
            }
            changeVisibility(STATUS_SHOW_SUGGEST)
            list_item.adapter = ArrayAdapter<String>(
                this
                , android.R.layout.simple_list_item_1, mSearchSuggest
            )
        }
    }


    override fun afterTextChanged(s: Editable?) {
        s.let {
            if (it.toString().isNullOrEmpty()) {
                changeVisibility(STATUS_SHOW_HOT)
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

    inner class HotSearchAdapter() : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

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
                helper.getView<AlphaLinearLayout>(R.id.music_list_ll).setOnClickListener {
                    startSearch(item.searchWord)
                }
            }
        }

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (mSearchSuggest.isNotEmpty() && position < mSearchSuggest.size) {
            startSearch(mSearchSuggest[position].toString())
        }
    }

}