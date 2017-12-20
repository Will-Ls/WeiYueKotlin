package com.will.weiyuekotlin.widget.channelDialog


interface OnChannelListener {
    fun onItemMove(starPos: Int, endPos: Int)
    fun onMoveToMyChannel(starPos: Int, endPos: Int)
    fun onMoveToOtherChannel(starPos: Int, endPos: Int)
    fun onFinish(selectedChannelName: String)
}
