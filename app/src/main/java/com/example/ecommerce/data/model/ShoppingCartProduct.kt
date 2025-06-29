package com.example.ecommerce.data.model

import com.google.gson.annotations.SerializedName

data class ShoppingCartProduct(
    @SerializedName("sepetId") val sepetId: Int,
    @SerializedName("ad") val ad: String,
    @SerializedName("resim") val resim: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("fiyat") val fiyat: Int,
    @SerializedName("marka") val marka: String,
    @SerializedName("siparisAdeti") var siparisAdeti: Int,
    @SerializedName("kullaniciAdi") val kullaniciAdi: String
)