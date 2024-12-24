package com.abdullah.lostfound.ui.Auth


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.abdullah.lostfound.ui.main.home.MainActivity
import com.abdullah.lostfound.databinding.ActivitySignUpBinding
import kotlinx.coroutines.launch


class SignUpActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var binding: ActivitySignUpBinding
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel= AuthViewModel()
      //  viewModel.checkUser()

        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Please wait while we create your account...")
        progressDialog.setCancelable(false)

        lifecycleScope.launch {
            viewModel.failureMessage.collect{
                progressDialog.dismiss()
                if (it!=null){
                    Toast.makeText(this@SignUpActivity,it,Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.currentUser.collect{
                if (it!=null){
                    progressDialog.dismiss()
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

        binding.signupTxt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.loginbtn.setOnClickListener {
            val email=binding.email.editText?.text.toString()
            val password=binding.password.editText?.text.toString()
            val confirPassword=binding.confirmPassword.editText?.text.toString()
            val name=binding.name.editText?.text.toString()

            if(name.toString().trim().isEmpty()){
                Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!email.contains("@")){
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6){
                Toast.makeText(this,"Password must be atleast 6 characters",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!password.equals(confirPassword)){
                Toast.makeText(this,"Confirm password does not match with password",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressDialog.show()

            viewModel.signUp(email,password,name)

        }


    }
}

