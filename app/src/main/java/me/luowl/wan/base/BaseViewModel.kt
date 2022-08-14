package me.luowl.wan.base

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.luowl.wan.data.model.BaseResp
import me.luowl.wan.http.ApiException


/*
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */

open class BaseViewModel : ViewModel() {

    val stateModel = StateModel()
    val refreshing = MutableLiveData<Boolean>().apply { value = false }

    val uc by lazy {
        UIChangeLiveData()
    }

    open fun startRefresh() {
        refreshing.value=true
        retry()
    }

    open fun stopRefresh() {
        refreshing.value=false
    }

    open fun startLoading() {
        if (!refreshing.value!!) {
            stateModel.startLoading()
        }
    }

    open fun loadDataFinish() {
        stopRefresh()
        stateModel.loadDataFinish()
    }

    open fun loadDataEmpty() {
        stopRefresh()
        stateModel.showEmpty()
    }

    open fun loadDataError() {
        stopRefresh()
        stateModel.loadDataError()
    }

    open fun retry() {

    }

    protected fun launch(
        block: suspend () -> Unit,
        error: suspend (e: Throwable) -> Unit
    ) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        }
    }

    protected fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            handleThrowable(e)
        }
    }

    @Throws(ApiException::class)
    fun checkResponseCode(resp: BaseResp<*>) {
        if (resp.errorCode != 0) throw ApiException(resp.errorCode, resp.errorMsg)
    }

    suspend fun handleThrowable(e: Throwable) {
        stateModel.loadDataError()
    }

    protected fun getUC(): UIChangeLiveData {
        return uc
    }

    open fun showDialog() {
        showDialog("请稍后...")
    }

    open fun showDialog(title: String) {
        uc.showDialogEvent.value = title
    }

    open fun dismissDialog() {
        uc.dismissDialogEvent.call()
    }

    fun showLoginDialog(){
        uc.showLoginDialogEvent.call()
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    fun startActivity(clz: Class<*>) {
        startActivity(clz, null)
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?) {
        val params = HashMap<String, Any>()
        params[CLASS] = clz
        if (bundle != null) {
            params[BUNDLE] = bundle
        }
        uc.startActivityEvent.postValue(params)
    }


    /**
     * 关闭界面
     */
    fun finish() {
        uc.finishEvent.call()
    }

    /**
     * 返回上一层
     */
    fun onBackPressed() {
        uc.onBackPressedEvent.call()
    }

    inner class UIChangeLiveData : SingleLiveEvent<Void>() {
        val showDialogEvent by lazy {
            SingleLiveEvent<String>()
        }
        val dismissDialogEvent: SingleLiveEvent<Void> by lazy {
            SingleLiveEvent<Void>()
        }
        val startActivityEvent: SingleLiveEvent<Map<String, Any>> by lazy {
            SingleLiveEvent<Map<String, Any>>()
        }
        val finishEvent: SingleLiveEvent<Void> by lazy {
            SingleLiveEvent<Void>()
        }
        val onBackPressedEvent: SingleLiveEvent<Void>by lazy {
            SingleLiveEvent<Void>()
        }
        val showLoginDialogEvent: SingleLiveEvent<Void>by lazy {
            SingleLiveEvent<Void>()
        }
    }

    companion object {
        const val CLASS = "CLASS"
        const val CANONICAL_NAME = "CANONICAL_NAME"
        const val BUNDLE = "BUNDLE"
    }

}