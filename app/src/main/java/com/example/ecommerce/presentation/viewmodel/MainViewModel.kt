package com.example.ecommerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val productRepository: ProductsRepositoryImpl) :
    ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private var addJob: Job? = null

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            _products.value = productRepository.getProducts()
        }
    }

    fun addProductToCart(product: Product) {
        if (addJob?.isActive == true) return

        addJob = viewModelScope.launch {
            try {
                val currentCart = try {
                    productRepository.getCartProducts()
                } catch (e: Exception) {
                    emptyList<ShoppingCartProduct>()
                }

                val existingProduct = currentCart.find { it.name == product.name }

                if (existingProduct != null) {
                    productRepository.deleteProductFromCart(existingProduct.cartId, "Hasan")
                }

                val productShoppingCart =
                    existingProduct?.copy(orderQuantity = existingProduct.orderQuantity + 1)
                        ?: ShoppingCartProduct(
                            cartId = 1,
                            name = product.name,
                            image = product.image,
                            category = product.category,
                            price = product.price,
                            brand = product.brand,
                            orderQuantity = 1,
                            userName = "Hasan"
                        )

                val response = productRepository.addProductToCart(productShoppingCart)
                Log.d(
                    "MainViewModel",
                    "Add to cart success: ${response.success}, message: ${response.message}"
                )
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error adding product to cart", e)
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            val allProducts = _products.value
            val result = productRepository.searchProducts(query, allProducts)
            _products.value = result
        }
    }
}
