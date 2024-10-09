package com.example.expensemanager.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.R
import com.example.expensemanager.databinding.RowTransactionsBinding
import com.example.expensemanager.models.Transactions
import com.example.expensemanager.utils.DataProvider
import com.example.expensemanager.utils.Utils

class TransactionAdapter(
    val context: Context,
    val transactionsList: MutableList<Transactions>,
    val onDeleteClick : (Transactions) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionVH>() {

    class TransactionVH(val binding: RowTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transactions: Transactions) {

            binding.apply {
                val category = DataProvider.getCategoryDetails(transactions.category)!!
                val context = binding.root.context
                transactionCategory.text = transactions.category
                transactionAmount.text = transactions.amount.toString()
                accountLabel.text = transactions.account
                transactionCategory.backgroundTintList = context.getColorStateList(DataProvider.getAccountColor(transactions.category))
                //important discovery
                transactionDate.text = Utils.dateFormatFromLong(transactions.date)

                transactionIcon.setImageResource(category.categoryIcon)
                transactionIcon.backgroundTintList = context.getColorStateList(category.categoryColor)

                if (transactions.type == DataProvider.income){
                    val color = ContextCompat.getColor(context, R.color.green)
                    transactionAmount.setTextColor(color)
                } else if (transactions.type == DataProvider.expense) {
                    val color = ContextCompat.getColor(context, R.color.orangeDark)
                    transactionAmount.setTextColor(color)
                }

            }
        }

        companion object {
            fun inflateFrom(parent: ViewGroup): TransactionVH {
                val layoutInflater = LayoutInflater.from(parent.context)
                val rootView = RowTransactionsBinding.inflate(layoutInflater, parent, false)
                return TransactionVH(rootView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionVH =
        TransactionVH.inflateFrom(parent)

    override fun getItemCount(): Int = transactionsList.size

    override fun onBindViewHolder(holder: TransactionVH, position: Int) {
        val currentTransaction = transactionsList[position]
        holder.bind(currentTransaction)
        holder.itemView.setOnLongClickListener {
             AlertDialog.Builder(context)
                .setTitle("Delete Entry")
                .setMessage("Do you want to delete this transaction?")
                .setPositiveButton("Yes"){dialog, _ ->
                    Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()
                    onDeleteClick(currentTransaction)
                    transactionsList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, transactionsList.size)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }
    }
}