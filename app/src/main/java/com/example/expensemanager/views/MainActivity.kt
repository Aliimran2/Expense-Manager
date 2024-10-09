package com.example.expensemanager.views

import android.os.Bundle
import android.util.Log
import android.view.Menu
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
import com.example.expensemanager.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            transactionAdapter = TransactionAdapter(this, transactions)
            binding.transactionRecyclerview.adapter = transactionAdapter
        }




        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment().show(supportFragmentManager, null)
        }

        binding.nextDate.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            expenseViewModel.fetchAllTransactions(calendar.time)
            updateDate()
        }



        binding.previousDate.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            expenseViewModel.fetchAllTransactions(calendar.time)
            updateDate()
        }

        expenseViewModel.totalIncome.observe(this){total ->
            binding.incomeAmount.text = total.toString()
        }

        expenseViewModel.totalExpense.observe(this){total ->
            binding.expenseAmount.text = total.toString()
        }

        expenseViewModel.totalBalance.observe(this){
            binding.balanceAmount.text = it.toString()
        }

        binding.transactionRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("FAB Scroll", "dy: $dy") // Log the scroll delta

                if (dy > 0) {
                    // Hide FAB when scrolling down
                    binding.floatingActionButton.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                        override fun onHidden(fab: FloatingActionButton?) {
                            super.onHidden(fab)
                            Log.d("FAB Status", "FAB Hidden") // Log when FAB is hidden
                        }
                    })
                } else if (dy < 0) {
                    // Show FAB when scrolling up
                    binding.floatingActionButton.show(object : FloatingActionButton.OnVisibilityChangedListener() {
                        override fun onShown(fab: FloatingActionButton?) {
                            super.onShown(fab)
                            Log.d("FAB Status", "FAB Shown") // Log when FAB is shown
                        }
                    })
                }
            }
        })


        expenseViewModel.fetchAllTransactions(calendar.time)
        updateDate()

    }

    fun getTransactions(){
        updateDate()
    }

    private fun updateDate() {
        val date = Utils.dateFormat(calendar.time)
        expenseViewModel.fetchAllTransactions(calendar.time)
//        expenseViewModel.calculateTotalIncome(calendar.time)
//        expenseViewModel.calculateTotalExpense(calendar.time)
        expenseViewModel.calculateTotal(calendar.time)
        binding.dateText.text = date
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
}