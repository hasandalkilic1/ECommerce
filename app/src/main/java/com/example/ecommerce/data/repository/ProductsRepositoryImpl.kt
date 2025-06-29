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
            product.ad,
            product.resim,
            product.kategori,
            product.fiyat,
            product.marka,
            product.siparisAdeti,
            product.kullaniciAdi
        )
    }

    override suspend fun deleteProductFromCart(sepetId: Int, kullaniciAdi: String) {
        productsRemoteDao.deleteProductFromCart(sepetId, kullaniciAdi)
    }

    override suspend fun searchProducts(query: String): List<Product> {
        TODO("Not yet implemented")
    }
}