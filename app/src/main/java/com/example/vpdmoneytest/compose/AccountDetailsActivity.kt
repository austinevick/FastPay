package com.example.vpdmoneytest.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.vpdmoneytest.components.CustomTextField
import com.example.vpdmoneytest.components.DecimalFormatter
import com.example.vpdmoneytest.components.DecimalInputVisualTransformation
import com.example.vpdmoneytest.components.TransferSummaryModal
import com.example.vpdmoneytest.models.User
import com.example.vpdmoneytest.utils.formatAmount
import com.example.vpdmoneytest.viewmodel.MainViewModel

class AccountDetailsActivity(
    private val sender: User,
    private val receiver: User) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val focusRequester = remember { FocusRequester() }
        val viewModel = hiltViewModel<MainViewModel>()

        val amount = remember { mutableStateOf("") }

        val bankName = remember { mutableStateOf(receiver.bankName) }
        val accountNumber = remember { mutableStateOf(receiver.accountNumber) }
        val receiverName = remember { mutableStateOf(receiver.name) }

        // To display error message for individual text field
        val amountErrorMessage = remember { mutableStateOf("") }

        // To determine if the text field is in error state
        val isAmountError = remember { mutableStateOf(false) }

        val isVisible = remember { mutableStateOf(false) }

        val textStyle = TextStyle(
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )
        LaunchedEffect(true) {
            focusRequester.requestFocus()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = { Text("Transfer") })
            }, contentWindowInsets =
            WindowInsets(0, 0, 0, 8)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .imePadding()
            ) {
                item {

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = sender.name, fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${sender.bankName} • ${sender.accountNumber}",
                        fontSize = 13.sp, color = Color.Black.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "${sender.accountType} Account", fontSize = 13.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Text(
                        text = formatAmount(sender.balance.toDouble()),
                        fontWeight = FontWeight.Bold,
                        color = if (amount.value.isEmpty()) Color.Black else
                        if (sender.balance.toDouble() < amount.value.toDouble())
                            Color.Red else Color.Black,
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Enter amount to transfer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CustomTextField(
                        value = amount.value,
                        onValueChange = { value ->
                            amount.value = DecimalFormatter().cleanup(value)
                            isAmountError.value = value.isEmpty()
                            if (value.isNotEmpty()) {
                                if (sender.balance.toDouble() < amount.value.toDouble()) {
                                    isAmountError.value = true
                                    amountErrorMessage.value = "Insufficient funds."
                                } else {
                                    isAmountError.value = false
                                    amountErrorMessage.value = ""
                                }
                            }
                        },
                        modifier = Modifier.focusRequester(focusRequester),
                        placeholderStyle = textStyle,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        isError = isAmountError.value,
                        keyboardType = KeyboardType.Decimal,
                        visualTransformation = DecimalInputVisualTransformation(
                            DecimalFormatter()
                        )
                    )
                    ErrorText(amountErrorMessage)
                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Destination account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    CustomTextField(
                        value = bankName.value,
                        readOnly = true,
                        onValueChange = {
                            bankName.value = it
                        },
                     textColor = Color.Black.copy(alpha = 0.6f),
                        placeholder = "Bank Name"
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    CustomTextField(
                        value = accountNumber.value,
                        readOnly = true,
                        onValueChange = {
                            accountNumber.value = it
                        },
                        textColor = Color.Black.copy(alpha = 0.6f),
                        placeholder = "Account number",
                        keyboardType = KeyboardType.Number
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    CustomTextField(
                        value = receiverName.value,
                        readOnly = true,
                        onValueChange = {
                            receiverName.value = it
                        },
                        textColor = Color.Black.copy(alpha = 0.6f),
                        placeholder = "Receiver's name"
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            if (amount.value.isEmpty()) {
                                isAmountError.value = true
                                amountErrorMessage.value = "Amount is required"
                            }else if (sender.balance.toDouble() < amount.value.toDouble()) {
                                isAmountError.value = true
                                amountErrorMessage.value = "Insufficient funds"
                            }else{
                            isVisible.value = true
                            }

                        }) {
                        Text(text = "Proceed")
                    }

                }
            }

            if (isVisible.value) {
                TransferSummaryModal(
                    isVisible = isVisible,
                    amount = amount.value,
                   receiver= receiver,
                    sender = sender
                )
            }

        }

    }

    @Composable
    private fun ErrorText(message: MutableState<String>) {
        Text(
            text = message.value,
            color = Color.Red, fontSize = 10.sp,
            style = TextStyle(platformStyle =
            PlatformTextStyle(includeFontPadding = false))
        )
    }


}
