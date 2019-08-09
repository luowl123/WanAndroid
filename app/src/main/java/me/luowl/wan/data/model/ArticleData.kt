package me.luowl.wan.data.model

/**
 *
 * Created by luowl
 * Date: 2019/7/23
 * Desc：
 */
class ArticleData {
    /**
    {
    "apkLink": "",
    "author": " 振之",
    "chapterId": 26,
    "chapterName": "基础UI控件",
    "collect": false,
    "courseId": 13,
    "desc": "",
    "envelopePic": "",
    "fresh": false,
    "id": 8575,
    "link": "https://juejin.im/post/597d88f75188257fc2177c36",
    "niceDate": "2019-06-03",
    "origin": "",
    "prefix": "",
    "projectLink": "",
    "publishTime": 1559534523000,
    "superChapterId": 26,
    "superChapterName": "常用控件",
    "tags": [],
    "title": "如何实现 “中间这几个字要加粗，但是不要太粗，比较纤细的那种粗” ？",
    "type": 0,
    "userId": -1,
    "visible": 1,
    "zan": 0
    }
     */
    var apkLink = ""
    var author = ""
    var chapterId = 0
    var chapterName = ""
    var collect = false
    var courseId = 0
    var desc = ""
    var envelopePic = ""
    var fresh = false
    var id = 0L
    var link = ""
    var niceDate = ""
        get() {
            return "时间：$field"
        }
    var origin = ""
    var originId=0L
    var prefix = ""
    var projectLink = ""
    var publishTime = 0L
    var superChapterId = 0
    var superChapterName = ""
    var title = ""
    var type = 0
    var userId = -1
    var visible = 1
    var zan = 0L

    var tags: MutableList<ArticleTag>? = null

    fun getChapter(): String {
        if (superChapterName.isNotEmpty())
            return "$superChapterName/$chapterName"
        return chapterName
    }

    fun hasDesc():Boolean{
        return desc.isNotEmpty()
    }

}