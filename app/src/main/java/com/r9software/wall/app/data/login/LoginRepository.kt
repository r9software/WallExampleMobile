package com.r9software.wall.app.data.login

import com.r9software.wall.app.data.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    fun login(username: String, password: String,callback: LoginCallback){
        // handle login
        dataSource.login(username, password,callback)
    }

    fun register(email: String, password: String, confirmation: String, name: String, callback: LoginCallback) {

        dataSource.register(email, password,confirmation,name,callback)
    }


}
