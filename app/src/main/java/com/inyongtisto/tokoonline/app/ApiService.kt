package com.inyongtisto.tokoonline.app


import com.inyongtisto.tokoonline.data.UsersList
import com.inyongtisto.tokoonline.model.Laporan
import com.inyongtisto.tokoonline.model.ResponModel
import com.inyongtisto.tokoonline.model.code
import com.inyongtisto.tokoonline.networking.ServerResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {



    @FormUrlEncoded
    @POST("login.php")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String

    ): Call<ResponModel>

    @GET("laporan.php?laporan")
    fun getLaporan(
    ): Call<Laporan>
    @GET("code.php")
    fun getcodeLaporan(
    ): Call<code>
    @Multipart
    @POST("upload_bukti.php")
    fun uploadbukti(
            @Header("Authorization") authorization: String?,
            @PartMap map: Map<String?, RequestBody?>?
    ): Call<ServerResponse>
    @Multipart
    @POST("uploadttd")
    fun uploadttd(
            @Header("Authorization") authorization: String?,
            @PartMap map: Map<String?, RequestBody?>?
    ): Call<ServerResponse>



    @FormUrlEncoded
    @POST("makelaporan.php")
    fun laporanmake(
            @Field("codelaporan") code: String,
            @Field("user") user: String,
            @Field("level") level: String,
            @Field("jenislaporan") jenislaporan: String,
            @Field("jeniskas") jeniskas: String,
            @Field("harga") harga: String,
            @Field("deskripsi") deskripsi: String

    ): Call<Laporan>



}