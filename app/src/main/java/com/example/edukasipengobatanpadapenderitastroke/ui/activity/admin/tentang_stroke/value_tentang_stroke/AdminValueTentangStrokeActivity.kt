package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.value_tentang_stroke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminValueTentangStrokeAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminValueTentangStrokeBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKeteranganBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogValTentangStrokeBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminValueTentangStrokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminValueTentangStrokeBinding
    private val viewModel: AdminValueTentangStrokeViewModel by viewModels()
    private lateinit var adapter: AdminValueTentangStrokeAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var idHalTentangStroke = "1"
    private lateinit var listHalamanTentangStroke: ArrayList<String>
    private lateinit var listIdHalamanTentangStroke: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminValueTentangStrokeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        fetchData()
        getValueTentangStroke()
        getTambahValueTentangStroke()
        getUpdateValueTentangStroke()
        getHapusValueTentangStroke()
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            val halTentangStroke = extras.getParcelableArrayList<TentangStrokeListModel>("hal_tentang_stroke")!!
            idHalTentangStroke = extras.getString("id_hal_tentang_stroke")!!
            listHalamanTentangStroke = searchDataArrayHalaman(halTentangStroke)
            listIdHalamanTentangStroke = searchDataArrayId(halTentangStroke)
            val halaman = extras.getString("halaman")!!
            binding.titleHeader.text = halaman
        }
    }

    private fun searchDataArrayId(list: ArrayList<TentangStrokeListModel>): ArrayList<String>{
        val valueList: ArrayList<String> = arrayListOf()
        for(value in list){
            valueList.add(value.id_hal_tentang_stroke!!)
        }

        return valueList
    }

    private fun searchDataArrayHalaman(list: ArrayList<TentangStrokeListModel>): ArrayList<String>{
        val valueList: ArrayList<String> = arrayListOf()
        for(value in list){
            valueList.add(value.halaman!!)
        }

        return valueList
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setShowDialogTambah() {
        val view = AlertDialogValTentangStrokeBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            var numberPosition = 0
            var selectedValue = ""

            val tambahDialogHalaman = binding.titleHeader.text.toString().trim()
            val listTambahDialog : ArrayList<String> = arrayListOf()

            listTambahDialog.add(tambahDialogHalaman)

            val arrayAdapter = ArrayAdapter(this@AdminValueTentangStrokeActivity, android.R.layout.simple_spinner_item, listTambahDialog)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spHalamanTentangStroke.adapter = arrayAdapter

            spHalamanTentangStroke.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spHalamanTentangStroke.selectedItemPosition
                    selectedValue = spHalamanTentangStroke.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spHalamanTentangStroke.adapter = arrayAdapter

            btnSimpan.setOnClickListener {
                val idHalTentangStroke = idHalTentangStroke
                val judul = etEditJudul.text.toString().trim()
                val deskripsi = etEditDeskripsi.text.toString().trim()

                postTambahValueTentangStroke(
                    idHalTentangStroke, deskripsi, judul
                )
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahValueTentangStroke(
        idHalTentangStroke: String, judul: String, deskripsi: String
    ) {
        viewModel.postTambahValueTentangStroke(
            idHalTentangStroke, judul, deskripsi
        )
    }

    private fun getTambahValueTentangStroke(){
        viewModel.getTambahValueTentangStroke().observe(this@AdminValueTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminValueTentangStrokeActivity)
                is UIState.Failure-> setFailureTambahValueTentangStroke(result.message)
                is UIState.Success-> setSuccessTambahValueTentangStroke(result.data)
            }
        }
    }

    private fun setSuccessTambahValueTentangStroke(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminValueTentangStrokeActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminValueTentangStrokeActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminValueTentangStrokeActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureTambahValueTentangStroke(message: String) {
        Toast.makeText(this@AdminValueTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchData() {
        viewModel.fetchValueTentangStroke(idHalTentangStroke)
    }
    private fun getValueTentangStroke(){
        viewModel.getTentangStrokeList().observe(this@AdminValueTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchValueTentangStroke(result.message)
                is UIState.Success-> setSuccessFetchValueTentangStroke(result.data)
            }
        }
    }

    private fun setFailureFetchValueTentangStroke(message: String) {
        Toast.makeText(this@AdminValueTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
    }

    private fun setSuccessFetchValueTentangStroke(data: ArrayList<TentangStrokeDetailModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminValueTentangStrokeActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<TentangStrokeDetailModel>) {
        adapter = AdminValueTentangStrokeAdapter(data, object: OnClickItem.ClickAdminValueTentangStroke{
            override fun clickItemHalaman(halaman: String, it: View) {
                showClickHalaman(halaman)
            }

            override fun clickItemJudul(judul: String, it: View) {
                showClickJudul(judul)
            }

            override fun clickItemDeskripsi(deskripsi: String, it: View) {
                showClickDeskripsi(deskripsi)
            }

            override fun clickItemSetting(pesanan: TentangStrokeDetailModel, it: View) {
                val popupMenu = PopupMenu(this@AdminValueTentangStrokeActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(pesanan)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(pesanan)
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
            rvValTentangStroke.layoutManager = LinearLayoutManager(
                this@AdminValueTentangStrokeActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvValTentangStroke.adapter = adapter
        }

    }

    private fun showClickHalaman(halaman: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Jenis Halaman"
            tvBodyKeterangan.text = halaman
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun showClickJudul(judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Judul Isi"
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun showClickDeskripsi(deskripsi: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Isi Penjelasan"
            tvBodyKeterangan.text = deskripsi
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(tentangStroke: TentangStrokeDetailModel) {
        val view = AlertDialogValTentangStrokeBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Edit Value Tentang Stroke"

        view.apply {
            etEditJudul.setText(tentangStroke.judul)
            etEditDeskripsi.setText(tentangStroke.deskripsi)

            var numberPosition = 0
            var selectedValue = ""

            val arrayAdapter = ArrayAdapter(this@AdminValueTentangStrokeActivity, android.R.layout.simple_spinner_item, listHalamanTentangStroke)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            var id = 0
            for((no, value) in listIdHalamanTentangStroke.withIndex()){
                if(value == tentangStroke.id_hal_tentang_stroke){
                    id = no
                    Log.d("DetailTAG", "val $no: $value")
                }
            }
            Log.d("DetailTAG", "val $id")

            spHalamanTentangStroke.setSelection(id)
            spHalamanTentangStroke.adapter = arrayAdapter

            spHalamanTentangStroke.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spHalamanTentangStroke.selectedItemPosition
                    selectedValue = spHalamanTentangStroke.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spHalamanTentangStroke.setSelection(id)


            btnSimpan.setOnClickListener {
                val idValueTentangStroke = tentangStroke.id_val_tentang_stroke!!
                val idHalTentangStroke = listIdHalamanTentangStroke[numberPosition]
                val judul = etEditJudul.text.toString().trim()
                val deskripsi = etEditDeskripsi.text.toString().trim()

                postUpdateValueTentangStroke(
                    idValueTentangStroke, idHalTentangStroke, judul, deskripsi
                )
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateValueTentangStroke(
        idValueTentangStroke: String, idHalTentangStroke: String, judul: String, deskripsi: String,
    ) {
        viewModel.postUpdateValueTentangStroke(
            idValueTentangStroke, idHalTentangStroke, judul, deskripsi
        )
    }

    private fun getUpdateValueTentangStroke(){
        viewModel.getUpdateValueTentangStroke().observe(this@AdminValueTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminValueTentangStrokeActivity)
                is UIState.Failure-> setFailureUpdateValueTentangStroke(result.message)
                is UIState.Success-> setSuccessUpdateValueTentangStroke(result.data)
            }
        }
    }

    private fun setSuccessUpdateValueTentangStroke(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminValueTentangStrokeActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminValueTentangStrokeActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminValueTentangStrokeActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureUpdateValueTentangStroke(message: String) {
        Toast.makeText(this@AdminValueTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(pesanan: TentangStrokeDetailModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueTentangStrokeActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus ${pesanan.judul!!} ?"
            tvBodyKonfirmasi.text = "Judul dan Isi ini akan hapus dan data tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                val idValueTentangStroke = pesanan.id_val_tentang_stroke!!
                postHapusValueTentangStroke(idValueTentangStroke)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusValueTentangStroke(idValueTentangStroke: String) {
        viewModel.postHapusValueTentangStroke(idValueTentangStroke)
    }

    private fun getHapusValueTentangStroke(){
        viewModel.getHapusValueTentangStroke().observe(this@AdminValueTentangStrokeActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminValueTentangStrokeActivity)
                is UIState.Failure-> setFailureHapusValueTentangStroke(result.message)
                is UIState.Success-> setSuccessHapusValueTentangStroke(result.data)
            }
        }
    }

    private fun setSuccessHapusValueTentangStroke(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminValueTentangStrokeActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminValueTentangStrokeActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminValueTentangStrokeActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureHapusValueTentangStroke(message: String) {
        Toast.makeText(this@AdminValueTentangStrokeActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setStopShimmer(){
        binding.apply {
            rvValTentangStroke.visibility = View.VISIBLE

            smValTentangStroke.stopShimmer()
            smValTentangStroke.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smValTentangStroke.startShimmer()
            smValTentangStroke.visibility = View.VISIBLE

            rvValTentangStroke.visibility = View.GONE
        }
    }
}