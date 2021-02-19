package com.brins.loacl.model

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.string2Bitmap
import com.brins.loacl.R
import com.brins.loacl.contarct.LocalContract
import java.io.ByteArrayOutputStream

/**
 * Created by lipeilin
 * on 2021/2/18
 */
class LocalModel : LocalContract.Model {

    private var URL_LOAD_LOCAL_MUSIC: Int = 0
    private val TAG = "LocalModel"

    override fun loadLocalMusic(
        manager: LoaderManager,
        callback: LoaderManager.LoaderCallbacks<Cursor>
    ) {
        manager.initLoader(URL_LOAD_LOCAL_MUSIC, null, callback)
    }

    override fun onDestroy() {

    }

    fun cursorToMusic(cursor: Cursor): BaseMusic {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
        val name =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
        val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
        val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
        val size = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
        val url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) ?: ""
        val artists = BaseMusic.Artist()
        artists.name = artist
        val cover = if (url.isNotEmpty()) loadingCover(url) else ""
        val bitmap = string2Bitmap(cover)
        val blurBitmap = string2Bitmap(cover)
        val albums = BaseMusic.Album()
        albums.name = album
        val song = BaseMusic.Song()
        song.apply {
            this.name = name
            this.artists = arrayListOf(artists)
            this.album = albums
        }

        val baseMusic = BaseMusic()
        baseMusic.apply {
            this.id = id.toString()
            this.name = title
            this.duration = duration
            this.artists = arrayListOf(artists)
            this.musicUrl = url
            this.bitmapCover = bitmap
            this.blurBitmap = blurBitmap
            this.song = song
        }

        return baseMusic
    }

    private fun loadingCover(mediaUri: String): String {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(mediaUri)
        val picture = mediaMetadataRetriever.embeddedPicture
        return if (picture == null) getStringCover() else Base64.encodeToString(
            picture,
            Base64.DEFAULT
        )
    }

    private fun getStringCover(bitmap: Bitmap? = null): String {
        val btStream = ByteArrayOutputStream()
        val bitmapTemp = bitmap ?: BitmapFactory.decodeResource(
            UIUtils.getContext().resources,
            R.drawable.base_icon_default_cover
        )
        bitmapTemp.compress(Bitmap.CompressFormat.PNG, 100, btStream)
        val resultpicture = btStream.toByteArray()
        return Base64.encodeToString(resultpicture, Base64.DEFAULT)
    }

}