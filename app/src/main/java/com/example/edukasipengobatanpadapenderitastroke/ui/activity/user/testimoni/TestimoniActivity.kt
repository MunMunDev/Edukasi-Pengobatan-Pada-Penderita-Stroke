package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.testimoni

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
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.admin.AdminHalamanGaleriHerbalAdapter
import com.example.edukasipengobatanpadapenderitastroke.adapter.user.TestimoniAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityTestimoniBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogKonfirmasiBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.KataAcak
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.LoadingAlertDialog
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin
import com.example.edukasipengobatanpadapenderitastroke.utils.TanggalDanWaktu
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TestimoniActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestimoniBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: TestimoniViewModel by viewModels()
    private lateinit var adapter: TestimoniAdapter
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private var testimoniSendiri: ArrayList<TestimoniModel> = arrayListOf()
    private var testimoniOrangLain: ArrayList<TestimoniModel> = arrayListOf()
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject lateinit var loading: LoadingAlertDialog
    private var tempAlertDialog: AlertDialog? = null
    private var fileImage: MultipartBody.Part? = null
    private var kataAcak = KataAcak()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestimoniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferencesLogin()
        setNavigationDrawer()
        fetchTestimoni()
        getTestimoni()
        setButton()
        getTambahTestimoni()
        getUpdateTestimoni()
        getHapusTestimoni()
    }

    private fun setButton() {
        binding.apply {
            btnTambahTestimoni.setOnClickListener {
                openAddTestimoni()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@TestimoniActivity)
    }

    private fun setNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@TestimoniActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@TestimoniActivity)
        }
    }

    private fun fetchTestimoni() {
        viewModel.fetchTestimoni()
    }

    private fun getTestimoni() {
        viewModel.getTestimoni().observe(this@TestimoniActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTestimoni(result.message)
                is UIState.Success-> setSuccessTestimoni(result.data)
            }
        }
    }

    private fun setFailureTestimoni(message: String) {
        setStopShimmer()
        Toast.makeText(this@TestimoniActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTestimoni(data: ArrayList<TestimoniModel>) {
        testimoniSendiri = arrayListOf()
        testimoniOrangLain = arrayListOf()
        for (value in data){
            if(value.id_user!!.trim().toInt() == sharedPreferencesLogin.getIdUser()){
                testimoniSendiri.add(value)
            } else{
                testimoniOrangLain.add(value)
            }
        }

        if(testimoniSendiri.size>0){
            setHaveDataTestimoni(testimoniSendiri)
        } else{
            setNoHaveDataTestimoni()
        }

//        setAdapter(data)
        setAdapter(testimoniOrangLain)
    }

    private fun setHaveDataTestimoni(testimoniSendiri: ArrayList<TestimoniModel>) {
        binding.apply {
            llTestimoniSendiri.visibility = View.VISIBLE
            btnTambahTestimoni.visibility = View.GONE
            llPostTestimoniSendiri.visibility = View.GONE

            tvTanggal.text = tanggalDanWaktu.konversiBulan(testimoniSendiri[0].tanggal!!)
            tvTestimoni.text = testimoniSendiri[0].testimoni!!

            when (testimoniSendiri[0].bintang!!.trim().toInt()) {
                1 -> {
                    setBintang1(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
                }
                2 -> {
                    setBintang2(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
                }
                3 -> {
                    setBintang3(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
                }
                4 -> {
                    setBintang4(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
                }
                5 -> {
                    setBintang5(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
                }
            }

            if(testimoniSendiri[0].gambar!!.trim().isEmpty()){
                ivBukti.visibility = View.GONE
            } else{
                ivBukti.visibility = View.VISIBLE

                Glide.with(this@TestimoniActivity)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${testimoniSendiri[0].gambar}") // URL Gambar
                    .error(R.drawable.gambar_error_image)
                    .into(ivBukti) // imageView mana yang akan diterapkan

//                Toast.makeText(this@TestimoniActivity, "${testimoniSendiri[0].gambar}", Toast.LENGTH_SHORT).show()
            }
            ivBukti.setOnClickListener {
                setShowImage(testimoniSendiri[0].gambar!!, testimoniSendiri[0].nama!!)
            }
            btnEdit.setOnClickListener {
                openEditTestimoni(testimoniSendiri)
            }
            btnHapus.setOnClickListener {
                setDialogHapusTestimoni(testimoniSendiri[0])
            }
        }

    }

    private fun setDialogHapusTestimoni(testimoni: TestimoniModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@TestimoniActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Testimoni ini?"
            tvBodyKonfirmasi.text = "Testimoni akan dihapus dan tidak dapat di kembalikan"

            btnKonfirmasi.setOnClickListener {
                postHapusTestimoni(testimoni.id_testimoni!!)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postHapusTestimoni(idTestimoni: String) {
        viewModel.postHapusTestimoni(idTestimoni)
    }

    private fun getHapusTestimoni(){
        viewModel.getResponseHapusTestimoni().observe(this@TestimoniActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@TestimoniActivity)
                is UIState.Failure-> setFailureHapusTestimoni(result.message)
                is UIState.Success-> setSuccessHapusTestimoni(result.data)
            }
        }
    }

    private fun setFailureHapusTestimoni(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@TestimoniActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessHapusTestimoni(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@TestimoniActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                binding.apply {
                    etTestimoni.setText("")
                    setBintang0(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
                }

                fetchTestimoni()
            } else{
                Toast.makeText(this@TestimoniActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@TestimoniActivity, "Ada error di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setNoHaveDataTestimoni(){
        binding.apply {
            btnTambahTestimoni.visibility = View.VISIBLE
            llPostTestimoniSendiri.visibility = View.GONE
            llTestimoniSendiri.visibility = View.GONE
        }
    }

    private fun setHiddenDataTestimoni(){
        binding.apply {
            btnTambahTestimoni.visibility = View.GONE
            llPostTestimoniSendiri.visibility = View.GONE
            llTestimoniSendiri.visibility = View.GONE
        }
    }

    private fun openAddTestimoni(){
        binding.apply {
            llPostTestimoniSendiri.visibility = View.VISIBLE
            btnTambahTestimoni.visibility = View.GONE
            llTestimoniSendiri.visibility = View.GONE

            var jumlahBintang = 0
            ivPostBintang1.setOnClickListener {
                setBintang1(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 1
            }
            ivPostBintang2.setOnClickListener {
                setBintang2(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 2
            }
            ivPostBintang3.setOnClickListener {
                setBintang3(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 3
            }
            ivPostBintang4.setOnClickListener {
                setBintang4(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 4
            }
            ivPostBintang5.setOnClickListener {
                setBintang5(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 5
            }

            etImageBukti.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnKirimTestimoni.setOnClickListener {
                if(jumlahBintang==0){
                    Toast.makeText(this@TestimoniActivity, "Masukkan Jumlah Bintang", Toast.LENGTH_SHORT).show()
                } else{
                    var check = true
                    if(etTestimoni.text.isEmpty()){
                        etTestimoni.error = "Masukkan Testimoni"
                        check = false
                    }

                    if(tvTextImageBukti.text.toString() == "Masukkan Gambar"){
                        tvTextImageBukti.error = "Masukkan Gambar"
                        check = false
                    }

                    if(check){
                        val idUser = sharedPreferencesLogin.getIdUser().toString()
                        val testimoni = etTestimoni.text.toString()
                        val kata = kataAcak.getHurufDanAngka()
                        Toast.makeText(this@TestimoniActivity, "$idUser dan kata $kata", Toast.LENGTH_SHORT).show()

//                        postTambahTestimoni(idUser, testimoni, jumlahBintang.toString())
                        postTambahTestimoni(
                            kata, idUser, testimoni, jumlahBintang.toString(), fileImage!!
                        )
                    }

                }
            }
            btnBatalTambah.setOnClickListener {
                btnTambahTestimoni.visibility = View.VISIBLE
                llPostTestimoniSendiri.visibility = View.GONE
                llTestimoniSendiri.visibility = View.GONE

//                etTestimoni.setText("")
//                tvTextImageBukti.text = ""
//                fileImage = null
//                setBintang0(ivBintang1, ivBintang2, ivBintang3, ivBintang4, ivBintang5)
            }
        }
    }

    private fun postTambahTestimoni(kata:String, id_user:String, testimoni:String, bintang:String, file: MultipartBody.Part) {
//        viewModel.postTambahData(id_user, testimoni, bintang)

        viewModel.postTambahData(
            convertStringToMultipartBody("post_tambah_testimoni"),
            convertStringToMultipartBody(kata),
            convertStringToMultipartBody(id_user),
            convertStringToMultipartBody(testimoni),
            convertStringToMultipartBody(bintang),
            file
        )
    }

    private fun getTambahTestimoni(){
        viewModel.getResponseTambahTestimoni().observe(this@TestimoniActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@TestimoniActivity)
                is UIState.Failure-> setFailureTambahTestimoni(result.message)
                is UIState.Success-> setSuccessTambahTestimoni(result.data)
            }
        }
    }

    private fun setFailureTambahTestimoni(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@TestimoniActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTambahTestimoni(data: java.util.ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@TestimoniActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                setHiddenDataTestimoni()
                fetchTestimoni()
            } else{
                Toast.makeText(this@TestimoniActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openEditTestimoni(testimoniSendiri: ArrayList<TestimoniModel>) {
        binding.apply {
            llPostTestimoniSendiri.visibility = View.VISIBLE
            btnTambahTestimoni.visibility = View.GONE
            llTestimoniSendiri.visibility = View.GONE

            etTestimoni.setText(testimoniSendiri[0].testimoni)
            when (testimoniSendiri[0].bintang!!.trim().toInt()) {
                1 -> {
                    setBintang1(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                }
                2 -> {
                    setBintang2(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                }
                3 -> {
                    setBintang3(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                }
                4 -> {
                    setBintang4(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                }
                5 -> {
                    setBintang5(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                }
            }

            var jumlahBintang = testimoniSendiri[0].bintang!!.trim().toInt()
            ivPostBintang1.setOnClickListener {
                setBintang1(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 1
            }
            ivPostBintang2.setOnClickListener {
                setBintang2(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 2
            }
            ivPostBintang3.setOnClickListener {
                setBintang3(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 3
            }
            ivPostBintang4.setOnClickListener {
                setBintang4(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 4
            }
            ivPostBintang5.setOnClickListener {
                setBintang5(ivPostBintang1, ivPostBintang2, ivPostBintang3, ivPostBintang4, ivPostBintang5)
                jumlahBintang = 5
            }

            etImageBukti.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnKirimTestimoni.setOnClickListener {
                if(jumlahBintang==0){
                    Toast.makeText(this@TestimoniActivity, "Masukkan Jumlah Bintang", Toast.LENGTH_SHORT).show()
                } else{
                    val idTestimoni = testimoniSendiri[0].id_testimoni!!
                    val idUser = sharedPreferencesLogin.getIdUser().toString()
                    val testimoni = etTestimoni.text.toString()
                    val kata = kataAcak.getHurufDanAngka()

                    if(fileImage == null){
                        postEditTestimoniNoHaveImage(idTestimoni, idUser, testimoni, jumlahBintang.toString())
                    } else{
                        postEditTestimoni(kata, idTestimoni, idUser, testimoni, jumlahBintang.toString(), fileImage!!)
                    }
                }
            }

            btnBatalTambah.setOnClickListener {
                llTestimoniSendiri.visibility = View.VISIBLE
                llPostTestimoniSendiri.visibility = View.GONE
                btnTambahTestimoni.visibility = View.GONE

                tvTextImageBukti.text = ""
                fileImage = null
            }
        }
    }
//    private fun postEditTestimoni(id_testimoni:String, id_user:String, testimoni:String, bintang:String) {
//        viewModel.postUpdatehData(
//            id_testimoni, id_user, testimoni, bintang
//        )
//    }

    private fun postEditTestimoni(kata:String, id_testimoni:String, id_user:String, testimoni:String, bintang:String, file: MultipartBody.Part) {
        viewModel.postUpdatehData(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(kata),
            convertStringToMultipartBody(id_testimoni),
            convertStringToMultipartBody(id_user),
            convertStringToMultipartBody(testimoni),
            convertStringToMultipartBody(bintang),
            file
        )
    }

    private fun postEditTestimoniNoHaveImage(id_testimoni:String, id_user:String, testimoni:String, bintang:String) {
        viewModel.postUpdatehDataNoHaveData(
            id_testimoni, id_user, testimoni, bintang
        )
    }

    private fun getUpdateTestimoni(){
        viewModel.getResponseUpdateTestimoni().observe(this@TestimoniActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@TestimoniActivity)
                is UIState.Failure-> setFailureUpdateTestimoni(result.message)
                is UIState.Success-> setSuccessUpdateTestimoni(result.data)
            }
        }
    }

    private fun setFailureUpdateTestimoni(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@TestimoniActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateTestimoni(data: java.util.ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@TestimoniActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                binding.tvTextImageBukti.text = ""
                setHiddenDataTestimoni()
                fetchTestimoni()
            } else{
                Toast.makeText(this@TestimoniActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBintang0(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_non_aktif)
        iv2.setImageResource(R.drawable.icon_star_non_aktif)
        iv3.setImageResource(R.drawable.icon_star_non_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang1(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_non_aktif)
        iv3.setImageResource(R.drawable.icon_star_non_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang2(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_non_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang3(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_aktif)
        iv4.setImageResource(R.drawable.icon_star_non_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang4(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_aktif)
        iv4.setImageResource(R.drawable.icon_star_aktif)
        iv5.setImageResource(R.drawable.icon_star_non_aktif)
    }
    private fun setBintang5(
        iv1: ImageView,
        iv2: ImageView,
        iv3: ImageView,
        iv4: ImageView,
        iv5: ImageView,
    ){
        iv1.setImageResource(R.drawable.icon_star_aktif)
        iv2.setImageResource(R.drawable.icon_star_aktif)
        iv3.setImageResource(R.drawable.icon_star_aktif)
        iv4.setImageResource(R.drawable.icon_star_aktif)
        iv5.setImageResource(R.drawable.icon_star_aktif)
    }

    private fun setAdapter(data: ArrayList<TestimoniModel>) {
        adapter = TestimoniAdapter(data, sharedPreferencesLogin.getIdUser().toString(), false, object: OnClickItem.ClickTestimoni{
            override fun clickGambar(gambar: String, nama: String, it: View) {
                setShowImage(gambar, nama)
            }

        })
        binding.apply {
            rvTestimoni.layoutManager = LinearLayoutManager(this@TestimoniActivity, LinearLayoutManager.VERTICAL, false)
            rvTestimoni.adapter = adapter
        }
        setStopShimmer()
    }

    private fun setShowImage(gambar: String, nama: String){
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@TestimoniActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = nama
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@TestimoniActivity)
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

            binding.tvTextImageBukti.text = nameImage

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "gambar_testimoni")
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
        startActivity(Intent(this@TestimoniActivity, MainActivity::class.java))
        finish()
    }
}