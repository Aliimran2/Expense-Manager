package com.example.expensemanager.views

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.example.expensemanager.adapters.TransactionAdapter
import com.example.expensemanager.databinding.ActivityMainBinding
import com.example.expensemanager.utils.DataProvider
import com.example.expensemanager.utils.Utils
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: Calendar
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()

        transactionAdapter = TransactionAdapter(this, DataProvider.transactionList)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transactions"

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment().show(supportFragmentManager, null)
        }

        binding.nextDate.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            updateDate()
        }



        binding.previousDate.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            updateDate()
        }

        binding.transactionRecyclerview.adapter = transactionAdapter
        binding.transactionRecyclerview.layoutManager = LinearLayoutManager(this)



        updateDate()



    }

    private fun updateDate() {
       val date = Utils.dateFormat(calendar.time)
        binding.dateText.text = date
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
}