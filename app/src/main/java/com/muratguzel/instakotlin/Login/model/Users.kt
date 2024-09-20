package com.muratguzel.instakotlin.Login.model

data class Users(
    var email: String? = null,
    var password: String? = null,
    var userName: String? = null,
    var nameAndSurName: String? = null,
    var phoneNumber: String? = null,
    var emailPhoneNumber: String? = null,
    var userId: String? = null,
    var usersDetail:UserDetails ? = null
) {
    override fun toString(): String {
        return "Users(email=$email, password=$password, userName=$userName, nameAndSurName=$nameAndSurName, phoneNumber=$phoneNumber, emailPhoneNumber=$emailPhoneNumber, userId=$userId, usersDetail=$usersDetail)"
    }
}

