package com.app.shopngo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.shopngo.Object.Helper
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.Model.HistoryEntity

class HistoryAdapter (var listhistory: List<HistoryEntity>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val IdTrans = itemView.findViewById<TextView>(R.id.tv_idtran)
        val TotalTrans = itemView.findViewById<TextView>(R.id.tv_total)
        val Status = itemView.findViewById<TextView>(R.id.tv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history,parent,false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.IdTrans.text = listhistory[position].idtrans
        val total = Integer.valueOf(listhistory[position].total)
        holder.TotalTrans.text = Helper().gantiRupiah(total)
        holder.Status.text = "Success"
    }

    override fun getItemCount(): Int {
        return listhistory.size
    }
}