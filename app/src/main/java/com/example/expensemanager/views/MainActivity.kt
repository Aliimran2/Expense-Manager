package com.example.expensemanager.views

import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.example.expensemanager.adapters.TransactionAdapter
import com.example.expensemanager.databinding.ActivityMainBinding
import com.example.expensemanager.models.Transactions
import com.example.expensemanager.utils.DataProvider
import com.example.expensemanager.utils.DataProvider.expense
import com.example.expensemanager.utils.DataProvider.income
import com.example.expensemanager.utils.Utils
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import java.util.Calendar
import java.util.Date


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendar: Calendar
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendar = Calendar.getInstance()



        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transactions"

        setupDatabase()

        realm.writeBlocking {
            val transaction = Transactions().apply {
                 type =  expense
                 category = "Investment"
                 account = "Bank"
                 note = "Notes"
                 amount  = -200.0
                 date = Date()  // Initialize with the current date
                 id = Date().time
            }
            copyToRealm(transaction)
        }

        val transactions = realm.query<Transactions>().find()
        transactionAdapter = TransactionAdapter(this, transactions)

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

    private fun setupDatabase() {
        val config = RealmConfiguration.Builder(schema = setOf(Transactions::class))
            .deleteRealmIfMigrationNeeded()
            .build()
        realm = Realm.open(config)




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