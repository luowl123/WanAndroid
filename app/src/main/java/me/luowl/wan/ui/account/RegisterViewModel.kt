package me.luowl.wan.ui.account

import androidx.lifecycle.MutableLiveData
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.event.LoginEvent
import me.luowl.wan.util.GlobalUtil
import me.luowl.wan.util.logDebug
import java.util.regex.Pattern

/*
 *
 * Created by luowl
 * Date: 2019/8/6 
 * Desc：
 */

class RegisterViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val repassword: MutableLiveData<String> = MutableLiveData()


    fun register() {
        logDebug("username=${username.value} password=${password.value}")
        val usernameValue = username.value
        val pwdValue = password.value
        val rePwdValue = repassword.value
        if (usernameValue.isNullOrEmpty()) {
            GlobalUtil.showToastShort("请输入用户名")
            return
        }
        if (usernameValue.length < 6) {
            GlobalUtil.showToastShort("用户名最少6位")
            return
        }
        if (pwdValue.isNullOrEmpty()) {
            GlobalUtil.showToastShort("请输入密码")
            return
        }
        if (!regexPwd(pwdValue)) {
            GlobalUtil.showToastShort("密码6~50位且为数字、字母、-、_")
            return
        }
        if (rePwdValue.isNullOrEmpty()) {
            GlobalUtil.showToastShort("请输入确认密码")
            return
        }
        if (!pwdValue.equals(rePwdValue, false)) {
            GlobalUtil.showToastShort("确认密码和密码不符")
            return
        }
        launch({
            val resp = repository.register(usernameValue, pwdValue, rePwdValue)
            when (resp.errorCode) {
                0 -> {
                    showDialog("注册成功")
                    AppConfig.saveLoginInfo(resp.data!!)
                    postLoginSuccessEvent()
                    finish()
                }
                else -> {
                    showDialog("注册失败【${resp.errorMsg}】")
                }
            }
        }, {

        })
    }

    private fun postLoginSuccessEvent() {
        LiveEventBus.get().with(AppConfig.LOGIN_KEY).post(LoginEvent(true))
    }

    private fun regexPwd(pwd: String): Boolean {
        //密码6~50位且为数字、字母、-、_
        val regex = "^[A-Za-z0-9_-]{6,50}\$"
        return Pattern.matches(regex, pwd)
    }

}