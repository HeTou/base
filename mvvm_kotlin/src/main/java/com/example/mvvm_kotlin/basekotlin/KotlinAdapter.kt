package com.example.mvvm_kotlin.basekotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_kotlin.R
import com.example.mvvm_kotlin.common.bean.Item
import com.example.mvvm_kotlin.databinding.ItemKotlinBinding

class KotlinAdapter constructor(var context: Context, val itemList: List<Item>) : RecyclerView.Adapter<KotlinAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_kotlin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList.get(position)
        holder.setText(item.name)
        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClicked(it,position)
        })
    }

    override fun getItemCount(): Int {
        return itemList.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var databind: ItemKotlinBinding? = null

        init {
            databind = DataBindingUtil.bind(itemView)
        }

        fun setText(name: String) {
            databind?.tv?.setText(name)
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }
}