package com.will.weiyuekotlin.ui.news

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.flyco.animation.SlideEnter.SlideRightEnter
import com.flyco.animation.SlideExit.SlideRightExit
import com.github.florent37.viewanimator.ViewAnimator
import com.will.weiyuekotlin.MyApp
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.NewsDetail
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.component.DaggerHttpComponent
import com.will.weiyuekotlin.net.NewsApi
import com.will.weiyuekotlin.net.NewsUtils
import com.will.weiyuekotlin.ui.adapter.NewsDetailAdapter
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.news.contract.DetailContract
import com.will.weiyuekotlin.ui.news.presenter.DetailPresenter
import com.will.weiyuekotlin.utils.ImageLoaderUtil
import com.will.weiyuekotlin.widget.ContextUtils
import com.will.weiyuekotlin.widget.CustomLoadMoreView
import com.will.weiyuekotlin.widget.NewsDelPop
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*


/**
 * desc: 新闻列表 .
 * author: Will .
 * date: 2017/12/5 .
 */
class DetailFragment : BaseFragment<DetailPresenter>(), DetailContract.View {
    private var mBannerList: MutableList<NewsDetail.ItemBean>? = mutableListOf()
    private var detailAdapter: NewsDetailAdapter? = null
    private var newsId: String? = null
    private var upPullNum = 1
    private var downPullNum = 1
    private var isRemoveHeaderView = false
    private var viewFocus: View? = null//顶部banner
    private var mBanner: Banner? = null
    private var newsDelPop: NewsDelPop? = null


