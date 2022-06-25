package com.inyongtisto.tokoonline.fragment

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by JC on 17/07/2017.
 */
class Application : Application() {

    companion object {
        lateinit var instance: com.inyongtisto.tokoonline.fragment.Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Fresco.initialize(this)
    }
}
