package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AkunViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _responsePostUpdateUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun postUpdateUser(
        idUser:String, nama: String, nomorHp: String,
        username: String, password: String, usernameLama:String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.postUpdateUser("", idUser, nama, nomorHp, username, password, usernameLama)
                _responsePostUpdateUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getUpdateData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostUpdateUser
}