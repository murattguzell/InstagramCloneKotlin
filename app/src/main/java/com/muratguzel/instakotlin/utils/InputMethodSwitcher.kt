package com.muratguzel.instakotlin.utils

import android.content.Context
import android.text.InputType
import android.view.View
import androidx.core.content.ContextCompat
import com.muratguzel.instakotlin.Login.view.RegisterActivity
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityRegisterBinding


class InputMethodSwitcher(val binding: ActivityRegisterBinding,val context:Context) {

    companion object {
        fun getInstance(binding: ActivityRegisterBinding,context: Context): InputMethodSwitcher {
            return InputMethodSwitcher(binding,context)
        }
    }
  fun showEmailInput(){
        binding.apply {
            viewPhone.visibility = View.INVISIBLE
            viewEposta.visibility = View.VISIBLE
            etInputMethod.setText("")
            etInputMethod.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.etInputMethod.hint = "E-posta"
            binding.tvPhoneNumberInfo.visibility = View.INVISIBLE
            SetUpBtnBackgroundViewUtil.setUpViewBackground(binding,context)
        }
    }

    fun showPhoneInput(){
        binding.apply {
            viewPhone.visibility = View.VISIBLE
            viewEposta.visibility = View.INVISIBLE
            etInputMethod.setText("")
            etInputMethod.inputType = InputType.TYPE_CLASS_PHONE
            binding.etInputMethod.hint = "Telefon"
            binding.tvPhoneNumberInfo.visibility = View.VISIBLE
            SetUpBtnBackgroundViewUtil.setUpViewBackground(binding,context)
        }
    }

}