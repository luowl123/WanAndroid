package me.luowl.wan.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.R
import me.luowl.wan.BR
import me.luowl.wan.base.BaseActivity
import me.luowl.wan.databinding.ActivityLoginBinding
import me.luowl.wan.event.LoginEvent

/*
 *
 * Created by luowl
 * Date: 2019/8/5 
 * Desc：
 */

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun getLayoutId() = R.layout.activity_login

    override fun initVariableId() = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        setupToolbar()
        binding.registerBtn.setOnClickListener {
            RegisterActivity.startActivity(this@LoginActivity)
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()
        LiveEventBus.get().with(AppConfig.LOGIN_KEY, LoginEvent::class.java)
            .observe(this, Observer {
                finish()
            })
    }

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}