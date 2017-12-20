package com.will.weiyuekotlin.ui.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.NewsArticleBean
import com.will.weiyuekotlin.bean.NewsDetail
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.component.DaggerHttpComponent
import com.will.weiyuekotlin.net.ApiConstants
import com.will.weiyuekotlin.ui.base.BaseImageDrowseActivity
import com.will.weiyuekotlin.ui.news.contract.ArticleReadContract
import com.will.weiyuekotlin.ui.news.presenter.ArticleReadPresenter
import com.will.weiyuekotlin.utils.StatusBarUtil
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import kotlinx.android.synthetic.main.activity_imagebrowse.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat


/**
 * desc: 图片浏览 .
 * author: Will .
 * date: 2017/12/8 .
 */

class ImageBrowseActivity : BaseImageDrowseActivity<ArticleReadPresenter>(), ArticleReadContract.View {
    private var isShow = true
    private var newsArticleBean: NewsArticleBean? = null

    companion object {
        private val AID = "aid"
        private val ISCMPP = "isCmpp"
        fun launch(context: Activity, bodyBean: NewsDetail.ItemBean) {
            val intent = Intent(context, ImageBrowseActivity::class.java)
            when {
                bodyBean.id!!.contains(ApiConstants.sGetNewsArticleCmppApi) || bodyBean.documentId!!.startsWith("cmpp") -> intent.putExtra(ISCMPP, true)
                else -> intent.putExtra(ISCMPP, false)
            }
            intent.putExtra(AID, bodyBean.documentId)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun getContentLayout(): Int = R.layout.activity_imagebrowse

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, android.R.color.black))
        scrollview.background.mutate().alpha = 100
        rl_top.background.mutate().alpha = 100
        btn_titlebar_left.setOnClickListener { finish() }
        initView(relativeLayout, swipeBackLayout, viewPager)
    }

    /**
     * ViewPager 滑动时更新文本内容
     *
     * @param position 下标
     */
    override fun setTvInfo(position: Int) {
        newsArticleBean?.let {
            tv_info.text = String.format(resources.getString(R.string.description), position + 1, it.body!!.slides!!.size, it.body!!.slides!![position].description)
        }
    }

    /**
     * 图片上下拖动时回调
     *
     * @param fractionAnchor relative to the anchor.
     */
    override fun onViewPositionChanged(fractionAnchor: Float) {
        super.onViewPositionChanged(fractionAnchor)
        val df = NumberFormat.getInstance() as DecimalFormat
        df.maximumFractionDigits = 1
        df.roundingMode = RoundingMode.HALF_UP
        val dd = df.format(fractionAnchor.toDouble())
        val alpha = 1 - (java.lang.Float.valueOf(dd)!! + 0.8)
        when {
            fractionAnchor == 0f && isShow -> {
                setViewVisible(View.VISIBLE, scrollview, rl_top)
            }
            else -> if (alpha == 0.0) {
                setViewVisible(View.GONE, scrollview, rl_top)
            } else {
                if (rl_top.visibility != View.GONE) {
                    rl_top.alpha = alpha.toFloat()
                    scrollview.alpha = alpha.toFloat()
                }
            }
        }
    }

    /**
     * 图片点击回调
     */
    override fun onPhotoTap() {
        super.onPhotoTap()
        when {
            isShow -> {
                isShow = false
                changeViewAlpha(rl_top, false)
                changeViewAlpha(scrollview, false)
            }
            else -> {
                isShow = true
                changeViewAlpha(rl_top, true)
                changeViewAlpha(scrollview, true)
            }
        }
    }

    override fun initData() {
        if (intent.extras == null) return
        val aid = intent.getStringExtra(AID)
        ///  val isCmpp = intent.getBooleanExtra(ISCMPP, false)
        mPresenter?.getData(aid)
    }

    /**
     * 加载新闻内容
     *
     * @param articleBean 新闻内容
     */
    override fun loadData(articleBean: NewsArticleBean?) {
        articleBean?.let {
            try {
                this.newsArticleBean = it
                tv_info.text = String.format(resources.getString(R.string.description), 1, it.body!!.slides!!.size, it.body!!.slides!![0].description)
                tv_titlebar_name.text = it.body!!.title
                var imgUrls: List<String> = emptyList()
                it.body?.slides?.forEach {
                    it.image?.let {
                        imgUrls += it
                    }
                }
                initViewPager(this, imgUrls)
            } catch (e: Exception) {
            }
        }
    }


}