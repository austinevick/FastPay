package com.example.vpdmoneytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.vpdmoneytest.compose.HomeActivity
import com.example.vpdmoneytest.compose.LoginActivity
import com.example.vpdmoneytest.ui.theme.VPDMoneyTestTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VPDMoneyTestTheme {
                 val auth = FirebaseAuth.getInstance()
                if(auth.currentUser?.uid ?.isNotEmpty() == true){
                    Navigator(HomeActivity()){
                        SlideTransition(it)
                    }
                }else
              Navigator(LoginActivity()){
                  SlideTransition(it)
              }
            }
        }
    }
}
