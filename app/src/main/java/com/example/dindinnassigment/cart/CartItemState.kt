package com.example.dindinnassigment.cart

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.example.dindinnassigment.items.ItemsPOJO

data class CartItemState (
    val cartitems: Async<List<ItemsPOJO>> = Uninitialized
) : MvRxState