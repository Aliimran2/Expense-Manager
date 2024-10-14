package com.example.expensemanager.views.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.expensemanager.R
import com.example.expensemanager.databinding.ActivityMainBinding
import com.example.expensemanager.views.fragments.StatsFragment
import com.example.expensemanager.views.fragments.TransactionFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Transactions"


        binding.bottomNavigation.setOnItemSelectedListener {item ->

            when(item.itemId){
                R.id.transactions -> replaceFragment(TransactionFragment())
                R.id.stats -> replaceFragment(StatsFragment())

            }

            true
        }




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