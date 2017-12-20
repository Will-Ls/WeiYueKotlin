package com.will.weiyuekotlin.ui.news

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.NewsArticleBean
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.component.DaggerHttpComponent
import com.will.weiyuekotlin.ui.base.BaseActivity
import com.will.weiyuekotlin.ui.news.contract.ArticleReadContract
import com.will.weiyuekotlin.ui.news.presenter.ArticleReadPresenter
import com.will.weiyuekotlin.utils.ImageLoaderUtil
import com.will.weiyuekotlin.utils.getTimestampString
import com.will.weiyuekotlin.utils.string2Date
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import kotlinx.android.synthetic.main.activity_artcleread.*




/**
 * desc: 新闻阅读 .
 * author: Will .
 * date: 2017/12/8 .
 */

class ArticleReadActivity : BaseActivity<ArticleReadPresenter>(), ArticleReadContract.View {

    companion object {
        fun launch(context: Context, aid: String) {
            val intent = Intent(context, ArticleReadActivity::class.java)
            intent.putExtra("aid", aid)
            context.startActivity(intent)
        }
    }

    override fun getContentLayout(): Int = R.layout.activity_artcleread

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = simpleMultiStateView

    override fun initInjector(appComponent: ApplicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        setWebViewSetting()
        setStatusBarColor(ContextCompat.getColor(this, R.color.statusBar), 30)
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            when {
                scrollY > constraintLayout.height -> rl_top.visibility = View.VISIBLE
                else -> rl_top.visibility = View.GONE
            }
        })
        iv_back.setOnClickListener { finish() }
    }

    override fun initData() {}

    /**
     * 加载新闻内容
     *
     * @param articleBean 新闻内容
     */
    override fun loadData(articleBean: NewsArticleBean?) {
        articleBean?.let({
            tv_title.text = it.body?.title
            tv_TopUpdateTime.text = it.body?.author
            tv_updateTime.text = getTimestampString(string2Date(it.body?.updateTime!!, "yyyy/MM/dd HH:mm:ss"))

            if (it.body?.subscribe != null) {
                ImageLoaderUtil.LoadImage(this, it.body?.subscribe!!.logo, iv_logo, RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                ImageLoaderUtil.LoadImage(this, it.body?.subscribe!!.logo, iv_topLogo, RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                tv_topname.text = it.body?.subscribe?.cateSource
                tv_name.text = it.body?.subscribe?.cateSource
                tv_TopUpdateTime.text = it.body?.subscribe?.catename
            } else {
                tv_topname.text = it.body?.source
                tv_name.text = it.body?.source
                tv_TopUpdateTime.text = if (!TextUtils.isEmpty(articleBean.body?.author)) articleBean.body?.author else articleBean.body?.editorcode
            }

            webView.post({
                val content = articleBean.body?.text
                val url = "javascript:show_content(\'$content\')"
                webView.loadUrl(url)
                showSuccess()
            })
        })

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSetting() {
        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.loadsImagesAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.loadUrl("file:///android_asset/ifeng/post_detail.html")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val aid = intent.getStringExtra("aid")
                mPresenter?.getData(aid)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.hasNestedScrollingParent()
            webView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
        }
//        webView.startSafeBrowsing(this, ValueCallback<Boolean> { success ->
//            mSafeBrowsingIsInitialized = true
//            if (!success) {
//                Log.e("MY_APP_TAG", "Unable to initialize Safe Browsing!")
//            }
//        })
    }

}