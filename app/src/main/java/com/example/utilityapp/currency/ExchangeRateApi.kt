package com.example.utilityapp.currency

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/{apiKey}/latest/USD")
    suspend fun getRates(
        @Path("apiKey") apiKey: String
    ): ExchangeRatesResponse
//    API ENDPOINT: https://v6.exchangerate-api.com/v6/YOUR_KEY/latest/USD
}