package com.example.vpdmoneytest.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vpdmoneytest.components.CircleAvatar
import com.example.vpdmoneytest.models.users
import com.example.vpdmoneytest.utils.dateFormatter
import com.example.vpdmoneytest.utils.formatAmount
import com.example.vpdmoneytest.viewmodel.MainViewModel
import com.example.vpdmoneytest.viewmodel.UIState

@Composable
fun TransactionHistory() {
    val viewModel = hiltViewModel<MainViewModel>()
    val state = viewModel.transactionState.collectAsState()


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(30.dp))

        when(state.value){
            is UIState.Loading -> CircularProgressIndicator()
            is UIState.Error ->{
                Text(text = (state.value as UIState.Error).message)
            }
            is UIState.Success ->{
                val transactions = (state.value as UIState.Success).data
               if(transactions.isNotEmpty()){
                   Text(text = "Transactions",fontSize = 22.sp,
                       fontWeight = FontWeight.SemiBold,
                       modifier = Modifier.align(Alignment.Start))
                   transactions.map {transaction->
                       OutlinedCard(
                           colors = CardDefaults.cardColors(containerColor = Color.White),
                           border = BorderStroke(0.7.dp, Color.Black.copy(alpha = 0.1f)),
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(vertical = 6.dp)) {
                           Row(
                               modifier = Modifier
                                   .padding(8.dp)
                                   .fillMaxWidth(),
                               verticalAlignment = Alignment.CenterVertically,
                               horizontalArrangement = Arrangement.SpaceBetween
                           ) {
                               CircleAvatar(transaction.sender.substring(0, 1))
                               Column {
                                   Text(text = "${transaction.sender} ",fontSize = 14.sp,
                                       fontWeight = FontWeight.W500,
                                       style = TextStyle(platformStyle =
                                       PlatformTextStyle(includeFontPadding = false))
                                   )
                                   Text(text = "to ${transaction.receiver}",
                                       fontSize = 11.sp,
                                       style = TextStyle(platformStyle =
                                       PlatformTextStyle(includeFontPadding = false))
                                       )
                               }
                               Spacer(modifier = Modifier.weight(1f))
                               Column(horizontalAlignment = Alignment.End) {
                                   Text(
                                       text = "-${formatAmount(transaction.amountSent.toDouble())}",
                                       fontSize = 15.sp,
                                       color = Color.Red,
                                       fontWeight = FontWeight.SemiBold
                                   )
                                   Text(text = dateFormatter(transaction.date),
                                       fontSize = 11.sp,
                                       style = TextStyle(platformStyle =
                                       PlatformTextStyle(includeFontPadding = false))
                                   )
                               }
                           }
                       }

                   }
               }
            }
        }
    }



}