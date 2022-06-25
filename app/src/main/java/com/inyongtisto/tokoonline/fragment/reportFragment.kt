package com.inyongtisto.tokoonline.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.inyongtisto.tokoonline.R

import com.inyongtisto.tokoonline.app.Api

import com.inyongtisto.tokoonline.data.Leagues
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.inyongtisto.tokoonline.data.UsersList
import com.inyongtisto.tokoonline.helper.SharedPref
import kotlinx.android.synthetic.main.fragment_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.widget.ArrayAdapter
import com.inyongtisto.tokoonline.app.ApiConfig
import com.inyongtisto.tokoonline.data.LaporanList
import com.inyongtisto.tokoonline.model.Laporan
import com.inyongtisto.tokoonline.model.ResponModel
import kotlinx.android.synthetic.main.activity_login.*


/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION", "UNUSED_VARIABLE")
class reportFragment : Fragment() {
    lateinit var tvjenislaporan: TextView
    var jenislap=""
    var dataList = ArrayList<Leagues>()
    lateinit var s: SharedPref
    lateinit var tvjeniskas: TextView
    lateinit var pb: ProgressBar
    lateinit var tvPhone: TextView
    lateinit var spinner_view: Spinner
    val BASE_URL = "https://api.github.com/search/"
    var tv_user: TextView? = null
    var str:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_report, container, false)
        tvjenislaporan = view.findViewById(R.id.jenislaporan)
        tvjeniskas = view.findViewById(R.id.jeniskas)
        tv_user = view.findViewById(R.id.tv_user)
        spinner_view=view.findViewById(R.id.spinner_view)

        pb = view.findViewById(R.id.pb)
       getUsers()

        return view
    }



    fun getUsers() {
        pb.visibility = View.VISIBLE
        var call= ApiConfig.instanceRetrofit.getLaporan()

        call.enqueue(object : Callback<Laporan> {

            override fun onResponse(call: Call<Laporan>?, response: Response<Laporan>?) {
                val respon = response?.body()!!
                pb.visibility = View.GONE

                    var data = respon.laporan

                   var length = data!!.size

                   for (i in 0 until length) {
                        str = str + "\n" + data.get(i).codelaporan
                       tv_user!!.text =str

                }


                val users = arrayOf("1","2")
                val searchmethod = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, users) }
                searchmethod?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_view.adapter = searchmethod
                spinner_view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        tvjenislaporan.text = "Code Selected : ${parent.getItemAtPosition(position).toString()}"
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }

            override fun onFailure(call: Call<Laporan>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(activity, "Error:"+t.message, Toast.LENGTH_SHORT).show()
            }




        })
    }

}





