package com.example.expensemanager.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.models.Transactions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {


    private val _transactions = MutableLiveData<List<Transactions>>()
    val transactions: LiveData<List<Transactions>> get() = _transactions

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome:LiveData<Double> get() = _totalIncome

    private val _totalExpense = MutableLiveData<Double>()
    val totalExpense:LiveData<Double> get() = _totalExpense


    private val _totalBalance = MutableLiveData<Double>()
    val totalBalance:LiveData<Double> get() = _totalBalance


    fun deleteTransaction(transaction: Transactions, date: Date){
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
            fetchAllTransactions(date)
        }
    }



    fun calculateTotal (date: Date){
        viewModelScope.launch(Dispatchers.IO) {
            val income = repository.getTotalIncome(date)
            val expense = repository.getTotalExpense(date)
            _totalExpense.postValue(expense)
            _totalIncome.postValue(income)

            val balance = income + expense
            _totalBalance.postValue(balance)
        }
    }

    fun addTransactions(transaction:Transactions, date: Date) {
        viewModelScope.launch {
            repository.addTransaction(transaction)

            fetchAllTransactions(date)
        }
    }


    fun fetchAllTransactions(date: Date) {

        val transList = repository.getAllTransactionsForDate(date)
        _transactions.postValue(transList)
    }

}

class ExpenseViewModelFactory(private val repository: ExpenseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}