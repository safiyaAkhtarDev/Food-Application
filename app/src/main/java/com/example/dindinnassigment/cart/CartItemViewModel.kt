package com.example.dindinnassigment.cart

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import com.example.dindinnassigment.items.ItemsPOJO
import io.reactivex.Observable

class CartItemViewModel(
    initialState: CartItemState,
    private val cartItemsReporsitory: CartItemsReporsitory
) : BaseMvRxViewModel<CartItemState>(initialState, debugMode = true) {
    val errorMessage = MutableLiveData<String>()

    init {
        // 1
        setState {
            copy(cartitems = Loading())
        }

        cartItemsReporsitory.getItems()
            .execute {
                copy(cartitems = it)
            }
    }


    fun removeItemFromCart(foodId: Long) {
        withState { state ->
            if (state.cartitems is Success) {
                val index = state.cartitems.invoke().indexOfFirst {
                    it.foodId.toLong() == foodId.toLong()
                }
                cartItemsReporsitory.removeItemFromCart(foodId)
                    .execute {
                        if (it is Success) {
                            copy(
                                cartitems = Success(
                                    state.cartitems.invoke().toMutableList().apply {
                                        set(index, it.invoke())
                                    }
                                )
                            )
                        } else if (it is Fail) {
                            errorMessage.postValue("Failed to remove movie from watchlist")
                            copy()
                        } else {
                            copy()
                        }
                    }
            }
        }
    }
    fun getItems(): Observable<List<ItemsPOJO>>? {

               return cartItemsReporsitory.getItems()


    }


    fun cartItems(foodId: Long) {
        withState { state ->
            if (state.cartitems is Success) {
                val index = state.cartitems.invoke().indexOfFirst {
                    it.foodId.toLong() == foodId
                }
                // 1
                cartItemsReporsitory.cartItem(foodId)
                    .execute {
                        // 2
                        if (it is Success) {
                            copy(
                                cartitems = Success(
                                    state.cartitems.invoke().toMutableList().apply {
                                        set(index, it.invoke())
                                    }
                                )
                            )
                            // 3
                        } else if (it is Fail) {
                            errorMessage.postValue("Failed to add movie to watchlist")
                            copy()
                        } else {
                            copy()
                        }
                    }
            }
        }
    }

    companion object : MvRxViewModelFactory<CartItemViewModel, CartItemState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: CartItemState
        ): CartItemViewModel? {
            val watchlistRepository =
                viewModelContext.app<CartApp>().cartItemsReporsitory
            return CartItemViewModel(state, watchlistRepository)
        }


    }
}