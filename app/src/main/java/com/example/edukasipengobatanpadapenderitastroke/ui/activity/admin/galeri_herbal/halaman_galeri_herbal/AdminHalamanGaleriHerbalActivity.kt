package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.halaman_galeri_herbal

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
import android.util.Log
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
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminHalamanGaleriHerbalAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminHalamanGaleriHerbalBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogHalGaleriHerbalBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.value_galeri_herbal.AdminValueGaleriHerbalActivity
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
class AdminHalamanGaleriHerbalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHalamanGaleriHerbalBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminHalamanGaleriHerbalViewModel by viewModels()
    private lateinit var adapter: AdminHalamanGaleriHerbalAdapter
    private var tempAlertDialog: AlertDialog? = null
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var tempView: AlertDialogHalGaleriHerbalBinding? = null
    private var fileImage: MultipartBody.Part? = null
    private val kataAcak = KataAcak()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHalamanGaleriHerbalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchGaleriHerbal()
        getGaleriHerbal()
        getTambahHalamanGaleriHerbal()
        getUpdateHalamanGaleriHerbal()
        getHapusHalamanGaleriHerbal()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminHalamanGaleriHerbalActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminHalamanGaleriHerbalActivity)
        }
    }

    private fun setButton() {
        binding.btnTambah.setOnClickListener {
            dialogTambahHalamanGaleriHerbal()
        }
    }

    private fun dialogTambahHalamanGaleriHerbal() {
        val view = AlertDialogHalGaleriHerbalBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanGaleriHerbalActivity)
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
                    postTambahHalamanGaleriHerbal(kata, judul, deskripsi, fileImage!!)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postTambahHalamanGaleriHerbal(
        kataAcak: String, judul: String, deskripsi: String, fileImage: MultipartBody.Part
    ) {
        viewModel.postTambahHalamanGaleriHerbal(
            convertStringToMultipartBody("post_tambah_hal_galeri_herbal"),
            convertStringToMultipartBody(kataAcak),
            convertStringToMultipartBody(judul),
            convertStringToMultipartBody(deskripsi),
            fileImage
        )
    }

    private fun getTambahHalamanGaleriHerbal(){
        viewModel.getResponseTambahHalamanGaleriHerbal().observe(this@AdminHalamanGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminHalamanGaleriHerbalActivity)
                is UIState.Failure-> setFailureTambahHalamanGaleriHerbal(result.message)
                is UIState.Success-> setSuccessHalamTambahanGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureTambahHalamanGaleriHerbal(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminHalamanGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamTambahanGaleriHerbal(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                fetchGaleriHerbal()
            } else{
                Toast.makeText(this@AdminHalamanGaleriHerbalActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchGaleriHerbal() {
        viewModel.fetchGaleriHerbal()
    }

    private fun getGaleriHerbal(){
        viewModel.getGaleriHerbal().observe(this@AdminHalamanGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureGaleriHerbal(result.message)
                is UIState.Success-> setSuccessGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureGaleriHerbal(message: String) {
        setStopShimmer()
        Toast.makeText(this@AdminHalamanGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessGaleriHerbal(data: ArrayList<GaleriHerbalMainModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<GaleriHerbalMainModel>) {
        adapter = AdminHalamanGaleriHerbalAdapter(data, object : OnClickItem.ClickAdminHalamanGaleriHerbal{
            override fun clickItemGambar(gambar: String, title: String, it: View) {
                setShowImage(gambar, title)
            }

            override fun clickItemNextPage(galeriHerbal: GaleriHerbalMainModel, it: View) {
                val i = Intent(this@AdminHalamanGaleriHerbalActivity, AdminValueGaleriHerbalActivity::class.java)
                i.putExtra("galeri_herbal_main", data)
                i.putExtra("id_hal_galeri_herbal", galeriHerbal.id_hal_galeri_herbal)
                i.putExtra("judul", galeriHerbal.judul)
                startActivity(i)
            }

            override fun clickItemSetting(galeriHerbal: GaleriHerbalMainModel, it: View) {
                val popupMenu = PopupMenu(this@AdminHalamanGaleriHerbalActivity, it)
                popupMenu.inflate(R.menu.popup_rincian_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.rincian -> {
                                val i = Intent(this@AdminHalamanGaleriHerbalActivity, AdminValueGaleriHerbalActivity::class.java)
                                i.putExtra("galeri_herbal_main", data)
                                i.putExtra("id_hal_galeri_herbal", galeriHerbal.id_hal_galeri_herbal)
                                i.putExtra("judul", galeriHerbal.judul)
                                startActivity(i)
                                return true
                            }
                            R.id.edit -> {
                                dialogEditHalamanGaleriHerbal(galeriHerbal)
                                return true
                            }
                            R.id.hapus -> {
                                dialogHapusHalamanGaleriHerbal(galeriHerbal.id_hal_galeri_herbal!!, galeriHerbal.judul!!)
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
            rvHalGaleriHerbal.layoutManager = LinearLayoutManager(this@AdminHalamanGaleriHerbalActivity, LinearLayoutManager.VERTICAL, false)
            rvHalGaleriHerbal.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, title:String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanGaleriHerbalActivity)
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

        Glide.with(this@AdminHalamanGaleriHerbalActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun dialogEditHalamanGaleriHerbal(galeriHerbal: GaleriHerbalMainModel) {
        val view = AlertDialogHalGaleriHerbalBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanGaleriHerbalActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Update Halaman Galeri Herbal"

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {
            etEditJudul.setText(galeriHerbal.judul)
            etEditDeskripsi.setText(galeriHerbal.deskripsi)

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
                        postEditHalamanGaleriHerbal(
                            galeriHerbal.id_hal_galeri_herbal!!, kataAcak.getHurufDanAngka(),
                            etEditJudul.text.toString(), etEditDeskripsi.text.toString(),
                            fileImage!!
                        )
                    } else{
                        postEditHalamanGaleriHerbalNoHaveImage(
                            galeriHerbal.id_hal_galeri_herbal!!,
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

    private fun postEditHalamanGaleriHerbal(
        idHalGaleriHerbal: String, kataAcak: String, judul: String, deskripsi: String, fileImage: MultipartBody.Part
    ) {
        viewModel.postUpdateHalamanGaleriHerbal(
            convertStringToMultipartBody("post_update_hal_galeri_herbal"),
            convertStringToMultipartBody(idHalGaleriHerbal),
            convertStringToMultipartBody(kataAcak),
            convertStringToMultipartBody(judul),
            convertStringToMultipartBody(deskripsi),
            fileImage
        )
    }

    private fun postEditHalamanGaleriHerbalNoHaveImage(
        idHalGaleriHerbal: String, judul: String, deskripsi: String
    ) {
        viewModel.postUpdateHalamanGaleriHerbalNoHaveImage(
            "post_update_hal_galeri_herbal_no_have_image", idHalGaleriHerbal, judul, deskripsi
        )
    }

    private fun getUpdateHalamanGaleriHerbal(){
        viewModel.getResponseUpdateHalamanGaleriHerbal().observe(this@AdminHalamanGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminHalamanGaleriHerbalActivity)
                is UIState.Failure-> setFailureUpdateHalamanGaleriHerbal(result.message)
                is UIState.Success-> setSuccessHalamUpdateanGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureUpdateHalamanGaleriHerbal(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminHalamanGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHalamUpdateanGaleriHerbal(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchGaleriHerbal()
            } else{
                Toast.makeText(this@AdminHalamanGaleriHerbalActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialogHapusHalamanGaleriHerbal(idHalGaleriHerbal: String, halaman: String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminHalamanGaleriHerbalActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus halaman ini?"
            tvBodyKonfirmasi.text = "Halaman $halaman akan dihapus dan data yang ada di dalamnya akan ikut terhapus. data yang telah terhapus tidak dapat di kembalikan"

            btnKonfirmasi.setOnClickListener {
                postHapusHalamanGaleriHerbal(idHalGaleriHerbal!!)
            }
            btnBatal.setOnClickListener {
                tempAlertDialog = null
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusHalamanGaleriHerbal(idHalGaleriHerbal: String) {
        viewModel.postHapusHalamanGaleriHerbal(idHalGaleriHerbal)
    }

    private fun getHapusHalamanGaleriHerbal(){
        viewModel.getResponseHapusHalamanGaleriHerbal().observe(this@AdminHalamanGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminHalamanGaleriHerbalActivity)
                is UIState.Failure-> setFailureHapusHalamanGaleriHerbal(result.message)
                is UIState.Success-> setSuccessHapusHalamanGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureHapusHalamanGaleriHerbal(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminHalamanGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHapusHalamanGaleriHerbal(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                fetchGaleriHerbal()
            } else{
                Toast.makeText(this@AdminHalamanGaleriHerbalActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminHalamanGaleriHerbalActivity, "ada error di aplikasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvHalGaleriHerbal.visibility = View.VISIBLE

            smHalGaleriHerbal.stopShimmer()
            smHalGaleriHerbal.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smHalGaleriHerbal.startShimmer()
            smHalGaleriHerbal.visibility = View.VISIBLE

            rvHalGaleriHerbal.visibility = View.GONE
        }
    }


    // permission add image
    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminHalamanGaleriHerbalAdapter::class.java))
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
        startActivity(Intent(this@AdminHalamanGaleriHerbalActivity, AdminMainActivity::class.java))
        finish()
    }
}