package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GaleriHerbalMainViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _galeriHerbalMain = MutableLiveData<UIState<ArrayList<GaleriHerbalMainModel>>>()

    fun fetchGaleriHerbal() {
        viewModelScope.launch {
            _galeriHerbalMain.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getGaleriHerbalMain("")
                _galeriHerbalMain.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _galeriHerbalMain.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getGaleriHerbal(): LiveData<UIState<ArrayList<GaleriHerbalMainModel>>> = _galeriHerbalMain
}