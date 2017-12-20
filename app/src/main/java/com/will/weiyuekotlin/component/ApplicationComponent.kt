package com.will.weiyuekotlin.component

import android.content.Context
import com.will.weiyuekotlin.MyApp

import com.will.weiyuekotlin.module.ApplicationModule
import com.will.weiyuekotlin.module.HttpModule
import com.will.weiyuekotlin.net.JanDanApi
import com.will.weiyuekotlin.net.NewsApi

import dagger.Component

/**
 * desc: .
 * author: Will .
 * date: 2017/9/2 .
 */
@Component(modules = [(ApplicationModule::class), (HttpModule::class)])
interface ApplicationComponent {

    val application: MyApp

    val context: Context

    fun getNetEaseApi(): NewsApi

    fun getJanDanApi(): JanDanApi


}
