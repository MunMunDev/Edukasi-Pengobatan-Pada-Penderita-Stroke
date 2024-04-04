package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.halaman_tentang_stroke

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
class AdminHalamanTentangStrokeViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _tentangStrokeList = MutableLiveData<UIState<ArrayList<TentangStrokeListModel>>>()
    private val _postTambahHalamanTentangStroke = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateHalamanTentangStroke = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusHalamanTentangStroke = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

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

    fun postTambahHalamanTentangStroke(
        halaman:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahHalamanTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanTentangStroke = api.addHalamanTentangStroke("", halaman)
                _postTambahHalamanTentangStroke.postValue(UIState.Success(postTambahHalamanTentangStroke))
            } catch (ex: Exception){
                _postTambahHalamanTentangStroke.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateHalamanTentangStroke(
        idHalamanTentangStroke:String, halaman:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateHalamanTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanTentangStroke = api.postUpdateHalamanTentangStroke("", idHalamanTentangStroke, halaman)
                _postUpdateHalamanTentangStroke.postValue(UIState.Success(postTambahHalamanTentangStroke))
            } catch (ex: Exception){
                _postUpdateHalamanTentangStroke.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusHalamanTentangStroke(id_hal_tentang_stroke: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusHalamanTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahHalamanTentangStroke = api.postHapusHalamanTentangStroke("", id_hal_tentang_stroke)
                _postHapusHalamanTentangStroke.postValue(UIState.Success(postTambahHalamanTentangStroke))
            } catch (ex: Exception){
                _postHapusHalamanTentangStroke.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTentangStrokeList(): LiveData<UIState<ArrayList<TentangStrokeListModel>>> = _tentangStrokeList
    fun getResponseTambahHalamanTentangStroke(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahHalamanTentangStroke
    fun getResponseUpdateHalamanTentangStroke(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateHalamanTentangStroke
    fun getResponseHapusHalamanTentangStroke(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusHalamanTentangStroke
}