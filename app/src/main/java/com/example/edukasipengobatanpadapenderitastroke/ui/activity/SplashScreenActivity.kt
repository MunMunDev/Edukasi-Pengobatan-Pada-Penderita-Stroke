package com.example.edukasipengobatanpadapenderitastroke.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.main.AdminMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.login.LoginActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.let {
            it.hide()
        }

        sharedPreferencesLogin = SharedPreferencesLogin(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferencesLogin.getIdUser() == 0){
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
            else{
                if(sharedPreferencesLogin.getSebagai() == "user"){
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }
                else if(sharedPreferencesLogin.getSebagai() == "admin"){
                    startActivity(Intent(this@SplashScreenActivity, AdminMainActivity::class.java))
                    finish()
                }
            }
        }, 3000)
    }
}