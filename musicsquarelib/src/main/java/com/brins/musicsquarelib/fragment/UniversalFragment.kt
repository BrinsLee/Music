package com.brins.musicsquarelib.fragment

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.utils.createRadialGradientBitmap
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.musicsquarelib.R
import com.brins.musicsquarelib.activity.MusicListSquareActivity
import com.brins.musicsquarelib.adapter.MusicListGridAdapter
import com.brins.musicsquarelib.contract.MusicSquareContract
import com.brins.musicsquarelib.presenter.MusicListSquarePresenter
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.musiclist.MusicListsResult
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_recommend.rv_music_list
import kotlinx.android.synthetic.main.fragment_universal.*

class UniversalFragment : BaseMvpFragment<MusicListSquarePresenter>(), MusicSquareContract.View {

    private var mResult: MusicListsResult? = null
    private var mAdapter: MusicListGridAdapter? = null
    var mPosition = 0

    fun getTitle(): String? {
        return arguments!!.getString("title")
    }

    fun getPosition(): Int {
        return arguments!!.getInt("position")
    }

    companion object {
        fun newInstance(title: String, position: Int): Fragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putInt("position", position)
            val fragment = UniversalFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutResID(): Int {
        return R.layout.fragment_universal
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        mAdapter = MusicListGridAdapter()
        val manager = GridLayoutManager(context, 3)
        manager.isSmoothScrollbarEnabled = true
        rv_music_list.layoutManager = manager
        rv_music_list.adapter = mAdapter
        rv_music_list.isNestedScrollingEnabled = false
        rv_music_list.setHasFixedSize(true)
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        ApiHelper.launch({ mPresenter?.loadMusicList("华语") }, {})

    }

    override fun onMusicListLoad(result: MusicListsResult?) {
        mResult = result
        result?.playlists?.let {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter?.setNewData(list)
            changeBackground()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume")
        changeBackground()
    }

    private fun changeBackground() {
        mResult?.playlists?.let {
            if (it[0].coverGradientDrawable == null) {
                GlideHelper.setRoundImageResource(iv_gone
                    , it[0].coverImgUrl, 2, object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.let {
                                Palette.from((it as BitmapDrawable).bitmap).generate {
                                    it?.let {
                                        when {
                                            it.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                                mResult!!.playlists!![0].coverGradientDrawable =
                                                    createRadialGradientBitmap(
                                                        context!!,
                                                        it.getDarkVibrantColor(Color.TRANSPARENT),
                                                        it.getVibrantColor(Color.TRANSPARENT)
                                                    )
                                                (mActivity as MusicListSquareActivity)
                                                    .setBackground(
                                                        mResult!!.playlists!![0].coverGradientDrawable!!
                                                    )
                                            }
                                            it.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                                mResult!!.playlists!![0].coverGradientDrawable =
                                                    createRadialGradientBitmap(
                                                        context!!,
                                                        it.getDarkMutedColor(Color.TRANSPARENT),
                                                        it.getMutedColor(Color.TRANSPARENT)
                                                    )
                                                (mActivity as MusicListSquareActivity)
                                                    .setBackground(
                                                        mResult!!.playlists!![0].coverGradientDrawable!!
                                                    )
                                            }
                                            else -> {
                                                mResult!!.playlists!![0].coverGradientDrawable =
                                                    createRadialGradientBitmap(
                                                        context!!,
                                                        it.getLightMutedColor(Color.TRANSPARENT),
                                                        it.getLightVibrantColor(Color.TRANSPARENT)
                                                    )
                                                (mActivity as MusicListSquareActivity)
                                                    .setBackground(
                                                        mResult!!.playlists!![0].coverGradientDrawable!!
                                                    )
                                            }
                                        }
                                    }
                                }
                            }

                            return true
                        }

                    })
            } else {
                (mActivity as MusicListSquareActivity)
                    .setBackground(
                        it[0].coverGradientDrawable!!
                    )
            }
        }
    }

    override fun useEventBus(): Boolean {
        return false
    }
}