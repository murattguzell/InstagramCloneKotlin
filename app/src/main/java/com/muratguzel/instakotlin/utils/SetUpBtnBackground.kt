import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.muratguzel.instakotlin.R
import com.muratguzel.instakotlin.databinding.ActivityLoginBinding
import com.muratguzel.instakotlin.databinding.ActivityRegisterBinding
import com.muratguzel.instakotlin.databinding.FragmentRegisterBinding


object SetUpBtnBackgroundViewUtil {
    fun setUpViewBackground(binding: ViewBinding, context: Context) {
        if (binding is ActivityRegisterBinding) {
            binding.apply {
                btnNext.isEnabled = false
                btnNext.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sonukmavi
                    )
                )
                btnNext.setBackgroundResource(R.drawable.register_button)
            }
        } else if (binding is FragmentRegisterBinding) {
            binding.apply {
                btnNextRegisterFragment.isEnabled = false
                btnNextRegisterFragment.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sonukmavi
                    )
                )
                btnNextRegisterFragment.setBackgroundResource(R.drawable.register_button)
            }
        } else if (binding is ActivityLoginBinding) {
            binding.apply {
                btnSignIn.isEnabled = false
                btnSignIn.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.sonukmavi
                    )
                )
                btnSignIn.setBackgroundResource(R.drawable.register_button)
            }
        }
    }

    fun setUpOnTextChange(binding: ViewBinding, context: Context) {
        if (binding is FragmentRegisterBinding){


            binding.apply {

                val watcher = object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        Log.d("RegisterFragment", "beforeTextChanged called")

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s!!.length > 5) {
                            if (binding.etNameAndSurName.text.toString().length > 5 && binding.etUserName.text.toString().length > 5 && binding.etPassword.text.toString().length > 5) {
                                binding.btnNextRegisterFragment.isEnabled = true
                                binding.btnNextRegisterFragment.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.beyaz
                                    )
                                )
                                binding.btnNextRegisterFragment.setBackgroundResource(R.drawable.register_button_active)
                            } else {
                              setUpViewBackground(binding, context)

                            }

                        } else {

                            setUpViewBackground(binding, context)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        Log.d("RegisterFragment", "afterTextChanged called")

                    }
                }
                binding.etNameAndSurName.addTextChangedListener(watcher)
                binding.etUserName.addTextChangedListener(watcher)
                binding.etPassword.addTextChangedListener(watcher)

            }
        } else if (binding is ActivityLoginBinding) {
            binding.apply {
                val watcher = object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        Log.d("RegisterFragment", "beforeTextChanged called")

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s!!.length > 5) {
                            if (binding.etEmailTelorUserName.text.toString().length > 5 && binding.etSignInPassword.text.toString().length > 5) {
                                binding.btnSignIn.isEnabled = true
                                binding.btnSignIn.setTextColor(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.beyaz
                                    )
                                )
                                binding.btnSignIn.setBackgroundResource(R.drawable.register_button_active)
                            } else {
                                setUpViewBackground(binding, context)

                            }

                        } else {

                            setUpViewBackground(binding, context)
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        Log.d("RegisterFragment", "afterTextChanged called")

                    }

                }
                binding.etEmailTelorUserName.addTextChangedListener(watcher)
                binding.etSignInPassword.addTextChangedListener(watcher)
            }
        }
    }
}
