package com.example.productiontest

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productiontest.databinding.ItemCoffeeBinding
import com.example.productiontest.model.Coffee
import com.example.productiontest.model.FilterData


class CoffeeAdapter(val coffeeList: ArrayList<Coffee>) :
    RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHodler>(),
    Filterable {

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private val filteredCoffee = ArrayList<Coffee>()
    private val itemFilter = ItemFilter()
    private var itemClickListener: OnItemClickListener? = null
    var filterData = FilterData()

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHodler {
        return CoffeeViewHodler(
            ItemCoffeeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CoffeeViewHodler, position: Int) {
        holder.bind(filteredCoffee[position])
    }

    override fun getItemCount(): Int {
        return filteredCoffee.size
    }

    fun getCoffeeList(position: Int): Coffee {
        return filteredCoffee[position]
    }

    fun changeItem(position: Int, coffee: Coffee) {
        filteredCoffee[position].favorite = coffee.favorite
    }

    inner class CoffeeViewHodler(private val binding: ItemCoffeeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coffee: Coffee) {
            binding.tvCoffeeName.text = coffee.product_NM
            binding.tvKcal.text = "${coffee.kcal} Kcal"
            binding.cbFavor.isChecked = coffee.favorite
            Glide.with(itemView)
                .load(coffee.file_PATH)
                .into(binding.ivCoffeeImg)
            if (adapterPosition != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    itemClickListener?.onClick(itemView, adapterPosition)
                }
            }
        }
    }

    fun checkCafein(check: Boolean, data: ArrayList<Coffee>): ArrayList<Coffee> {
        val temp = ArrayList<Coffee>()
        for (i in 0 until data.size) {
            if (check) {
                if (data[i].caffeine == "0") {
                    temp.add(data[i])
                }
            } else {
                temp.add(data[i])
            }
        }
        return temp
    }

    fun sort(sortNumber: Int, data: ArrayList<Coffee>): ArrayList<Coffee> {
        val temp = ArrayList<Coffee>()
        when (sortNumber) {
            -1 -> {
                temp.addAll(data.sortedBy { it.kcal.toInt() })
            }
            1 -> {
                temp.addAll(data.sortedByDescending { it.kcal.toInt() })
            }
            else -> {
                return data
            }
        }
        return temp
    }

    fun proRange(proStart: Int, proEnd: Int, data: ArrayList<Coffee>): ArrayList<Coffee> {
        val temp = ArrayList<Coffee>()
        for (i in 0 until data.size) {
            if (proStart <= data[i].protein.toDouble() && data[i].protein.toDouble() <= proEnd)
                temp.add(data[i])
        }
        return temp
    }

    fun fatRange(fatStart: Int, fatEnd: Int, data: ArrayList<Coffee>): ArrayList<Coffee> {
        val temp = ArrayList<Coffee>()
        for (i in 0 until data.size) {
            if (fatStart <= data[i].fat.toDouble() && data[i].fat.toDouble() <= fatEnd)
                temp.add(data[i])
        }
        return temp
    }

    fun sugarRange(sugarStart: Int, sugarEnd: Int, data: ArrayList<Coffee>): ArrayList<Coffee> {
        val temp = ArrayList<Coffee>()
        for (i in 0 until data.size) {
            if (sugarStart <= data[i].sugars.toDouble() && data[i].sugars.toDouble() <= sugarEnd)
                temp.add(data[i])
        }
        return temp
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()
            val filteredList = ArrayList<Coffee>()
            val defaultList = checkCafein(filterData.checkCafein, coffeeList)
            val sortfilter = sort(filterData.sort, defaultList)
            val profilter = proRange(filterData.startProtein, filterData.endProtein, sortfilter)
            val fatfilter = fatRange(filterData.startFat, filterData.endFat, profilter)
            val sugarfilter = sugarRange(filterData.startSugar, filterData.endSugar, fatfilter)

            if (filterString.trim().isEmpty() || charSequence == null) {
                results.values = sugarfilter
                results.count = sugarfilter.size
            } else {
                for (coffee in sugarfilter) {
                    if (coffee.product_NM.contains(filterString)) {
                        filteredList.add(coffee)
                    }
                }
                results.values = filteredList
                results.count = filteredList.size
            }
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
            filteredCoffee.clear()
            filteredCoffee.addAll(filterResults.values as ArrayList<Coffee>)
            notifyDataSetChanged()
        }
    }
}


