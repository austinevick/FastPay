package com.example.vpdmoneytest.database

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {

    val getAllTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun addTransaction(transactionEntity: TransactionEntity) {
        transactionDao.addTransaction(transactionEntity)
    }

}