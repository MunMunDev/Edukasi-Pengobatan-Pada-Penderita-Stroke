package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.value_galeri_herbal

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminValueGaleriHerbalAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminValueGaleriHerbalBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogHalGaleriHerbalBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKeteranganBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogValGaleriHerbalBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.KataAcak
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.Youtube
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminValueGaleriHerbalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminValueGaleriHerbalBinding
    private val viewModel: AdminValueGaleriHerbalViewModel by viewModels()
    private lateinit var adapter: AdminValueGaleriHerbalAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var idHalGaleriHerbal = "1"
    private lateinit var listHalamanGaleriHerbal: ArrayList<String>
    private lateinit var listIdHalamanGaleriHerbal: ArrayList<String>
    private var youtube = Youtube()
    private var tempAlertDialog: AlertDialog? = null
    private var tempView: AlertDialogValGaleriHerbalBinding? = null
    private var fileImage: MultipartBody.Part? = null
    private var kataAcak = KataAcak()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminValueGaleriHerbalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        fetchData()
        getValueGaleriHerbal()
        getTambahValueGaleriHerbal()
        getUpdateValueGaleriHerbal()
        getHapusValueGaleriHerbal()
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            var halGaleriHerbal = extras.getParcelableArrayList<GaleriHerbalMainModel>("galeri_herbal_main")!!
            idHalGaleriHerbal = extras.getString("id_hal_galeri_herbal")!!
            listHalamanGaleriHerbal = searchDataArrayHalaman(halGaleriHerbal)
            listIdHalamanGaleriHerbal = searchDataArrayId(halGaleriHerbal)
            val judul = extras.getString("judul")!!
            binding.titleHeader.text = judul
        }
    }

    private fun searchDataArrayHalaman(list: ArrayList<GaleriHerbalMainModel>): ArrayList<String> {
        val valueList: ArrayList<String> = arrayListOf()
        for(value in list){
            valueList.add(value.judul!!)
        }

        return valueList
    }

    private fun searchDataArrayId(list: ArrayList<GaleriHerbalMainModel>): ArrayList<String> {
        val valueList: ArrayList<String> = arrayListOf()
        for(value in list){
            valueList.add(value.id_hal_galeri_herbal!!)
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
        val view = AlertDialogValGaleriHerbalBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueGaleriHerbalActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {

            var numberPosition = 0
            var selectedValue = ""

            val tambahDialogHalaman = binding.titleHeader.text.toString().trim()
            val listTambahDialog : ArrayList<String> = arrayListOf()

            listTambahDialog.add(tambahDialogHalaman)


            val arrayAdapter = ArrayAdapter(this@AdminValueGaleriHerbalActivity, android.R.layout.simple_spinner_item, listTambahDialog)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spHalamanGaleriHerbal.adapter = arrayAdapter

            spHalamanGaleriHerbal.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spHalamanGaleriHerbal.selectedItemPosition
                    selectedValue = spHalamanGaleriHerbal.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spHalamanGaleriHerbal.adapter = arrayAdapter

            etEditGambar.setOnClickListener{
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener {
                var checkValue = true
                if(etEditGambar.text.toString() == "Masukkan Gambar"){
                    etEditGambar.error = "Masukkan gambar"
                    checkValue = false
                }
                if(etEditJudul.text.isEmpty()){
                    etEditJudul.error = "Masukkan gambar"
                    checkValue = false
                }
                if(etEditDeskripsi.text.isEmpty()){
                    etEditDeskripsi.error = "Masukkan gambar"
                    checkValue = false
                }
                if(etEditTataCaraPengolahan.text.isEmpty()){
                    etEditTataCaraPengolahan.error = "Masukkan gambar"
                    checkValue = false
                }
                if(etEditLinkYoutube.text.isEmpty()){
                    etEditLinkYoutube.error = "Masukkan gambar"
                    checkValue = false
                }

                if(checkValue){
                    val idHalGaleriHerbal = idHalGaleriHerbal
                    val nama = etEditJudul.text.toString().trim()
                    val judul = etEditJudul.text.toString().trim()
                    val deskripsi = etEditDeskripsi.text.toString().trim()
                    val tataCaraPengolahan = etEditTataCaraPengolahan.text.toString().trim()
                    val linkYoutube = etEditLinkYoutube.text.toString().trim()

                    postTambahValueGaleriHerbal(
                        idHalGaleriHerbal, nama, judul, deskripsi, tataCaraPengolahan, fileImage!!, linkYoutube
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
                tempView = null
            }
        }
    }

    private fun postTambahValueGaleriHerbal(
        idHalGaleriHerbal: String, kataAcak: String, nama: String, deskripsi: String, tataCaraPengolahan: String,
        fileImage: MultipartBody.Part, linkYoutube: String,
    ) {
        viewModel.postTambahValueGaleriHerbal(
            convertStringToMultipartBody("post_tambah_val_galeri_herbal"),
            convertStringToMultipartBody(idHalGaleriHerbal),
            convertStringToMultipartBody(kataAcak),
            convertStringToMultipartBody(nama),
            convertStringToMultipartBody(deskripsi),
            convertStringToMultipartBody(tataCaraPengolahan),
            fileImage,
            convertStringToMultipartBody(linkYoutube),
        )
    }

    private fun getTambahValueGaleriHerbal(){
        viewModel.getResponseTambahValueGaleriHerbal().observe(this@AdminValueGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminValueGaleriHerbalActivity)
                is UIState.Failure-> setFailureTambahValueGaleriHerbal(result.message)
                is UIState.Success-> setSuccessTambahValueGaleriHerbal(result.data)
            }
        }
    }

    private fun setSuccessTambahValueGaleriHerbal(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminValueGaleriHerbalActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                fetchData()
            } else{
                Toast.makeText(this@AdminValueGaleriHerbalActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminValueGaleriHerbalActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureTambahValueGaleriHerbal(message: String) {
        Toast.makeText(this@AdminValueGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchData() {
        viewModel.fetchGaleriHerbal(idHalGaleriHerbal)
    }
    private fun getValueGaleriHerbal(){
        viewModel.getGaleriHerbal().observe(this@AdminValueGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchValueGaleriHerbal(result.message)
                is UIState.Success-> setSuccessFetchValueGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureFetchValueGaleriHerbal(message: String) {
        Toast.makeText(this@AdminValueGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
    }

    private fun setSuccessFetchValueGaleriHerbal(data: ArrayList<GaleriHerbalListModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminValueGaleriHerbalActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<GaleriHerbalListModel>) {
        adapter = AdminValueGaleriHerbalAdapter(data, object: OnClickItem.ClickAdminValueGaleriHerbal{
            override fun clickItemJudul(judul: String, it: View) {
                showClickText("Judul Isi", judul)
            }

            override fun clickItemDeskripsi(deskripsi: String, it: View) {
                showClickText("Isi Penjelasan", deskripsi)
            }

            override fun clickItemTataCaraPengolahan(tataCaraPengolahan: String, it: View) {
                showClickText("Tata Cara Pengolahan", tataCaraPengolahan)
            }

            override fun clickItemGambar(gambar: String, title: String, it: View) {
                setShowImage(gambar, title)
            }

            override fun clickItemYoutube(linkYoutube: String, it: View) {
                youtube.setToYoutubeVideo(this@AdminValueGaleriHerbalActivity, linkYoutube)
            }

            override fun clickItemSetting(galeriHerbal: GaleriHerbalListModel, it: View) {
                val popupMenu = PopupMenu(this@AdminValueGaleriHerbalActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(galeriHerbal)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(galeriHerbal)
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
            rvValGaleriHerbal.layoutManager = LinearLayoutManager(
                this@AdminValueGaleriHerbalActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvValGaleriHerbal.adapter = adapter
        }

    }

    private fun showClickText(keterangan:String, judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueGaleriHerbalActivity)
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

        val alertDialog = AlertDialog.Builder(this@AdminValueGaleriHerbalActivity)
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

        Glide.with(this@AdminValueGaleriHerbalActivity)
            .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setShowDialogEdit(tentangStroke: GaleriHerbalListModel) {
        val view = AlertDialogValGaleriHerbalBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueGaleriHerbalActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Edit Value Galeri Herbal"

        tempAlertDialog = dialogInputan

        view.apply {
            etEditJudul.setText(tentangStroke.nama)
            etEditDeskripsi.setText(tentangStroke.deskripsi)
            etEditTataCaraPengolahan.setText(tentangStroke.tata_cara_pengolahan)
            etEditLinkYoutube.setText(tentangStroke.youtube)

            var numberPosition = 0
            var selectedValue = ""

            val arrayAdapter = ArrayAdapter(this@AdminValueGaleriHerbalActivity, android.R.layout.simple_spinner_item, listHalamanGaleriHerbal)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            var id = 0
            for((no, value) in listIdHalamanGaleriHerbal.withIndex()){
                if(value == tentangStroke.id_hal_galeri_herbal){
                    id = no
                    Log.d("DetailTAG", "val $no: $value")
                }
            }
            Log.d("DetailTAG", "val $id")

            spHalamanGaleriHerbal.setSelection(id)
            spHalamanGaleriHerbal.adapter = arrayAdapter

            spHalamanGaleriHerbal.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spHalamanGaleriHerbal.selectedItemPosition
                    selectedValue = spHalamanGaleriHerbal.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spHalamanGaleriHerbal.setSelection(id)

            etEditGambar.setOnClickListener{
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener {
                var checkValue = true

                if(etEditJudul.text.isEmpty()){
                    etEditJudul.error = "Masukkan Judul"
                    checkValue = false
                }
                if(etEditDeskripsi.text.isEmpty()){
                    etEditJudul.error = "Masukkan Deskripsi"
                    checkValue = false
                }
                if(etEditTataCaraPengolahan.text.isEmpty()){
                    etEditJudul.error = "Masukkan Tata Cara Pengolahan"
                    checkValue = false
                }
                if(etEditLinkYoutube.text.isEmpty()){
                    etEditJudul.error = "Masukkan Link Youtube"
                    checkValue = false
                }

                if(checkValue){
                    val idValueGaleriHerbal = tentangStroke.id_val_galeri_herbal!!
                    val idHalGaleriHerbal = listIdHalamanGaleriHerbal[numberPosition]
                    val nama = etEditJudul.text.toString().trim()
                    val deskripsi = etEditDeskripsi.text.toString().trim()
                    val tataCaraPengolahan = etEditTataCaraPengolahan.text.toString().trim()
                    val linkYoutube = etEditLinkYoutube.text.toString().trim()
                    val valueKataAcak = kataAcak.getHurufDanAngka()

                    if(fileImage != null){
                        postUpdateValueGaleriHerbal(
                            idValueGaleriHerbal, idHalGaleriHerbal, valueKataAcak, nama, deskripsi, tataCaraPengolahan, fileImage!!, linkYoutube
                        )
                    } else{
                        postUpdateValueGaleriHerbalNoHaveImage(
                            idValueGaleriHerbal, idHalGaleriHerbal, nama, deskripsi, tataCaraPengolahan, linkYoutube
                        )
                    }
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
            }
        }
    }

    private fun postUpdateValueGaleriHerbal(
        idValGaleriHerbal: String, idHalGaleriHerbal: String, kataAcak: String, nama: String, deskripsi: String,
        tataCaraPengolahan: String, fileImage: MultipartBody.Part, linkYoutube: String,
    ) {
        viewModel.postUpdateValueGaleriHerbal(
            convertStringToMultipartBody("post_tambah_val_galeri_herbal"),
            convertStringToMultipartBody(idValGaleriHerbal),
            convertStringToMultipartBody(idHalGaleriHerbal),
            convertStringToMultipartBody(kataAcak),
            convertStringToMultipartBody(nama),
            convertStringToMultipartBody(deskripsi),
            convertStringToMultipartBody(tataCaraPengolahan),
            fileImage,
            convertStringToMultipartBody(linkYoutube),
        )
    }

    private fun postUpdateValueGaleriHerbalNoHaveImage(
        idValGaleriHerbal: String, idHalGaleriHerbal: String, nama: String,
        deskripsi: String, tataCaraPengolahan: String, linkYoutube: String,
    ) {
        viewModel.postUpdateValueGaleriHerbalNoHaveImage(
            "post_update_val_galeri_herbal_no_have_image", idValGaleriHerbal, idHalGaleriHerbal,
            nama, deskripsi, tataCaraPengolahan, linkYoutube
        )
    }

    private fun getUpdateValueGaleriHerbal(){
        viewModel.getResponseUpdateValueGaleriHerbal().observe(this@AdminValueGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminValueGaleriHerbalActivity)
                is UIState.Failure-> setFailureUpdateValueGaleriHerbal(result.message)
                is UIState.Success-> setSuccessUpdateValueGaleriHerbal(result.data)
            }
        }
    }

    private fun setSuccessUpdateValueGaleriHerbal(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminValueGaleriHerbalActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                fetchData()
            } else{
                Toast.makeText(this@AdminValueGaleriHerbalActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminValueGaleriHerbalActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureUpdateValueGaleriHerbal(message: String) {
        Toast.makeText(this@AdminValueGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(pesanan: GaleriHerbalListModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminValueGaleriHerbalActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus ${pesanan.nama!!} ?"
            tvBodyKonfirmasi.text = "Data ini akan terhapus dan data tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                val idValueGaleriHerbal = pesanan.id_val_galeri_herbal!!
                postHapusValueGaleriHerbal(idValueGaleriHerbal)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusValueGaleriHerbal(idValueGaleriHerbal: String) {
        viewModel.postHapusValueGaleriHerbal(idValueGaleriHerbal)
    }

    private fun getHapusValueGaleriHerbal(){
        viewModel.getResponseHapusValueGaleriHerbal().observe(this@AdminValueGaleriHerbalActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminValueGaleriHerbalActivity)
                is UIState.Failure-> setFailureHapusValueGaleriHerbal(result.message)
                is UIState.Success-> setSuccessHapusValueGaleriHerbal(result.data)
            }
        }
    }

    private fun setSuccessHapusValueGaleriHerbal(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminValueGaleriHerbalActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminValueGaleriHerbalActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminValueGaleriHerbalActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureHapusValueGaleriHerbal(message: String) {
        Toast.makeText(this@AdminValueGaleriHerbalActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setStopShimmer(){
        binding.apply {
            rvValGaleriHerbal.visibility = View.VISIBLE

            smValGaleriHerbal.stopShimmer()
            smValGaleriHerbal.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smValGaleriHerbal.startShimmer()
            smValGaleriHerbal.visibility = View.VISIBLE

            rvValGaleriHerbal.visibility = View.GONE
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
}