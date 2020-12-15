package com.example.dindinnassigment.cart

import android.app.Application
import android.content.Context
import androidx.core.content.res.TypedArrayUtils.getString
import com.example.dindinnassigment.R
import com.example.dindinnassigment.items.ItemsPOJO
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class CartItemsReporsitory {


    private val itemslist = mutableListOf<ItemsPOJO>()

    fun cartItem(movieId: Long): Observable<ItemsPOJO> {
        return Observable.fromCallable {
            val items = itemslist.first { movie -> movie.foodId.toLong() == movieId }
            items.copy(isAddedToCart = true)
        }
    }

    fun removeItemFromCart(movieId: Long): Observable<ItemsPOJO> {
        return Observable.fromCallable {
            val movie = itemslist.first { movie -> movie.foodId.toLong() == movieId }
            movie.copy(isAddedToCart = false)
        }
    }

    fun removeCart(): Observable<ItemsPOJO> {
        return Observable.fromCallable {
            val mutableIterator = itemslist.iterator()
            val movie = mutableIterator.next()
            movie.copy(isAddedToCart = false)
        }
    }

    fun getItems() = Observable.fromCallable<List<ItemsPOJO>> {
//        causing delay just like when we call from APis
        Thread.sleep(2000)
        itemslist.addAll(
            listOf(
                ItemsPOJO(
                    1,
                    "Deluxe",
                    "<p>chicken, hams, peperroni, tomato sauce,<span style='color:red'> spicy chorrizo </span> and mozzerilla</p>",
                    "46 usd",
                    "150 grams, 35 cm",
                    "p",
                    R.mipmap.pizza2,
                    true,
                    false
                ),
                ItemsPOJO(
                    2,
                    "QUATTRO FORMAGGI",
                    "<p>Fresh mozzarella, basil, gorgonzola, garlic, extra virgin olive oil, house cheese, grana padano.</p>",
                    "26 usd",
                    "150 grams, 35 cm",
                    "p",
                    R.mipmap.pizza3,
                    false,
                    false
                ),
                ItemsPOJO(
                    3,
                    "SALAMI",
                    "<p>Tomato sauce, fresh mozzarella, salami, basil, grana padano.</p>",
                    "30 usd",
                    "50 grams, 35 cm",
                    "p",
                    R.mipmap.pizza4,
                    false,
                    false
                ),
                ItemsPOJO(
                    4,
                    "MARGHERITA",
                    "<p>Tomato sauce, fresh mozzarella, basil, extra virgin olive oil, grana padano.</p>",
                    "10 usd",
                    "150 grams, 15 cm",
                    "p",
                    R.mipmap.pizza5,
                    false,
                    false
                ),
                ItemsPOJO(
                    5,
                    "CHEESE", "<p>Tomato sauce, diced mozzarella, grana padano.</p>",
                    "80 usd", "250 grams, 65 cm", "p", R.mipmap.pizza6, false, false
                ),
                ItemsPOJO(
                    6,
                    "The Egoist",
                    "<p>Cucumber, Asparagus, Avocado, Lettuce, Cooked Carrot, Shiso and esame Dressing</p>",
                    "36 usd",
                    "450 grams, 18 piece",
                    "s",
                    R.mipmap.sushiplatter,
                    true,
                    false
                ), ItemsPOJO(
                    7,

                    "Sushi Rolls",
                    "<p>6 pieces of Crispy Tempura Sushi Rice topped with Spicy Tuna Tartar, avocado, jalapeno, tobiko and sweet sauce.</p>",
                    "26 usd",
                    "150 grams, 5 piece",
                    "s",
                    R.mipmap.shushirolls,
                    false,
                    false
                ), ItemsPOJO(
                    8,

                    "SALAMI",
                    "<p>Tomato sauce, fresh mozzarella, salami, basil, grana padano.</p>",
                    "30 usd",
                    "50 grams, 35 cm",
                    "s",
                    R.mipmap.pizza4,
                    false,
                    false
                ), ItemsPOJO(
                    9,

                    "Shalmon Sushi",
                    "<p>Bok choy, wasabi tobiko, shiitake mushroom, otoshi and ponzu sauce",
                    "70 usd",
                    "650 grams, 35 Piece", "s",
                    R.mipmap.shushishalmonbig,
                    false, false
                ), ItemsPOJO(
                    10,

                    "Sushi",
                    "<p>Asparagus and mushrooms sautéed in a sake garlic-butter</p>",
                    "50 usd",
                    "350 grams, 15 piece",
                    "s",
                    R.mipmap.shushis,
                    false,
                    false
                )


            )
        )
        itemslist

    }.subscribeOn(Schedulers.io())
}
