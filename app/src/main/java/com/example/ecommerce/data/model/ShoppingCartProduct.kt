package com.example.ecommerce.data.model

import com.google.gson.annotations.SerializedName

data class ShoppingCartProduct(
    @SerializedName("sepetId") val cartId: Int,
    @SerializedName("ad") val name: String,
    @SerializedName("resim") val image: String,
    @SerializedName("kategori") val category: String,
    @SerializedName("fiyat") val price: Int,
    @SerializedName("marka") val brand: String,
    @SerializedName("siparisAdeti") var orderQuantity: Int,
    @SerializedName("kullaniciAdi") val userName: String
)