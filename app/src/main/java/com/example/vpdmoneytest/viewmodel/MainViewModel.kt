package com.example.vpdmoneytest.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.vpdmoneytest.compose.HomeActivity
import com.example.vpdmoneytest.database.TransactionEntity
import com.example.vpdmoneytest.database.TransactionRepository
import com.example.vpdmoneytest.models.User
import com.example.vpdmoneytest.models.users
import com.example.vpdmoneytest.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userState = MutableLiveData<List<User>>()
    val userState: Flow<List<User>> = _userState.asFlow()

    private val _transactionState = MutableStateFlow<UIState>(UIState.Loading)
    val transactionState: StateFlow<UIState> = _transactionState.asStateFlow()


    init {
        viewModelScope.launch {
            _userState.value = users.toList()
            getTransactions()
        }
    }

    private suspend fun getTransactions() {
        try {
            _transactionState.value = UIState.Loading
            val transactions = transactionRepository.getAllTransactions
            transactions.collect {
                _transactionState.value = UIState.Success(it)
                Log.d("Transactions", it.toString())
            }
        } catch (e: Exception) {
            _transactionState.value = UIState.Error(e.message.toString())
        }
    }

    suspend fun addTransaction(transaction: TransactionEntity) {
        try {
            _isLoading.value = true
            transactionRepository.addTransaction(transaction)
        } catch (e: Exception) {
            _isLoading.value = false
            Log.d("Error", e.message.toString())
        }
    }

    fun handleTransaction(sender: User, receiver: User, transferAmount: Double) {
        _userState.value?.forEach {
            if (it.accountNumber == sender.accountNumber) {
                it.balance = (it.balance.toDouble() - transferAmount).toString()
            }
            if (it.accountNumber == receiver.accountNumber) {
                it.balance = (it.balance.toDouble() + transferAmount).toString()
            }
        }
    }


    fun registerUser(
        email: String,
        password: String,
        context: Context,
        navigator: Navigator?
    ) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                navigator?.push(HomeActivity())
            }
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    showToast(
                        context,
                        "Account Created Successfully"
                    )
                } else {
                    showToast(
                        context,
                        task.exception?.localizedMessage
                    )
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                showToast(
                    context, it.localizedMessage
                )
            }
    }


    fun loginUser(
        email: String,
        password: String,
        context: Context,
        navigator: Navigator?
    ) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                navigator?.push(HomeActivity())
            }
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (!task.isSuccessful) {
                    showToast(
                        context,
                        task.exception?.localizedMessage
                    )
                }
            }
            .addOnFailureListener {
                _isLoading.value = false
                showToast(
                    context, it.localizedMessage
                )
            }
    }
}

sealed class UIState {
    data object Loading : UIState()
    data class Error(val message: String) : UIState()
    data class Success(val data: List<TransactionEntity>) : UIState()
}






