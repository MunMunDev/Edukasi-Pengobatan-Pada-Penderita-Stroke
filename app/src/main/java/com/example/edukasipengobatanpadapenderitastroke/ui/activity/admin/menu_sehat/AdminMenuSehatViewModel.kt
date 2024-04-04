package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.menu_sehat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminMenuSehatViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _menuSehat = MutableLiveData<UIState<ArrayList<MenuSehatModel>>>()
    private val _postTambahMenuSehat = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMenuSehat = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusMenuSehat = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchMenuSehat() {
        viewModelScope.launch {
            _menuSehat.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getMenuSehat("")
                _menuSehat.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _menuSehat.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahMenuSehat(
        judul: String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahMenuSehat.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMenuSehat = api.addMenuSehat("", judul, deskripsi)
                _postTambahMenuSehat.postValue(UIState.Success(postTambahMenuSehat))
            } catch (ex: Exception){
                _postTambahMenuSehat.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateMenuSehat(
        id_menu_sehat: String, judul: String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMenuSehat.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMenuSehat = api.postUpdateMenuSehat(
                    "", id_menu_sehat, judul, deskripsi
                )
                _postUpdateMenuSehat.postValue(UIState.Success(postTambahMenuSehat))
            } catch (ex: Exception){
                _postUpdateMenuSehat.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusMenuSehat(id_menu_sehat: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusMenuSehat.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMenuSehat = api.postHapusMenuSehat("", id_menu_sehat)
                _postHapusMenuSehat.postValue(UIState.Success(postTambahMenuSehat))
            } catch (ex: Exception){
                _postHapusMenuSehat.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getMenuSehat(): LiveData<UIState<ArrayList<MenuSehatModel>>> = _menuSehat
    fun getResponseTambahMenuSehat(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahMenuSehat
    fun getResponseUpdateMenuSehat(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMenuSehat
    fun getResponseHapusMenuSehat(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusMenuSehat
}