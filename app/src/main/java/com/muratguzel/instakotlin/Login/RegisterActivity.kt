package com.muratguzel.instakotlin.Login

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityRegisterBinding

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
            binding.viewPhone.visibility = View.INVISIBLE
            binding.viewEposta.visibility = View.VISIBLE
            binding.etInputMethod.setText("")
            binding.etInputMethod.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.etInputMethod.hint = "E-posta"
            binding.tvPhoneNumberInfo.visibility = View.INVISIBLE
            setUpBtnBackground()
        }
        binding.tvPhoneNumber.setOnClickListener {
            binding.viewPhone.visibility = View.VISIBLE
            binding.viewEposta.visibility = View.INVISIBLE
            binding.etInputMethod.setText("")
            binding.etInputMethod.inputType = InputType.TYPE_CLASS_PHONE
            binding.etInputMethod.hint = "Telefon"
            binding.tvPhoneNumberInfo.visibility = View.VISIBLE
            setUpBtnBackground()
        }
        binding.etInputMethod.doOnTextChanged { text, start, before, count ->
            if (start + before >= 10) {
                binding.btnNext.isEnabled = true
                binding.btnNext.setTextColor(
                    ContextCompat.getColor(
                        this@RegisterActivity,
                        R.color.beyaz
                    )
                )
                binding.btnNext.setBackgroundResource(R.drawable.register_button_active)
            } else {
                setUpBtnBackground()
            }
        }
        binding.btnNext.setOnClickListener {
            if (binding.etInputMethod.hint.equals("Telefon")) {
                binding.loginRoot.visibility = View.GONE
                binding.loginContainer.visibility = View.VISIBLE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, PhoneCodeEnterFragment())
                transaction.addToBackStack("PhoneCodeEnterFragment")
                transaction.commit()
                viewModel.updateData(binding.etInputMethod.text.toString())
            } else {
                //email
                binding.loginRoot.visibility = View.GONE
                binding.loginContainer.visibility = View.VISIBLE
                var transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer, EmailLoginProcessFragment())
                transaction.addToBackStack("EmailLoginProcessFragment")
                transaction.commit()
                viewModel.updateData(binding.etInputMethod.text.toString())
            }
        }
    }

    fun setUpBtnBackground() {
        binding.btnNext.isEnabled = false
        binding.btnNext.setTextColor(
            ContextCompat.getColor(
                this@RegisterActivity,
                R.color.sonukmavi
            )
        )
        binding.btnNext.setBackgroundResource(R.drawable.register_button)
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.loginRoot.visibility = View.VISIBLE
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    binding.etInputMethod.setText("")
                    setUpBtnBackground()
                } else {
                    finish()
                    overridePendingTransition(0, 0)
                }
            }

        })
    }
}