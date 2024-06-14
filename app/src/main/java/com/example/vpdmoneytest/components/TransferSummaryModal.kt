package com.example.vpdmoneytest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.example.vpdmoneytest.compose.TransactionSuccessful
import com.example.vpdmoneytest.database.TransactionEntity
import com.example.vpdmoneytest.models.User
import com.example.vpdmoneytest.models.users
import com.example.vpdmoneytest.utils.formatAmount
import com.example.vpdmoneytest.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TransferSummaryModal(
    isVisible: MutableState<Boolean>,
    amount: String,
    receiver: User,
    sender: User
) {
    val navigator = LocalNavigator.current
    val viewModel = hiltViewModel<MainViewModel>()
    val state = viewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()


    ModalBottomSheet(
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        containerColor = Color.White,
        onDismissRequest = {
            isVisible.value = false
        }) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Transaction Summary", fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = formatAmount(amount.toDouble()),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            SummaryText("Receiver's Name", receiver.name)
            SummaryText("Account Number", receiver.accountNumber)
            SummaryText("Bank Name", receiver.bankName)

            Spacer(modifier = Modifier.height(50.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Black.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = !state.value,
                onClick = {
                    val transactionEntity = TransactionEntity(
                        bankName = receiver.bankName,
                        receiver = receiver.name,
                        amountSent = amount,
                        sender = sender.name,
                        date = Date()
                    )
                    scope.launch {
                        viewModel.addTransaction(transactionEntity)
                        viewModel.handleTransaction(sender, receiver, amount.toDouble())
                        delay(2000)
                        isVisible.value = false
                        navigator?.push(TransactionSuccessful(user = receiver,amount = amount))
                    }

                }) {
                if (state.value)
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                else Text(text = "SEND")
            }


        }
    }
}

@Composable
private fun SummaryText(leading: String, trailing: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = leading, fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = trailing, fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
