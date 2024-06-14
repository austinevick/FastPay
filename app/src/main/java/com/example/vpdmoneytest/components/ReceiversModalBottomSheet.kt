package com.example.vpdmoneytest.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.vpdmoneytest.compose.AccountDetailsActivity
import com.example.vpdmoneytest.models.User
import com.example.vpdmoneytest.models.users
import com.example.vpdmoneytest.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiversModalBottomSheet(
    showModal: MutableState<Boolean>,
    sender: User
) {
    val navigator = LocalNavigator.current
    val viewModel = hiltViewModel<MainViewModel>()
    val receiverState = viewModel.userState.collectAsState(initial = users)
val scope = rememberCoroutineScope()
    ModalBottomSheet(
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        onDismissRequest =
        { showModal.value = false }) {

        Column(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Beneficiary", fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {
                val receivers = receiverState.value.filter {
                        it.accountNumber != sender.accountNumber }
                items(receivers.size) {

                    OutlinedCard(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(0.7.dp, Color.Black.copy(alpha = 0.1f)),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillParentMaxWidth(),
                        onClick = {
                            navigator?.push(
                                AccountDetailsActivity(
                                    sender = sender,
                                    receiver = receivers[it]
                                )
                            )
                            scope.launch {
                            delay(1000)
                            showModal.value = false
                            }

                        }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircleAvatar(receivers[it].name.substring(0, 1))

                            Column {
                                Text(
                                    text = receivers[it].name,
                                    fontWeight = FontWeight.W700,
                                    style = TextStyle(
                                        platformStyle =
                                        PlatformTextStyle(includeFontPadding = false)
                                    ),
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${receivers[it].bankName} • ${receivers[it].accountNumber}",
                                    fontSize = 14.sp, color = Color.Black.copy(alpha = 0.6f),
                                    style = TextStyle(
                                        platformStyle =
                                        PlatformTextStyle(includeFontPadding = false)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))

                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "")

                        }
                    }
                }
            }
        }
    }

}