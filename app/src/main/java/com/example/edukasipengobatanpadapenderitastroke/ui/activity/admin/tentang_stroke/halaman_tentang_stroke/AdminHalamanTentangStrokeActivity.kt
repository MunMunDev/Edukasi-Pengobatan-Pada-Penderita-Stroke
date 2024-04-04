package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.halaman_tentang_stroke

import android.content.Intent
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
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminHalamanTentangStrokeAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminHalamanTentangStrokeBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogHalTentangStrokeBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.main.AdminMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.value_tentang_stroke.AdminValueTentangStrokeActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminHalamanTentangStrokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHalamanTentangStrokeBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminHalamanTentangStrokeViewModel by viewModels()
    private lateinit var adapter: AdminHalamanTentangStrokeAdapter
    private var tempAlertDialog: AlertDialog? = null
    @Inject lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHalamanTentangStrokeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchTentangStroke()
        getTentangStroke()
        getTambahHalamanTentangStroke()
        getUpdateHalamanTentangStroke()
        getHapusHalamanTentangStroke()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminHalamanTentangStrokeActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminHalamanTentangStrokeActivity)
        }
    }

    private fun setButton() {
        binding.btnTambah.setOnClickListener {
            dialogTambahHalamanTentangStroke()
        }
    }

    private fun dialogTambahHalamanTentangStroke() {
        val view = AlertDialogHalTentangStrokeBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            btnSimpan.setOnClickListener {
                var check = true

                if(etEditNamaHalaman.text.isEmpty()){
                    etEditNamaHalaman.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(check){
                    postTambahHalamanTentangStroke(etEditNamaHalaman.text.toString())
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postTambahHalamanTentangStroke(namaHalaman: String) {
        viewModel.postTambahHalamanTentangStroke(namaHalaman)
    }

    private fun getTambahHalamanTentangStroke(){
        viewModel.getResponseTambahHalamanTentangStroke().observe(this@AdminHalamanTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminHalamanTentangStrokeActivity)
                is UIState.Failure-> setFailureTambahHalamanTentangStroke(result.message)
                is UIState.Success-> setSuccessHalamTambahanTentangStroke(result.data)
            }
        }
    }

    private fun setFailureTambahHalamanTentangStroke(message: String) {
        Toast.makeText(this@AdminHalamanTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamTambahanTentangStroke(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminHalamanTentangStrokeActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchTentangStroke()
            } else{
                Toast.makeText(this@AdminHalamanTentangStrokeActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminHalamanTentangStrokeActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTentangStroke() {
        viewModel.fetchTentangStrokeList()
    }

    private fun getTentangStroke(){
        viewModel.getTentangStrokeList().observe(this@AdminHalamanTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTentangStroke(result.message)
                is UIState.Success-> setSuccessTentangStroke(result.data)
            }
        }
    }

    private fun setFailureTentangStroke(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminHalamanTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTentangStroke(data: ArrayList<TentangStrokeListModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminHalamanTentangStrokeActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<TentangStrokeListModel>) {
        adapter = AdminHalamanTentangStrokeAdapter(data, object : OnClickItem.ClickAdminTentangStroke{
            override fun clickItem(
                hal_tentang_stroke: TentangStrokeListModel,
                id_hal_tentang_stroke: String,
                halaman: String,
                it: View
            ) {
                val popupMenu = PopupMenu(this@AdminHalamanTentangStrokeActivity, it)
                popupMenu.inflate(R.menu.popup_rincian_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.rincian -> {
                                val i = Intent(this@AdminHalamanTentangStrokeActivity, AdminValueTentangStrokeActivity::class.java)
                                i.putExtra("hal_tentang_stroke", data)
                                i.putExtra("id_hal_tentang_stroke", id_hal_tentang_stroke)
                                i.putExtra("halaman", halaman)
                                startActivity(i)
                                return true
                            }
                            R.id.edit -> {
                                dialogEditHalamanTentangStroke(id_hal_tentang_stroke, halaman)
                                return true
                            }
                            R.id.hapus -> {
                                dialogHapusHalamanTentangStroke(id_hal_tentang_stroke, halaman)
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
            rvTentangStroke.layoutManager = LinearLayoutManager(this@AdminHalamanTentangStrokeActivity, LinearLayoutManager.VERTICAL, false)
            rvTentangStroke.adapter = adapter
        }
    }

    private fun dialogEditHalamanTentangStroke(id_hal_tentang_stroke: String, halaman: String) {
        val view = AlertDialogHalTentangStrokeBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Update Halaman Tentang Stroke"

        tempAlertDialog = dialogInputan

        view.apply {
            etEditNamaHalaman.setText(halaman)

            btnSimpan.setOnClickListener {
                var check = true

                if(etEditNamaHalaman.text.isEmpty()){
                    etEditNamaHalaman.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(check){
                    postEditHalamanTentangStroke(id_hal_tentang_stroke, etEditNamaHalaman.text.toString())
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postEditHalamanTentangStroke(idHalTentangStroke: String, halaman: String) {
        viewModel.postUpdateHalamanTentangStroke(idHalTentangStroke, halaman )
    }

    private fun getUpdateHalamanTentangStroke(){
        viewModel.getResponseUpdateHalamanTentangStroke().observe(this@AdminHalamanTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminHalamanTentangStrokeActivity)
                is UIState.Failure-> setFailureUpdateHalamanTentangStroke(result.message)
                is UIState.Success-> setSuccessHalamUpdateanTentangStroke(result.data)
            }
        }
    }

    private fun setFailureUpdateHalamanTentangStroke(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminHalamanTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamUpdateanTentangStroke(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminHalamanTentangStrokeActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchTentangStroke()
            } else{
                Toast.makeText(this@AdminHalamanTentangStrokeActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminHalamanTentangStrokeActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialogHapusHalamanTentangStroke(idHalTentangStroke: String, halaman: String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus halaman tentang Stroke ini ini?"
            tvBodyKonfirmasi.text = "Halaman $halaman akan dihapus dan data yang ada di dalamnya akan ikut terhapus. data yang telha terhapus tidak dapat di kembalikan"

            btnKonfirmasi.setOnClickListener {
                postHapusHalamanTentangStroke(idHalTentangStroke!!)
            }
            btnBatal.setOnClickListener {
                tempAlertDialog = null
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusHalamanTentangStroke(idHalTentangStroke: String) {
        viewModel.postHapusHalamanTentangStroke(idHalTentangStroke)
    }

    private fun getHapusHalamanTentangStroke(){
        viewModel.getResponseHapusHalamanTentangStroke().observe(this@AdminHalamanTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminHalamanTentangStrokeActivity)
                is UIState.Failure-> setFailureHapusHalamanTentangStroke(result.message)
                is UIState.Success-> setSuccessHapusHalamanTentangStroke(result.data)
            }
        }
    }

    private fun setFailureHapusHalamanTentangStroke(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminHalamanTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHapusHalamanTentangStroke(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminHalamanTentangStrokeActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchTentangStroke()
            } else{
                Toast.makeText(this@AdminHalamanTentangStrokeActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminHalamanTentangStrokeActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvTentangStroke.visibility = View.VISIBLE

            smTentangStroke.stopShimmer()
            smTentangStroke.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTentangStroke.startShimmer()
            smTentangStroke.visibility = View.VISIBLE

            rvTentangStroke.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminHalamanTentangStrokeActivity, AdminMainActivity::class.java))
        finish()
    }
}