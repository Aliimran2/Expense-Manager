package com.example.expensemanager.models

import java.util.Date

data class Transactions(
    val type:String,
    val category: String,
    val account : String,
    val note: String,
    val amount : Double,
    val date: Date,
    val id : Long
)
