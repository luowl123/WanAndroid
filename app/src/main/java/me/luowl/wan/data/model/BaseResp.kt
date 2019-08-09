package me.luowl.wan.data.model

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
open class BaseResp<T> {
    var errorCode = 0

    var errorMsg = ""

    var data: T? = null
}