package com.example.utilityapp.currency

data class ExchangeRatesResponse(
    val result: String,
    val base_code: String,
    val conversion_rates: Map<String, Double>
)