package com.muratguzel.instakotlin.Login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.muratguzel.instakotlin.Home.HomeActivity
import com.muratguzel.instakotlin.Login.viewmodel.AuthViewModel
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        currentUserNavigate()
        observeLiveData()

        binding.SignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        SetUpBtnBackgroundViewUtil.setUpOnTextChange(binding, this)


        binding.btnSignIn.setOnClickListener {
            viewModel.signInControl(
                binding.etEmailTelorUserName.text.toString(),
                binding.etSignInPassword.text.toString()
            )
        }
    }

    private fun currentUserNavigate() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun observeLiveData() {
        viewModel.loginStates.observe(this) { loginStates ->
            if (loginStates) {
                val intent = Intent(
                    this@LoginActivity,
                    HomeActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "oturum açma başarısız oldu", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
