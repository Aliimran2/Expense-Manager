package com.example.expensemanager.utils

import com.example.expensemanager.R
import com.example.expensemanager.models.Accounts
import com.example.expensemanager.models.Category
import com.example.expensemanager.models.Transactions

object DataProvider {

    const val INCOME = "INCOME"
    const val EXPENSE = "EXPENSE"

    const val DAILY = 0
    const val MONTHLY = 1
    const val CALENDER = 2
    const val SUMMARY = 3
    const val NOTES = 4


    var SELECTED_TAB = 0

    val categoryList: MutableList<Category> = ArrayList()
    val accountList: MutableList<Accounts> = ArrayList()
    val transactionList: MutableList<Transactions> = ArrayList()

    private fun addCategory(catName: String, catIcon: Int, catColor: Int) {
        val cat = Category(catName, catIcon, catColor)
        categoryList.add(cat)
    }


//    private fun addTransaction(
//        type: String,
//        category: String,
//        account: String,
//        note: String,
//        amount: Double,
//        date: Date,
//        id: Long
//    ) {
//        val transactions = Transactions(type,category, account, note, amount, date, id)
//        transactionList.add(transactions)
//    }

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

//        addTransaction(income, "Business", "cash","Notes Here", 500.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(expense, "Investment", "EasyPaisa","Notes Here", 2000.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(income, "Rent", "cash","Notes Here", 1500.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(expense, "Loan", "cash","Notes Here", 2500.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(income, "Salary", "cash","Notes Here", 500.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(income, "Rent", "cash","Notes Here", 500.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(income, "Others", "cash","Notes Here", 500.0, Date(), SystemClock.elapsedRealtime())
//        addTransaction(expense, "Loan", "cash","Notes Here", 500.0, Date(), SystemClock.elapsedRealtime())
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
}