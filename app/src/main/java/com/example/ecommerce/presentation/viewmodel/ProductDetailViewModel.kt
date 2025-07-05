package com.example.ecommerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductsRepositoryImpl
) : ViewModel() {

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> get() = _totalPrice

    private var addJob: Job? = null

    fun addProductToCart(product: ShoppingCartProduct) {
        if (addJob?.isActive == true) return

        addJob = viewModelScope.launch {
            try {
                val currentCart = try {
                    productRepository.getCartProducts()
                } catch (e: Exception) {
                    emptyList()
                }

                val existingProduct = currentCart.find { it.name == product.name }

                if (existingProduct != null) {
                    productRepository.deleteProductFromCart(existingProduct.cartId, "Hasan")
                }

                val updatedProduct = existingProduct?.copy(
                    orderQuantity = existingProduct.orderQuantity + product.orderQuantity
                ) ?: product

                productRepository.addProductToCart(updatedProduct)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error adding product to cart", e)
            }
        }
    }

    fun calculateTotalPrice(price: Int, quantity: Int) {
        _totalPrice.value = price * quantity
    }
}