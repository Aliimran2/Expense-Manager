package com.example.expensemanager.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.expensemanager.adapters.TransactionAdapter
import com.example.expensemanager.databinding.FragmentStatsBinding
import com.example.expensemanager.db.ExpenseRepository
import com.example.expensemanager.db.ExpenseViewModel
import com.example.expensemanager.db.ExpenseViewModelFactory
import com.example.expensemanager.utils.DataProvider.DAILY
import com.example.expensemanager.utils.DataProvider.MONTHLY
import com.example.expensemanager.utils.DataProvider.SELECTED_TAB
import com.example.expensemanager.utils.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Calendar


class StatsFragment : Fragment() {
    private lateinit var calendar: Calendar
    private val expenseViewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(ExpenseRepository())
    }
    private val binding by lazy { FragmentStatsBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calendar = Calendar.getInstance()


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




        val pie = AnyChart.pie()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Apples", 6371664))
        data.add(ValueDataEntry("Pears", 789622))
        data.add(ValueDataEntry("Bananas", 7216301))
        data.add(ValueDataEntry("Grapes", 1486621))
        data.add(ValueDataEntry("Oranges", 1200000))

        pie.data(data)

        pie.title("Fruits imported in 2015 (in kg)")

        pie.labels().position("outside")

        pie.legend().title().enabled(true)
        pie.legend().title()
            .text("Retail channels")
            .padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)

            binding.anyChart.setChart(pie)
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

    private fun changeDate(step : Int){
        if (SELECTED_TAB == DAILY){
            calendar.add(Calendar.DATE, step)
        } else if (SELECTED_TAB == MONTHLY){
            calendar.add(Calendar.MONTH, step)
        }

        updateDate()
    }

}