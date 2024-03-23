package com.example.edukasipengobatanpadapenderitastroke.ui.activity.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _user = MutableLiveData<UIState<ArrayList<UsersModel>>>()

    fun fetchDataUser(username:String, password:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getUser("", username, password)
                _user.postValue(UIState.Success(data))

                Log.d("TAG", "fetchDataUser: berhasil $data ")
            } catch (ex: Exception){
                _user.postValue(UIState.Failure("Error bang! ${ex.message}"))
                Log.d("TAG", "fetchDataUser: ${ex.message}")
            }
        }
    }

    fun getDataUser(): LiveData<UIState<ArrayList<UsersModel>>> = _user
}