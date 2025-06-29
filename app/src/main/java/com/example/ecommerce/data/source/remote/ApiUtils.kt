package com.example.ecommerce.data.source.remote

class ApiUtils {
    companion object {
        private const val BASE_URL = "http://kasimadalan.pe.hu/"
        fun getProductsDao(): ProductsRemoteDao {
            return RetrofitClient.getClient(BASE_URL).create(ProductsRemoteDao::class.java)
        }
    }
}