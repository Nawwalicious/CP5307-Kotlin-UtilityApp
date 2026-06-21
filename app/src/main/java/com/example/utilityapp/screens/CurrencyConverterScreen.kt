package com.example.utilityapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.utilityapp.currency.ALL_CURRENCIES
import com.example.utilityapp.currency.CurrencyViewModel
import com.example.utilityapp.currency.LOCKED_CURRENCIES
import com.example.utilityapp.currency.MAX_EXTRA

@Composable
fun CurrencyConverterScreen(onBack: () -> Unit) {
    val viewModel: CurrencyViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    var showPicker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .statusBarsPadding()
        ) {
            // Header
            Column(
                modifier = Modifier.padding(
                    start  = 24.dp,
                    top    = 24.dp,
                    bottom = 16.dp
                )
            ) {
                Text(
                    text     = "←",
                    color    = Color(0xFF99FFFF),
                    fontSize = 28.sp,
                    modifier = Modifier
                        .clickable { onBack() }
                        .padding(bottom = 8.dp)
                )
                Text(
                    text       = "currency",
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Light,
                    color      = Color.White
                )
                Text(
                    text       = "converter",
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Light,
                    color      = Color(0xFF99FFFF)
                )
            }

            when {
                !state.hasApiKey    -> ApiKeyEntry(onSave = { viewModel.saveApiKey(it) })
                state.isLoading     -> LoadingView()
                state.error != null -> ErrorView(
                    error   = state.error!!,
                    onClear = { viewModel.clearApiKey() }
                )
                else -> CurrencyList(
                    amounts         = state.amounts,
                    extraCurrencies = state.extraCurrencies,
                    canAddMore      = state.extraCurrencies.size < MAX_EXTRA,
                    onAmountChanged = { code, value ->
                        viewModel.onAmountChanged(code, value)
                    },
                    onShowPicker    = { showPicker = true },
                    onRemove        = { viewModel.removeCurrency(it) }
                )
            }
        }

        // Currency picker dialog
        if (showPicker) {
            CurrencyPickerDialog(
                existingCurrencies = LOCKED_CURRENCIES + state.extraCurrencies,
                onSelect           = {
                    viewModel.addCurrency(it)
                    showPicker = false
                },
                onDismiss          = { showPicker = false }
            )
        }
    }
}

@Composable
fun ApiKeyEntry(onSave: (String) -> Unit) {
    var input by remember { mutableStateOf("") }

    Column(
        modifier            = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text       = "enter api key",
            color      = Color.White,
            fontSize   = 18.sp,
            fontWeight = FontWeight.Light
        )
        Text(
            text     = "get a free key at exchangerate-api.com",
            color    = Color(0xFF99FFFF),
            fontSize = 13.sp
        )
        OutlinedTextField(
            value         = input,
            onValueChange = { input = it },
            placeholder   = {
                Text("paste your api key here", color = Color.Gray)
            },
            modifier = Modifier.fillMaxWidth(),
            colors   = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Color(0xFF0078D4),
                unfocusedBorderColor = Color(0xFF1F1F1F),
                focusedTextColor     = Color.White,
                unfocusedTextColor   = Color.White,
                cursorColor          = Color(0xFF99FFFF)
            )
        )
        Button(
            onClick  = { if (input.isNotBlank()) onSave(input.trim()) },
            colors   = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0078D4)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("save & connect", color = Color.White)
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF0078D4))
    }
}

@Composable
fun ErrorView(error: String, onClear: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text     = "something went wrong",
            color    = Color.White,
            fontSize = 18.sp
        )
        Text(text = error, color = Color(0xFF99FFFF), fontSize = 13.sp)
        Button(
            onClick = onClear,
            colors  = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0078D4)
            )
        ) {
            Text("reset api key", color = Color.White)
        }
    }
}

@Composable
fun CurrencyList(
    amounts         : Map<String, String>,
    extraCurrencies : List<String>,
    canAddMore      : Boolean,
    onAmountChanged : (String, String) -> Unit,
    onShowPicker    : () -> Unit,
    onRemove        : (String) -> Unit
) {
    val allCurrencies = LOCKED_CURRENCIES + extraCurrencies

    LazyColumn(
        modifier            = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(allCurrencies) { code ->
            CurrencyRow(
                code            = code,
                amount          = amounts[code] ?: "",
                isLocked        = code in LOCKED_CURRENCIES,
                onAmountChanged = { onAmountChanged(code, it) },
                onRemove        = { onRemove(code) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            if (canAddMore) {
                Button(
                    onClick  = onShowPicker,
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0078D4)
                    ),
                    shape    = RectangleShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "+ add currency",
                        color    = Color.White,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CurrencyRow(
    code            : String,
    amount          : String,
    isLocked        : Boolean,
    onAmountChanged : (String) -> Unit,
    onRemove        : () -> Unit
) {
    val info = ALL_CURRENCIES[code]

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F1F1F))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Remove button — only for extras
        if (!isLocked) {
            Text(
                text     = "×",
                color    = Color(0xFF99FFFF),
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable { onRemove() }
                    .padding(end = 12.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(32.dp))
        }

        // Code + name
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = code,
                color      = Color.White,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Light
            )
            if (info != null) {
                Text(
                    text     = info.name,
                    color    = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }

        // Amount input
        OutlinedTextField(
            value         = amount,
            onValueChange = { onAmountChanged(it) },
            modifier      = Modifier.weight(2f),
            textStyle     = TextStyle(
                color      = if (isLocked) Color(0xFF99FFFF) else Color.White,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Light
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            colors     = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Color(0xFF0078D4),
                unfocusedBorderColor = Color.Transparent,
                cursorColor          = Color(0xFF99FFFF)
            ),
            singleLine = true
        )
    }
}

@Composable
fun CurrencyPickerDialog(
    existingCurrencies : List<String>,
    onSelect           : (String) -> Unit,
    onDismiss          : () -> Unit
) {
    var search by remember { mutableStateOf("") }

    val available = ALL_CURRENCIES.keys
        .filter { it !in existingCurrencies }
        .filter {
            val info = ALL_CURRENCIES[it]
            it.contains(search, ignoreCase = true) ||
                    info?.name?.contains(search, ignoreCase = true) == true
        }
        .sorted()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1F1F1F))
                .padding(16.dp)
        ) {
            Text(
                text       = "add currency",
                color      = Color.White,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Light,
                modifier   = Modifier.padding(bottom = 12.dp)
            )

            // Search box
            OutlinedTextField(
                value         = search,
                onValueChange = { search = it },
                placeholder   = { Text("search...", color = Color.Gray) },
                modifier      = Modifier.fillMaxWidth(),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = Color(0xFF0078D4),
                    unfocusedBorderColor = Color(0xFF444444),
                    focusedTextColor     = Color.White,
                    unfocusedTextColor   = Color.White,
                    cursorColor          = Color(0xFF99FFFF)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Currency list
            LazyColumn(
                modifier            = Modifier.height(360.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(available) { code ->
                    val info = ALL_CURRENCIES[code]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(code) }
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text     = code,
                            color    = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text     = info?.name ?: "",
                            color    = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dismiss
            Button(
                onClick  = onDismiss,
                colors   = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0078D4)
                ),
                shape    = RectangleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("cancel", color = Color.White)
            }
        }
    }
}