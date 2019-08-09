package me.luowl.wan.ui.architecture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_architecture_category.*
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.base.adapter.CommonFragmentStatePagerAdapter
import me.luowl.wan.data.model.Architecture
import me.luowl.wan.databinding.ActivityArchitectureCategoryBinding

/*
 *
 * Created by luowl
 * Date: 2019/7/29 
 * Desc：
 */

class ArchitectureCategoryActivity :
    BaseActivity<ActivityArchitectureCategoryBinding, ArchitectureCategoryViewModel>() {

    override fun getViewModelClass(): Class<ArchitectureCategoryViewModel> = ArchitectureCategoryViewModel::class.java

    override fun getLayoutId() = R.layout.activity_architecture_category

    override fun initVariableId() = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
        val data = intent.getBundleExtra("data")
        data?.let {
            setUpTabLayout(data.getSerializable("architecture") as Architecture)
        }
    }

    private fun setUpTabLayout(data: Architecture) {
        title = data.name
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        for (item in data.children!!) {
            titles.add(item.name)
            fragments.add(ArchitectureArticleListFragment.newInstance(item.id))
        }
        view_pager.adapter = CommonFragmentStatePagerAdapter(supportFragmentManager, titles, fragments)
        tab_layout.setupWithViewPager(view_pager)
    }

    companion object {
        fun startActivity(context: Context, data: Architecture) {
            val intent = Intent(context, ArchitectureCategoryActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putSerializable("architecture", data)
                putExtra("data", bundle)
            }
            context.startActivity(intent)
        }
    }


}