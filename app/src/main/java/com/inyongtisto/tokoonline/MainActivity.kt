package com.inyongtisto.tokoonline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inyongtisto.tokoonline.fragment.AkunFragment
import com.inyongtisto.tokoonline.fragment.HomeFragment
import com.inyongtisto.tokoonline.helper.SharedPref
import kotlinx.android.synthetic.main.activity_main.*

import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView;
import com.inyongtisto.tokoonline.activity.LoginActivity
import com.inyongtisto.tokoonline.fragment.reportFragment
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mToggle : ActionBarDrawerToggle

    private val fragmentHome: Fragment = HomeFragment()

    private var fragmentAkun: Fragment = AkunFragment()
    private var fragmentReport: Fragment = reportFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView
    private lateinit var menunav: Menu
    private lateinit var menuItemnav: MenuItem
    private lateinit var sideNav:DrawerLayout

    private var mCameraFile: File? = null
    private var mGalleryFile: File? = null
    private var mProfileFile: File? = null


    private lateinit var s: SharedPref
    private lateinit var drawer: DrawerLayout
    private var dariDetail: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // memunculkan tombol burger menu
        s = SharedPref(this)
        if (s.getStatusLogin()==false){
            s.clear()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        drawer = findViewById(R.id.drawer_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        // tambahkan mToggle ke drawer_layout sebagai pengendali open dan close drawer
        drawer_layout.addDrawerListener(mToggle)

        mToggle.syncState()
        val navigationView: NavigationView = findViewById(R.id.nav_viewside)
        navigationView.setNavigationItemSelectedListener(this)
         fun onOptionsItemSelected(item: MenuItem): Boolean {
            return mToggle.onOptionsItemSelected(item)
        }


        setUpBottomNav()

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))

    }




    val mMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
        }
    }


    fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentReport).hide(fragmentReport).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)

        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    callFargment(0, fragmentHome)
                }

                R.id.navigation_report-> {
                    callFargment(1,fragmentReport )

                }

                R.id.navigation_akun -> {
                    callFargment(2, fragmentAkun)

                }
            }

            false
        }

    }


    fun callFargment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                callFargment(0, fragmentHome)
            }

            R.id.seelap-> {
                callFargment(1,fragmentReport )

            }

            R.id.profile-> {
                callFargment(2, fragmentAkun)

            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


}
