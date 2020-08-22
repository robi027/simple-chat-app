package com.example.simplechatapp.login

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simplechatapp.main.MainActivity
import com.example.simplechatapp.R
import com.example.simplechatapp.Status
import com.example.simplechatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val dialogBuilder by lazy { AlertDialog.Builder(this) }
    private lateinit var dialogLoading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewClicked()
        subscribeVM()
        initLoadingDialog()
    }

    override fun onStart() {
        super.onStart()
        checkSessionUser()
    }

    private fun viewClicked(){
        binding.btLogin.setOnClickListener {
            validation()
        }
    }

    private fun initLoadingDialog(){
        dialogBuilder.setView(R.layout.dialog_loading)
        dialogLoading = dialogBuilder.create()
        dialogLoading.setCancelable(false)
    }

    private fun showLoading(){ dialogLoading.show()}
    private fun hideLoading(){dialogLoading.dismiss()}

    private fun validation(){
        if(binding.etEmail.text.toString().isEmpty()){
            binding.etEmail.error = "Invalid Email"
        }else if(binding.etPass.text.toString().isEmpty()){
            binding.etPass.error = "Invalid Pass"
        }else{
            viewModel.doLogin(binding.etEmail.text.toString(), binding.etPass.text.toString())
        }
    }

    private fun checkSessionUser(){
        if (viewModel.isLogin()){
            Intent(this, MainActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }
    }

    private fun subscribeVM(){
        viewModel.loginResult.observe(this, Observer { status ->
            Log.e("Status", "$status")
            status?.let {state ->
                when(state){
                    Status.LOADING->{showLoading()}
                    Status.ERROR->{
                        hideLoading()
                        Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS->{
                        hideLoading()
                        Intent(this, MainActivity::class.java).also { intent ->
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        })
    }
}