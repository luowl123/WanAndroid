package me.luowl.wan.ui.account

import android.os.Bundle
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.BR
import me.luowl.wan.R
import me.luowl.wan.base.BaseFragment
import me.luowl.wan.databinding.FragmentMineBinding
import me.luowl.wan.event.LoginEvent
import me.luowl.wan.ui.webview.WebViewActivity
import me.luowl.wan.util.GlobalUtil

/*
 *
 * Created by luowl
 * Date: 2019/8/4 
 * Desc：
 */

class MineFragment : BaseFragment<FragmentMineBinding, MineViewModel>() {
    override fun getViewModelClass(): Class<MineViewModel> = MineViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_mine

    override fun initVariableId() = BR.viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        super.setupViews(savedInstanceState)
        updateInfo()
        binding.collectionView.setOnClickListener {
            if (viewModel.isLogin()) {
                context?.let { ctx -> CollectionActivity.startActivity(ctx) }
            } else {
                GlobalUtil.showToastShort("请先登录")
            }
        }

        binding.feedbackView.setOnClickListener {
            context?.let {
                WebViewActivity.startActivity(
                    it,
                    GlobalUtil.getString(R.string.text_feedback),
                    AppConfig.PROJECT_ISSUES_URL
                )
            }
        }

        binding.aboutView.setOnClickListener {
            context?.let {
                WebViewActivity.startActivity(
                    it,
                    GlobalUtil.getString(R.string.text_feedback),
                    AppConfig.ABOUT_APP_URL
                )
            }
        }
    }

    private fun updateInfo() {
        if (AppConfig.user.isNotEmpty()) {
            binding.usernameTv.text = AppConfig.user
            binding.userImg.setOnClickListener {
            }
        } else {
            binding.usernameTv.text = "未登录"
            binding.userImg.setOnClickListener {
                context?.let {
                    LoginActivity.startActivity(it)
                }
            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()
        LiveEventBus.get().with(AppConfig.LOGIN_KEY, LoginEvent::class.java)
            .observe(this, Observer {
                updateInfo()
                refreshLayout()
            })
    }

}