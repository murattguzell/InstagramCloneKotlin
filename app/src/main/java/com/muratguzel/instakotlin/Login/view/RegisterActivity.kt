package com.muratguzel.instakotlin.Login.view

import SetUpBtnBackgroundViewUtil
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.muratguzel.instakotlin.Login.viewmodel.RegisterViewModel
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityRegisterBinding
import com.muratguzel.instakotlin.utils.FragmentUtil
import com.muratguzel.instakotlin.utils.InputMethodSwitcher
import com.muratguzel.instakotlin.utils.ValidationUtil.isValidEmail
import com.muratguzel.instakotlin.utils.ValidationUtil.isValidPhoneNumber

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        handleBackPress()
    }

    private fun init() {
        binding.tvEposta.setOnClickListener {
            InputMethodSwitcher.getInstance(binding, this).showEmailInput()
            SetUpBtnBackgroundViewUtil.setUpViewBackground(binding, this)
        }
        binding.tvPhoneNumber.setOnClickListener {
            InputMethodSwitcher.getInstance(binding, this).showPhoneInput()
            SetUpBtnBackgroundViewUtil.setUpViewBackground(binding, this)
        }
        binding.etInputMethod.doOnTextChanged { text, start, before, count ->
            if (text!!.length >= 10) {
                binding.btnNext.isEnabled = true
                binding.btnNext.setTextColor(
                    ContextCompat.getColor(
                        this@RegisterActivity,
                        R.color.beyaz
                    )
                )
                binding.btnNext.setBackgroundResource(R.drawable.register_button_active)
            } else {
                SetUpBtnBackgroundViewUtil.setUpViewBackground(binding, this)
            }
        }
        binding.btnNext.setOnClickListener {
            if (binding.etInputMethod.hint.equals("Telefon")) {
                if (isValidPhoneNumber(binding.etInputMethod.text.toString())) {
                    binding.loginRoot.visibility = View.GONE
                    binding.loginContainer.visibility = View.VISIBLE
                    FragmentUtil.replaceFragment(
                        supportFragmentManager,
                        R.id.loginContainer,
                        PhoneCodeEnterFragment(),
                        true,
                        "PhoneCodeEnterFragment"
                    )
                    viewModel.emailKayit.value = false
                    viewModel.updateData(
                        binding.etInputMethod.text.toString(),
                        email = null,
                        verificationId = null,
                        code = null,
                    )
                } else {
                    Snackbar.make(
                        binding.btnNext,
                        "Lütfen geçerli bir telefon numarası giriniz",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            } else {
                //email
                if (isValidEmail(binding.etInputMethod.text.toString())) {
                    binding.loginRoot.visibility = View.GONE
                    binding.loginContainer.visibility = View.VISIBLE
                    FragmentUtil.replaceFragment(
                        supportFragmentManager,
                        R.id.loginContainer,
                        RegisterFragment(),
                        true,
                        "EmailLoginProcessFragment"
                    )
                    viewModel.emailKayit.value = true
                    viewModel.updateData(
                        phoneNumber = null,
                        email = binding.etInputMethod.text.toString(),
                        verificationId = null,
                        code = null,
                    )
                } else {
                    Snackbar.make(
                        binding.btnNext,
                        "Lütfen geçerli bir e-posta adresi giriniz",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }


    private fun handleBackPress() {
        supportFragmentManager.addOnBackStackChangedListener {
            onBackStackChanged()
        }
    }

    private fun onBackStackChanged() {
        val elemanSayisi = supportFragmentManager.backStackEntryCount

        if (elemanSayisi == 0) {
            binding.loginRoot.visibility = View.VISIBLE
            SetUpBtnBackgroundViewUtil.setUpViewBackground(binding, this)
            binding.etInputMethod.setText("")
        }
    }

}

