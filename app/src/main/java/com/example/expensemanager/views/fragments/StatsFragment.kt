package com.example.expensemanager.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.expensemanager.databinding.FragmentStatsBinding
import com.example.expensemanager.db.ExpenseRepository
import com.example.expensemanager.db.ExpenseViewModel
import com.example.expensemanager.db.ExpenseViewModelFactory
import com.example.expensemanager.utils.DataProvider.DAILY
import com.example.expensemanager.utils.DataProvider.MONTHLY
import com.example.expensemanager.utils.DataProvider.SELECTED_TAB_STAT
import com.example.expensemanager.utils.Utils
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Calendar


class StatsFragment : Fragment() {
    private lateinit var calendar: Calendar
    private val expenseViewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(ExpenseRepository())
    }
    private val binding by lazy { FragmentStatsBinding.inflate(layoutInflater) }
    private var totalExpense = 0
    private var totalIncome = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calendar = Calendar.getInstance()
        setupPieChart()


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
                        SELECTED_TAB_STAT = 0
                        updateDate()
                    } else if (tab.text == "Monthly") {
                        SELECTED_TAB_STAT = 1
                        updateDate()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        expenseViewModel.totalExpense.observe(viewLifecycleOwner){ expense ->
            totalExpense = -1 * expense.toInt()
            updatePieChart()
        }

        expenseViewModel.totalIncome.observe(viewLifecycleOwner){income ->
            totalIncome = income.toInt()
            updatePieChart()
        }

        expenseViewModel.fetchAllTransactions(calendar.time)
        updateDate()

    }


    private fun updateDate() {
        if (SELECTED_TAB_STAT == DAILY) {
            val date = Utils.dateFormat(calendar.time)
            binding.dateText.text = date
        } else if (SELECTED_TAB_STAT == MONTHLY) {
            val date = Utils.dateFormatByMonth(calendar.time)
            binding.dateText.text = date
        }

        expenseViewModel.fetchAllTransactions(calendar.time)
        expenseViewModel.calculateTotal(calendar.time)

    }

    private fun changeDate(step: Int) {
        if (SELECTED_TAB_STAT == DAILY) {
            calendar.add(Calendar.DATE, step)
        } else if (SELECTED_TAB_STAT == MONTHLY) {
            calendar.add(Calendar.MONTH, step)
        }

        updateDate()
    }

    private fun setupPieChart() {
        val pieChart = binding.anyChart
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.centerText = "Budget Overview"
        pieChart.setCenterTextColor(Color.BLUE)
        pieChart.setCenterTextSize(18f) // Increase the font size
        pieChart.setEntryLabelColor(Color.WHITE) // Change the entry label color
        pieChart.animateY(1000, Easing.EaseInOutCubic) // Smooth animation
    }

    private fun updatePieChart() {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(totalIncome.toFloat(), "Income"))
        entries.add(PieEntry(totalExpense.toFloat(), "Expense"))

        val dataSet = PieDataSet(entries, "Budget")

        // Set custom colors with gradients
        dataSet.colors = listOf(
            Color.parseColor("#4CAF50"), // Income color
            Color.parseColor("#FF5733")  // Expense color
        )

        // Optionally add a shadow effect to slices
        dataSet.setDrawValues(true)
        dataSet.valueTextColor = Color.WHITE // Set value text color
        dataSet.valueTextSize = 14f // Set value text size


        val pieData = PieData(dataSet)

        binding.anyChart.data = pieData
        binding.anyChart.invalidate() // Refresh the chart
    }


}