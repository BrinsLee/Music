package com.brins.home.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.route.RouterHub.Companion.MUSICLISTSQUAREACTIVITY
import com.brins.home.R
import com.brins.home.adapter.BaseHomeAdapter
import com.brins.home.contract.HomeContract
import com.brins.home.presenter.HomePresenter
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.brins.networklib.model.daily.DailyData
import com.brins.baselib.module.MusicList
import com.brins.networklib.model.personal.PersonalizedMusicList
import com.brins.networklib.model.personal.PersonalizedMusics
import com.brins.networklib.model.personal.PersonalizedResult
import com.brins.networklib.model.recommend.RecommendResult
import com.brins.networklib.model.title.SingleTitleData
import com.brins.networklib.model.title.SingleTitleMoreData
import com.chad.library.adapter.base.model.BaseData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment_home.*
import java.net.ConnectException

@AndroidEntryPoint
@Route(path = RouterHub.HOMEFRAGMENT)
class HomeFragment : BaseMvpFragment<HomePresenter>(), HomeContract.View {

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var mAdapter: BaseHomeAdapter

    /*    @Inject
        lateinit var mPresenter: HomePresenter*/
    override fun getLayoutResID(): Int {
        return R.layout.home_fragment_home
    }

    override fun init(savedInstanceState: Bundle?) {
        mAdapter = BaseHomeAdapter(base_scroll_view, manager = childFragmentManager)
    }

    override fun reLoad() {
        loadData()
    }

    /**
     * 推荐音乐列表
     *
     * @param data
     */
    override fun onPersonalizedMusicListLoad(data: PersonalizedResult<PersonalizedMusicList>?) {
        data?.let {
            val list = mutableListOf<BaseData>()
            list.add(SingleTitleData().setTitle("根据你喜欢的歌曲推荐"))
            list.add(it)
            list.add(
                SingleTitleMoreData().setTitle("晚霞灿烂，音乐惬意")
                    .setListener(View.OnClickListener {
                        ARouterUtils.go(MUSICLISTSQUAREACTIVITY)
                    })
            )
            list.add(PersonalizedMusics())
            list.add(AlbumResult<NewestAlbum>())
            mAdapter.addData(list)
            launch({ mPresenter?.loadHotOrNewRecommend("hot") }, {})
        }
    }

    /**
     * 横幅广告
     *
     * @param data
     */
    override fun onBannerLoad(data: BaseData?) {

        data?.let {
            val list = mutableListOf<BaseData>()
            list.add(it)
            list.add(DailyData())
            mAdapter.addData(list)
        }
        launch({
            mPresenter?.loadPersonalizedMusicList()
        }, {})
    }

    /**
     * 最新推荐
     *
     * @param data
     */
    override fun onNewRecommendLoad(data: RecommendResult<MusicList>?) {
        data?.let {
            val list = mutableListOf<BaseData>()
            list.add(SingleTitleData().setTitle("最新推荐"))
            list.add(it)
            mAdapter.addData(list)
        }
    }

    /**
     * 热门推荐
     *
     * @param data
     */
    override fun onHotRecommendLoad(data: RecommendResult<MusicList>?) {
        data?.let {
            val list = mutableListOf<BaseData>()
            list.add(SingleTitleData().setTitle("热门推荐"))
            list.add(it)
            mAdapter.addData(list)
        }
        launch({ mPresenter?.loadHotOrNewRecommend("new") }, {})
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mAdapter.setOnLoadDataListener { p0, p1, p2 ->
            loadData()
            p2.onLoadDataFail()
        }
        recommendList.adapter = mAdapter
        recommendList.layoutManager = LinearLayoutManager(getMyContext())
    }

    private fun loadData() {
        mAdapter.clear()
        launch({
            mPresenter?.loadBannerData()
        }, {
            if (it is ConnectException) {
                showError()
            }
        })
    }

}