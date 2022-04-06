package com.example.yolharakatiqoidalari.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yolharakatiqoidalari.R
import com.example.yolharakatiqoidalari.database.Rules
import com.example.yolharakatiqoidalari.databinding.ItemRvBinding

interface ItemClick {
    fun itemClick(rules: Rules)
    fun editClick(rules: Rules)
    fun deleteClick(rules: Rules)
    fun likeClick(rules: Rules)
}

class ShowRulesAdapter(val list: List<Rules>, val itemClick: ItemClick) :
    RecyclerView.Adapter<ShowRulesAdapter.VH>() {

    inner class VH(var binding: ItemRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        if (item.imagePath!!.isEmpty()) {
            holder.binding.ruleImage.setImageResource(R.drawable.ic_outline_image_24)
        } else {
            Glide.with(holder.binding.root).load(item.imagePath)
                .into(holder.binding.ruleImage)
        }
        holder.binding.ruleTitle.text = item.ruleName
        holder.binding.like.setImageResource(getFavouriteImage(item.favourite.toBoolean()))

        holder.binding.itemRv.setOnClickListener {
            itemClick.itemClick(item)
        }

        holder.binding.editRule.setOnClickListener {
            itemClick.editClick(item)

        }

        holder.binding.deleteRule.setOnClickListener {
            itemClick.deleteClick(item)
        }

        holder.binding.like.setOnClickListener {
            item.favourite = (!item.favourite.toBoolean()).toString()
            holder.binding.like.setImageResource(getFavouriteImage(item.favourite.toBoolean()))
            itemClick.likeClick(item)
        }
    }

    override fun getItemCount(): Int = list.size


    private fun getFavouriteImage(isFavourite: Boolean): Int {
        return if (isFavourite) {
            R.drawable.ic_heart_22
        } else {
            R.drawable.ic_favorite
        }
    }
}


