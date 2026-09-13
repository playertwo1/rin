package com.playertwo1.rin

import android.app.Application
import com.playertwo1.rin.core.di.AppContainer
import com.playertwo1.rin.core.di.DefaultAppContainer

class RinApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
