package com.example.dindinnassigment.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.andremion.counterfab.CounterFab
import com.example.dindinnassigment.R

import com.example.dindinnassigment.items.ItemsPOJO
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class CartFragment : BaseMvRxFragment() {
    var recycler_cart: RecyclerView? = null
    var cartAdapter: CartAdapter? = null
    var itemsPOJOSlist: ArrayList<ItemsPOJO>? = null
    internal var view: View? = null
    var txt_cart_total: AppCompatTextView? = null
    var totallist: List<String>? = null
    var totalamount: Int? = 0
    var progress_bar: ProgressBar? = null

    private val cartItemViewModel: CartItemViewModel by activityViewModel()

    override fun invalidate() {
        withState(cartItemViewModel) { state ->
            when (state.cartitems) {
                is Loading -> {
                    progress_bar!!.visibility = View.VISIBLE
                    recycler_cart!!.visibility = View.GONE
                }
                is Success -> {
                    val cartitemlist = state.cartitems.invoke().filter {
                        it.isAddedToCart == true
                    }
                    Log.d("safiyas", cartitemlist.toString())
                    progress_bar!!.visibility = View.GONE
                    recycler_cart!!.visibility = View.VISIBLE
                    totalamount = 0
                    cartitemlist.forEach {
                        val cart = it

                        totalamount =
                            totalamount?.plus(Integer.parseInt(cart.price!!.split(" ")[0]))

                    }
                    txt_cart_total!!.text = totalamount.toString()

                    cartAdapter!!.setItems(cartitemlist)
                }
                is Fail -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    //Overriden method onCreateView
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.cart_fragment, container, false)

        recycler_cart = view!!.findViewById(R.id.recycler_pizza)
        progress_bar = view!!.findViewById(R.id.progress_bar)
        txt_cart_total = view!!.findViewById(R.id.txt_cart_total)

        itemsPOJOSlist = ArrayList()

        recycler_cart!!.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        cartAdapter = CartAdapter(context, object : CartClickListner {
            override fun onclicklistner(foodId: Long) {
                cartItemViewModel.removeItemFromCart(foodId)
            }

        })
        recycler_cart!!.setAdapter(cartAdapter)
        return view
    }

}