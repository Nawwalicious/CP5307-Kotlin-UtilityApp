package com.example.utilityapp.currency

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CurrencyUiState(
    val hasApiKey: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val rates: Map<String, Double> = emptyMap(),
    val amounts: Map<String, String> = emptyMap(),
    val extraCurrencies: List<String> = emptyList()
)

val LOCKED_CURRENCIES = listOf("USD", "EUR", "GBP", "AUD", "JPY", "CAD")
const val MAX_EXTRA = 4

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CurrencyRepository(application)

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState

    init {
        loadApiKey()
    }

    private fun loadApiKey() {
        viewModelScope.launch {
            val key = repository.getApiKey()
            if (key != null) {
                _uiState.value = _uiState.value.copy(hasApiKey = true)
                fetchRates(key)
            }
        }
    }

    fun saveApiKey(key: String) {
        viewModelScope.launch {
            repository.saveApiKey(key)
            _uiState.value = _uiState.value.copy(hasApiKey = true)
            fetchRates(key)
        }
    }

    fun clearApiKey() {
        viewModelScope.launch {
            repository.clearApiKey()
            _uiState.value = CurrencyUiState()
        }
    }

    private fun fetchRates(apiKey: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val rates = repository.getRates(apiKey)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    rates     = rates,
                    amounts   = recalculate(
                        base   = "USD",
                        input  = "1",
                        rates  = rates,
                        extras = _uiState.value.extraCurrencies
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = e.message
                )
            }
        }
    }

    fun onAmountChanged(currencyCode: String, input: String) {
        val state = _uiState.value
        if (state.rates.isEmpty()) return
        _uiState.value = state.copy(
            amounts = recalculate(
                base   = currencyCode,
                input  = input,
                rates  = state.rates,
                extras = state.extraCurrencies
            )
        )
    }

    fun addCurrency(code: String) {
        val state = _uiState.value
        if (state.extraCurrencies.size >= MAX_EXTRA) return
        if (code in LOCKED_CURRENCIES) return
        if (code in state.extraCurrencies) return
        val newExtras = state.extraCurrencies + code
        _uiState.value = state.copy(
            extraCurrencies = newExtras,
            amounts = recalculate(
                base   = "USD",
                input  = state.amounts["USD"] ?: "1",
                rates  = state.rates,
                extras = newExtras
            )
        )
    }

    fun removeCurrency(code: String) {
        val state = _uiState.value
        if (code in LOCKED_CURRENCIES) return
        _uiState.value = state.copy(
            extraCurrencies = state.extraCurrencies.filter { it != code }
        )
    }

    private fun recalculate(
        base: String,
        input: String,
        rates: Map<String, Double>,
        extras: List<String>
    ): Map<String, String> {
        val amount   = input.toDoubleOrNull() ?: 0.0
        val baseRate = rates[base] ?: 1.0
        val allCodes = LOCKED_CURRENCIES + extras

        return allCodes.associate { code ->
            val rate   = rates[code] ?: 1.0
            val result = if (code == base) input
            else "%.2f".format((amount / baseRate) * rate)
            code to result
        }
    }
}