package com.example.expensemanager.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.expensemanager.R
import com.example.expensemanager.adapters.TransactionAdapter
import com.example.expensemanager.databinding.FragmentTransactionBinding
import com.example.expensemanager.db.ExpenseRepository
import com.example.expensemanager.db.ExpenseViewModel
import com.example.expensemanager.db.ExpenseViewModelFactory
import com.example.expensemanager.utils.DataProvider.DAILY
import com.example.expensemanager.utils.DataProvider.MONTHLY
import com.example.expensemanager.utils.DataProvider.SELECTED_TAB
import com.example.expensemanager.utils.DataProvider.SELECTED_TAB_STAT
import com.example.expensemanager.utils.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Calendar


class TransactionFragment : Fragment() {
    private lateinit var calendar: Calendar
    private lateinit var transactionAdapter: TransactionAdapter
    private val expenseViewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(ExpenseRepository())
    }

    private val binding by lazy {
        FragmentTransactionBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()

        expenseViewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter =
                TransactionAdapter(requireContext(), transactions.toMutableList()) {
                    expenseViewModel.deleteTransaction(it, calendar.time)
                    updateDate()
                }
            binding.transactionRecyclerview.adapter = transactionAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment().show(parentFragmentManager, null)
        }

        binding.nextDate.setOnClickListener {
            changeDate(1)
            expenseViewModel.fetchAllTransactions(calendar.time)

        }

        binding.previousDate.setOnClickListener {
            changeDate(-1)
            expenseViewModel.fetchAllTransactions(calendar.time)
        }




        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (tab.text == "Daily") {
                        SELECTED_TAB = 0
                        updateDate()
                    } else if (tab.text == "Monthly") {
                        SELECTED_TAB = 1
                        updateDate()
                    }
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })



        expenseViewModel.totalIncome.observe(viewLifecycleOwner) { total ->
            binding.incomeAmount.text = total.toString()
            updateDate()
        }

        expenseViewModel.totalExpense.observe(viewLifecycleOwner) { total ->
            binding.expenseAmount.text = total.toString()
            updateDate()
        }

        expenseViewModel.totalBalance.observe(viewLifecycleOwner) {
            binding.balanceAmount.text = it.toString()
            updateDate()
        }

        expenseViewModel.fetchAllTransactions(calendar.time)
        updateDate()

    }


    private fun updateDate() {
        if (SELECTED_TAB == DAILY) {
            val date = Utils.dateFormat(calendar.time)
            binding.dateText.text = date
        } else if (SELECTED_TAB == MONTHLY) {
            val date = Utils.dateFormatByMonth(calendar.time)
            binding.dateText.text = date
        }

        expenseViewModel.fetchAllTransactions(calendar.time)
        expenseViewModel.calculateTotal(calendar.time)

    }

    private fun changeDate(step: Int) {
        if (SELECTED_TAB_STAT == DAILY) {
            calendar.add(Calendar.DATE, step)
        } else if (SELECTED_TAB == MONTHLY) {
            calendar.add(Calendar.MONTH, step)
        }

        updateDate()
    }


}