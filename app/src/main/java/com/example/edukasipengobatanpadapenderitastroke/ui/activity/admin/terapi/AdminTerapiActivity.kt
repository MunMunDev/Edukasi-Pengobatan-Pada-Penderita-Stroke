package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.terapi

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
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminTerapiAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminTerapiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKeteranganBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogTerapiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
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
class AdminTerapiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminTerapiBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminTerapiViewModel by viewModels()
    private lateinit var adapter: AdminTerapiAdapter
    private var tempAlertDialog: AlertDialog? = null
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var tempView: AlertDialogTerapiBinding? = null
    private var fileImage: MultipartBody.Part? = null
    private val kataAcak = KataAcak()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminTerapiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchTerapi()
        getTerapi()
        getTambahTerapi()
        getUpdateTerapi()
        getHapusTerapi()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminTerapiActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminTerapiActivity)
        }
    }

    private fun setButton() {
        binding.btnTambah.setOnClickListener {
            dialogTambahTerapi()
        }
    }

    private fun dialogTambahTerapi() {
        val view = AlertDialogTerapiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminTerapiActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {
            etEditGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            btnSimpan.setOnClickListener {
                var check = true

                if(etEditGambar.text.toString().trim() == "Masukkan Gambar".trim()){
                    etEditGambar.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditJudul.text.isEmpty()){
                    etEditJudul.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditDeskripsi.text.isEmpty()){
                    etEditDeskripsi.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(check){
                    val kata = kataAcak.getHurufDanAngka()
                    val judul = etEditJudul.text.toString()
                    val deskripsi = etEditDeskripsi.text.toString()
                    postTambahTerapi(kata, judul, deskripsi, fileImage!!)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postTambahTerapi(
        kataAcak: String, judul: String, deskripsi: String, fileImage: MultipartBody.Part
    ) {
        viewModel.postTambahTerapi(
            convertStringToMultipartBody("post_tambah_terapi"),
            convertStringToMultipartBody(kataAcak),
            convertStringToMultipartBody(judul),
            convertStringToMultipartBody(deskripsi),
            fileImage
        )
    }

    private fun getTambahTerapi(){
        viewModel.getResponseTambahTerapi().observe(this@AdminTerapiActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminTerapiActivity)
                is UIState.Failure-> setFailureTambahTerapi(result.message)
                is UIState.Success-> setSuccessHalamTambahanTerapi(result.data)
            }
        }
    }

    private fun setFailureTambahTerapi(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminTerapiActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamTambahanTerapi(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminTerapiActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                fetchTerapi()
            } else{
                Toast.makeText(this@AdminTerapiActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminTerapiActivity, "ada error di API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTerapi() {
        viewModel.fetchTerapi()
    }

    private fun getTerapi(){
        viewModel.getTerapi().observe(this@AdminTerapiActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTerapi(result.message)
                is UIState.Success-> setSuccessTerapi(result.data)
            }
        }
    }

    private fun setFailureTerapi(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminTerapiActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTerapi(data: ArrayList<TerapiModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminTerapiActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<TerapiModel>) {
        adapter = AdminTerapiAdapter(data, object : OnClickItem.ClickAdminTerapi{
            override fun clickItemJudul(namaTerapi: String, it: View) {
                showClickText("Nama Terapip", namaTerapi)
            }

            override fun clickItemDeskripsi(deskripsi: String, it: View) {
                showClickText("Deskripsi Terapi", deskripsi)
            }

            override fun clickItemGambar(gambar: String, title: String, it: View) {
                setShowImage(gambar, title)
            }

            override fun clickItemSetting(terapi: TerapiModel, it: View) {
                val popupMenu = PopupMenu(this@AdminTerapiActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                dialogEditTerapi(terapi)
                                return true
                            }
                            R.id.hapus -> {
                                dialogHapusTerapi(terapi.id_terapi!!, terapi.nama_terapi!!)
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
            rvTerapi.layoutManager = LinearLayoutManager(this@AdminTerapiActivity, LinearLayoutManager.VERTICAL, false)
            rvTerapi.adapter = adapter
        }
    }

    private fun showClickText(keterangan:String, judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminTerapiActivity)
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

        val alertDialog = AlertDialog.Builder(this@AdminTerapiActivity)
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

        Glide.with(this@AdminTerapiActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun dialogEditTerapi(terapi: TerapiModel) {
        val view = AlertDialogTerapiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminTerapiActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Update Halaman Galeri Herbal"

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {
            etEditJudul.setText(terapi.nama_terapi)
            etEditDeskripsi.setText(terapi.deskripsi)

            etEditGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

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
                    if(fileImage != null){
                        postEditTerapi(
                            terapi.id_terapi!!, kataAcak.getHurufDanAngka(),
                            etEditJudul.text.toString(), etEditDeskripsi.text.toString(),
                            fileImage!!
                        )
                    } else{
                        postEditTerapiNoHaveImage(
                            terapi.id_terapi!!,
                            etEditJudul.text.toString(), etEditDeskripsi.text.toString()
                        )
                    }
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postEditTerapi(
        idHalTerapi: String, kataAcak: String, judul: String, deskripsi: String, fileImage: MultipartBody.Part
    ) {
        viewModel.postUpdateTerapi(
            convertStringToMultipartBody("post_update_terapi"),
            convertStringToMultipartBody(idHalTerapi),
            convertStringToMultipartBody(kataAcak),
            convertStringToMultipartBody(judul),
            convertStringToMultipartBody(deskripsi),
            fileImage
        )
    }

    private fun postEditTerapiNoHaveImage(
        idHalTerapi: String, judul: String, deskripsi: String
    ) {
        viewModel.postUpdateTerapiNoHaveImage(
            "post_update_terapi_no_have_image", idHalTerapi, judul, deskripsi
        )
    }

    private fun getUpdateTerapi(){
        viewModel.getResponseUpdateTerapi().observe(this@AdminTerapiActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminTerapiActivity)
                is UIState.Failure-> setFailureUpdateTerapi(result.message)
                is UIState.Success-> setSuccessHalamUpdateanTerapi(result.data)
            }
        }
    }

    private fun setFailureUpdateTerapi(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminTerapiActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamUpdateanTerapi(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminTerapiActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchTerapi()
            } else{
                Toast.makeText(this@AdminTerapiActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminTerapiActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialogHapusTerapi(idHalTerapi: String, namaTerapi: String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminTerapiActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Terapi ini?"
            tvBodyKonfirmasi.text = "$namaTerapi akan dihapus dan data yang ada di dalamnya akan ikut terhapus. data yang telah terhapus tidak dapat di kembalikan"

            btnKonfirmasi.setOnClickListener {
                postHapusTerapi(idHalTerapi!!)
            }
            btnBatal.setOnClickListener {
                tempAlertDialog = null
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusTerapi(idHalTerapi: String) {
        viewModel.postHapusTerapi(idHalTerapi)
    }

    private fun getHapusTerapi(){
        viewModel.getResponseHapusTerapi().observe(this@AdminTerapiActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminTerapiActivity)
                is UIState.Failure-> setFailureHapusTerapi(result.message)
                is UIState.Success-> setSuccessHapusTerapi(result.data)
            }
        }
    }

    private fun setFailureHapusTerapi(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminTerapiActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHapusTerapi(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminTerapiActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchTerapi()
            } else{
                Toast.makeText(this@AdminTerapiActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminTerapiActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvTerapi.visibility = View.VISIBLE

            smTerapi.stopShimmer()
            smTerapi.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTerapi.startShimmer()
            smTerapi.visibility = View.VISIBLE

            rvTerapi.visibility = View.GONE
        }
    }


    // permission add image
    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminTerapiAdapter::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, Constant.IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etEditGambar.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "galeri_herbal_image")
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(imageUri: Uri?, fileName: String, nameAPI:String): MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        imageUri?.let {
            val file = contentResolver.openInputStream(imageUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", fileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, fileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, fileName: String, nameAPI:String): MultipartBody.Part?{
//        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val requestFile = file.toRequestBody("application/image".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, fileName, requestFile)

        return filePart
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminTerapiActivity, AdminMainActivity::class.java))
        finish()
    }
}