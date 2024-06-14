package com.example.vpdmoneytest.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val name: String="",
    val accountNumber: String="",
    var balance: String="",
    val bankName: String="",
    val accountType: String="",
    val isExpanded: Boolean = false
): Parcelable


val users = listOf(
    User("Olawale Segun", "0099887766", "10000.0", "First Bank Plc", "Savings"),
    User("Johnson Williams", "2276543217", "200000.0", "Access Bank Plc", "Current"),
    User("Kehinde Adewale", "0074543200", "550000.0", "Opay", "Savings"),
    User("Seyi Peter", "0074549900", "1500000.0", "Unity Bank", "Savings"),
    User("Franklin Adams", "224544100", "50000.0", "Zenith Bank", "Current"),

    )