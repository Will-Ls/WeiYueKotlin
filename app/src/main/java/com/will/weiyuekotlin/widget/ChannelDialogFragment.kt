package com.will.weiyuekotlin.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.bean.Channel
import com.will.weiyuekotlin.event.NewChannelEvent
import com.will.weiyuekotlin.event.SelectChannelEvent
import com.will.weiyuekotlin.ui.adapter.NewAdapter
import com.will.weiyuekotlin.widget.channelDialog.ItemDragHelperCallBack
import com.will.weiyuekotlin.widget.channelDialog.OnChannelListener
import org.greenrobot.eventbus.EventBus
import java.io.Serializable
import java.util.*


/**
 * desc: 频道管理 .
 * author: Will .
 * date: 2017/12/9 .
 */

class ChannelDialogFragment : DialogFragment(), OnChannelListener {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var miVClose: ImageView
    private val mData = ArrayList<Channel>()
    private lateinit var mSelectedData: ArrayList<Channel>
    private lateinit var mUnSelectedData: ArrayList<Channel>
    private var mAdapter: NewAdapter? = null
    //是否需要更新 主页频道信息
    private var isUpdate = false


    private var onChannelListener: OnChannelListener? = null

    fun setOnChannelListener(onChannelListener: OnChannelListener) {
        this.onChannelListener = onChannelListener
    }

    companion object {
        fun newInstance(selectedData: List<Channel>?, unselectedData: List<Channel>?): ChannelDialogFragment {
            val dialogFragment = ChannelDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable("dataSelected", selectedData as Serializable)
            bundle.putSerializable("dataUnselected", unselectedData as Serializable)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialog = dialog
        //添加动画
        dialog?.window!!.setWindowAnimations(R.style.dialogSlideAnim)
        return inflater!!.inflate(R.layout.dialog_channel, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view!!.findViewById(R.id.recyclerView) as RecyclerView
        miVClose = view.findViewById(R.id.icon_collapse) as ImageView
        miVClose.setOnClickListener { dismiss() }
        processLogic()
    }

    private fun processLogic() {
        val channel = Channel()
        channel.viewType = Channel.TYPE_MY
        channel.channelName = "我的频道"
        mData.add(channel)

        mSelectedData = arguments.getSerializable("dataSelected") as ArrayList<Channel>
        mUnSelectedData = arguments.getSerializable("dataUnselected") as ArrayList<Channel>
        setDataType(mSelectedData, Channel.TYPE_MY_CHANNEL)
        setDataType(mUnSelectedData, Channel.TYPE_OTHER_CHANNEL)
        mData.addAll(mSelectedData)

        val moreChannel = Channel()
        moreChannel.viewType = Channel.TYPE_OTHER
        moreChannel.channelName = "频道推荐"
        mData.add(moreChannel)

        mData.addAll(mUnSelectedData)

        val callback = ItemDragHelperCallBack(this)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(mRecyclerView)

        mAdapter = NewAdapter(mData, helper)
        val manager = GridLayoutManager(activity, 4)
        mRecyclerView.layoutManager = manager
        mRecyclerView.adapter = mAdapter
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = mAdapter!!.getItemViewType(position)
                return if (itemViewType == Channel.TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL) 1 else 4
            }
        }
        mAdapter?.onChannelListener(this)
    }

    private fun setDataType(data: List<Channel>, type: Int) {
        for (i in data.indices) {
            data[i].viewType = type
        }
    }

    /**
     * 拖动频道回调
     *
     * @param starPos 起始位置
     * @param endPos  结束位置
     */
    override fun onItemMove(starPos: Int, endPos: Int) {
        if (starPos < 0 || endPos < 0) return
        if (mData[endPos].channelName.equals("头条")) return
        //我的频道之间移动
        onChannelListener?.onItemMove(starPos - 1, endPos - 1)//去除标题所占的一个index
        onMove(starPos, endPos, false)
    }

    /**
     * 添加到我的频道回调
     *
     * @param starPos 起始位置
     * @param endPos  结束位置
     */
    override fun onMoveToMyChannel(starPos: Int, endPos: Int) {
        onMove(starPos, endPos, true)
    }

    /**
     * 移动到推荐频道回调
     *
     * @param starPos 起始位置
     * @param endPos  结束位置
     */
    override fun onMoveToOtherChannel(starPos: Int, endPos: Int) {
        onMove(starPos, endPos, false)
    }

    private var firstAddChannelName = ""

    /**
     * 频道移动回调
     *
     * @param starPos 起始位置
     * @param endPos  结束位置
     * @param isAdd   是否是添加操作
     */
    private fun onMove(starPos: Int, endPos: Int, isAdd: Boolean) {
        isUpdate = true
        val startChannel = mData[starPos]
        //先删除之前的位置
        mData.removeAt(starPos)
        //添加到现在的位置
        mData.add(endPos, startChannel)
        mAdapter?.notifyItemMoved(starPos, endPos)
        if (isAdd) {
            if (TextUtils.isEmpty(firstAddChannelName)) {
                firstAddChannelName = startChannel.channelName!!
            }
        } else {
            if (startChannel.channelName!! == firstAddChannelName) {
                firstAddChannelName = ""
            }
        }
    }

    /**
     * 点击选中频道 关闭管理页面
     *
     * @param selectedChannelName 选中频道名称
     */
    override fun onFinish(selectedChannelName: String) {
        EventBus.getDefault().post(SelectChannelEvent(selectedChannelName))
        dismiss()
    }

    override fun onPause() {
        if (isUpdate) {
            EventBus.getDefault().post(NewChannelEvent(mAdapter?.data, firstAddChannelName))
        }
        super.onPause()
    }

}