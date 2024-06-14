package com.example.vpdmoneytest.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.vpdmoneytest.R
import com.example.vpdmoneytest.components.CustomTextField
import com.example.vpdmoneytest.utils.showToast
import com.example.vpdmoneytest.viewmodel.MainViewModel

class RegisterActivity:Screen {
    @Composable
    override fun Content() {
val navigator = LocalNavigator.current

        val viewModel = hiltViewModel<MainViewModel>()
        val state = viewModel.isLoading.collectAsState()
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current

        val snackbarHostState = remember { SnackbarHostState() }

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        val isEmailEmpty = remember { mutableStateOf(false) }
        val isPasswordEmpty = remember { mutableStateOf(false) }


        fun handleLogin() {
            focusManager.clearFocus()
            if (email.value.isEmpty()) {
                isEmailEmpty.value = true
                showToast(context, "Please enter a valid email")
            } else if (password.value.isEmpty()) {
                isPasswordEmpty.value = true
                showToast(context, "Please enter a password")
            } else {
                viewModel.registerUser(
                    email.value, password.value,
                    context, navigator
                )
            }
        }

        Scaffold(
            snackbarHost =
            { SnackbarHost(snackbarHostState) },

            ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {

                        val modifier = Modifier.padding(horizontal = 16.dp)

                        Spacer(modifier = Modifier.height(30.dp))

                        Image(
                            painterResource(id = R.drawable.logo),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(50.dp))

                        Text(
                            text = "Sign up now to secure your financial future",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        CustomTextField(
                            value = email.value,
                            onValueChange = {
                                email.value = it
                                isEmailEmpty.value = it.isEmpty()
                            },
                            placeholder = "Email",
                            keyboardType = KeyboardType.Email,
                            isError = isEmailEmpty.value,
                            modifier = modifier
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomTextField(
                            value = password.value,
                            onValueChange = {
                                password.value = it
                                isPasswordEmpty.value = it.isEmpty()
                            },
                            placeholder = "Password",
                            isError = isPasswordEmpty.value,
                            visualTransformation = PasswordVisualTransformation(),
                            imeAction = ImeAction.Go,
                            keyboardActions = KeyboardActions(onGo = { handleLogin() }),
                            modifier = modifier
                        )
                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = { handleLogin() },
                            enabled = state.value.not(),
                            modifier = modifier
                                .height(45.dp)
                                .width(
                                    LocalConfiguration
                                        .current.screenWidthDp.dp
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                disabledContainerColor = Color.Black.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (state.value)
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            else Text(text = "Register")
                        }

                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Text(text = "Already have an account?")
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "Sign In", color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navigator?.pop()
                        })
                }

            }
        }
    }
}