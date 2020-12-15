package com.example.dindinnassigment.items

import android.os.Parcelable

import androidx.versionedparcelable.ParcelField

data class RepoResult(val items: List<ItemsPOJO>)

data class ItemsPOJO(
    var foodId: Int,
    var name: String,
    var tags: String,
    var price: String,
    var weight: String,
    var type: String,
    var image: Int,
    var isNonveg: Boolean,
    var isAddedToCart: Boolean
)
data class Owner(val login: String?, val id: Long?, val avatarUrl: String?)