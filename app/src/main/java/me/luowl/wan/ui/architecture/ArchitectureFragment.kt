package me.luowl.wan.ui.architecture

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_architecture.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.RVAdapter
import me.luowl.wan.data.model.Architecture
import me.luowl.wan.databinding.FragmentArchitectureBinding
import me.luowl.wan.widget.SimpleDividerItemDecoration

/*
 *
 * Created by luowl
 * Date: 2019/7/27 
 * Desc：
 */

class ArchitectureFragment : BaseFragment<FragmentArchitectureBinding, ArchitectureViewModel>() {


    private lateinit var adapter: RVAdapter<Architecture>

    override fun getViewModelClass(): Class<ArchitectureViewModel> = ArchitectureViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_architecture

    override fun initVariableId() = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(SimpleDividerItemDecoration(context, 10f))

        adapter = object :
            RVAdapter<Architecture>(mutableListOf(), R.layout.recycler_item_architecture, BR.data) {
            override fun addListener(binding: ViewDataBinding, itemData: Architecture, position: Int) {
                binding.root.setOnClickListener {
                    context?.run {
                        ArchitectureCategoryActivity.startActivity(this, itemData)
                    }
                }
            }
        }
        recycler_view.adapter = adapter
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.dataList.observe(this, Observer {
            adapter.setData(it)
        })
    }

    override fun startLoadData() {
        super.startLoadData()
        viewModel.getData()
    }

}