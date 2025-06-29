package com.example.ecommerce.data.model

import com.google.gson.annotations.SerializedName

data class ShoppingCartProductResponse(
    @SerializedName("urunler_sepeti")
    val products: List<ShoppingCartProduct>,

    @SerializedName("success")
    val success: Int
)