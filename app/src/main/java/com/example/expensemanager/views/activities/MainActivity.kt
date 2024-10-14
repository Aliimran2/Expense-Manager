package com.example.expensemanager.views.activities

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.example.expensemanager.views.fragments.AddTransactionFragment
import com.example.expensemanager.views.fragments.TransactionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transactions"

        replaceFragment(TransactionFragment())




    }

    private  fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
}