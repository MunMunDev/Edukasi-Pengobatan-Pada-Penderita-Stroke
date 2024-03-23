package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TerapiListViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _terapi = MutableLiveData<UIState<ArrayList<TerapiModel>>>()

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

    fun getTerapi(): LiveData<UIState<ArrayList<TerapiModel>>> = _terapi
}