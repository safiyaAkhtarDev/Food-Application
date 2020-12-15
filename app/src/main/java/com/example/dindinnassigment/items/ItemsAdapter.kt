package com.example.dindinnassigment.items

import android.content.ClipData
import android.content.Context
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.andremion.counterfab.CounterFab
import com.bumptech.glide.Glide
import com.example.dindinnassigment.R

class ItemsAdapter(
    private val context: Context?,
    private val itemAddClickListner: ItemAddClickListner
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
    private val list = mutableListOf<ItemsPOJO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_items, parent, false)
        return ViewHolder(view)
    }

    fun setItems(listitems: List<ItemsPOJO>) {
        list.clear()

        list.addAll(listitems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tv_itemname.text = item.name
        holder.tv_tags.text = Html.fromHtml(item.tags)
        holder.tv_price.text = item.price
        holder.tv_weight.text = item.weight
        Glide.with(context!!).load(item.image).placeholder(R.mipmap.pizza2).dontAnimate()
            .into(holder.img_itemimage)
        if (item.isNonveg) {
            holder.img_nonveg.visibility = View.VISIBLE
        } else {
            holder.img_nonveg.visibility = View.GONE
        }
        holder.tv_price.setOnClickListener {
            holder.tv_add_item.visibility = View.VISIBLE
            holder.tv_price.visibility = View.GONE
            Log.d("safiyas", item.foodId.toString())
            itemAddClickListner.addToCart(item.foodId.toLong())

            val handler = Handler()
            handler.postDelayed({ //                        change back to add icon
                holder.tv_add_item.visibility = View.GONE
                holder.tv_price.visibility = View.VISIBLE
            }, 500)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_itemname: AppCompatTextView
        var tv_tags: AppCompatTextView
        var tv_weight: AppCompatTextView
        var tv_price: AppCompatTextView
        var tv_add_item: AppCompatTextView
        var img_itemimage: AppCompatImageView
        var img_nonveg: AppCompatImageView

        init {
            tv_itemname = itemView.findViewById(R.id.tv_itemname)
            tv_tags = itemView.findViewById(R.id.tv_tags)
            tv_add_item = itemView.findViewById(R.id.tv_add_item)
            tv_weight = itemView.findViewById(R.id.tv_weight)
            tv_price = itemView.findViewById(R.id.tv_price)
            img_itemimage = itemView.findViewById(R.id.img_itemimage)
            img_nonveg = itemView.findViewById(R.id.img_nonveg)
        }
    }
}