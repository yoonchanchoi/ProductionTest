package com.example.productiontest

import android.app.Application

class ProductApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }
}