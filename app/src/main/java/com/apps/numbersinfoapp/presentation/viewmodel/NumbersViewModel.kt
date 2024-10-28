package com.apps.numbersinfoapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.numbersinfoapp.domain.repository.NumbersRepository
import com.apps.numbersinfoapp.data.retrofit.NetworkResult
import com.apps.numbersinfoapp.util.mvi.UnidirectionalViewModel
import com.apps.numbersinfoapp.util.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NumbersViewModel @Inject constructor(
    private val numbersRepository: NumbersRepository,
) : ViewModel(),
    UnidirectionalViewModel<NumbersContract.State, NumbersContract.Event, NumbersContract.Effect> by mvi(
        NumbersContract.State(),
    ) {
    override fun event(event: NumbersContract.Event) = when (event) {
        NumbersContract.Event.ClearData -> {
            clear()
        }

        NumbersContract.Event.GetRandomNumberInfo -> {
            getRandomInfo()
        }

        is NumbersContract.Event.SetCurrentNumber -> {
            setCurrentNumber(event.number)
        }

        is NumbersContract.Event.SetCurrentNumberInfo -> {
            setNumberInfo(event.info)
        }

        NumbersContract.Event.UpdateDataFromDB -> {
            updateFromDB()
        }

        is NumbersContract.Event.ValidateNumber -> {
            validateNumber()
        }

        is NumbersContract.Event.GetNumberInfo -> {
            getNumberInfo()
        }
    }


    private fun isDigit(value: String): Boolean {
        return value.all { it.isDigit() }
    }

    private fun validateNumber() {
        updateUiState { copy(isNumberCorrect = !(state.value.currentNumber.isEmpty() || !isDigit(state.value.currentNumber))) }
    }

    private fun setCurrentNumber(num: String) {
        updateUiState { copy(currentNumber = num) }
        validateNumber()
    }

    private fun setNumberInfo(info: String) {
        updateUiState { copy(currentNumberInfo = info) }
    }


    private fun clear() {
        updateUiState { copy(currentNumber = "", currentNumberInfo = "", isNumberCorrect = null) }
    }

    private fun getNumberInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            collectNetworkResult(numbersRepository.getNumberInfo(state.value.currentNumber))
        }
    }

    private fun getRandomInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            collectNetworkResult(numbersRepository.getRandomInfo())
        }
    }

    private suspend fun collectNetworkResult(flow: Flow<NetworkResult<String?>>) {

            try {
                flow.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            updateUiState { copy(isLoading = true) }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let {updateUiState {  copy(currentNumberInfo = it, isLoading = false) } }
                        }

                        is NetworkResult.Error -> {
                            updateUiState { copy(isLoading = false) }
                            viewModelScope.emitSideEffect(NumbersContract.Effect.ShowToast)
                        }

                        is NetworkResult.Initial -> {

                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to collect network result", e)
            }
    }

    private fun updateFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            numbersRepository.getSavedNumbers().collect { numbers ->
                updateUiState { copy(numbersFromDB = MutableStateFlow(numbers)) }
            }
        }
    }






}
