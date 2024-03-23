package com.example.edukasipengobatanpadapenderitastroke.data.database.api

import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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
        @Field("alamat") alamat:String,
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


}