package me.luowl.wan.ui.account

import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.base.SingleLiveEvent
import me.luowl.wan.data.WanRepository
import me.luowl.wan.event.LoginEvent

/*
 *
 * Created by luowl
 * Date: 2019/8/4 
 * Desc：
 */

class MineViewModel constructor(private val repository: WanRepository) : BaseViewModel() {

    val myCoinCount = SingleLiveEvent<String>().apply { value = "我的积分" }
    val showMyCoin = SingleLiveEvent<Boolean>().apply { value = false }

    fun getMyCoin() {
        launch({
            val resp = repository.getMyCoinCount()
            checkResponseCode(resp)
            myCoinCount.value="我的积分：<font color='red'>${resp.data}</font>"
            showMyCoin.setValue(true)
        }, {
            showMyCoin.setValue(false)
        })
    }

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
        showMyCoin.value = false
        AppConfig.clearLoginInfo()
        LiveEventBus.get().with(AppConfig.LOGIN_KEY).post(LoginEvent(false))
    }

}