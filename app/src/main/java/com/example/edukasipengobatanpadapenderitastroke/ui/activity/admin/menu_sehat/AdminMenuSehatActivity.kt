package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.menu_sehat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminMenuSehatAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminMenuSehatBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKeteranganBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogMenuSehatBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.main.AdminMainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.KataAcak
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AdminMenuSehatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMenuSehatBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminMenuSehatViewModel by viewModels()
    private lateinit var adapter: AdminMenuSehatAdapter
    private var tempAlertDialog: AlertDialog? = null
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var tempView: AlertDialogMenuSehatBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMenuSehatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchMenuSehat()
        getMenuSehat()
        getTambahMenuSehat()
        getUpdateMenuSehat()
        getHapusMenuSehat()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMenuSehatActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMenuSehatActivity)
        }
    }

    private fun setButton() {
        binding.btnTambah.setOnClickListener {
            dialogTambahMenuSehat()
        }
    }

    private fun dialogTambahMenuSehat() {
        val view = AlertDialogMenuSehatBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMenuSehatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {
            btnSimpan.setOnClickListener {
                var check = true

                if(etEditJudul.text.isEmpty()){
                    etEditJudul.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditDeskripsi.text.isEmpty()){
                    etEditDeskripsi.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(check){
                    val judul = etEditJudul.text.toString()
                    val deskripsi = etEditDeskripsi.text.toString()

                    postTambahMenuSehat(judul, deskripsi)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postTambahMenuSehat(
        judul: String, deskripsi: String
    ) {
        viewModel.postTambahMenuSehat(
            judul, deskripsi
        )
    }

    private fun getTambahMenuSehat(){
        viewModel.getResponseTambahMenuSehat().observe(this@AdminMenuSehatActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMenuSehatActivity)
                is UIState.Failure-> setFailureTambahMenuSehat(result.message)
                is UIState.Success-> setSuccessHalamTambahanMenuSehat(result.data)
            }
        }
    }

    private fun setFailureTambahMenuSehat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminMenuSehatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamTambahanMenuSehat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMenuSehatActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                fetchMenuSehat()
            } else{
                Toast.makeText(this@AdminMenuSehatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMenuSehatActivity, "ada error di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMenuSehat() {
        viewModel.fetchMenuSehat()
    }

    private fun getMenuSehat(){
        viewModel.getMenuSehat().observe(this@AdminMenuSehatActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureMenuSehat(result.message)
                is UIState.Success-> setSuccessMenuSehat(result.data)
            }
        }
    }

    private fun setFailureMenuSehat(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminMenuSehatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessMenuSehat(data: ArrayList<MenuSehatModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminMenuSehatActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<MenuSehatModel>) {
        adapter = AdminMenuSehatAdapter(data, object : OnClickItem.ClickAdminMenuSehat{
            override fun clickItemJudul(namaMenuSehat: String, it: View) {
                showClickText("Nama MenuSehatp", namaMenuSehat)
            }

            override fun clickItemDeskripsi(deskripsi: String, it: View) {
                showClickText("Deskripsi MenuSehat", deskripsi)
            }


            override fun clickItemSetting(menuSehat: MenuSehatModel, it: View) {
                val popupMenu = PopupMenu(this@AdminMenuSehatActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                dialogEditMenuSehat(menuSehat)
                                return true
                            }
                            R.id.hapus -> {
                                dialogHapusMenuSehat(menuSehat.id_menu_sehat!!, menuSehat.judul!!)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

        })

        binding.apply {
            rvMenuSehat.layoutManager = LinearLayoutManager(this@AdminMenuSehatActivity, LinearLayoutManager.VERTICAL, false)
            rvMenuSehat.adapter = adapter
        }
    }

    private fun showClickText(keterangan:String, judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMenuSehatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = keterangan
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowImage(gambar: String, title:String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMenuSehatActivity)
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

        Glide.with(this@AdminMenuSehatActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun dialogEditMenuSehat(menuSehat: MenuSehatModel) {
        val view = AlertDialogMenuSehatBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMenuSehatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Update Halaman Menu Sehat"

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {
            etEditJudul.setText(menuSehat.judul)
            etEditDeskripsi.setText(menuSehat.deskripsi)

            btnSimpan.setOnClickListener {
                var check = true

                if(etEditJudul.text.isEmpty()){
                    etEditJudul.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditDeskripsi.text.isEmpty()){
                    etEditDeskripsi.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(check){
                    postEditMenuSehat(
                        menuSehat.id_menu_sehat!!, etEditJudul.text.toString(), etEditDeskripsi.text.toString(),
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postEditMenuSehat(
        idHalMenuSehat: String, judul: String, deskripsi: String
    ) {
        viewModel.postUpdateMenuSehat(
            idHalMenuSehat, judul, deskripsi
        )
    }

    private fun getUpdateMenuSehat(){
        viewModel.getResponseUpdateMenuSehat().observe(this@AdminMenuSehatActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMenuSehatActivity)
                is UIState.Failure-> setFailureUpdateMenuSehat(result.message)
                is UIState.Success-> setSuccessHalamUpdateanMenuSehat(result.data)
            }
        }
    }

    private fun setFailureUpdateMenuSehat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminMenuSehatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamUpdateanMenuSehat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMenuSehatActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchMenuSehat()
            } else{
                Toast.makeText(this@AdminMenuSehatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMenuSehatActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialogHapusMenuSehat(idHalMenuSehat: String, namaMenuSehat: String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMenuSehatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Penjelasan ini?"
            tvBodyKonfirmasi.text = "Penjelasan ini akan dihapus dan data yang ada di dalamnya akan ikut terhapus. data yang telah terhapus tidak dapat di kembalikan"

            btnKonfirmasi.setOnClickListener {
                postHapusMenuSehat(idHalMenuSehat!!)
            }
            btnBatal.setOnClickListener {
                tempAlertDialog = null
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusMenuSehat(idHalMenuSehat: String) {
        viewModel.postHapusMenuSehat(idHalMenuSehat)
    }

    private fun getHapusMenuSehat(){
        viewModel.getResponseHapusMenuSehat().observe(this@AdminMenuSehatActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMenuSehatActivity)
                is UIState.Failure-> setFailureHapusMenuSehat(result.message)
                is UIState.Success-> setSuccessHapusMenuSehat(result.data)
            }
        }
    }

    private fun setFailureHapusMenuSehat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminMenuSehatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHapusMenuSehat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMenuSehatActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchMenuSehat()
            } else{
                Toast.makeText(this@AdminMenuSehatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMenuSehatActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvMenuSehat.visibility = View.VISIBLE

            smMenuSehat.stopShimmer()
            smMenuSehat.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smMenuSehat.startShimmer()
            smMenuSehat.visibility = View.VISIBLE

            rvMenuSehat.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminMenuSehatActivity, AdminMainActivity::class.java))
        finish()
    }
}