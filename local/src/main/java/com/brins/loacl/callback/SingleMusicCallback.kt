package com.brins.loacl.callback

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.brins.baselib.config.MEDIA_URI
import com.brins.baselib.config.ORDER_BY
import com.brins.baselib.config.PROJECT_LOCAL
import com.brins.baselib.utils.UIUtils

/**
 * Created by lipeilin
 * on 2021/2/19
 */
abstract class SingleMusicCallback : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val loader = CursorLoader(
            UIUtils.getContext(), MEDIA_URI,
            PROJECT_LOCAL,
            MediaStore.Audio.Media.IS_MUSIC + "=1 AND "
                    + MediaStore.Audio.Media.SIZE + ">0",
            null,
            ORDER_BY
        )
        return loader
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}