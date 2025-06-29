package com.example.ecommerce.data.model

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("urunler")
    val products: List<Product>,

    @SerializedName("success")
    val success: Int
)