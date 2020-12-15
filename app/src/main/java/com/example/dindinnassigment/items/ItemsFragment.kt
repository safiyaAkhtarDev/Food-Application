package com.example.dindinnassigment.items

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.andremion.counterfab.CounterFab
import com.example.dindinnassigment.R
import com.example.dindinnassigment.cart.CartItemViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ItemsFragment : BaseMvRxFragment() {
    var recycler_pizza: RecyclerView? = null
    var progress_bar: ProgressBar? = null
    internal var view: View? = null
    var itemsAdapter: ItemsAdapter? = null
    var itemsPOJOSlist: ArrayList<ItemsPOJO>? = null
    var itemsPOJO: ItemsPOJO? = null

    private val repoRetriever = ItemRetriever()

    private val callback = object : Callback<RepoResult> {
        override fun onFailure(call: Call<RepoResult>?, t: Throwable?) {
            Log.e("MainActivity", "Problem calling Github API ${t?.message}")
        }

        override fun onResponse(call: Call<RepoResult>?, response: Response<RepoResult>?) {
            response?.isSuccessful.let {
                val resultList = RepoResult(
                    response?.body()?.items ?: emptyList()
                )
                itemsAdapter!!.setItems(resultList.items)
            }
        }
    }
  
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
                    var list = state.cartitems.invoke().filter {
                        it.type.equals("p")
                    }
                    progress_bar!!.visibility = View.GONE
                    recycler_pizza!!.visibility = View.VISIBLE
                    itemsAdapter!!.setItems(list)
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
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Returning the layout file after inflating
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


//*************** to load data using  retrofit***********
        loadJSON()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        //1
        val connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //2
        val activeNetwork = connectivityManager.activeNetwork
        //3
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        //4
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadJSON() {
        if (isNetworkConnected()) {
            repoRetriever.getPizza(callback)
        } else {
            AlertDialog.Builder(context).setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
        }


    }


}