package me.luowl.wan.data.model

import java.io.Serializable

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class Architecture :Serializable{
    /**
    "courseId": 13,
    "id": 150,
    "name": "开发环境",
    "order": 1,
    "parentChapterId": 0,
    "userControlSetTop": false,
    "visible": 1,
    "children":[]
     * */

    var courseId = 0
    var id = 0L
    var name = ""
    var order = 0
    var parentChapterId = 0
    var userControlSetTop = false
    var visible = 1
    var children: List<Architecture>? = null

    fun getChildrenNames(): String {
        if (children == null) return ""
        return children!!.joinToString(separator = "\t",transform = {it.name})
    }
}