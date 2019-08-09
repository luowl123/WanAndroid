package me.luowl.wan.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.databinding.ActivityRegisterBinding

/*
 *
 * Created by luowl
 * Date: 2019/8/6 
 * Desc：
 */

class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>() {
    override fun getViewModelClass(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun initVariableId(): Int = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}