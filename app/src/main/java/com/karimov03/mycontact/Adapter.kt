package com.karimov03.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.karimov03.mycontact.databinding.ItemRvBinding

class Adapter(val list: ArrayList<MyContact>) : RecyclerView.Adapter<Adapter.vh>() {
    inner class vh(val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vh {
        return vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: vh, position: Int) {
        val list = list[position]
        holder.itemRvBinding.tvName.text = list.name
        holder.itemRvBinding.tvNumber.text = list.number
    }


}