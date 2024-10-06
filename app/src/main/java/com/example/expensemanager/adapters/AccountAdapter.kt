package com.example.expensemanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.databinding.RowAccountBinding
import com.example.expensemanager.models.Accounts

class AccountAdapter(
    val context: Context,
    val accountList: List<Accounts>,
    val onAccountClick: (Accounts) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountVH>() {


    class AccountVH(val binding: RowAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Accounts) {
            binding.accountName.text = account.accountName
        }

        companion object {
            fun inflateFrom(parent: ViewGroup): AccountVH {
                val inflater = LayoutInflater.from(parent.context)
                val root = RowAccountBinding.inflate(inflater, parent, false)
                return AccountVH(root)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountVH =
        AccountVH.inflateFrom(parent)

    override fun getItemCount(): Int = accountList.size

    override fun onBindViewHolder(holder: AccountVH, position: Int) {
        val currentAccount = accountList[position]
        holder.bind(currentAccount)
        holder.itemView.setOnClickListener {
            onAccountClick(currentAccount)
        }
    }
}