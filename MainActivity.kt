package com.example.wesplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wesplit.ui.theme.WeSplitTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeSplitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculatorApp()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorApp() {
    var checkAmount by remember { mutableStateOf("") }
    var numberOfPeople by remember { mutableStateOf(2) }
    var tipPercentage by remember { mutableStateOf(20) }
    val tipPercentages = listOf(0, 10, 15, 20, 25)
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    val totalPerPerson = calculateTotalPerPerson(
        checkAmount.toDoubleOrNull() ?: 0.0,
        numberOfPeople,
        tipPercentage
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WeSplit",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = checkAmount,
            onValueChange = { checkAmount = it },
            label = { Text("Enter the bill amount") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Number of people: $numberOfPeople",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = numberOfPeople.toFloat(),
            onValueChange = { numberOfPeople = it.toInt() },
            valueRange = 2f..100f,
            steps = 98
        )


        Spacer(modifier = Modifier.height(16.dp))

        Text("Tip percentage:")
        Row {
            tipPercentages.forEach { percentage ->
                RadioButton(
                    selected = tipPercentage == percentage,
                    onClick = { tipPercentage = percentage }
                )
                Text("$percentage%")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Each person pays: ${NumberFormat.getCurrencyInstance().format(totalPerPerson)}",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { focusManager.clearFocus() }) {
            Text("Done")
        }
    }
}

fun calculateTotalPerPerson(checkAmount: Double, peopleCount: Int, tipPercentage: Int): Double {
    val tipValue = checkAmount * tipPercentage / 100
    val grandTotal = checkAmount + tipValue
    return grandTotal / peopleCount
}
