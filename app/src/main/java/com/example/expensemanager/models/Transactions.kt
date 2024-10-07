package com.example.expensemanager.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.Date


 class Transactions : RealmObject {
     var type: String = ""
     var category: String = ""
     var account: String = ""
     var note: String = ""
     var amount: Double = 0.0
     @Ignore
     var date: Date = Date()  // Initialize with the current date
     @PrimaryKey
     var id: Long = 0L
 }
