package com.example.dindinnassigment.items

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.andremion.counterfab.CounterFab
import com.example.dindinnassigment.R
import com.example.dindinnassigment.cart.CartItemViewModel
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class SushiFragment : BaseMvRxFragment() {
    var recycler_pizza: RecyclerView? = null
    var progress_bar: ProgressBar? = null
    internal var view: View? = null
    var itemsAdapter: ItemsAdapter? = null
    var itemsPOJOSlist: ArrayList<ItemsPOJO>? = null
    var itemsPOJO: ItemsPOJO? = null

    //    to load data from retrofit
    private val cartItemViewModel: CartItemViewModel by activityViewModel()
    override fun invalidate() {
        withState(cartItemViewModel) { state ->
            when (state.cartitems) {
                // 1
                is Loading -> {
                    progress_bar!!.visibility = View.VISIBLE
                    recycler_pizza!!.visibility = View.GONE
                }
                // 2
                is Success -> {
                    progress_bar!!.visibility = View.GONE
                    recycler_pizza!!.visibility = View.VISIBLE
                    itemsAdapter!!.setItems(state.cartitems.invoke().filter {
                        it.type.equals("s")
                    })
                }
                // 3
                is Fail -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load all Pizza Data",
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
        view = inflater.inflate(R.layout.item_fragment, container, false)
        recycler_pizza = view!!.findViewById(R.id.recycler_pizza)
        progress_bar = view!!.findViewById(R.id.progress_bar)
        itemsPOJOSlist = ArrayList()

        recycler_pizza!!.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        val cart_fab = activity!!.findViewById<CounterFab>(R.id.cart_fab)
        itemsAdapter = ItemsAdapter(context, object : ItemAddClickListner {

            override fun addToCart(foodId: Long) {
                cartItemViewModel.cartItems(foodId)
                try {
                    cart_fab!!.increase()
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        })
        recycler_pizza!!.setAdapter(itemsAdapter)



        return view
    }


}