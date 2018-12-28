package com.will.weiyuekotlin.ui.video

import `in`.srain.cube.views.ptr.PtrDefaultHandler
import `in`.srain.cube.views.ptr.PtrFrameLayout
import `in`.srain.cube.views.ptr.PtrHandler
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.VideoChannelBean
import com.will.weiyuekotlin.bean.VideoDetailBean
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.component.DaggerHttpComponent
import com.will.weiyuekotlin.ui.adapter.VideoDetailAdapter
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.video.contract.VideoContract
import com.will.weiyuekotlin.ui.video.presenter.VideoPresenter
import com.will.weiyuekotlin.widget.CustomLoadMoreView
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import kotlinx.android.synthetic.main.fragment_detail.*


/**
 * desc: 视频详情.
 * author: Will .
 * date: 2017/12/7 .
 */

class DetailFragment : BaseFragment<VideoPresenter>(), VideoContract.View {
    private var videoDetailBean: VideoDetailBean? = null
    private lateinit var detailAdapter: VideoDetailAdapter
    private var pageNum = 1
    private var typeId: String? = null

    companion object {
        fun newInstance(typeId: String): DetailFragment {
            val args = Bundle()
            args.putCharSequence("typeId", typeId)
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
            override fun checkCanDoRefresh(frame: PtrFrameLayout, content: View, header: View): Boolean {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header)
            }

            override fun onRefreshBegin(frame: PtrFrameLayout) {
                detailAdapter.lastPlayIndex=-1
                detailAdapter.currentPlayIndex=-1
                JCVideoPlayer.releaseAllVideos()
                pageNum = 1
                mPresenter?.getVideoDetails(pageNum, "list", typeId!!)
            }
        })
        videoDetailBean = VideoDetailBean()
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        detailAdapter = VideoDetailAdapter(activity, R.layout.item_detail_video, videoDetailBean?.item)
        mRecyclerView.adapter = detailAdapter
        detailAdapter.setEnableLoadMore(true)
        detailAdapter.setLoadMoreView(CustomLoadMoreView())
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        detailAdapter.setOnLoadMoreListener({ mPresenter?.getVideoDetails(pageNum, "list", typeId!!) }, mRecyclerView)

        //添加滑动自动播放
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
                        //获取第一个可见view的位置
                        val currentVisiableItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                        autoPlayNext(currentVisiableItemPosition)

                    }
                }
            }

        })
    }

    /**
     * 播放下一个
     */
    private fun autoPlayNext(currentVisiableItemPosition: Int) {
        detailAdapter.play(currentVisiableItemPosition + 1)
    }

    override fun initData() {
        when (arguments) {
            null -> return
            else -> {
                typeId = arguments.getString("typeId")
                mPresenter?.getVideoDetails(pageNum, "list", typeId!!)
            }
        }
    }

    /**
     * 加载频道视频列表
     *
     * @param detailBean 视频列表
     */
    override fun loadVideoDetails(detailBean: List<VideoDetailBean>?) {
        when (detailBean) {
            null -> {
                showError()
                return
            }
            else -> {
                pageNum++
                detailAdapter.setNewData(detailBean[0].item)
                mPtrFrameLayout.refreshComplete()
                showSuccess()
            }
        }
    }

    /**
     * 加载更多频道视频列表
     *
     * @param detailBean 视频列表
     */
    override fun loadMoreVideoDetails(detailBean: List<VideoDetailBean>?) {
        when (detailBean) {
            null -> {
                detailAdapter.loadMoreEnd()
                return
            }
            else -> {
                pageNum++
                detailBean[0].item?.let {
                    detailAdapter.addData(it)
                    detailAdapter.loadMoreComplete()
                }
            }
        }
    }

    override fun loadVideoChannel(channelBean: List<VideoChannelBean>?) {
    }

}