package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.terapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
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
class AdminTerapiViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _terapi = MutableLiveData<UIState<ArrayList<TerapiModel>>>()
    private val _postTambahTerapi = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateTerapi = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusTerapi = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchTerapi() {
        viewModelScope.launch {
            _terapi.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTerapi("")
                _terapi.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _terapi.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahTerapi(
        post: RequestBody, kataAcak: RequestBody, judul: RequestBody, deskripsi: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahTerapi.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTerapi = api.addTerapi(post, kataAcak, judul, deskripsi, gambar)
                _postTambahTerapi.postValue(UIState.Success(postTambahTerapi))
            } catch (ex: Exception){
                _postTambahTerapi.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateTerapi(
        post: RequestBody, id_hal_galeri_herbal: RequestBody, kataAcak: RequestBody, judul: RequestBody, deskripsi: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateTerapi.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTerapi = api.postUpdateTerapi(
                    post, id_hal_galeri_herbal, kataAcak, judul, deskripsi, gambar
                )
                _postUpdateTerapi.postValue(UIState.Success(postTambahTerapi))
            } catch (ex: Exception){
                _postUpdateTerapi.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateTerapiNoHaveImage(
        post: String, id_hal_galeri_herbal: String, judul: String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateTerapi.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTerapi = api.postUpdateTerapiNoHaveImage(
                    post, id_hal_galeri_herbal, judul, deskripsi
                )
                _postUpdateTerapi.postValue(UIState.Success(postTambahTerapi))
            } catch (ex: Exception){
                _postUpdateTerapi.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusTerapi(id_hal_galeri_herbal: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusTerapi.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTerapi = api.postHapusTerapi("", id_hal_galeri_herbal)
                _postHapusTerapi.postValue(UIState.Success(postTambahTerapi))
            } catch (ex: Exception){
                _postHapusTerapi.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTerapi(): LiveData<UIState<ArrayList<TerapiModel>>> = _terapi
    fun getResponseTambahTerapi(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahTerapi
    fun getResponseUpdateTerapi(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateTerapi
    fun getResponseHapusTerapi(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusTerapi
}