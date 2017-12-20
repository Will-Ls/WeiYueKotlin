package com.will.weiyuekotlin.ui.news

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.View
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.Channel
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.component.DaggerHttpComponent
import com.will.weiyuekotlin.database.ChannelDao
import com.will.weiyuekotlin.event.NewChannelEvent
import com.will.weiyuekotlin.event.SelectChannelEvent
import com.will.weiyuekotlin.ui.adapter.ChannelPagerAdapter
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.news.contract.NewsContract
import com.will.weiyuekotlin.ui.news.presenter.NewsPresenter
import com.will.weiyuekotlin.widget.ChannelDialogFragment
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import kotlinx.android.synthetic.main.fragment_news_new.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * desc: 新闻.
 * author: Will .
 * date: 2017/11/28 .
 */

class NewsFragment : BaseFragment<NewsPresenter>(), NewsContract.View {

    private var mSelectedData: MutableList<Channel>? = null
    private var mUnSelectedData: MutableList<Channel>? = null
    private var selectedIndex: Int = 0
    private var selectedChannel: String? = null
    private var mChannelPagerAdapter: ChannelPagerAdapter? = null

    companion object {
        fun newInstance(): NewsFragment {
            val args = Bundle()
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getContentLayout(): Int = R.layout.fragment_news_new

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                selectedIndex = position
                selectedChannel = mSelectedData?.get(position)?.channelName
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        iv_edit.setOnClickListener {
            val dialogFragment = ChannelDialogFragment.newInstance(mSelectedData, mUnSelectedData)
            dialogFragment.show(childFragmentManager, "CHANNEL")
        }
    }

    override fun initData() {
        mPresenter?.getChannel()
    }

    /**
     * 初始化频道
     */
    override fun loadData(channels: List<Channel>, otherChannels: List<Channel>) {
        mSelectedData = channels as MutableList<Channel>
        mUnSelectedData = otherChannels as MutableList<Channel>
        mChannelPagerAdapter = ChannelPagerAdapter(childFragmentManager, channels)
        viewpager.adapter = mChannelPagerAdapter
        viewpager.offscreenPageLimit = 2
        viewpager.setCurrentItem(0, false)
        slidingTabLayout.setViewPager(viewpager)
    }

    /**
     * 频道管理页面选中频道回调
     *
     * @param selectChannelEvent 当前选中频道
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectChannelEvent(selectChannelEvent: SelectChannelEvent?) {
        selectChannelEvent?.let {
            val integers = mSelectedData?.map { it.channelName!! }
            integers?.let { setViewpagerPosition(it, selectChannelEvent.channelName) }
        }
    }

    /**
     * 频道管理页面关闭后更新频道并切换到选中频道
     * 添加频道规则  切换到选中频道的第一个
     * 删除频道规则  若删除进入频道管理之前选中频道则切换到该频道前一位，反之只更新频道
     *
     * @see  NewChannelEvent.selectedData 选中频道
     * @see  NewChannelEvent.unSelectedData 未选中频道
     * @see  NewChannelEvent.firstChannelName 添加的第一个频道名称
     */
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun updateChannel(event: NewChannelEvent?) {
        event?.let {
            mSelectedData = it.selectedData
            mUnSelectedData = it.unSelectedData
            mChannelPagerAdapter?.updateChannel(mSelectedData!!)
            slidingTabLayout.notifyDataSetChanged()
            ChannelDao.saveChannels(it.allChannel)
            val integers = mSelectedData?.map { it.channelName!! }
            integers?.let {
                when {
                    TextUtils.isEmpty(event.firstChannelName) -> if (!integers.contains(selectedChannel)) {
                        selectedChannel = mSelectedData!![selectedIndex].channelName
                        viewpager.setCurrentItem(selectedIndex, false)
                    } else {
                        setViewpagerPosition(integers, selectedChannel!!)
                    }
                    else -> setViewpagerPosition(integers, event.firstChannelName)
                }
            }
        }
    }

    /**
     * 设置 当前选中页
     *
     * @param integers  所有频道名称
     * @param channelName  选中频道名称
     */
    private fun setViewpagerPosition(integers: List<String>?, channelName: String) {
        if (TextUtils.isEmpty(channelName) || integers == null) return
        for (j in integers.indices) {
            if (integers[j] == channelName) {
                selectedChannel = integers[j]
                selectedIndex = j
                break
            }
        }
        viewpager.postDelayed({ viewpager.setCurrentItem(selectedIndex, false) }, 100)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}