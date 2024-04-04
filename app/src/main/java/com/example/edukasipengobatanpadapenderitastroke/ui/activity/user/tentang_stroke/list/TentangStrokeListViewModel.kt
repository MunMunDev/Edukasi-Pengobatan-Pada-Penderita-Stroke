package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TentangStrokeListViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _tentangStrokeList = MutableLiveData<UIState<ArrayList<TentangStrokeListModel>>>()

    fun fetchTentangStrokeList() {
        viewModelScope.launch {
            _tentangStrokeList.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTentangStrokeList("")
                _tentangStrokeList.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _tentangStrokeList.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getTentangStrokeList(): LiveData<UIState<ArrayList<TentangStrokeListModel>>> = _tentangStrokeList
}