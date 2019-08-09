package me.luowl.wan.ui.account

import androidx.lifecycle.MutableLiveData
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.event.LoginEvent
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug

/*
 *
 * Created by luowl
 * Date: 2019/8/5 
 * Desc：
 */

class LoginViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    fun login() {
        logDebug("username=${username.value} password=${password.value}")
        val usernameValue = username.value
        val pwdValue = password.value
        if (usernameValue.isNullOrEmpty()) {
            GlobalUtil.showToastShort("请输入用户名")
            return
        }
        if (pwdValue.isNullOrEmpty()) {
            GlobalUtil.showToastShort("请输入密码")
            return
        }
        launch({
            val resp = repository.login(usernameValue, pwdValue)
            when (resp.errorCode) {
                0 -> {
                    showDialog("登录成功")
                    AppConfig.saveLoginInfo(resp.data!!)
                    postLoginSuccessEvent()
//                    onBackPressed()
                }
                else -> {
                    showDialog("登录失败【${resp.errorMsg}】")
                }
            }
        }, {})
    }

    private fun postLoginSuccessEvent(){
        LiveEventBus.get().with(AppConfig.LOGIN_KEY).post(LoginEvent(true))
    }


}