package com.sarikaya.yemeksepeti.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sarikaya.yemeksepeti.R
import com.sarikaya.yemeksepeti.adapter.PreOrderAdapter.ViewHolder
import com.sarikaya.yemeksepeti.model.oncekiler

class PreOrderAdapter(val preOrderList: ArrayList<oncekiler>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.previousorder,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = preOrderList[position].title
        holder.price.text = preOrderList[position].price + " TL"
    }

    override fun getItemCount(): Int {
        return preOrderList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title : TextView
        val price : TextView

        init {
            title = itemView.findViewById(R.id.preTitle)
            price = itemView.findViewById(R.id.prePrice)
        }
    }
}