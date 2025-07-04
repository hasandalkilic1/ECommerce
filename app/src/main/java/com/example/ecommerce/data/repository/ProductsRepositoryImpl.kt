package com.example.ecommerce.data.repository

import com.example.ecommerce.data.model.CRUDResponse
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.source.remote.ProductsRemoteDao
import com.example.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsRepositoryImpl(private val productsRemoteDao: ProductsRemoteDao) : ProductRepository {
    override suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        return@withContext productsRemoteDao.getAllProducts().products
    }

    override suspend fun getCartProducts(): List<ShoppingCartProduct> =
        withContext(Dispatchers.IO) {
            return@withContext productsRemoteDao.getCartProducts("Hasan").products
        }

    override suspend fun addProductToCart(product: ShoppingCartProduct): CRUDResponse {
        return productsRemoteDao.addProductToCart(
            product.name,
            product.image,
            product.category,
            product.price,
            product.brand,
            product.orderQuantity,
            product.userName
        )
    }

    override suspend fun deleteProductFromCart(cartId: Int, userName: String) {
        productsRemoteDao.deleteProductFromCart(cartId, userName)
    }

    override suspend fun searchProducts(query: String, allProducts: List<Product>): List<Product> {
        return withContext(Dispatchers.IO) {
            if (query.isEmpty()) {
                productsRemoteDao.getAllProducts().products
            } else {
                allProducts.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.brand.contains(query, ignoreCase = true)
                }
            }
        }
    }
}