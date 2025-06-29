package com.example.ecommerce.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("ad") val ad: String,
    @SerializedName("resim") val resim: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("fiyat") val fiyat: Int,
    @SerializedName("marka") val marka: String
) : Serializable