package com.will.weiyuekotlin.event


import com.will.weiyuekotlin.bean.Channel
import java.util.*

/**
 * desc:
 * author: Will .
 * date: 2017/9/10 .
 */
class NewChannelEvent(allChannels: MutableList<Channel>?,
                      /**
                       * 添加的第一个频道名称
                       */
                      var firstChannelName: String) {
    //选中频道
    lateinit var selectedData: MutableList<Channel>

    //未选中频道
    lateinit var unSelectedData: MutableList<Channel>

    //全部频道
    lateinit var allChannel: List<Channel>

    init {
        allChannels?.let {
            this.allChannel = allChannels
            selectedData = ArrayList()
            unSelectedData = ArrayList()

            val iterator = it.iterator()
            while (iterator.hasNext()) {
                val channel = iterator.next()
                when {
                    channel.itemType == Channel.TYPE_MY || channel.itemType == Channel.TYPE_OTHER -> iterator.remove()
                    channel.itemType == Channel.TYPE_MY_CHANNEL -> selectedData.add(channel)
                    else -> unSelectedData.add(channel)
                }
            }
        }

    }
}
