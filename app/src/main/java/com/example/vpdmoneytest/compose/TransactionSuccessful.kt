package com.example.vpdmoneytest.compose

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.vpdmoneytest.R
import com.example.vpdmoneytest.models.User
import com.example.vpdmoneytest.utils.formatAmount

data class TransactionSuccessful(val user: User, val amount: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val haptic = LocalHapticFeedback.current


        LaunchedEffect(true) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }

        Surface {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                BackHandler {
                    navigator?.popUntilRoot()
                }
                Spacer(modifier = Modifier.height(150.dp))
                Image(
                    painter = painterResource(id = R.drawable.success),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .animateContentSize()
                )
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Successful",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Text(
                    text = "You sent ${formatAmount(amount.toDouble())} to ${user.name}",
                    fontSize = 12.sp, fontWeight = FontWeight.W700, color = Color.Gray
                )
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navigator?.popUntilRoot() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        disabledContainerColor = Color.Black.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text(text = "Go back to home")
                }


            }
        }
    }


}