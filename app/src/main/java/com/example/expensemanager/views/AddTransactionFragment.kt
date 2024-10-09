package com.example.expensemanager.views

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.R
import com.example.expensemanager.adapters.AccountAdapter
import com.example.expensemanager.adapters.CategoryAdapter
import com.example.expensemanager.databinding.FragmentAddTransactionBinding
import com.example.expensemanager.databinding.ListDialogBinding
import com.example.expensemanager.db.ExpenseViewModel
import com.example.expensemanager.models.Transactions
import com.example.expensemanager.utils.DataProvider
import com.example.expensemanager.utils.DataProvider.expense
import com.example.expensemanager.utils.DataProvider.income
import com.example.expensemanager.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar


class AddTransactionFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private val expenseViewModel : ExpenseViewModel by activityViewModels()
    private lateinit var transactions: Transactions


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(layoutInflater, container, false)

        transactions = Transactions()

        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.background = AppCompatResources.getDrawable(requireContext(), R.drawable.income_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.green))
            binding.expenseBtn.background = AppCompatResources.getDrawable(requireContext(), R.drawable.default_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.black))
            transactions.type = income


        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.background = AppCompatResources.getDrawable(requireContext(), R.drawable.default_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.black))
            binding.expenseBtn.background = AppCompatResources.getDrawable(requireContext(), R.drawable.expense_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.orange))

            transactions.type = expense
        }

        binding.date.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext())
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                val calendar  = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

               val dateToShow = Utils.dateFormat(calendar.time)
                //important discovery
                val date = calendar.time
                transactions.date = date.time
                Log.d("DateCheck", "${calendar.time}")

                binding.date.setText(dateToShow)

            }

            datePicker.show()
        }

        binding.category.setOnClickListener {
            val dialogBinding = ListDialogBinding.inflate(inflater)
            val alertDialog = AlertDialog.Builder(requireContext()).create()
            alertDialog.setView(dialogBinding.root)

            val categoryList =DataProvider.categoryList
            val catAdapter = CategoryAdapter(requireContext(), categoryList) {cat ->
                binding.category.setText(cat.category)
                transactions.category = cat.category
                alertDialog.dismiss()
            }
            dialogBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(),3)
            dialogBinding.recyclerView.setHasFixedSize(true)
            dialogBinding.recyclerView.adapter = catAdapter

           alertDialog.show()

        }

        binding.account.setOnClickListener {
            val accountDialogBinding = ListDialogBinding.inflate(inflater)

            val accountAlertDialog = AlertDialog.Builder(requireContext()).create()
            accountAlertDialog.setView(accountDialogBinding.root)

            val accountList = DataProvider.accountList
            accountDialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            accountDialogBinding.recyclerView.adapter = AccountAdapter(requireContext(), accountList){account ->
                binding.account.setText(account.accountName)
                transactions.account = account.accountName
                accountAlertDialog.dismiss()
            }

            accountAlertDialog.show()

        }

        binding.saveBtn.setOnClickListener {
            val id : Long = System.currentTimeMillis()
            val amount = binding.amount.text.toString().toDouble()
            val notes = binding.note.text.toString()
            transactions.id = id
            transactions.note = notes
            transactions.amount = amount
            expenseViewModel.addTransactions(transactions)

            dismiss()
        }

        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}