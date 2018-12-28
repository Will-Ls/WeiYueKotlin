package com.will.weiyuekotlin.ui.video

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.VideoChannelBean
import com.will.weiyuekotlin.bean.VideoDetailBean
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.component.DaggerHttpComponent
import com.will.weiyuekotlin.ui.adapter.VideoPagerAdapter
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.video.contract.VideoContract
import com.will.weiyuekotlin.ui.video.presenter.VideoPresenter
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import kotlinx.android.synthetic.main.fragment_video.*


/**
 * desc: 视频 .
 * author: Will .
 * date: 2017/11/28 .
 */

class VideoFragment : BaseFragment<VideoPresenter>(), VideoContract.View {
    private var mVideoPagerAdapter: VideoPagerAdapter? = null

    companion object {
        fun newInstance(): VideoFragment {
            val args = Bundle()
            val fragment = VideoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun getContentLayout(): Int = R.layout.fragment_video

    override fun bindView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        mPresenter?.getVideoChannel()
    }

    /**
     * 加载视频频道列表
     *
     * @param channelBean 频道列表
     */
    override fun loadVideoChannel(channelBean: List<VideoChannelBean>?) {
        mVideoPagerAdapter = VideoPagerAdapter(childFragmentManager, channelBean?.get(0))
        viewPager.adapter = mVideoPagerAdapter
        viewPager.offscreenPageLimit = 5
        viewPager.setCurrentItem(0, false)
        tabLayout.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                JCVideoPlayer.releaseAllVideos()
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    override fun loadVideoDetails(detailBean: List<VideoDetailBean>?) {
    }

    override fun loadMoreVideoDetails(detailBean: List<VideoDetailBean>?) {
    }
}