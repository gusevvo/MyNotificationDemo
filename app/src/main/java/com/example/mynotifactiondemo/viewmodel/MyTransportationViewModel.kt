package com.example.mynotifactiondemo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynotifactiondemo.data.api.dto.MyTransportationResponseDto
import com.example.mynotifactiondemo.data.repository.MyTransportationsRepository
import com.example.mynotifactiondemo.viewmodel.model.ViewModelResult
import kotlinx.coroutines.launch

class MyTransportationViewModel @ViewModelInject constructor(
    private val myTransportationsRepository: MyTransportationsRepository
) : ViewModel() {

    private val _myTransportation = MutableLiveData<ViewModelResult<MyTransportationResponseDto>>()
    val myTransportation: LiveData<ViewModelResult<MyTransportationResponseDto>>
        get() = _myTransportation


    private val _verificationCodeRequested = MutableLiveData<ViewModelResult<Boolean>>()
    val verificationCodeRequested: LiveData<ViewModelResult<Boolean>>
        get() = _verificationCodeRequested

    private val _rejected = MutableLiveData<ViewModelResult<Boolean>>()
    val rejected: LiveData<ViewModelResult<Boolean>>
        get() = _rejected


    init {
        _myTransportation.value = ViewModelResult.loading()
    }

    fun getMyTransportation(id: String) {
        viewModelScope.launch {
            _myTransportation.postValue(ViewModelResult.loading())
            try {
                val dto = myTransportationsRepository.getMyTransportation(id)
                _myTransportation.postValue(ViewModelResult.success(dto))
            } catch (throwable: Throwable) {
                _myTransportation.postValue(ViewModelResult.failure(throwable))
            }
        }
    }

    fun requestVerificationCode(id: String, deviceToken: String) {
        viewModelScope.launch {
            _verificationCodeRequested.postValue(ViewModelResult.loading())
            try {
                myTransportationsRepository.requestVerificationCode(id, deviceToken)
                _verificationCodeRequested.postValue(ViewModelResult.success(true))
            } catch (throwable: Throwable) {
                _verificationCodeRequested.postValue(ViewModelResult.failure(throwable))
            }
        }
    }

    fun rejectMyTransportation(id: String) {
        viewModelScope.launch {
            _rejected.postValue(ViewModelResult.loading())
            try {
                myTransportationsRepository.rejectMyTransportation(id)
                _rejected.postValue(ViewModelResult.success(true))
            } catch (throwable: Throwable) {
                _rejected.postValue(ViewModelResult.failure(throwable))
            }
        }
    }

    fun acceptMyTransportation(id: String, code: String) {
        viewModelScope.launch {
            _myTransportation.postValue(ViewModelResult.loading())
            try {
                val dto = myTransportationsRepository.acceptMyTransportation(id, code)
                _myTransportation.postValue(ViewModelResult.success(dto))
            } catch (throwable: Throwable) {
                _myTransportation.postValue(ViewModelResult.failure(throwable))
            }
        }
    }
}