package com.brins.radiolib.activity


import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvvmActivity
import com.brins.baselib.module.*
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.route.RouterHub.Companion.RADIODETAILACTIVITY
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CommonHeaderView
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.radio.DjProgram
import com.brins.networklib.model.radio.Radio
import com.brins.radiolib.R
import com.brins.radiolib.viewmodel.RadioViewModel
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_radio_detail.*

@Route(path = RADIODETAILACTIVITY)
class RadioDetailActivity : BaseMvvmActivity<RadioViewModel>() {

    var mPos: Int = -1

    @Autowired(name = "mRadioId")
    lateinit var mRadioId: String

    private var mRadio: Radio? = null

    private var mRadioProgramDataObserver: Observer<MutableList<DjProgram>>? = null

    private var mAdapter: RadioDetailAdapter? = null

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return RadioViewModel.getInstance(application)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_radio_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        if (::mRadioId.isInitialized) {
            mRadio = mViewModel?.getRadioByRid(mRadioId)
            mRadio?.let {
                mAdapter = RadioDetailAdapter()
                musicRecycler.adapter = mAdapter
                musicRecycler.layoutManager = LinearLayoutManager(this)
                GlideHelper.setRoundImageResource(
                    coverMusicList,
                    it.picUrl,
                    15,
                    R.drawable.base_icon_default_cover
                )
                GlideHelper.setBlurImageResource(
                    cover,
                    it.picUrl,
                    15f,
                    R.color.white
                )
                it.dj?.let { profile ->
                    GlideHelper.setCircleImageResource(
                        avatar,
                        profile.avatarUrl
                    )
                }
                mRadioProgramDataObserver = Observer { djProgram ->
                    val list = mutableListOf<BaseData>()
                    list.addAll(djProgram)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getProgramLiveData()?.observe(this, mRadioProgramDataObserver!!)
                ApiHelper.launch({
                    mViewModel?.getRadioProgram(mRadioId)
                }, { ToastUtils.show(getString(R.string.load_fail), Toast.LENGTH_SHORT) })
            }
        }
    }

    class RadioDetailAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {
        init {
            addItemType(ITEM_RADIO_PROGRAM_DATA, R.layout.radio_item_music)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            if (item.itemType == ITEM_RADIO_PROGRAM_DATA) {
                helper.setText(R.id.tv_music_num, "${helper.adapterPosition}")
                helper.setText(R.id.tv_music_name, (item as DjProgram).name)
                helper.setText(R.id.tv_music_artist, getArtists(item.mainSong))

                helper.getView<LinearLayout>(R.id.music_list_ll).setOnClickListener{
                    EventBusManager.post(EventBusKey.KEY_EVENT_CHANGE_PLAYMODE, PlayMode.LOOP)
                    playMusic(helper.adapterPosition)
                }
            }
        }

        private fun playMusic(pos: Int) {
            val musicList = mutableListOf<BaseMusic>()
            data.forEach {
                musicList.add((it as DjProgram).mainSong as BaseMusic)
            }
            EventBusManager.post(
                EventBusKey.KEY_EVENT_PERSONALIZED_MUSIC,
                musicList,
                "$pos"
            )
        }

        private fun getArtists(data: BaseMusic?): String {
            val builder = StringBuilder()
            if (data != null) {
                data.artists?.let {
                    for (i in 0 until it.size) {
                        builder.append(it[i].name)
                        if (i != it.size - 1)
                            builder.append("，")
                    }
                }
            }
            return builder.toString()
        }

    }
}