package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TentangStrokeDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _tentangStrokeDetail = MutableLiveData<UIState<ArrayList<TentangStrokeDetailModel>>>()

    fun fetchTentangStrokeDetail(id_hal_tentang_stroke:String) {
        viewModelScope.launch {
            _tentangStrokeDetail.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTentangStrokeDetail("", id_hal_tentang_stroke)
                _tentangStrokeDetail.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _tentangStrokeDetail.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getTentangStrokeDetail(): LiveData<UIState<ArrayList<TentangStrokeDetailModel>>> = _tentangStrokeDetail
}