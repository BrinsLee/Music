package com.brins.networklib.helper

import android.util.Log
import com.brins.baselib.config.BASEURL
import com.brins.baselib.utils.GsonUtils
import com.brins.baselib.utils.LogUtils
import com.brins.networklib.RetrofitFactory
import com.brins.networklib.service.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object ApiHelper {

    private var mPersonalizedService: PersonalizedService? = null
    private var mMusicService: MusicService? = null
    private var mMusicListService: MusicListService? = null
    private var mAlbumService: AlbumService? = null
    private var mLoginService: LoginService? = null
    private var mSearchService: SearchService? = null
    private var mMineService: MyService? = null
    private var mFindService: FindService? = null
    private var mEventDetailService: EventDetailService? = null
    private var mMusicVideoService: MusicVideoService? = null


    fun getEventDetailService(): EventDetailService {
        if (mEventDetailService == null) {
            synchronized(EventDetailService::class.java) {
                if (mEventDetailService == null) {
                    mEventDetailService = RetrofitFactory.newRetrofit(
                        BASEURL
                    ).create(EventDetailService::class.java)
                }
            }
        }
        return mEventDetailService!!
    }

    fun getPersonalizedService(): PersonalizedService {
        if (mPersonalizedService == null) {
            synchronized(PersonalizedService::class.java) {
                if (mPersonalizedService == null) {
                    mPersonalizedService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(PersonalizedService::class.java)
                }
            }
        }
        return mPersonalizedService!!
    }

    fun getMusicService(): MusicService {
        if (mMusicService == null) {
            synchronized(MusicService::class.java) {
                if (mMusicService == null) {
                    mMusicService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(MusicService::class.java)
                }
            }
        }
        return mMusicService!!
    }

    fun getMusicListService(): MusicListService {
        if (mMusicListService == null) {
            synchronized(MusicListService::class.java) {
                if (mMusicListService == null) {
                    mMusicListService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(MusicListService::class.java)
                }
            }
        }
        return mMusicListService!!
    }

    fun getAlbumService(): AlbumService {
        if (mAlbumService == null) {
            synchronized(AlbumService::class.java) {
                if (mAlbumService == null) {
                    mAlbumService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(AlbumService::class.java)
                }
            }
        }
        return mAlbumService!!
    }

    fun getLoginService(): LoginService {
        if (mLoginService == null) {
            synchronized(LoginService::class.java) {
                if (mLoginService == null) {
                    mLoginService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(LoginService::class.java)
                }
            }
        }
        return mLoginService!!
    }

    fun getSearchService(): SearchService {
        if (mSearchService == null) {
            synchronized(SearchService::class.java) {
                if (mSearchService == null) {
                    mSearchService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(SearchService::class.java)
                }
            }
        }
        return mSearchService!!
    }

    fun getMineService(): MyService {
        if (mMineService == null) {
            synchronized(MyService::class.java) {
                if (mMineService == null) {
                    mMineService = RetrofitFactory.newRetrofit(
                        BASEURL
                    )
                        .create(MyService::class.java)
                }
            }
        }
        return mMineService!!
    }

    fun getFindService(): FindService {
        if (mFindService == null) {
            synchronized(FindService::class.java) {
                if (mFindService == null) {
                    mFindService = RetrofitFactory.newRetrofit(BASEURL)
                        .create(FindService::class.java)
                }
            }
        }
        return mFindService!!
    }

    fun getMusicVideoService(): MusicVideoService {
        if (mMusicVideoService == null) {
            synchronized(MusicVideoService::class.java) {
                if (mMusicVideoService == null) {
                    mMusicVideoService = RetrofitFactory.newRetrofit(BASEURL)
                        .create(MusicVideoService::class.java)
                }
            }
        }
        return mMusicVideoService!!
    }

    suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.d("OkHttp", "error: " + t.message)
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    LogUtils.d(
                        "await",
                        "message: ${response.message()}**** response: ${GsonUtils.toJson(body)}"
                    )
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) = CoroutineScope(
        Dispatchers.Main
    ).launch {
        try {
            block()
        } catch (e: Throwable) {
            try {
                error(e)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
