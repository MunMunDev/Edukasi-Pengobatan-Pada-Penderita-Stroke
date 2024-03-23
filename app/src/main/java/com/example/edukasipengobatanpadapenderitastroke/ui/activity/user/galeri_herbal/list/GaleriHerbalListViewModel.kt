package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GaleriHerbalListViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _galeriHerbalList = MutableLiveData<UIState<ArrayList<GaleriHerbalListModel>>>()

    fun fetchGaleriHerbalList(id_hal_galeri_herbal: String) {
        viewModelScope.launch {
            _galeriHerbalList.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getGaleriHerbalList("", id_hal_galeri_herbal)
                _galeriHerbalList.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _galeriHerbalList.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getGaleriHerbalList(): LiveData<UIState<ArrayList<GaleriHerbalListModel>>> = _galeriHerbalList
}