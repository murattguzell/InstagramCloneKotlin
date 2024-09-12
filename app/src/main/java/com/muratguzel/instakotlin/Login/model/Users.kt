package com.muratguzel.instakotlin.Login.model

class Users {
    lateinit var email: String
    lateinit var password: String
    lateinit var userName: String
    lateinit var nameAndSurName: String
    lateinit var phoneNumber: String
    lateinit var emailPhoneNumber: String

    constructor(){}
    //Email constructor
    constructor(email: String,password: String,userName: String,nameAndSurName:String){
        this.email = email
        this.password = password
        this.userName = userName
        this.nameAndSurName = nameAndSurName
    }

    constructor(phoneNumber: String,password: String,userName: String,nameAndSurName:String,emailPhoneNumber: String){
        this.phoneNumber = phoneNumber
        this.password = password
        this.userName = userName
        this.nameAndSurName = nameAndSurName
        this.emailPhoneNumber = emailPhoneNumber
    }
}