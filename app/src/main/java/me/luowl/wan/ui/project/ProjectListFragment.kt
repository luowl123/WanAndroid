package me.luowl.wan.ui.project

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_project_list.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.LoadMoreState
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.databinding.FragmentProjectListBinding
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.util.logDebug
import me.luowl.wan.widget.SimpleDividerItemDecoration

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

class ProjectListFragment : BaseFragment<FragmentProjectListBinding, ProjectListViewModel>() {

    private lateinit var adapter: RVAdapter<ArticleData>

    override fun getViewModelClass(): Class<ProjectListViewModel> = ProjectListViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_project_list

    override fun initVariableId() = BR.viewModel

    override fun initParams() {
        super.initParams()
        arguments?.let {
            viewModel.treeId = it.getLong(EXTRA_PROJECT_TREE_ID)
        }
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.appThemeColor))
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.addItemDecoration(SimpleDividerItemDecoration(context, 10f))
        adapter = object : RVAdapter<ArticleData>(mutableListOf(), R.layout.recycler_item_project, BR.article) {
            override fun addListener(binding: ViewDataBinding, itemData: ArticleData, position: Int) {
                binding.root.setOnClickListener {
                    context?.let {
                        WebViewActivity.startActivity(it, itemData.title, itemData.link)
                    }
                }
            }
        }
        adapter.setRequestLoadMoreListener(object : RVAdapter.LoadMoreListener {
            override fun loadMore() {
                viewModel.getData()
            }
        }, recycler_view)
        recycler_view.adapter = adapter
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.dataList.observe(this, Observer {
            logDebug("dataChange")
            adapter.setData(viewModel.dataList.value)
        })
        viewModel.requestLoadDataState.observe(this, Observer {
            when (it) {
                LoadMoreState.STATE_LOAD_FAIL -> adapter.loadMoreFail()
                LoadMoreState.STATE_LOAD_END -> adapter.loadMoreEnd()
                LoadMoreState.STATE_LOAD_NONE -> adapter.loadMoreComplete()
            }
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

    companion object {
        const val EXTRA_PROJECT_TREE_ID = "treeId"
        fun newInstance(treeId: Long): ProjectListFragment {
            val fragment = ProjectListFragment()
            val data = Bundle()
            data.putLong(EXTRA_PROJECT_TREE_ID, treeId)
            fragment.arguments = data
            return fragment
        }
    }
}