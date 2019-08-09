package me.luowl.wan.base.adapter

import androidx.databinding.BaseObservable

/*
 *
 * Created by luowl
 * Date: 2019/7/24 
 * Desc：
 */

class LoadMoreState : BaseObservable() {

    private var state = STATE_LOAD_NONE
        set(value) {
            field = value
            notifyChange()
        }

    val loading
        get() = state == STATE_LOADING

    val error
        get() = state == STATE_LOAD_FAIL

    val end
        get() = state == STATE_LOAD_END

    val none
        get() = state == STATE_LOAD_NONE

    fun loadMoreComplete() {
        state = STATE_LOAD_NONE
    }

    fun startLoading() {
        state = STATE_LOADING
    }

    fun loadMoreFail() {
        state = STATE_LOAD_FAIL
    }

    fun loadMoreEnd() {
        state = STATE_LOAD_END
    }

    companion object {
        public const val STATE_LOADING = 1
        public const val STATE_LOAD_FAIL = 2
        public const val STATE_LOAD_END = 3
        public const val STATE_LOAD_NONE = 4
    }
}