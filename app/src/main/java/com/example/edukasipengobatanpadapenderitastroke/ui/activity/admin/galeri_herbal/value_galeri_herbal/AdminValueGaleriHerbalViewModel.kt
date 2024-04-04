package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.value_galeri_herbal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
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
class AdminValueGaleriHerbalViewModel @Inject constructor(
    private var api: ApiService
): ViewModel() {
    private val _valueGaleriHerbal = MutableLiveData<UIState<ArrayList<GaleriHerbalListModel>>>()
    private val _postTambahValueGaleriHerbal = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateValueGaleriHerbal = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusValueGaleriHerbal = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchGaleriHerbal(id_hal_galeri_herbal:String) {
        viewModelScope.launch {
            _valueGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getGaleriHerbalList("", id_hal_galeri_herbal)
                _valueGaleriHerbal.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _valueGaleriHerbal.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahValueGaleriHerbal(
        post: RequestBody, idHalGaleriHerbal: RequestBody, kataAcak: RequestBody, nama: RequestBody,
        deskripsi: RequestBody, tataCaraPengolahan: RequestBody, gambar: MultipartBody.Part, youtube: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahValueGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueGaleriHerbal = api.addValueGaleriHerbal(
                    post, idHalGaleriHerbal, kataAcak, nama, deskripsi, tataCaraPengolahan, gambar, youtube
                )
                _postTambahValueGaleriHerbal.postValue(UIState.Success(postTambahValueGaleriHerbal))
            } catch (ex: Exception){
                _postTambahValueGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateValueGaleriHerbal(
        post: RequestBody, idValGaleriHerbal: RequestBody, idHalGaleriHerbal: RequestBody, kataAcak: RequestBody, nama: RequestBody,
        deskripsi: RequestBody, tataCaraPengolahan: RequestBody, gambar: MultipartBody.Part, youtube: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateValueGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueGaleriHerbal = api.postUpdateValueGaleriHerbal(
                    post, idValGaleriHerbal, idHalGaleriHerbal, kataAcak, nama, deskripsi, tataCaraPengolahan, gambar, youtube
                )
                _postUpdateValueGaleriHerbal.postValue(UIState.Success(postTambahValueGaleriHerbal))
            } catch (ex: Exception){
                _postUpdateValueGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateValueGaleriHerbalNoHaveImage(
        post: String, idValGaleriHerbal: String, idHalGaleriHerbal: String, nama: String,
        deskripsi: String, tataCaraPengolahan: String, youtube: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateValueGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueGaleriHerbal = api.postUpdateValueGaleriHerbalNoHaveImage(
                    "", idValGaleriHerbal, idHalGaleriHerbal, nama, deskripsi, tataCaraPengolahan, youtube
                )
                _postUpdateValueGaleriHerbal.postValue(UIState.Success(postTambahValueGaleriHerbal))
            } catch (ex: Exception){
                _postUpdateValueGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusValueGaleriHerbal(idValGaleriHerbal: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusValueGaleriHerbal.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueGaleriHerbal = api.postHapusValueGaleriHerbal("", idValGaleriHerbal)
                _postHapusValueGaleriHerbal.postValue(UIState.Success(postTambahValueGaleriHerbal))
            } catch (ex: Exception){
                _postHapusValueGaleriHerbal.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getGaleriHerbal(): LiveData<UIState<ArrayList<GaleriHerbalListModel>>> = _valueGaleriHerbal
    fun getResponseTambahValueGaleriHerbal(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahValueGaleriHerbal
    fun getResponseUpdateValueGaleriHerbal(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateValueGaleriHerbal
    fun getResponseHapusValueGaleriHerbal(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusValueGaleriHerbal
}