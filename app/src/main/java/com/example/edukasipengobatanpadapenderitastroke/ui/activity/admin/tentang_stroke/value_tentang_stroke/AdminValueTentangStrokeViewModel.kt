package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.value_tentang_stroke

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edukasipengobatanpadapenderitastroke.data.database.api.ApiService
import com.example.edukasipengobatanpadapenderitastroke.data.model.ResponseModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminValueTentangStrokeViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _valueTentangStroke = MutableLiveData<UIState<ArrayList<TentangStrokeDetailModel>>>()
    private val _postTambahValueTentangStroke = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateValueTentangStroke = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusValueTentangStroke = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchValueTentangStroke(id_val_tentang_stroke: String) {
        viewModelScope.launch {
            _valueTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getTentangStrokeDetail("", id_val_tentang_stroke)
                _valueTentangStroke.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _valueTentangStroke.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahValueTentangStroke(
        idHalaman:String, judul:String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahValueTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueTentangStroke = api.addValueTentangStroke("", idHalaman, judul, deskripsi)
                _postTambahValueTentangStroke.postValue(UIState.Success(postTambahValueTentangStroke))
            } catch (ex: Exception){
                _postTambahValueTentangStroke.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateValueTentangStroke(
        idValueTentangStroke:String, idHalaman:String, judul:String, deskripsi: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateValueTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueTentangStroke = api.postUpdateValueTentangStroke("", idValueTentangStroke, idHalaman, judul, deskripsi)
                _postUpdateValueTentangStroke.postValue(UIState.Success(postTambahValueTentangStroke))
            } catch (ex: Exception){
                _postUpdateValueTentangStroke.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusValueTentangStroke(id_val_tentang_stroke: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusValueTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahValueTentangStroke = api.postHapusValueTentangStroke("", id_val_tentang_stroke)
                _postHapusValueTentangStroke.postValue(UIState.Success(postTambahValueTentangStroke))
            } catch (ex: Exception){
                _postHapusValueTentangStroke.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTentangStrokeList(): LiveData<UIState<ArrayList<TentangStrokeDetailModel>>> = _valueTentangStroke
    fun getTambahValueTentangStroke(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahValueTentangStroke
    fun getUpdateValueTentangStroke(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateValueTentangStroke
    fun getHapusValueTentangStroke(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusValueTentangStroke
}