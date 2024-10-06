package com.example.expensemanager.utils

import com.example.expensemanager.R
import com.example.expensemanager.models.Accounts
import com.example.expensemanager.models.Category

object DataProvider {

    val categoryList : MutableList<Category> = ArrayList()
    val accountList : MutableList<Accounts> = ArrayList()

    private fun addCategory(catName: String, catIcon: Int, catColor:Int) {
        val cat = Category(catName, catIcon, catColor)
        categoryList.add(cat)
    }

    private fun addAccount( amount : Double, accountName : String) {
        val account = Accounts(amount, accountName)
        accountList.add(account)
    }

    init {
        addCategory("Business", R.drawable.ic_business, R.color.cat1)
        addCategory("Salary", R.drawable.ic_salary, R.color.cat2)
        addCategory("Investment", R.drawable.ic_investment, R.color.cat3)
        addCategory("Loan",R.drawable.ic_loan, R.color.cat4)
        addCategory("Rent", R.drawable.ic_rent, R.color.cat5)
        addCategory("Others", R.drawable.ic_other, R.color.cat6)

        addAccount(0.0, "Bank")
        addAccount(0.0, "EasyPaisa")
        addAccount(0.0, "Cash")
        addAccount(0.0, "Others")
    }
}