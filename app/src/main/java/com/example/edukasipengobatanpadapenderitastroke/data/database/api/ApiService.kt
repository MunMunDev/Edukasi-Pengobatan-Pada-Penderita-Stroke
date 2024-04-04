package com.example.edukasipengobatanpadapenderitastroke.data.database.api

import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getAllUser(@Query("all_user") allUser: String
    ): ArrayList<UsersModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getUser(@Query("get_user") getUser: String,
                        @Query("username") username: String,
                        @Query("password") password: String
    ): ArrayList<UsersModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getTestimoni(@Query("get_testimoni") getTestimoni: String
    ): ArrayList<TestimoniModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getGaleriHerbalMain(@Query("get_galeri_herbal_main") get_galeri_herbal_main: String
    ): ArrayList<GaleriHerbalMainModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getGaleriHerbalList(@Query("get_galeri_herbal_list") get_galeri_herbal_list: String,
                                    @Query("id_hal_galeri_herbal") id_hal_galeri_herbal: String
    ): ArrayList<GaleriHerbalListModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getTerapi(@Query("get_terapi") get_terapi: String
    ): ArrayList<TerapiModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getTentangStrokeList(@Query("get_tentang_stroke_list") get_tentang_stroke_list: String
    ): ArrayList<TentangStrokeListModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getTentangStrokeDetail(@Query("get_tentang_stroke_detail") get_tentang_stroke_detail: String,
                                       @Query("id_hal_tentang_stroke") id_hal_tentang_stroke: String
    ): ArrayList<TentangStrokeDetailModel>

    @GET("edukasi-pengobatan-stroke/api/get.php")
    suspend fun getMenuSehat(@Query("get_menu_sehat") get_menu_sehat: String
    ): ArrayList<MenuSehatModel>



    // POST
    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateUser(
        @Field("update_akun") updateAkun:String,
        @Field("id_user") idUser: String,
        @Field("nama") nama:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("username_lama") usernameLama: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusUser(
        @Field("hapus_akun") hapusAkun:String,
        @Field("id_user") idUser: String
    ): ArrayList<ResponseModel>


    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addTestimoni(
        @Field("post_tambah_testimoni") post_tambah_testimoni:String,
        @Field("id_user") id_user:String,
        @Field("testimoni") testimoni:String,
        @Field("bintang") bintang:String
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addTestimoni(
        @Part("post_tambah_testimoni") post_tambah_testimoni: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("id_user") id_user: RequestBody,
        @Part("testimoni") testimoni: RequestBody,
        @Part("bintang") bintang: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateTestimoni(
        @Field("post_update_testimoni") post_update_testimoni:String,
        @Field("id_testimoni") id_testimoni:String,
        @Field("id_user") id_user:String,
        @Field("testimoni") testimoni:String,
        @Field("bintang") bintang:String
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateTestimoni(
        @Part("post_update_testimoni") post_update_testimoni: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("id_testimoni") id_testimoni: RequestBody,
        @Part("id_user") id_user: RequestBody,
        @Part("testimoni") testimoni: RequestBody,
        @Part("bintang") bintang: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateTestimoniNoHaveImage(
        @Field("post_update_testimoni_no_have_image") post_update_testimoni_no_have_image:String,
        @Field("id_testimoni") id_testimoni:String,
        @Field("id_user") id_user:String,
        @Field("testimoni") testimoni:String,
        @Field("bintang") bintang:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusTestimoni(
        @Field("post_hapus_testimoni") post_hapus_testimoni:String,
        @Field("id_testimoni") id_testimoni: String
    ): ArrayList<ResponseModel>


    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addHalamanTentangStroke(
        @Field("post_tambah_hal_tentang_stroke") post_tambah_hal_tentang_stroke:String,
        @Field("halaman") halaman:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateHalamanTentangStroke(
        @Field("post_update_hal_tentang_stroke") post_update_hal_tentang_stroke:String,
        @Field("id_hal_tentang_stroke") id_hal_tentang_stroke:String,
        @Field("halaman") halaman:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusHalamanTentangStroke(
        @Field("post_hapus_hal_tentang_stroke") post_hapus_hal_tentang_stroke:String,
        @Field("id_hal_tentang_stroke") id_hal_tentang_stroke: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addValueTentangStroke(
        @Field("post_tambah_val_tentang_stroke") post_tambah_val_tentang_stroke:String,
        @Field("id_hal_tentang_stroke") id_hal_tentang_stroke:String,
        @Field("judul") judul:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateValueTentangStroke(
        @Field("post_update_val_tentang_stroke") post_update_val_tentang_stroke:String,
        @Field("id_val_tentang_stroke") id_val_tentang_stroke:String,
        @Field("id_hal_tentang_stroke") id_hal_tentang_stroke:String,
        @Field("judul") judul:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusValueTentangStroke(
        @Field("post_hapus_val_tentang_stroke") post_hapus_val_tentang_stroke:String,
        @Field("id_val_tentang_stroke") id_val_tentang_stroke: String
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addHalamanGaleriHerbal(
        @Part("post_tambah_hal_galeri_herbal") post_tambah_hal_galeri_herbal: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateHalamanGaleriHerbal(
        @Part("post_update_hal_galeri_herbal") post_update_hal_galeri_herbal: RequestBody,
        @Part("id_hal_galeri_herbal") id_hal_galeri_herbal: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateHalamanGaleriHerbalNoHaveImage(
        @Field("post_update_hal_galeri_herbal_no_have_image") post_update_hal_galeri_herbal_no_have_image: String,
        @Field("id_hal_galeri_herbal") id_hal_galeri_herbal: String,
        @Field("judul") judul: String,
        @Field("deskripsi") deskripsi: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusHalamanGaleriHerbal(
        @Field("post_hapus_hal_galeri_herbal") post_hapus_hal_galeri_herbal:String,
        @Field("id_hal_galeri_herbal") id_hal_galeri_herbal: String
    ): ArrayList<ResponseModel>


    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addValueGaleriHerbal(
        @Part("post_tambah_val_galeri_herbal") post_tambah_val_galeri_herbal: RequestBody,
        @Part("id_hal_galeri_herbal") id_hal_galeri_herbal: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("tata_cara_pengolahan") tata_cara_pengolahan: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("youtube") youtube: RequestBody
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateValueGaleriHerbal(
        @Part("post_update_val_galeri_herbal") post_update_val_galeri_herbal: RequestBody,
        @Part("id_val_galeri_herbal") id_val_galeri_herbal: RequestBody,
        @Part("id_hal_galeri_herbal") id_hal_galeri_herbal: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("tata_cara_pengolahan") tata_cara_pengolahan: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("youtube") youtube: RequestBody
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateValueGaleriHerbalNoHaveImage(
        @Field("post_update_val_galeri_herbal_no_have_image") post_update_val_galeri_herbal_no_have_image: String,
        @Field("id_val_galeri_herbal") id_val_galeri_herbal: String,
        @Field("id_hal_galeri_herbal") id_hal_galeri_herbal: String,
        @Field("nama") nama: String,
        @Field("deskripsi") deskripsi: String,
        @Field("tata_cara_pengolahan") tata_cara_pengolahan: String,
        @Field("youtube") youtube: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusValueGaleriHerbal(
        @Field("post_hapus_val_galeri_herbal") post_hapus_val_galeri_herbal:String,
        @Field("id_val_galeri_herbal") id_val_galeri_herbal: String
    ): ArrayList<ResponseModel>


    // Admin Terapi
    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addTerapi(
        @Part("post_tambah_terapi") post_tambah_terapi: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("nama_terapi") nama_terapi: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateTerapi(
        @Part("post_update_terapi") post_update_terapi: RequestBody,
        @Part("id_terapi") id_terapi: RequestBody,
        @Part("kata_acak") kata_acak: RequestBody,
        @Part("nama_terapi") nama_terapi: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part gambar: MultipartBody.Part
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateTerapiNoHaveImage(
        @Field("post_update_terapi_no_have_image") post_update_terapi_no_have_image: String,
        @Field("id_terapi") id_terapi: String,
        @Field("nama_terapi") nama_terapi: String,
        @Field("deskripsi") deskripsi: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusTerapi(
        @Field("post_hapus_terapi") post_hapus_terapi:String,
        @Field("id_terapi") id_terapi: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun addMenuSehat(
        @Field("post_tambah_menu_sehat") post_tambah_menu_sehat:String,
        @Field("judul") judul:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postUpdateMenuSehat(
        @Field("post_update_menu_sehat") post_update_menu_sehat:String,
        @Field("id_menu_sehat") id_menu_sehat:String,
        @Field("judul") judul:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("edukasi-pengobatan-stroke/api/post.php")
    suspend fun postHapusMenuSehat(
        @Field("post_hapus_menu_sehat") post_hapus_menu_sehat:String,
        @Field("id_menu_sehat") id_menu_sehat: String
    ): ArrayList<ResponseModel>
}