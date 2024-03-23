package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _testimoni = MutableLiveData<UIState<ArrayList<TestimoniModel>>>()

    fun fetchTestimoni() {
        viewModelScope.launch {
            _testimoni.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataMateri = api.getTestimoni("")
                _testimoni.postValue(UIState.Success(dataMateri))
            } catch (ex: Exception) {
                _testimoni.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getTestimoni(): LiveData<UIState<ArrayList<TestimoniModel>>> = _testimoni
}