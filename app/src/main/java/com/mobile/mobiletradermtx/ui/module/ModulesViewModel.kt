package com.mobile.mobiletradermtx.ui.module

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.mobiletradermtx.dto.ModulesResponse
import com.mobile.mobiletradermtx.ui.module.repository.ModulesRepo
import com.mobile.mobiletradermtx.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ModulesViewModel @ViewModelInject constructor(private val repo: ModulesRepo): ViewModel() {

    private val _userModulesResponseState = MutableStateFlow<NetworkResult<ModulesResponse>>(NetworkResult.Empty)
    val userModulesResponseState get() = _userModulesResponseState

    fun fetchAllSalesEntries(employee_id: Int) = viewModelScope.launch {
        _userModulesResponseState.value = NetworkResult.Loading
        try {
            val data = repo.userModules(employee_id)
            _userModulesResponseState.value = NetworkResult.Success(data)
        } catch (e: Throwable) {
            _userModulesResponseState.value = NetworkResult.Error(e)
        }
    }
}