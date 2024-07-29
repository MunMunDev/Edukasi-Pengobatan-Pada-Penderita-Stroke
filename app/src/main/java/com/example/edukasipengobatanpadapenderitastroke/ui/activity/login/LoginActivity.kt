package com.example.edukasipengobatanpadapenderitastroke.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityLoginBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.main.AdminMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.registrasi.RegistrasiActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject
    lateinit var loading : LoadingAlertDialog
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        konfigurationUtils()
        button()
        getData()
    }

    private fun konfigurationUtils() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@LoginActivity)
    }

    private fun button(){
        btnLogin()
    }

    private fun btnLogin() {
        loginBinding.apply {
            btnLogin.setOnClickListener{
                if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    loading.alertDialogLoading(this@LoginActivity)
                    cekUsers(etUsername.text.toString(), etPassword.text.toString())
                }
                else{
                    if(etUsername.text.isEmpty()){
                        etUsername.error = "Masukkan Username"
                    }
                    if(etPassword.text.isEmpty()){
                        etPassword.error = "Masukkan Password"
                    }
                }
            }
        }
    }

    private fun cekUsers(username: String, password: String) {
        loginViewModel.fetchDataUser(username, password)
    }

    private fun getData(){
        loginViewModel.getDataUser().observe(this@LoginActivity){result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@LoginActivity)
                is UIState.Success-> setSuccessDataUser(result.data)
                is UIState.Failure -> setFailureDataUser(result.message)
            }
        }
    }

    private fun setFailureDataUser(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDataUser(data: ArrayList<UsersModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            successFetchLogin(data)
        } else{
            Toast.makeText(this@LoginActivity, "Data tidak ditemukan \nPastikan Username dan Password ada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun successFetchLogin(userModel: ArrayList<UsersModel>){
        var valueIdUser = 0
        userModel[0].idUser?.let {
            valueIdUser = it.toInt()
        }
        val valueNama = userModel[0].nama.toString()
        val valueNomorHp = userModel[0].nomorHp.toString()
        val valueUsername = userModel[0].username.toString()
        val valuePassword = userModel[0].password.toString()
        val valueSebagai= userModel[0].sebagai.toString()

        try{
            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
            sharedPreferencesLogin.setLogin(valueIdUser, valueNama, valueNomorHp, valueUsername, valuePassword, valueSebagai)
            if(valueSebagai=="admin"){
                startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
                finish()
            } else{
                Toast.makeText(this@LoginActivity, "Tolong periksa username dan password", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Exception){
            Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}