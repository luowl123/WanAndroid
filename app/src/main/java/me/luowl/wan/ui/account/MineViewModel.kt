package me.luowl.wan.ui.account

import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.data.WanRepository
import me.luowl.wan.event.LoginEvent
import me.luowl.wan.util.logDebug

/*
 *
 * Created by luowl
 * Date: 2019/8/4 
 * Desc：
 */

class MineViewModel constructor(private val repository: WanRepository) : BaseViewModel() {


    fun isLogin(): Boolean {
        return AppConfig.isLogin()
    }

    fun logout() {
        launch({
            repository.logout()
            clearData()
        }, {
            clearData()
        })
    }

    private fun clearData() {
        AppConfig.clearLoginInfo()
        LiveEventBus.get().with(AppConfig.LOGIN_KEY).post(LoginEvent(false))
    }

}