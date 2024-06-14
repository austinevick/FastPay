package com.example.vpdmoneytest.utils

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date


fun showToast(context: Context, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
fun dateFormatter(date: Date):String{
    return SimpleDateFormat("dd/MM/yy hh:mm a").format(date.time)
}
fun formatAmount(amount: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.currency = Currency.getInstance("NGN");
    format.maximumFractionDigits = 0
    return format.format(amount)
}