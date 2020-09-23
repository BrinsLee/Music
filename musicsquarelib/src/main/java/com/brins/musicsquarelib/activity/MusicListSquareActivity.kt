package com.brins.musicsquarelib.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.musicsquarelib.R
import com.brins.musicsquarelib.presenter.MusicListSquarePresenter

class MusicListSquareActivity : BaseMvpActivity<MusicListSquarePresenter>() {



    override fun getLayoutResId(): Int {
        return R.layout.activity_music_list_square
    }
}