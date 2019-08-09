package me.luowl.wan.base

import androidx.databinding.BaseObservable

/*
 *
 * Created by luowl
 * Date: 2019/8/2 
 * Desc：
 */

class StateModel : BaseObservable() {

    private var state = STATE_NORMAL
        set(value) {
            field = value
            notifyChange()
        }

    val empty
        get() = state == STATE_EMPTY

    val loading
        get() = state == STATE_LOADING

    val error
        get() = state == STATE_ERROR

    val badNetwork
        get() = state == STATE_BAD_NETWORK

    val success
        get() = state == STATE_NORMAL

    fun showEmpty() {
        state = STATE_EMPTY
    }

    fun startLoading() {
        state = STATE_LOADING
    }

    fun loadDataError() {
        state = STATE_ERROR
    }

    fun loadDataFinish() {
        state = STATE_NORMAL
    }

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_EMPTY = 1
        const val STATE_LOADING = 2
        const val STATE_ERROR = 3
        const val STATE_BAD_NETWORK = 4
    }
}