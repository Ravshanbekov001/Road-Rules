package com.example.yolharakatiqoidalari.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yolharakatiqoidalari.database.Rules
import com.example.yolharakatiqoidalari.databinding.ViewPagerItemBinding

class ViewPagerAdapter(
    val list: List<Rules>,
    val list2: List<Rules>,
    val list3: List<Rules>,
    val list4: List<Rules>,
    val itemClick: ItemClick,
) :
    RecyclerView.Adapter<ViewPagerAdapter.VH>() {

    inner class VH(var binding: ViewPagerItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ViewPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.root.adapter =
            ShowRulesAdapter(
                (when (position) {
                    0 -> list
                    1 -> list2
                    2 -> list3
                    3 -> list4
                    else -> list
                }),
                itemClick
            )
    }

    override fun getItemCount(): Int = 4
}