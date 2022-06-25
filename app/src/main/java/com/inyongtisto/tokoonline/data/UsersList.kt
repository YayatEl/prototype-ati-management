package com.inyongtisto.tokoonline.data

import com.google.gson.annotations.SerializedName
import com.inyongtisto.tokoonline.model.Users

/**
 * Created by Eyehunt Team on 12/06/18.
 */
class UsersList {
    @SerializedName("items")
    var users: List<Users>? = null
}