    companion object {
        fun newInstance(newsId: String): DetailFragment {
            val args = Bundle()
            args.putString("newsId", newsId)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getContentLayout(): Int = R.layout.fragment_detail

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = mSimpleMultiStateView

    override fun initInjector(appComponent: ApplicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        mPtrFrameLayout.disableWhenHorizontalMove(true)
        mPtrFrameLayout.setPtrHandler(object : PtrHandler {
            override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean
                    = PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header)

            override fun onRefreshBegin(frame: PtrFrameLayout?) {
                mPresenter?.getData(newsId!!, NewsApi.ACTION_DOWN, downPullNum)

            }
        })

        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        detailAdapter = NewsDetailAdapter(null, activity)
        mRecyclerView.adapter = detailAdapter
        detailAdapter?.setEnableLoadMore(true)
        detailAdapter?.setLoadMoreView(CustomLoadMoreView())
        detailAdapter?.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        detailAdapter?.setOnLoadMoreListener({
            mPresenter?.getData(newsId!!, NewsApi.ACTION_UP, upPullNum)
        }, mRecyclerView)

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val itemBean = baseQuickAdapter.getItem(position) as NewsDetail.ItemBean?
                toRead(itemBean)
            }
        })
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val itemBean = baseQuickAdapter.getItem(position) as NewsDetail.ItemBean?
                when (view?.id) {
                    R.id.iv_close -> {
                        view.height
                        val location = IntArray(2)
                        view.getLocationInWindow(location)

                        itemBean?.style?.let {
                            when {
                                //根据当前 view 的绝对坐标 判断上下箭头的可见性  具体参考 http://www.jianshu.com/p/09acbcf28635
                                ContextUtils.getSreenWidth(MyApp.instance) - 50 - location[1] < ContextUtils.dip2px(MyApp.instance, 80F)
                                -> newsDelPop
                                        ?.anchorView(view)
                                        ?.gravity(Gravity.TOP)
                                        ?.setBackReason(itemBean.style?.backreason!!, true, position)
                                        ?.show()
                                else -> newsDelPop
                                        ?.anchorView(view)
                                        ?.gravity(Gravity.BOTTOM)
                                        ?.setBackReason(itemBean.style?.backreason!!, false, position)
                                        ?.show()
                            }
                        }
                    }
                }
            }
        })

        viewFocus = View.inflate(activity, R.layout.news_detail_headerview, null)
        mBanner = viewFocus?.findViewById(R.id.banner)
        mBanner?.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                ?.setImageLoader(object : ImageLoader() {
                    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                        //Glide 加载图片简单用法
                        ImageLoaderUtil.LoadImage(activity, path, imageView)
                    }
                })
                ?.setDelayTime(3000)
                ?.setIndicatorGravity(BannerConfig.RIGHT)
                ?.setOnBannerListener { position ->
                    mBannerList?.let {
                        bannerToRead(it[position])
                    }
                }

        initDelPop()
    }

    /**
     * 初始化屏蔽弹窗
     */
    private fun initDelPop(){
        newsDelPop = NewsDelPop(activity)
                .alignCenter(false)
                .widthScale(0.95f)
                .showAnim(SlideRightEnter())
                .dismissAnim(SlideRightExit())
                .offset((-100).toFloat(), 0F)
                .dimEnabled(true)
        newsDelPop?.setOnClickListener(object : NewsDelPop.OnClickListener {
            override fun onClick(position: Int) {
                newsDelPop!!.dismiss()
                detailAdapter!!.remove(position)
                showToast(0, false)
            }
        })
    }

    override fun initData() {
        if (arguments == null) return
        newsId = arguments.getString("newsId")
        mPresenter?.getData(newsId!!, NewsApi.ACTION_DEFAULT, 1)
    }

    override fun onRetry() {
        super.onRetry()
        initData()
    }

    /**
     * 加载顶部banner数据
     *
     * @param newsDetail
     */
    override fun loadBannerData(newsDetail: NewsDetail?) {
        val mTitleList = ArrayList<String>()
        val mUrlList = ArrayList<String>()
        mBannerList?.clear()

        newsDetail?.item?.forEach {
            if (!TextUtils.isEmpty(it.thumbnail)) {
                mTitleList.add(it.title!!)
                mBannerList?.add(it)
                mUrlList.add(it.thumbnail!!)
            }
        }

        if (mUrlList.size > 0) {
            mBanner?.setImages(mUrlList)
            mBanner?.setBannerTitles(mTitleList)
            mBanner?.start()
            if (detailAdapter?.headerLayoutCount!! < 1) {
                detailAdapter?.addHeaderView(viewFocus)
            }
        }
    }

    /**
     * 加载置顶新闻数据
     *
     * @param newsDetail
     */
    override fun loadTopNewsData(newsDetail: NewsDetail?) {
    }

    /**
     * 加载新闻数据
     *
     * @param itemBeanList
     */
    override fun loadData(itemBeanList: List<NewsDetail.ItemBean>?) {
        when (itemBeanList) {
            null -> showError()
            else -> {
                downPullNum++
                if (isRemoveHeaderView) {
                    detailAdapter?.removeAllHeaderView()
                }
                detailAdapter?.setNewData(itemBeanList)
                showToast(itemBeanList.size, true)
                mPtrFrameLayout.refreshComplete()
                showSuccess()
            }
        }
    }

    /**
     * 加载更多新闻数据
     *
     * @param itemBeanList
     */
    override fun loadMoreData(itemBeanList: List<NewsDetail.ItemBean>?) {
        when (itemBeanList) {
            null -> detailAdapter?.loadMoreFail()
            else -> {
                upPullNum++
                detailAdapter?.addData(itemBeanList)
                detailAdapter?.loadMoreComplete()
            }
        }
    }

    /**
     * 显示顶部弹出消息
     *
     * @param num        获取的数据size
     * @param isRefresh  是否下拉刷新操作
     */
    private fun showToast(num: Int, isRefresh: Boolean) {
        when {
            isRefresh -> tv_toast.text = String.format(resources.getString(R.string.news_toast), num.toString() + "")
            else -> tv_toast.text = "将为你减少此类内容"
        }
        rl_top_toast.visibility = View.VISIBLE
        ViewAnimator.animate(rl_top_toast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop {
                    ViewAnimator.animate(rl_top_toast)
                            .bounceOut()
                            .duration(1000)
                            .start()
                }
    }

    /**
     * 顶部 banner 去阅读
     *
     * @param itemBean 点击 item 数据
     */
    private fun bannerToRead(itemBean: NewsDetail.ItemBean?) {
        itemBean?.let {
            when (it.type) {
                NewsUtils.TYPE_DOC -> {
                    it.documentId?.let {
                        ArticleReadActivity.launch(activity, it)
                    }
                }
                NewsUtils.TYPE_SLIDE -> ImageBrowseActivity.launch(activity, it)
                NewsUtils.TYPE_ADVERT -> it.link?.weburl?.let { AdvertActivity.launch(activity, it) }
                NewsUtils.TYPE_PHVIDEO -> toast("TYPE_PHVIDEO")
                else -> {
                }
            }
        }
    }

    /**
     *  跳转到阅读页面
     *
     *  @param itemBean 点击 item 数据
     */
    private fun toRead(itemBean: NewsDetail.ItemBean?) {
        itemBean?.let {
            when (it.itemType) {
                NewsDetail.ItemBean.TYPE_DOC_TITLEIMG, NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG -> {
                    it.documentId?.let {
                        ArticleReadActivity.launch(activity, it)
                    }
                }
                NewsDetail.ItemBean.TYPE_SLIDE -> ImageBrowseActivity.launch(activity, it)
                NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG, NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG, NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG -> {
                    it.link?.weburl?.let { AdvertActivity.launch(activity, it) }
                }
                NewsDetail.ItemBean.TYPE_PHVIDEO -> toast("TYPE_PHVIDEO")
                else -> {

                }
            }
        }

    }
}