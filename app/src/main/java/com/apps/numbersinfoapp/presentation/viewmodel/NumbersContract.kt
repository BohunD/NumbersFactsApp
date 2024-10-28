package com.apps.numbersinfoapp.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.apps.numbersinfoapp.domain.model.NumberModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface NumbersContract {

    @Immutable
    data class State(
        val currentNumber: String = "",
        val currentNumberInfo: String = "",
        val isNumberCorrect: Boolean? = null,
        val isLoading: Boolean = false,
        var numbersFromDB: StateFlow<List<NumberModel?>> = MutableStateFlow(emptyList())
    )

    sealed interface Event {
        data class SetCurrentNumber(val number: String): Event
        data object GetNumberInfo: Event
        data class SetCurrentNumberInfo(val info: String): Event
        data object GetRandomNumberInfo: Event
        data object ClearData: Event
        data object UpdateDataFromDB: Event
        data object ValidateNumber: Event
    }

    sealed interface Effect {
        data object ShowToast: Effect
    }
}