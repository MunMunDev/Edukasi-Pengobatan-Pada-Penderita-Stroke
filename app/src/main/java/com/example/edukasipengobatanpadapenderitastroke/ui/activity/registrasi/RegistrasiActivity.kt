package com.example.edukasipengobatanpadapenderitastroke.ui.activity.registrasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityRegistrasiBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.login.LoginActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrasiBinding
    @Inject
    lateinit var loading: LoadingAlertDialog
    private val viewModel : RegistrasiViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        postRegistrasiUser()
    }

    private fun setButton() {
        binding.apply {
            tvLogin.setOnClickListener{
                startActivity(Intent(this@RegistrasiActivity, LoginActivity::class.java))
                finish()
            }
            btnRegistrasi.setOnClickListener {
                buttonRegistrasi()
            }
        }
    }

    private fun buttonRegistrasi() {
        binding.apply {
            var cek = false
            if (etEditNama.toString().isEmpty()) {
                etEditNama.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditNomorHp.toString().isEmpty()) {
                etEditNomorHp.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditUsername.toString().isEmpty()) {
                etEditUsername.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditPassword.toString().isEmpty()) {
                etEditPassword.error = "Tidak Boleh Kosong"
                cek = true
            }

            if (!cek) {
                postTambahData(
                    etEditNama.text.toString().trim(),
                    etEditNomorHp.text.toString().trim(),
                    etEditUsername.text.toString().trim(),
                    etEditPassword.text.toString().trim()
                )
            }
        }
    }

    private fun postTambahData(nama: String, nomorHp: String, username: String, password: String){
        viewModel.postRegistrasiUser(nama, nomorHp, username, password, "user")
    }

    private fun postRegistrasiUser(){
        viewModel.getRegistrasiUser().observe(this@RegistrasiActivity){ result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@RegistrasiActivity)
                is UIState.Failure-> responseFailureRegistrasiUser(result.message)
                is UIState.Success-> responseSuccessRegiserUser(result.data)
            }
        }
    }

    private fun responseFailureRegistrasiUser(message: String) {
        Toast.makeText(this@RegistrasiActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun responseSuccessRegiserUser(data: ArrayList<ResponseModel>) {
        if (data.isNotEmpty()){
            if (data[0].status == "0"){
                Toast.makeText(this@RegistrasiActivity, "Berhasil melakukan registrasi", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this@RegistrasiActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this@RegistrasiActivity, "Maaf gagal", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }
}