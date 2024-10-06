package com.example.expensemanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.R
import com.example.expensemanager.databinding.SampleCategoryItemBinding
import com.example.expensemanager.models.Category

class CategoryAdapter(val context: Context, val catList: List<Category>, val onClickCat : (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CatVH>() {

        class CatVH( val binding: SampleCategoryItemBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(category: Category){
                binding.categoryTitle.text = category.category
                binding.categoryIcon.setImageResource(category.categoryIcon)
            }

          companion object {
              fun inflateFrom(parent : ViewGroup) : CatVH {
                  val inflater = LayoutInflater.from(parent.context)
                  val view = SampleCategoryItemBinding.inflate(inflater, parent, false)
                  return CatVH(view)
              }
          }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatVH = CatVH.inflateFrom(parent)

    override fun getItemCount(): Int = catList.size

    override fun onBindViewHolder(holder: CatVH, position: Int) {
        val currentCat = catList[position]
        holder.bind(currentCat)

        holder.binding.categoryIcon.backgroundTintList = context.getColorStateList(currentCat.categoryColor)
        holder.itemView.setOnClickListener {
            onClickCat(currentCat)
        }
    }
}