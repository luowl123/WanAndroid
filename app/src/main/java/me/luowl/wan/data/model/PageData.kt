package me.luowl.wan.data.model

import com.google.gson.annotations.SerializedName

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class PageData {
    @SerializedName("curPage")
    var currentPage = 0
    var datas: MutableList<ArticleData>? = null
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0L
}