package me.luowl.wan.ui.navigation

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import kotlinx.android.synthetic.main.fragment_navigation.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.ArticleData
import me.luowl.wan.data.model.Navigation
import me.luowl.wan.databinding.FragmentNavigationBinding
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.widget.SimpleDividerItemDecoration

/*
 *
 * Created by luowl
 * Date: 2019/7/27 
 * Desc：
 */

class NavigationFragment : BaseFragment<FragmentNavigationBinding, NavigationViewModel>() {

    private lateinit var adapter: RVAdapter<Navigation>
    private lateinit var rightAdapter: RVAdapter<ArticleData>

    override fun getViewModelClass(): Class<NavigationViewModel> = NavigationViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_navigation

    override fun initVariableId() = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.addItemDecoration(SimpleDividerItemDecoration(context, 1f))

        adapter = object : RVAdapter<Navigation>(mutableListOf(), R.layout.recycler_item_navigation_left_tab, BR.data) {

            fun getCurrentIndex(): Int {
                return viewModel.leftTabIndex.value ?: 0
            }

            override fun addListener(binding: ViewDataBinding, itemData: Navigation, position: Int) {
                super.addListener(binding, itemData, position)
                binding.root.setOnClickListener {
                    viewModel.leftTabIndex.value = position
                }
            }

            override fun onBindDefViewHolder(viewHolder: RVViewHolder, data: Navigation, position: Int) {
                super.onBindDefViewHolder(viewHolder, data, position)
                viewHolder.binding.setVariable(BR.currentPosition, getCurrentIndex())
                viewHolder.binding.setVariable(BR.position, position)
            }
        }
        recycler_view.adapter = adapter

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.alignItems = AlignItems.STRETCH
        layoutManager.justifyContent = JustifyContent.FLEX_START
        recycler_view_sub.layoutManager = layoutManager
        recycler_view_sub.addItemDecoration(SimpleDividerItemDecoration(context, 5f))
        rightAdapter =
            object : RVAdapter<ArticleData>(mutableListOf(), R.layout.recycler_item_navigation_sub_child, BR.data) {
                override fun addListener(binding: ViewDataBinding, itemData: ArticleData, position: Int) {
                    super.addListener(binding, itemData, position)
                    binding.root.setOnClickListener {
                        context?.run {
                            WebViewActivity.startActivity(this, itemData.title, itemData.link)
                        }
                    }
                }
            }
        recycler_view_sub.adapter = rightAdapter
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.dataList.observe(this, Observer {
            adapter.setData(ArrayList(it))
        })
        viewModel.leftTabIndex.observe(this, Observer { index ->
            adapter.notifyDataSetChanged()
            if (viewModel.dataList.value != null && viewModel.dataList.value!!.isNotEmpty()) {
                rightAdapter.run { setData(ArrayList(viewModel.dataList.value?.get(index)?.articles)) }
            }
        })
    }

    override fun startLoadData() {
        super.startLoadData()
        viewModel.getData()
    }

}