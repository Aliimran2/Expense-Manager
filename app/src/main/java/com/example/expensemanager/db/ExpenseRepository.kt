package com.example.expensemanager.db

import com.example.expensemanager.models.Transactions
import com.example.expensemanager.utils.DataProvider.DAILY
import com.example.expensemanager.utils.DataProvider.EXPENSE
import com.example.expensemanager.utils.DataProvider.INCOME
import com.example.expensemanager.utils.DataProvider.MONTHLY
import com.example.expensemanager.utils.DataProvider.SELECTED_TAB
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class ExpenseRepository {

    private val config = RealmConfiguration.Builder(schema = setOf(Transactions::class))
        .deleteRealmIfMigrationNeeded()
        .build()


    private fun getRealmInstance(): Realm {
        return Realm.open(config)
    }

    suspend fun deleteTransaction(transaction: Transactions) {
        withContext(Dispatchers.IO) {
            val realm = getRealmInstance()
            realm.writeBlocking {
                val liveTransaction = findLatest(transaction)
                liveTransaction?.let { delete(it) }
            }
        }
    }

    suspend fun addTransaction(transaction: Transactions) {
        withContext(Dispatchers.IO) {
            val realm = getRealmInstance()
            realm.writeBlocking {
                copyToRealm(transaction)
            }
        }
    }

    fun getTotalIncome(date: Date): Double {

        val realm = getRealmInstance()
        //get the start and the end of the day
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.timeInMillis

        val incomeTransactions = realm.query<Transactions>(
            "type == $0 AND date >= $1 AND date <= $2",
            INCOME,
            startDate,
            endDate
        ).find()


        var totalIncome = 0.0
        for (transaction in incomeTransactions) {
            totalIncome += transaction.amount
        }
        return totalIncome

    }


    fun getTotalExpense(date: Date): Double {

        val realm = getRealmInstance()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.timeInMillis

        val expenseTransaction = realm.query<Transactions>(
            "type == $0 AND date >= $1 AND date <= $2",
            EXPENSE,
            startDate,
            endDate
        ).find()


        var totalExpense = 0.0
        for (transaction in expenseTransaction) {
            totalExpense += transaction.amount
        }
        return totalExpense

    }


    fun getAllTransactionsForDate(date: Date): List<Transactions> {
        val realm = getRealmInstance()
        val calendar = Calendar.getInstance()
        when (SELECTED_TAB) {

            DAILY -> {
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startDate = calendar.timeInMillis

                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                val endDate = calendar.timeInMillis

                return realm.query<Transactions>("date >= $0 AND date <= $1", startDate, endDate)
                    .find()
            }

            MONTHLY -> {
                calendar.time = date
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startDate = calendar.timeInMillis

                calendar.add(Calendar.MONTH, 1)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.add(Calendar.DATE, -1) // Move to last day of previous month
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                val endDate = calendar.timeInMillis

                return realm.query<Transactions>("date >= $0 AND date<=$1", startDate, endDate).find()
            }
            else -> return emptyList()


        }


    }

}