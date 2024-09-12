package com.muratguzel.instakotlin.utils

object ValidationUtil {

    // E-posta adresinin geçerli olup olmadığını kontrol eden fonksiyon
    fun isValidEmail(email: String?): Boolean {
        if (email == null) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Telefon numarasının geçerli olup olmadığını kontrol eden fonksiyon
    fun isValidPhoneNumber(phoneNumber: String?): Boolean {
        if (phoneNumber == null || phoneNumber.length > 14 || phoneNumber.length <= 7) {
            return false
        }
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches()
    }
}