package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.menu_sehat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuSehatViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _menuSehat = MutableLiveData<UIState<ArrayList<MenuSehatModel>>>()

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

    fun getMenuSehat(): LiveData<UIState<ArrayList<MenuSehatModel>>> = _menuSehat
}