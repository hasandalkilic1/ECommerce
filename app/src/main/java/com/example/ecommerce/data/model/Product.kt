package com.example.ecommerce.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("ad") val name: String,
    @SerializedName("resim") val image: String,
    @SerializedName("kategori") val category: String,
    @SerializedName("fiyat") val price: Int,
    @SerializedName("marka") val brand: String
) : Serializable