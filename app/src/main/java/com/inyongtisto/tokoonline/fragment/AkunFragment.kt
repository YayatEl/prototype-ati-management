package com.inyongtisto.tokoonline.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.inyongtisto.tokoonline.R
import com.inyongtisto.tokoonline.activity.LoginActivity
import com.inyongtisto.tokoonline.helper.SharedPref

/**
 * A simple [Fragment] subclass.
 */
class AkunFragment : Fragment() {

    lateinit var s: SharedPref
    lateinit var btnLogout: TextView
    lateinit var tvNama: TextView
    lateinit var tvEmail: TextView
    lateinit var tvPhone: TextView
    lateinit var tvJabatan: TextView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)
     tvNama=view.findViewById(R.id.tv_nama)
        s = SharedPref(requireActivity())

        btnLogout = view.findViewById(R.id.btn_logout)

        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
            s.clear()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

        }

        tvNama.text = s.name
        return view
    }




      //  tvEmail.text = user.email
      //tvPhone.text = user.phone
       //


    private fun init(view: View) {

    }


}
