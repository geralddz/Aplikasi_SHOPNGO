package com.app.shopngo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.CartEntity

open class CartAdapter (
    var list: List<CartEntity>,
    val cartItemClickInterface : CartItemClickInerface
    ) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val NamaBarang = itemView.findViewById<TextView>(R.id.tv_namabarang)
        val HargaBarang = itemView.findViewById<TextView>(R.id.tv_hargabarang)
        val JumlahBarang = itemView.findViewById<TextView>(R.id.tv_jumlah)
        val Delete = itemView.findViewById<ImageView>(R.id.btn_delete)
//        val btnTambah = itemView.findViewById<ImageView>(R.id.btn_tambah)
//        val btnKurang = itemview.findViewById<ImageView>(R.id.btn_kurang)
    }

    interface CartItemClickInerface{
        fun onItemClick(cartEntity: CartEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.NamaBarang.text = list.get(position).nama
        holder.JumlahBarang.text = list.get(position).jumlah.toString()
        val itemtotal : Int = list.get(position).harga * list.get(position).jumlah
        holder.HargaBarang.text = itemtotal.toString()
        holder.Delete.setOnClickListener{
            cartItemClickInterface.onItemClick(list.get(position))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}