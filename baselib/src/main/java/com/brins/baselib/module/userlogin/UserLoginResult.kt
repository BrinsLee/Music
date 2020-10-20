package com.brins.baselib.module.userlogin

import com.google.gson.annotations.SerializedName

class UserLoginResult {
    var code : Int = 404
    @SerializedName("account")
    var account : UserAccountBean =
        UserAccountBean()
    @SerializedName("profile")
    var profile : UserProfileBean =
        UserProfileBean()

}