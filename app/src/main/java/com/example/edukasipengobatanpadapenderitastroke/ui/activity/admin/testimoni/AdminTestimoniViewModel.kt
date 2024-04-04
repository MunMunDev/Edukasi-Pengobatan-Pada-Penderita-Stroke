package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.testimoni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminTestimoniViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _testimoni = MutableLiveData<UIState<ArrayList<TestimoniModel>>>()
    fun fetchTestimoni() {
        viewModelScope.launch {
            _testimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTestimoni("")
                _testimoni.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _testimoni.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getTestimoni(): LiveData<UIState<ArrayList<TestimoniModel>>> = _testimoni
}
