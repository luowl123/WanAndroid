package me.luowl.wan.ui.wxarticle

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.Architecture
import me.luowl.wan.databinding.FragmentWxArticleChaptersBinding
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug
import me.luowl.wan.widget.SimpleDividerItemDecoration

/*
 *
 * Created by luowl
 * Date: 2019/7/24 
 * Desc：
 */

class WXArticleChaptersFragment : BaseFragment<FragmentWxArticleChaptersBinding, WXArticleChaptersViewModel>() {

    private lateinit var adapter: RVAdapter<Architecture>

    override fun getViewModelClass(): Class<WXArticleChaptersViewModel> = WXArticleChaptersViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_wx_article_chapters

    override fun initVariableId() = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(
            SimpleDividerItemDecoration(
                context,
                20f,
                GlobalUtil.getColor(R.color.white),
                20f, 15f, 15f
            )
        )
        adapter = object : RVAdapter<Architecture>(mutableListOf(), R.layout.recycler_item_wx_chapter, BR.data) {
            override fun addListener(binding: ViewDataBinding, itemData: Architecture, position: Int) {
                binding.root.setOnClickListener {
                    context?.let {
                        ArticleListActivity.startActivity(it, itemData.id, itemData.name)
                    }
                }
            }
        }
        recycler_view.adapter = adapter
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.dataList.observe(this, Observer {
            logDebug("dataChange")
            adapter.setData(viewModel.dataList.value)
        })
    }

    override fun startLoadData() {
        super.startLoadData()
        viewModel.dataList.value?.run {
            if (isEmpty()) {
                viewModel.getData()
            }
        }
    }
}