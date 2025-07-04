package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.model.CRUDResponse
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.ShoppingCartProduct

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getCartProducts(): List<ShoppingCartProduct>
    suspend fun addProductToCart(product: ShoppingCartProduct): CRUDResponse
    suspend fun deleteProductFromCart(cartId: Int, userName: String)
    suspend fun searchProducts(query: String, products: List<Product>): List<Product>
}