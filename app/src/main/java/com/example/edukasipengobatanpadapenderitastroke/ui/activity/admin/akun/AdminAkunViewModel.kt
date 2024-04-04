package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAkunViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel(){
    private var _semuaAkun = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    private var _postTambahAkun = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postUpdateAkun = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _postDeleteAkun = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchAkun(){
        viewModelScope.launch(Dispatchers.IO) {
            _semuaAkun.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchAkun = api.getAllUser("")
                _semuaAkun.postValue(UIState.Success(fetchAkun))
            } catch (ex: Exception){
                _semuaAkun.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahAkun(
        nama:String, nomorHp:String, username:String, password:String, sebagai:String
    ){
        viewModelScope.launch {
            _postTambahAkun.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahAkun = api.addUser("", nama, nomorHp, username, password, "user")
                _postTambahAkun.postValue(UIState.Success(postTambahAkun))
            } catch (ex: Exception){
                _postTambahAkun.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateAkun(
        idUser: String, nama:String, nomorHp:String, username:String, password:String, usernameLama:String
    ){
        viewModelScope.launch {
            _postUpdateAkun.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahAkun = api.postUpdateUser(
                    "", idUser, nama, nomorHp, username, password, usernameLama
                )
                _postUpdateAkun.postValue(UIState.Success(postTambahAkun))
            } catch (ex: Exception){
                _postUpdateAkun.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteAkun(idUser: String){
        viewModelScope.launch {
            _postDeleteAkun.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahAkun = api.postHapusUser(
                    "", idUser
                )
                _postDeleteAkun.postValue(UIState.Success(postTambahAkun))
            } catch (ex: Exception){
                _postDeleteAkun.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getAkun(): LiveData<UIState<ArrayList<UsersModel>>> = _semuaAkun
    fun getTambahAkun(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahAkun
    fun getUpdateAkun(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateAkun
    fun getDeleteAkun(): LiveData<UIState<ArrayList<ResponseModel>>> = _postDeleteAkun
}