package me.luowl.wan.ui.project

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_project.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.base.adapter.CommonFragmentStatePagerAdapter
import me.luowl.wan.data.model.Architecture
import me.luowl.wan.databinding.FragmentProjectBinding

/*
 *
 * Created by luowl
 * Date: 2019/7/25 
 * Desc：
 */

class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectViewModel>() {

    override fun getViewModelClass(): Class<ProjectViewModel> = ProjectViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_project

    override fun initVariableId() = BR.viewModel

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.dataList.observe(this, Observer {
            setUpTabLayout(it)
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

    private fun setUpTabLayout(items: List<Architecture>) {
        val titles = ArrayList<String>()
        val fragments = ArrayList<Fragment>()
        for (item in items) {
            titles.add(item.name)
            fragments.add(ProjectListFragment.newInstance(item.id))
        }
        view_pager.adapter =
            CommonFragmentStatePagerAdapter(childFragmentManager, titles, fragments)
        tab_layout.setupWithViewPager(view_pager)
    }
}