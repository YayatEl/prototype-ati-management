package com.inyongtisto.tokoonline.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.inyongtisto.tokoonline.data.LaporanList


class Laporan {
 @SerializedName("laporan")
 var laporan: List<LaporanList>? = null
 lateinit var message: String
 var success = 0
    //@SerializedName("id")
   // @Expose
    //private var id: Int? = null
    //@SerializedName("codelaporan")
    //@Expose
   // var codelaporan: String? = null
    //@SerializedName("user")
    //@Expose
    //var user: String? = null
    //@SerializedName("jenislaporan")
     //@Expose
     //var jenislaporan: String? = null
     //@SerializedName("jeniskas")
     // @Expose
      //var jeniskas: String? = null




}