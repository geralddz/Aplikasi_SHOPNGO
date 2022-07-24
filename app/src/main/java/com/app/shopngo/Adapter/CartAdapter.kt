package com.app.shopngo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.app.shopngo.Object.Helper
import com.app.shopngo.R
import com.app.shopngo.RoomDatabase.Model.CartEntity
import com.squareup.picasso.Picasso

open class CartAdapter(
    var list: List<CartEntity>,
    val cartItemClickInterface: CartItemClickInerface
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val NamaBarang = itemView.findViewById<TextView>(R.id.tv_namabarang)
        val HargaBarang = itemView.findViewById<TextView>(R.id.tv_hargabarang)
        val JumlahBarang = itemView.findViewById<TextView>(R.id.tv_jumlah)
        val imgProduk = itemView.findViewById<ImageView>(R.id.img_produk)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val tvJumlah = itemView.findViewById<TextView>(R.id.tv_jumlah)
        val Delete = itemView.findViewById<ImageView>(R.id.btn_delete)
        val btnTambah = itemView.findViewById<ImageView>(R.id.btn_tambah)
        val btnKurang = itemView.findViewById<ImageView>(R.id.btn_kurang)
    }

    interface CartItemClickInerface{
        fun onDelete(cartEntity: CartEntity)
        fun onUpdate(cartEntity: CartEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var jumlah = list[position].jumlah
        val data = list[position]
        val harga = Integer.valueOf(data.harga)
        holder.NamaBarang.text = list[position].nama
        holder.JumlahBarang.text = list[position].jumlah.toString()
        val itemtotal : Int = list[position].harga * list.get(position).jumlah
        holder.HargaBarang.text = Helper().gantiRupiah(itemtotal)
        val image = list[position].imgurl.toUri()
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.shopgo)
            .error(R.drawable.shopgo)
            .into(holder.imgProduk)

        holder.btnTambah.setOnClickListener {
            jumlah++
            data.jumlah = jumlah
            holder.tvJumlah.text = jumlah.toString()
            holder.HargaBarang.text = Helper().gantiRupiah(harga * jumlah)
            cartItemClickInterface.onUpdate(data)
        }

        holder.btnKurang.setOnClickListener {
            if (jumlah <= 1) return@setOnClickListener
            jumlah--
            data.jumlah = jumlah
            holder.tvJumlah.text = jumlah.toString()
            holder.HargaBarang.text = Helper().gantiRupiah(harga * jumlah)
            cartItemClickInterface.onUpdate(data)
        }
        holder.checkBox.isChecked = list[position].selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            list[position].selected = isChecked
            cartItemClickInterface.onUpdate(data)
        }
        holder.Delete.setOnClickListener{
            cartItemClickInterface.onDelete(list[position])
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

}