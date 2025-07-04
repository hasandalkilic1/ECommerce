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
        @Field("ad") name: String,
        @Field("resim") image: String,
        @Field("kategori") category: String,
        @Field("fiyat") price: Int,
        @Field("marka") brand: String,
        @Field("siparisAdeti") orderQuantity: Int,
        @Field("kullaniciAdi") userName: String
    ): CRUDResponse

    @FormUrlEncoded
    @POST("urunler/sepettekiUrunleriGetir.php")
    suspend fun getCartProducts(
        @Field("kullaniciAdi") userName: String
    ): ShoppingCartProductResponse

    @FormUrlEncoded
    @POST("urunler/sepettenUrunSil.php")
    suspend fun deleteProductFromCart(
        @Field("sepetId") cartId: Int,
        @Field("kullaniciAdi") userName: String
    ): CRUDResponse
}