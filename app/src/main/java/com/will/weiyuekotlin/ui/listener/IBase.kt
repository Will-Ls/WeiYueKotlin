package com.will.weiyuekotlin.ui.listener

import android.os.Bundle
import android.view.View
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.widget.SimpleMultiStateView


/**
 * desc: .
 * author: Will .
 * date: 2017/11/27 .
 */
interface IBase {

    /**
     * 获取页面布局 id
     */
    fun getContentLayout(): Int

    /**
     * 获取状态布局
     */
    fun getSimpleMultiStateView(): SimpleMultiStateView?

    /**
     * Dagger2 注入
     */
    fun initInjector(appComponent: ApplicationComponent)

    /**
     * 初始化布局
     */
    fun bindView(view: View, savedInstanceState: Bundle?)

    /**
     * 加载数据
     */
    fun initData()
}