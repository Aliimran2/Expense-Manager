package com.example.expensemanager.views

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.R
import com.example.expensemanager.adapters.TransactionAdapter
import com.example.expensemanager.databinding.ActivityMainBinding
import com.example.expensemanager.db.ExpenseRepository
import com.example.expensemanager.db.ExpenseViewModel
import com.example.expensemanager.db.ExpenseViewModelFactory
import com.example.expensemanager.utils.DataProvider.DAILY
import com.example.expensemanager.utils.DataProvider.MONTHLY
import com.example.expensemanager.utils.DataProvider.SELECTED_TAB
import com.example.expensemanager.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: Calendar
    private lateinit var transactionAdapter: TransactionAdapter


    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(ExpenseRepository())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transactions"

        expenseViewModel.transactions.observe(this) { transactions ->
            transactionAdapter = TransactionAdapter(this, transactions.toMutableList()){
                expenseViewModel.deleteTransaction(it, calendar.time)
                updateDate()
            }
            binding.transactionRecyclerview.adapter = transactionAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment().show(supportFragmentManager, null)
        }

        binding.nextDate.setOnClickListener {
            if (SELECTED_TAB == DAILY){
                calendar.add(Calendar.DATE, 1)
            } else if (SELECTED_TAB == MONTHLY){
                calendar.add(Calendar.MONTH, 1)
            }



            expenseViewModel.fetchAllTransactions(calendar.time)
            updateDate()
        }

        binding.previousDate.setOnClickListener {
            if (SELECTED_TAB == DAILY){
                calendar.add(Calendar.DATE, -1)
            } else if (SELECTED_TAB == MONTHLY){
                calendar.add(Calendar.MONTH, -1)
            }
            expenseViewModel.fetchAllTransactions(calendar.time)
            updateDate()
        }


        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (tab.text == "Daily"){
                        SELECTED_TAB = 0
                        updateDate()
                    } else  if (tab.text == "Monthly"){
                        SELECTED_TAB = 1
                        updateDate()
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })



        expenseViewModel.totalIncome.observe(this){total ->
            binding.incomeAmount.text = total.toString()
            updateDate()
        }

        expenseViewModel.totalExpense.observe(this){total ->
            binding.expenseAmount.text = total.toString()
            updateDate()
        }

        expenseViewModel.totalBalance.observe(this){
            binding.balanceAmount.text = it.toString()
            updateDate()
        }


        expenseViewModel.fetchAllTransactions(calendar.time)
        updateDate()

    }

    fun getTransactions(){
        updateDate()
    }

    private fun updateDate() {
        if (SELECTED_TAB == DAILY){
            val date = Utils.dateFormat(calendar.time)
            binding.dateText.text = date
        } else if (SELECTED_TAB == MONTHLY) {
            val date = Utils.dateFormatByMonth(calendar.time)
            binding.dateText.text = date
        }



        expenseViewModel.fetchAllTransactions(calendar.time)
        expenseViewModel.calculateTotal(calendar.time)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
}