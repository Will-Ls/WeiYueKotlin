package com.will.weiyuekotlin.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.flyco.dialog.widget.popup.base.BasePopup
import com.will.weiyuekotlin.R
import java.util.*


/**
 * desc: .
 * author: Will .
 * date: 2017/9/12 .
 */
class NewsDelPop(context: Context) : BasePopup<NewsDelPop>(context) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoLike: TextView
    private lateinit var ivTop: ImageView
    private lateinit var ivDown: ImageView
    private lateinit var backReason: List<String>
    private lateinit var selectId: MutableList<Int>
    private lateinit var adapter: BaseQuickAdapter<String, BaseViewHolder>

    private var position: Int = 0
    private var mOnClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    fun setBackReason(backReason: List<String>, isTop: Boolean, position: Int): NewsDelPop {
        this.backReason = backReason
        this.position = position
        selectId = ArrayList()
        selectId.clear()
        adapter.setNewData(backReason)
        if (isTop) {
            ivTop.visibility = View.GONE
            ivDown.visibility = View.VISIBLE
        } else {
            ivTop.visibility = View.VISIBLE
            ivDown.visibility = View.GONE
        }
        return this
    }

    override fun onCreatePopupView(): View {
        val inflate = View.inflate(mContext, R.layout.popup_newsdel, null)
        recyclerView = inflate.findViewById(R.id.recyclerView)
        tvNoLike = inflate.findViewById(R.id.tv_nolike)
        ivTop = inflate.findViewById(R.id.iv_top)
        ivDown = inflate.findViewById(R.id.iv_down)
        adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_newsdelpop_del, null) {
            override fun convert(helper: BaseViewHolder, s: String) {
                helper.setText(R.id.tv_backreason, s)
                if (selectId.contains(helper.adapterPosition)) {
                    helper.getView<View>(R.id.tv_backreason).background = ContextCompat.getDrawable(mContext, R.drawable.delpop_tv_selected_bg)
                    helper.setTextColor(R.id.tv_backreason, ContextCompat.getColor(mContext, android.R.color.holo_red_light))
                } else {
                    helper.getView<View>(R.id.tv_backreason).background = ContextCompat.getDrawable(mContext, R.drawable.delpop_tv_bg)
                    helper.setTextColor(R.id.tv_backreason, ContextCompat.getColor(mContext, android.R.color.black))
                }
            }
        }
        recyclerView.layoutManager = GridLayoutManager(mContext, 2)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(baseQuickAdapter: BaseQuickAdapter<*, *>, view: View, i: Int) {
                if (selectId.contains(i)) {
                    selectId.remove(i as Any)
                } else {
                    selectId.add(i)
                }
                if (selectId.size > 0) {
                    tvNoLike.text = "确定"
                } else {
                    tvNoLike.text = "不感兴趣"
                }
                adapter.notifyItemChanged(i)
            }
        })
        tvNoLike.setOnClickListener { mOnClickListener!!.onClick(position) }
        return inflate
    }

    override fun setUiBeforShow() {

    }


}
