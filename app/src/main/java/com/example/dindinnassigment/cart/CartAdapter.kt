package com.example.dindinnassigment.cart

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dindinnassigment.R
import com.example.dindinnassigment.items.ItemsPOJO
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class CartAdapter(
    private val context: Context?,
    private val cartClickListner: CartClickListner
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val list = mutableListOf<ItemsPOJO>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_cart_items, parent, false)
        return ViewHolder(view)
    }

    fun setItems(listitems: List<ItemsPOJO>) {
        list.clear()
        list.addAll(listitems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.txt_itemname.text = item.name
        holder.txt_price.text = item.price
        Glide.with(context!!).load(item.image).placeholder(R.mipmap.pizza2).dontAnimate()
            .into(holder.img_itemImage)
        val sharedPref: SharedPreferences = context?.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        holder.img_close.setOnClickListener {
            cartClickListner.onclicklistner(item.foodId.toLong())
            list.remove(item)

            val editor = sharedPref.edit()
            editor.putInt(context.getString(R.string.cart_count), list.size)
            editor.apply()


            notifyDataSetChanged()


        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_itemname: AppCompatTextView
        var txt_price: AppCompatTextView
        var img_itemImage: AppCompatImageView
        var img_close: AppCompatImageView

        init {
            txt_itemname = itemView.findViewById(R.id.txt_itemname)
            txt_price = itemView.findViewById(R.id.txt_price)
            img_itemImage = itemView.findViewById(R.id.img_itemImage)
            img_close = itemView.findViewById(R.id.img_close)
        }
    }
}