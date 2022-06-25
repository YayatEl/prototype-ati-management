package com.inyongtisto.tokoonline.model

import com.google.gson.annotations.SerializedName

class ResponModel {
    var success = 0
    lateinit var message: String

    var user = User()

    var laporan=Laporan()

}