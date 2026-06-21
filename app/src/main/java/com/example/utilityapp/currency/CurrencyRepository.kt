package com.example.utilityapp.currency

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "currency_prefs")

class CurrencyRepository(private val context: Context) {

    private val api: ExchangeRateApi = Retrofit.Builder()
        .baseUrl("https://v6.exchangerate-api.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ExchangeRateApi::class.java)

    private val gson = Gson()

    companion object {
        val KEY_API_KEY    = stringPreferencesKey("api_key")
        val KEY_RATES      = stringPreferencesKey("cached_rates")
        val KEY_TIMESTAMP  = stringPreferencesKey("cache_timestamp")
        val CACHE_DURATION = 60 * 60 * 1000L // 1 hour in milliseconds
    }

    suspend fun saveApiKey(apiKey: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_API_KEY] = apiKey
        }
    }

    suspend fun getApiKey(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_API_KEY]
    }

    suspend fun getRates(apiKey: String): Map<String, Double> {
        // Check cache first
        val prefs       = context.dataStore.data.first()
        val cachedRates = prefs[KEY_RATES]
        val timestamp   = prefs[KEY_TIMESTAMP]?.toLongOrNull() ?: 0L

        val cacheValid  = cachedRates != null &&
                System.currentTimeMillis() - timestamp < CACHE_DURATION

        if (cacheValid && cachedRates != null) {
            val type = object : com.google.gson.reflect.TypeToken<Map<String, Double>>() {}.type
            return gson.fromJson(cachedRates, type)
        }

        // Cache expired or empty — fetch fresh
        val response = api.getRates(apiKey)

        if (response.result == "success") {
            // Save to cache
            context.dataStore.edit { prefs ->
                prefs[KEY_RATES]     = gson.toJson(response.conversion_rates)
                prefs[KEY_TIMESTAMP] = System.currentTimeMillis().toString()
            }
            return response.conversion_rates
        }

        throw Exception("API error — check your API key")
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_API_KEY)
        }
    }
}