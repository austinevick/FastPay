package com.example.vpdmoneytest.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.vpdmoneytest.components.CircleAvatar
import com.example.vpdmoneytest.components.ReceiversModalBottomSheet
import com.example.vpdmoneytest.models.User
import com.example.vpdmoneytest.models.users
import com.example.vpdmoneytest.utils.formatAmount
import com.example.vpdmoneytest.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
class HomeActivity : Screen {
    @Composable
    override fun Content() {

        val viewModel = hiltViewModel<MainViewModel>()
        val usersState = viewModel.userState.collectAsState(initial = users)
        val selectedSender = remember { mutableStateOf<User?>(null) }

        val showModal = rememberSaveable { mutableStateOf(false) }


        Scaffold(topBar = {
            TopAppBar(title = {
                Text(text = "Hi👋")
            })
        }) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                items(usersState.value.size) { i ->
                    val users = usersState.value[i]
                    val isExpanded = rememberSaveable { mutableStateOf(users.isExpanded) }



                    OutlinedCard(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(0.7.dp, Color.Black.copy(alpha = 0.1f)),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .animateContentSize()
                            .fillParentMaxWidth(),
                        onClick = {
                            isExpanded.value = !isExpanded.value
                        }) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircleAvatar(users.name.substring(0, 1))
                                Column {
                                    Text(
                                        text = users.name,
                                        fontWeight = FontWeight.W700,
                                        style = TextStyle(
                                            platformStyle =
                                            PlatformTextStyle(includeFontPadding = false)
                                        ),
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${users.bankName} • ${users.accountNumber}",
                                        fontSize = 14.sp, color = Color.Black.copy(alpha = 0.6f),
                                        style = TextStyle(
                                            platformStyle =
                                            PlatformTextStyle(includeFontPadding = false)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    when (isExpanded.value) {
                                        true -> Icons.Default.KeyboardArrowUp
                                        false -> Icons.Default.KeyboardArrowDown
                                    }, ""
                                )
                            }
                            if (isExpanded.value) Spacer(modifier = Modifier.height(16.dp))
                            if (isExpanded.value) Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = formatAmount(users.balance.toDouble()),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${users.accountType} account",
                                        fontSize = 13.sp,
                                        color = Color.Black.copy(alpha = 0.6f)
                                    )

                                }
                                Spacer(modifier = Modifier.weight(1f))
                                OutlinedButton(
                                    border = BorderStroke(
                                        1.dp, Color.Black
                                            .copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier.height(35.dp),
                                    onClick = {
                                        showModal.value = true
                                        selectedSender.value = users
                                    }) {
                                    Text(text = "Transfer funds")
                                }
                            }

                        }
                    }
                }
                item {
                    TransactionHistory()
                }
            }
            if (showModal.value) ReceiversModalBottomSheet(showModal, selectedSender.value!!)

        }
    }
}