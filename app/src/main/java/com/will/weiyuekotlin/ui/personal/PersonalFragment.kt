package com.will.weiyuekotlin.ui.personal

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.will.weiyuekotlin.R
import com.will.weiyuekotlin.component.ApplicationComponent
import com.will.weiyuekotlin.ui.base.BaseContract
import com.will.weiyuekotlin.ui.base.BaseFragment
import com.will.weiyuekotlin.ui.base.BasePresenter
import com.will.weiyuekotlin.utils.ImageLoaderUtil
import com.will.weiyuekotlin.widget.SimpleMultiStateView
import kotlinx.android.synthetic.main.fragment_personal.*


/**
 * desc: 个人 .
 * author: Will .
 * date: 2017/11/28 .
 */

class PersonalFragment : BaseFragment<BasePresenter<BaseContract.BaseView>>() {
    companion object {
        fun newInstance(): PersonalFragment {
            val args = Bundle()
            val fragment = PersonalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getSimpleMultiStateView(): SimpleMultiStateView? = null

    override fun initInjector(appComponent: ApplicationComponent) {}

    override fun getContentLayout(): Int = R.layout.fragment_personal

    override fun initData() {}

    override fun bindView(view: View, savedInstanceState: Bundle?) {
        ImageLoaderUtil.LoadImage(this, "http://oon8y1sqh.bkt.clouddn.com/avatar.JPG", ivAuthor,
                RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
        val mTypeFace = Typeface.createFromAsset(activity.assets, "font/consolab.ttf")
        tvContacts.typeface = mTypeFace
        setFont(tvName, tvBlog, tvGithub, tvEmail, tvGithubUrl, tvUrl, tvEmailUrl)

        tvUrl.setOnClickListener {
            toWeb(resources.getString(R.string.willUrl))
        }
        tvGithubUrl.setOnClickListener {
            toWeb(resources.getString(R.string.gitHubUrl))
        }
    }

    private fun setFont(vararg view: TextView) {
        val typeface = Typeface.createFromAsset(activity.assets, "font/consola.ttf")
        view.forEach {
            it.typeface = typeface
        }
    }

    private fun toWeb(url: String) {
        val webUrl = Uri.parse(url)
        val webIntent = Intent(Intent.ACTION_VIEW, webUrl)
        activity.startActivity(webIntent)
    }

}