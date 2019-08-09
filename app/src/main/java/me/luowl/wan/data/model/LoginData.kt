package me.luowl.wan.data.model

import java.io.Serializable

/*
 *
 * Created by luowl
 * Date: 2019/8/5 
 * Desc：
 */

class LoginData : Serializable {
    var admin = false
    var email = ""
    var icon = ""
    var id = 0L
    var nickname = ""
    var password = ""
    var token = ""
    var type = 0
    var username = ""
}