package com.example.expensemanager.db

import com.example.expensemanager.models.Transactions
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenseRepository {

    private val config = RealmConfiguration.create(schema = setOf(Transactions::class))

    private fun getRealmInstance(): Realm {
        return Realm.open(config)
    }

    suspend fun addTransaction(transaction: Transactions) {
        withContext(Dispatchers.IO) {
            val realm = getRealmInstance()
            realm.writeBlocking {
                copyToRealm(transaction)
            }
        }
    }

    fun getAllTransactions(): List<Transactions> {
        val realm = getRealmInstance()
        return realm.query<Transactions>().find()
    }

}