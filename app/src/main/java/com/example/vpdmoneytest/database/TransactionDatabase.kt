package com.example.vpdmoneytest.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vpdmoneytest.utils.DateConverter

@TypeConverters(value = [DateConverter::class])
@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
 abstract class TransactionDatabase:RoomDatabase() {

  abstract fun transactionDao():TransactionDao
}
