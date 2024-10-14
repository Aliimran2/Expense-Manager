package com.example.expensemanager.utils

import com.example.expensemanager.R
import com.example.expensemanager.models.Accounts
import com.example.expensemanager.models.Category
import com.example.expensemanager.models.Transactions
import java.util.Calendar
import java.util.Date

object DataProvider {

    const val INCOME = "INCOME"
    const val EXPENSE = "EXPENSE"

    const val DAILY = 0
    const val MONTHLY = 1
    const val CALENDER = 2
    const val SUMMARY = 3
    const val NOTES = 4
    var SELECTED_TAB = 0
    var SELECTED_TAB_STAT = 0
    var SELECTED_TAB_CATEGORY = ""

    val categoryList: MutableList<Category> = ArrayList()
    val accountList: MutableList<Accounts> = ArrayList()
    val transactionList: MutableList<Transactions> = ArrayList()

    private fun addCategory(catName: String, catIcon: Int, catColor: Int) {
        val cat = Category(catName, catIcon, catColor)
        categoryList.add(cat)
    }

    private fun addAccount(amount: Double, accountName: String) {
        val account = Accounts(amount, accountName)
        accountList.add(account)
    }

    fun getCategoryDetails(catName: String): Category? {
        categoryList.forEach { cat ->
            if (cat.category == catName) {
                return cat
            }
        }
        return null
    }

    init {
        addCategory("Business", R.drawable.ic_business, R.color.cat1)
        addCategory("Salary", R.drawable.ic_salary, R.color.cat2)
        addCategory("Investment", R.drawable.ic_investment, R.color.cat3)
        addCategory("Loan", R.drawable.ic_loan, R.color.cat4)
        addCategory("Rent", R.drawable.ic_rent, R.color.cat5)
        addCategory("Others", R.drawable.ic_other, R.color.cat6)

        addAccount(0.0, "Bank")
        addAccount(0.0, "EasyPaisa")
        addAccount(0.0, "Cash")
        addAccount(0.0, "Others")

    }

    fun getAccountColor(accountName : String) : Int {

       return when(accountName){
            "Investment" -> R.color.cat1
            "Rent" -> R.color.cat6
            "Loan" -> R.color.cat4
            "Salary" -> R.color.cat3
            "Others" -> R.color.cat5
            else -> R.color.black
        }
    }

    fun setStartTimeOfDay(date: Date) : Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun setEndTimeOfDay(date: Date) : Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    fun setStartOfMonth(date: Date) : Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun setEndOfMonth(date: Date) : Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.DATE, -1) // Move to last day of previous month
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}