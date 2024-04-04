package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.akun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminAkunAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminAkunBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogAkunBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKeteranganBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminAkunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAkunBinding
    private val viewModel: AdminAkunViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminAkunAdapter
    private var tempAlertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchSemuaAkun()
        getSemuaAkun()
        getTambahUser()
        getUpdateUser()
        getHapusUser()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminAkunActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminAkunActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                showDialogTambahData()
            }
        }
    }

    private fun showDialogTambahData() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {

            btnSimpan.setOnClickListener {
                val nama = etEditNama.text.toString().trim()
                val nomorHp = etEditNomorHp.text.toString().trim()
                val username = etEditUsername.text.toString().trim()
                val password = etEditPassword.text.toString().trim()
                postTambahUser(nama, nomorHp, username, password)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postTambahUser(
        nama: String, nomorHp: String,
        username: String, password: String
    ) {
        viewModel.postTambahAkun(nama, nomorHp, username, password, "user")
    }

    private fun getTambahUser(){
        viewModel.getTambahAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureTambahAkun(result.message)
                is UIState.Success-> setSuccessTambahAkun(result.data)
            }
        }
    }

    private fun setSuccessTambahAkun(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminAkunActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchSemuaAkun()
            } else{
                Toast.makeText(this@AdminAkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminAkunActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureTambahAkun(message: String) {
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchSemuaAkun() {
        viewModel.fetchAkun()
    }

    private fun getSemuaAkun(){
        viewModel.getAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchAkun(result.message)
                is UIState.Success-> setSuccessFetchAkun(result.data)
            }
        }
    }

    private fun setFailureFetchAkun(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchAkun(data: ArrayList<UsersModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminAkunActivity, "tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<UsersModel>) {
        binding.apply {
            adapter = AdminAkunAdapter(data, object: OnClickItem.ClickAkun{
                override fun clickItemSetting(akun: UsersModel, it: View) {
                    val popupMenu = PopupMenu(this@AdminAkunActivity, it)
                    popupMenu.inflate(R.menu.popup_edit_hapus)
                    popupMenu.setOnMenuItemClickListener(object :
                        PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                            when (menuItem!!.itemId) {
                                R.id.edit -> {
                                    setShowDialogEdit(akun)
                                    return true
                                }
                                R.id.hapus -> {
                                    setShowDialogHapus(akun)
                                    return true
                                }
                            }
                            return true
                        }

                    })
                    popupMenu.show()
                }
            })
        }

        binding.apply {
            rvAkun.layoutManager = LinearLayoutManager(this@AdminAkunActivity, LinearLayoutManager.VERTICAL, false)
            rvAkun.adapter = adapter
        }

    }


    private fun setShowDialogEdit(akun: UsersModel) {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            etEditNama.setText(akun.nama)
            etEditNomorHp.setText(akun.nomorHp)
            etEditUsername.setText(akun.username)
            etEditPassword.setText(akun.password)

            btnSimpan.setOnClickListener {
                val idUser = akun.idUser!!
                val nama = etEditNama.text.toString().trim()
                val nomorHp = etEditNomorHp.text.toString().trim()
                val username = etEditUsername.text.toString().trim()
                val password = etEditPassword.text.toString().trim()
                val usernameLama = akun.username!!
                postUpdateUser(idUser, nama, nomorHp, username, password, usernameLama)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postUpdateUser(
        idUser: String, nama: String, nomorHp: String,
        username: String, password: String, usernameLama: String
    ) {
        viewModel.postUpdateAkun(idUser, nama, nomorHp, username, password, usernameLama)
    }

    private fun getUpdateUser(){
        viewModel.getUpdateAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureUpdateAkun(result.message)
                is UIState.Success-> setSuccessUpdateAkun(result.data)
            }
        }
    }

    private fun setSuccessUpdateAkun(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminAkunActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchSemuaAkun()
            } else{
                Toast.makeText(this@AdminAkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminAkunActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureUpdateAkun(message: String) {
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(akun: UsersModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus ${akun.nama!!} ?"
            tvBodyKonfirmasi.text = "Akun ini akan hapus dan data tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                val idUser = akun.idUser!!
                postHapusUser(idUser)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusUser(idUser: String){
        viewModel.postDeleteAkun(idUser!!)
    }

    private fun getHapusUser(){
        viewModel.getDeleteAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureHapusAkun(result.message)
                is UIState.Success-> setSuccessHapusAkun(result.data)
            }
        }
    }

    private fun setSuccessHapusAkun(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminAkunActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchSemuaAkun()
            } else{
                Toast.makeText(this@AdminAkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminAkunActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureHapusAkun(message: String) {
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setStopShimmer(){
        binding.apply {
            rvAkun.visibility = View.VISIBLE

            smAkun.stopShimmer()
            smAkun.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smAkun.startShimmer()
            smAkun.visibility = View.VISIBLE

            rvAkun.visibility = View.GONE
        }
    }
}