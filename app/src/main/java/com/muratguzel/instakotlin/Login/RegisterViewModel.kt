package com.muratguzel.instakotlin.Login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel(){
    val inputData = MutableLiveData<String>()

    fun updateData(newData: String) {
        inputData.value = newData
    }
}