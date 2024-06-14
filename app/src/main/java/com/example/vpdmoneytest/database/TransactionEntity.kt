package com.example.vpdmoneytest.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vpdmoneytest.utils.Constants.Companion.DATABASE_TABLE
import java.util.Date

@Entity(tableName = DATABASE_TABLE)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val receiver: String,
    val sender: String,
    val bankName: String,
    val amountSent: String,
    val date: Date
)