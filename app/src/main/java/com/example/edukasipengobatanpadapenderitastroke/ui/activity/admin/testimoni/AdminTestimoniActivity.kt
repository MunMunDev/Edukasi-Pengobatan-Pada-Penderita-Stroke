package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.testimoni

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminTestimoniAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminTestimoniBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminTestimoniActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminTestimoniBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminTestimoniViewModel by viewModels()
    private lateinit var adapter: AdminTestimoniAdapter
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject
    lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTestimoniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferencesLogin()
        setNavigationDrawer()
        fetchTestimoni()
        getTestimoni()
    }

    @SuppressLint("SetTextI18n")
    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@AdminTestimoniActivity)
    }

    private fun setNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminTestimoniActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminTestimoniActivity)
        }
    }

    private fun fetchTestimoni() {
        viewModel.fetchTestimoni()
    }

    private fun getTestimoni() {
        viewModel.getTestimoni().observe(this@AdminTestimoniActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTestimoni(result.message)
                is UIState.Success-> setSuccessTestimoni(result.data)
            }
        }
    }

    private fun setFailureTestimoni(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminTestimoniActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTestimoni(data: ArrayList<TestimoniModel>) {
        setAdapter(data)
    }

    private fun setAdapter(data: ArrayList<TestimoniModel>) {
        adapter = AdminTestimoniAdapter(data, object : OnClickItem.ClickTestimoni{
            override fun clickGambar(gambar: String, nama: String, it: View) {
                setShowImage(gambar, nama)
            }
        })
        binding.apply {
            rvTestimoni.layoutManager = LinearLayoutManager(this@AdminTestimoniActivity, LinearLayoutManager.VERTICAL, false)
            rvTestimoni.adapter = adapter
        }
        setStopShimmer()
    }

    private fun setShowImage(gambar: String, title:String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminTestimoniActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = "Gambar $title"
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@AdminTestimoniActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setStopShimmer(){
        binding.apply {
            rvTestimoni.visibility = View.VISIBLE

            smTestimoni.stopShimmer()
            smTestimoni.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTestimoni.startShimmer()
            smTestimoni.visibility = View.VISIBLE

            rvTestimoni.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminTestimoniActivity, MainActivity::class.java))
        finish()
    }
}