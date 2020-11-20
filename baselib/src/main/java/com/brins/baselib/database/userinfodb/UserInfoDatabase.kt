package com.brins.baselib.database.userinfodb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brins.baselib.module.userlogin.UserAccountBean
import com.brins.baselib.module.userlogin.UserProfileBean


@Database(
    entities = arrayOf(UserAccountBean::class, UserProfileBean::class),
    version = 1,
    exportSchema = false
)
abstract class UserInfoDatabase : RoomDatabase() {
    abstract fun dao(): UserInfoDao
}