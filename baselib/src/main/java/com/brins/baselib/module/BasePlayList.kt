package com.brins.baselib.module

import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import java.util.*

open class BasePlayList() : BaseData() {

    /**
     * 播放下一首
     *
     * @return
     */
    fun last(): BaseMusic? {
        if (songs.isEmpty()) {
            return null
        }
        when (playMode) {
            PlayMode.LOOP, PlayMode.LIST, PlayMode.SINGLE, PlayMode.HEART -> {
                var newIndex = playingIndex - 1
                if (newIndex < 0) {
                    newIndex = songs.size - 1
                }
                playingIndex = newIndex
            }
            PlayMode.SHUFFLE -> playingIndex = randomPlayIndex()
        }
        return songs[playingIndex]
    }

    /**
     * 播放下一首
     *
     * @return
     */
    operator fun next(): BaseMusic? {
        if (songs.isEmpty()) {
            return null
        }
        when (playMode) {
            PlayMode.LOOP, PlayMode.LIST, PlayMode.HEART -> {
                var newIndex = playingIndex + 1
                if (newIndex >= songs.size) {
                    newIndex = 0
                }
                playingIndex = newIndex
            }
            PlayMode.SINGLE -> {
            }
            PlayMode.SHUFFLE -> playingIndex = randomPlayIndex()
        }
        return songs[playingIndex]
    }

    /**
     * 随机播放
     *
     * @return
     */
    private fun randomPlayIndex(): Int {
        val randomIndex = Random().nextInt(songs.size)
        // Make sure not play the same song twice if there are at least 2 data
        if (songs.size > 1 && randomIndex == playingIndex) {
            randomPlayIndex()
        }
        return randomIndex
    }

    fun setPlayMode(mode: PlayMode) {
        this.playMode = mode
    }

    fun getPlayMode(): PlayMode {
        return playMode!!
    }


    fun add(music: BaseMusic) {
        if (!songs.contains(music)) {
            songs.forEach {
                if (it.id == music.id) {
                    playingIndex = songs.indexOf(it)
                    return
                }
            }
            songs.add(music)
            numOfSongs = songs.size
            playingIndex = songs.lastIndex
        } else {
            playingIndex = songs.indexOf(music)
        }
    }

    fun add(list: MutableList<BaseMusic>) {
        songs.clear()
        songs.addAll(list)
        numOfSongs = songs.size
    }

    fun delete(music: BaseMusic): Boolean {
        if (songs.contains(music)) {
            if (songs.size == 1) {
                return false
            }
            return songs.remove(music)
        }
        return false
    }

    fun deleteAll(): Boolean {
        val song = getCurrentSong()
        val iterator: MutableIterator<BaseMusic> = songs.iterator()
        while (iterator.hasNext()) {
            val music = iterator.next()
            if (music != song) {
                iterator.remove()
            }
        }
        playingIndex = songs.lastIndex
        return true
    }

    fun prepare(): Boolean {
        if (songs.isEmpty()) return false
        playingIndex = if (playingIndex == NO_POSITION) {
            0
        } else {
            playingIndex
        }
        return true
    }

    fun getCurrentSong(): BaseMusic? {
        return if (playingIndex != NO_POSITION) {
            songs[playingIndex]
        } else null
    }

    fun getPlayingIndex(): Int {
        return playingIndex
    }

    fun setPlayingIndex(index: Int) {
        this.playingIndex = index
    }

    fun getNumOfSongs(): Int {
        return numOfSongs
    }

    fun hasNext(fromComplete: Boolean): Boolean {
        if (songs.isEmpty()) return false
        if (fromComplete) {
            if (playMode === PlayMode.LIST && playingIndex + 1 >= songs.size) return false
        }
        return true
    }

    fun getSong(): ArrayList<BaseMusic> = songs

    @NonNull
    private var id: Int = 0

    private var name: String = ""

    protected var favorite: Boolean = false

    private var playingIndex = -1

    private var songs: ArrayList<BaseMusic> = arrayListOf()


    private var numOfSongs: Int = songs.size

    private var playMode: PlayMode? = PlayMode.LOOP
    override val itemType: Int
        get() = ITEM_BASE_PLAYLIST

}