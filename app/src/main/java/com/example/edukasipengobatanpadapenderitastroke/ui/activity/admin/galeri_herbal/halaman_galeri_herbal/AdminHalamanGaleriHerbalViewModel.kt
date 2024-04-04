package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.halaman_galeri_herbal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminHalamanGaleriHerbalViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _galeriHerbal = MutableLiveData<UIState<ArrayList<GaleriHerbalMainModel>>>()
    private val _postTambahHalamanGaleriHerbal = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateHalamanGaleriHerbal = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusHalamanGaleriHerbal = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchGaleriHerbal() {
        viewModelScope.launch {
            _galeriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getGaleriHerbalMain("")
                _galeriHerbal.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _galeriHerbal.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahHalamanGaleriHerbal(
        post: RequestBody, kataAcak: RequestBody, judul: RequestBody, deskripsi: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahHalamanGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanGaleriHerbal = api.addHalamanGaleriHerbal(post, kataAcak, judul, deskripsi, gambar)
                _postTambahHalamanGaleriHerbal.postValue(UIState.Success(postTambahHalamanGaleriHerbal))
            } catch (ex: Exception){
                _postTambahHalamanGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateHalamanGaleriHerbal(
        post: RequestBody, id_hal_galeri_herbal: RequestBody, kataAcak: RequestBody, judul: RequestBody, deskripsi: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateHalamanGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanGaleriHerbal = api.postUpdateHalamanGaleriHerbal(
                    post, id_hal_galeri_herbal, kataAcak, judul, deskripsi, gambar
                )
                _postUpdateHalamanGaleriHerbal.postValue(UIState.Success(postTambahHalamanGaleriHerbal))
            } catch (ex: Exception){
                _postUpdateHalamanGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateHalamanGaleriHerbalNoHaveImage(
        post: String, id_hal_galeri_herbal: String, judul: String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateHalamanGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanGaleriHerbal = api.postUpdateHalamanGaleriHerbalNoHaveImage(
                    post, id_hal_galeri_herbal, judul, deskripsi
                )
                _postUpdateHalamanGaleriHerbal.postValue(UIState.Success(postTambahHalamanGaleriHerbal))
            } catch (ex: Exception){
                _postUpdateHalamanGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusHalamanGaleriHerbal(id_hal_galeri_herbal: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusHalamanGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanGaleriHerbal = api.postHapusHalamanGaleriHerbal("", id_hal_galeri_herbal)
                _postHapusHalamanGaleriHerbal.postValue(UIState.Success(postTambahHalamanGaleriHerbal))
            } catch (ex: Exception){
                _postHapusHalamanGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getGaleriHerbal(): LiveData<UIState<ArrayList<GaleriHerbalMainModel>>> = _galeriHerbal
    fun getResponseTambahHalamanGaleriHerbal(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahHalamanGaleriHerbal
    fun getResponseUpdateHalamanGaleriHerbal(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateHalamanGaleriHerbal
    fun getResponseHapusHalamanGaleriHerbal(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusHalamanGaleriHerbal
}