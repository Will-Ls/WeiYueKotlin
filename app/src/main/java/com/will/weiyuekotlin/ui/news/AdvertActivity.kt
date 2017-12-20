package com.will.weiyuekotlin.ui.news

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.ui.base.BaseActivity
import com.will.weiyuekotlin.ui.base.BaseContract
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import kotlinx.android.synthetic.main.activity_advert.*


/**
 * desc: 广告 .
 * author: Will .
 * date: 2017/12/7 .
 */

class AdvertActivity : BaseActivity<BaseContract.BasePresenter>() {

    companion object {
        fun launch(context: Context, url: String) {
            val intent = Intent(context, AdvertActivity::class.java)
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }

    override fun getContentLayout(): Int = R.layout.activity_advert

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.statusBar), 30)
        getSetting(wv_advert)
        wv_advert.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }

        wv_advert.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (pb_progress == null) {
                    return
                }
                if (newProgress == 100) {
                    pb_progress.visibility = View.GONE
                } else {
                    pb_progress.visibility = View.VISIBLE
                    pb_progress.progress = newProgress
                }
            }
        }

        iv_back.setOnClickListener { finish() }
    }

    override fun initData() {
        if (intent == null) return
        val url = intent.getStringExtra("url")
        if (!TextUtils.isEmpty(url)) {
            wv_advert.loadUrl(url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getSetting(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.useWideViewPort = false
        webView.settings.setAppCacheEnabled(true)
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK//LOAD_CACHE_ELSE_NETWORK模式下，无论是否有网络，只要本地有缓存，都使用缓存。本地没有缓存时才从网络上获取
        webView.settings.domStorageEnabled = true
        webView.setOnLongClickListener { true }
    }
}