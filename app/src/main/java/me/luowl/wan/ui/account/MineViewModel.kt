package me.luowl.wan.ui.account

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableChar
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.jeremyliao.liveeventbus.LiveEventBus
import me.luowl.wan.AppConfig
import me.luowl.wan.base.BaseViewModel
import me.luowl.wan.base.SingleLiveEvent
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

    val myCoinCount: ObservableField<String> = ObservableField("我的积分")
    val showMyCoin: ObservableBoolean = ObservableBoolean(false)

    fun getMyCoin() {
        launch({
            val resp = repository.getMyCoinCount()
            checkResponseCode(resp)
            myCoinCount.set("我的积分：<font color='red'>${resp.data}</font>")
            showMyCoin.set(true)
        }, {
            showMyCoin.set(false)
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
        showMyCoin.set(false)
        AppConfig.clearLoginInfo()
        LiveEventBus.get().with(AppConfig.LOGIN_KEY).post(LoginEvent(false))
    }

}