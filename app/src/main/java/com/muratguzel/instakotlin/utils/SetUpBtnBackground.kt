import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.muratguzel.instakotlin.Login.view.RegisterFragment
import com.muratguzel.instakotlin.R
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
            }
        }
    }

    fun setUpOnTextChange(binding: ViewBinding, context: Context) {
        if (binding is FragmentRegisterBinding)
            binding.apply {

                 var watcher: TextWatcher = object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        Log.d("RegisterFragment", "beforeTextChanged called")

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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
                                SetUpBtnBackgroundViewUtil.setUpViewBackground(binding,context)

                            }

                        } else {
                            binding.btnNextRegisterFragment.isEnabled = false
                            binding.btnNextRegisterFragment.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.sonukmavi
                                )
                            )
                            SetUpBtnBackgroundViewUtil.setUpViewBackground(binding,context)            }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        Log.d("RegisterFragment", "afterTextChanged called")

                    }
                }
                binding.etNameAndSurName.addTextChangedListener(watcher)
                binding.etUserName.addTextChangedListener(watcher)
                binding.etPassword.addTextChangedListener(watcher)

            }
    }
}
