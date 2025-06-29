package com.example.ecommerce.data.source.remote

import com.example.ecommerce.data.model.CRUDResponse
import com.example.ecommerce.data.model.ProductsResponse
import com.example.ecommerce.data.model.ShoppingCartProductResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductsRemoteDao {
    @GET("urunler/tumUrunleriGetir.php")
    suspend fun getAllProducts(): ProductsResponse

    @FormUrlEncoded
    @POST("urunler/sepeteUrunEkle.php")
    suspend fun addProductToCart(
        @Field("ad") ad: String,
        @Field("resim") resim: String,
        @Field("kategori") kategori: String,
        @Field("fiyat") fiyat: Int,
        @Field("marka") marka: String,
        @Field("siparisAdeti") siparisAdeti: Int,
        @Field("kullaniciAdi") kullaniciAdi: String
    ): CRUDResponse

    @FormUrlEncoded
    @POST("urunler/sepettekiUrunleriGetir.php")
    suspend fun getCartProducts(
        @Field("kullaniciAdi") kullaniciAdi: String
    ): ShoppingCartProductResponse

    @FormUrlEncoded
    @POST("urunler/sepettenUrunSil.php")
    suspend fun deleteProductFromCart(
        @Field("sepetId") sepetId: Int,
        @Field("kullaniciAdi") kullaniciAdi: String
    ): CRUDResponse
}