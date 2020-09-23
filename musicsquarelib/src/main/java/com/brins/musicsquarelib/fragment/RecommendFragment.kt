package com.brins.musicsquarelib.fragment

import android.os.Bundle
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.musicsquarelib.R
import com.brins.musicsquarelib.contract.MusicSquareContract
import com.brins.musicsquarelib.presenter.MusicListSquarePresenter
import com.brins.musicsquarelib.widget.PlayListCover
import com.brins.musicsquarelib.widget.PlayListPager
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.musiclist.MusicListsResult
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlin.math.min

class RecommendFragment : BaseMvpFragment<MusicListSquarePresenter>(), MusicSquareContract.View {


    override fun getLayoutResID(): Int {
        return R.layout.fragment_recommend
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        launch({ mPresenter?.loadMusicList() }, {})
    }

    override fun onMusicListLoad(result: MusicListsResult?) {
        result?.playlists?.let {

            for (i in 0..min(it.size, 2)) {
                val cover = PlayListCover(context!!)
                cover.apply {
                    setCover(it[i].coverImgUrl)
                    setText(it[i].name)
                    pager.addView(cover)
                    val lp: PlayListPager.PlayLayoutParams =
                        cover.layoutParams as PlayListPager.PlayLayoutParams
                    lp.from = i
                    lp.to = i
                    lp.index = i
                }
//                pager.setOnClickListener()
            }
        }
    }
}