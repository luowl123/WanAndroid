package me.luowl.wan.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
 *
 * Created by luowl
 * Date: 2019/8/31 
 * Desc：
 */

class CoinPage :Serializable{
    @SerializedName("curPage")
    var currentPage = 0
    var datas: MutableList<CoinData>? = null
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0L
}