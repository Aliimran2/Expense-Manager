package com.example.expensemanager.db

import com.example.expensemanager.models.Transactions
import com.example.expensemanager.utils.DataProvider
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
        val calendar = Calendar.getInstance()

        when(SELECTED_TAB){
            DAILY -> {
                val startDate = DataProvider.setStartTimeOfDay(date)
                val endDate = DataProvider.setEndTimeOfDay(date)

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
            MONTHLY -> {
                val startDate = DataProvider.setStartOfMonth(date)
                val endDate = DataProvider.setEndOfMonth(date)

                // Query income transactions for the month
                val incomeTransactions = realm.query<Transactions>(
                    "type == $0 AND date >= $1 AND date <= $2",
                    INCOME, startDate, endDate
                ).find()

                // Calculate total income for the month
                var totalIncome = 0.0
                for (transaction in incomeTransactions) {
                    totalIncome += transaction.amount
                }
                return totalIncome

            }
            else -> return 0.0
        }


    }


    fun getTotalExpense(date: Date): Double {

        val realm = getRealmInstance()
        val calendar = Calendar.getInstance()

        when(SELECTED_TAB){
            DAILY -> {
                val startDate = DataProvider.setStartTimeOfDay(date)
                val endDate = DataProvider.setEndTimeOfDay(date)

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
            MONTHLY -> {
                val startDate = DataProvider.setStartOfMonth(date)
                val endDate = DataProvider.setEndOfMonth(date)

                // Query income transactions for the month
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
            else -> return 0.0

        }

    }


    fun getAllTransactionsForDate(date: Date): List<Transactions> {
        val realm = getRealmInstance()
        when (SELECTED_TAB) {

            DAILY -> {
                val startDate = DataProvider.setStartTimeOfDay(date)
                val endDate = DataProvider.setEndTimeOfDay(date)
                return realm.query<Transactions>("date >= $0 AND date <= $1", startDate, endDate)
                    .find()
            }

            MONTHLY -> {
                val startDate = DataProvider.setStartOfMonth(date)
                val endDate = DataProvider.setEndOfMonth(date)
                return realm.query<Transactions>("date >= $0 AND date<=$1", startDate, endDate).find()
            }
            else -> return emptyList()
        }
    }

}