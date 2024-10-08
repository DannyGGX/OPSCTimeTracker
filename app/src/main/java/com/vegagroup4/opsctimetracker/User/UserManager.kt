package com.vegagroup4.opsctimetracker.User

object UserManager
{
    var user: UserData? = null

    fun signIn(user: UserData) {
        this.user = user
    }

    fun signOut() {
        this.user = null
    }

    fun get(): UserData? {
        return user
    }
}