package com.inyongtisto.tokoonline.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.inyongtisto.tokoonline.model.User

class SharedPref(activity: FragmentActivity) {

    val login = "login"
    val name = "name"
    val phone = "phone"
    val jabatan = "jabatan"
    val email = "email"
    val level="level"
    val user = "users"
    val laporan = "laporan"
    val mypref = "MAIN_PRF"
    val sp: SharedPreferences

    init {
        sp = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean) {
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sp.getBoolean(login, false)
    }

    fun setUser(value: User) {
        val data: String = Gson().toJson(value, User::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser(): User? {
        val data:String = sp.getString(user, null) ?: return null

        return Gson().fromJson<User>(data, User::class.java)
    }








    fun setString(key: String, vlaue: String) {
        sp.edit().putString(key, vlaue).apply()
    }

    fun getString(key: String): String {
        return sp.getString(key, "")!!
    }


    fun clear(){
        sp.edit().clear().apply()
    }

}