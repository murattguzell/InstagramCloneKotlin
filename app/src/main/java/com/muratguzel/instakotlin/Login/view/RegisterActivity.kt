package com.muratguzel.instakotlin.Login.view

import SetUpBtnBackgroundViewUtil
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.muratguzel.instakotlin.Login.viewmodel.AuthViewModel
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityRegisterBinding
import com.muratguzel.instakotlin.utils.FragmentUtil
import com.muratguzel.instakotlin.utils.InputMethodSwitcher
import com.muratguzel.instakotlin.utils.ValidationUtil.isValidEmail
import com.muratguzel.instakotlin.utils.ValidationUtil.isValidPhoneNumber

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    val viewModel: AuthViewModel by viewModels()
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
        binding.tvSignIn.setOnClickListener {
            val intent  = Intent(this,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
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
                    viewModel.phoneControl(binding.etInputMethod.text.toString())
                    viewModel.phoneUsage.observe(this, Observer { phoneUsage ->
                        if (phoneUsage) {
                            Toast.makeText(this, "Telefon Kullanılıyor", Toast.LENGTH_SHORT).show()
                        } else {

                            binding.loginRoot.visibility = View.GONE
                            binding.loginContainer.visibility = View.VISIBLE
                            FragmentUtil.replaceFragment(
                                supportFragmentManager,
                                R.id.loginContainer,
                                RegisterFragment(),
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
                        }
                    })
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
                    viewModel.emailControl(binding.etInputMethod.text.toString())
                    viewModel.emailUsage.observe(this, Observer { isEmailUsed ->
                        Log.d("EmailObserver", "Observer tetiklendi: $isEmailUsed")
                        if (isEmailUsed) {
                            Toast.makeText(this, "Email Kullanılıyor", Toast.LENGTH_SHORT).show()
                        } else {
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
                                code = null
                            )
                        }
                    })
                }else {
                    Snackbar.make(
                        binding.btnNext,
                        "Lütfen geçerli bir email adresi giriniz",
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

