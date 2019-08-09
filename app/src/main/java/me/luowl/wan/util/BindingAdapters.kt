package me.luowl.wan.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import me.luowl.wan.R

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

object BindingAdapters {
    @BindingAdapter(value = ["app:imageUrl"], requireAll = true)
    @JvmStatic
    fun loadImage(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }

    @BindingAdapter(value = ["app:collect"], requireAll = true)
    @JvmStatic
    fun setCollectionImageSrc(view: ImageView, collect: Boolean) {
        view.setImageResource(if (collect) R.mipmap.ic_collect else R.mipmap.ic_uncollect)
    }

    //是否刷新中
    @JvmStatic
    @BindingAdapter("refreshing")
    fun setRefreshing(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
        if (swipeRefreshLayout.isRefreshing != refreshing)
            swipeRefreshLayout.isRefreshing = refreshing
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "refreshing", event = "onRefresh")
    fun isRefreshing(swipeRefreshLayout: SwipeRefreshLayout): Boolean {
        logDebug("refreshing:$swipeRefreshLayout.isRefreshing")
        return swipeRefreshLayout.isRefreshing
    }

    @JvmStatic
    @BindingAdapter("onRefresh", requireAll = false)
    fun setOnRefreshListener(swipeRefreshLayout: SwipeRefreshLayout, bindingListener: InverseBindingListener?) {
        if (bindingListener != null)
            swipeRefreshLayout.setOnRefreshListener {
                bindingListener.onChange()
            }
    }
}