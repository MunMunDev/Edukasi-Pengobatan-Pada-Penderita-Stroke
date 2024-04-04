package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.testimoni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class TestimoniViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _testimoni = MutableLiveData<UIState<ArrayList<TestimoniModel>>>()
    private val _postTambahTestimoni = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateTestimoni = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusTestimoni = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    fun fetchTestimoni() {
        viewModelScope.launch {
            _testimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTestimoni("")
                _testimoni.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _testimoni.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahData(
        id_user:String, testimoni:String, bintang:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.addTestimoni("", id_user, testimoni, bintang)
                _postTambahTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postTambahTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postTambahData(
        post: RequestBody, kataAcak: RequestBody, id_user: RequestBody, testimoni: RequestBody,
        bintang: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.addTestimoni(post, kataAcak, id_user, testimoni, bintang, gambar)
                _postTambahTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postTambahTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehData(
        id_testimoni:String, id_user:String, testimoni:String, bintang:String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.postUpdateTestimoni("", id_testimoni, id_user, testimoni, bintang)
                _postUpdateTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postUpdateTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehData(
        post: RequestBody, kataAcak: RequestBody, id_testimoni: RequestBody, id_user: RequestBody, testimoni: RequestBody,
        bintang: RequestBody, gambar: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.postUpdateTestimoni(
                    post, kataAcak, id_testimoni, id_user, testimoni, bintang, gambar
                )
                _postUpdateTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postUpdateTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehDataNoHaveData(
        id_testimoni:String, id_user:String, testimoni:String, bintang:String,
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.postUpdateTestimoniNoHaveImage("", id_testimoni, id_user, testimoni, bintang)
                _postUpdateTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postUpdateTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusTestimoni(id_testimoni: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusTestimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahTestimoni = api.postHapusTestimoni("", id_testimoni)
                _postHapusTestimoni.postValue(UIState.Success(postTambahTestimoni))
            } catch (ex: Exception){
                _postHapusTestimoni.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTestimoni(): LiveData<UIState<ArrayList<TestimoniModel>>> = _testimoni
    fun getResponseTambahTestimoni(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahTestimoni
    fun getResponseUpdateTestimoni(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateTestimoni
    fun getResponseHapusTestimoni(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusTestimoni

}