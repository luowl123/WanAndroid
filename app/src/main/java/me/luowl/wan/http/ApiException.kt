package me.luowl.wan.http

/*
 *
 * Created by luowl
 * Date: 2019/8/6 
 * Desc：
 */

class ApiException(val errorCode:Int,val errorMsg:String) : Throwable(errorMsg) {
}