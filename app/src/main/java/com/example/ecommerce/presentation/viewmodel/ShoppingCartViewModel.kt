package com.example.ecommerce.presentation.viewmodel

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
class ShoppingCartViewModel @Inject constructor(
    private val productRepository: ProductsRepositoryImpl
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<List<ShoppingCartProduct>>(emptyList())
    val cartProducts: StateFlow<List<ShoppingCartProduct>> get() = _cartProducts

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> get() = _totalPrice

    private var updateJob: Job? = null

    init {
        getCartProducts()
    }

    fun getCartProducts() {
        viewModelScope.launch {
            try {
                _cartProducts.value = productRepository.getCartProducts() ?: emptyList()
                sortProductsAlphabetically()
                calculateTotalPrice()
            } catch (e: Exception) {
                _cartProducts.value = emptyList()
                _totalPrice.value = 0
            }
        }
    }

    private fun sortProductsAlphabetically() {
        _cartProducts.value = _cartProducts.value.sortedBy { it.brand.lowercase() }
    }

    fun updateProductQuantity(product: ShoppingCartProduct, newQuantity: Int) {
        if (updateJob?.isActive == true) return

        updateJob = viewModelScope.launch {
            try {
                productRepository.deleteProductFromCart(product.cartId, product.userName)

                val updatedProduct = product.copy(orderQuantity = newQuantity)
                productRepository.addProductToCart(updatedProduct)

                _cartProducts.value = productRepository.getCartProducts() ?: emptyList()
                sortProductsAlphabetically()
                calculateTotalPrice()
            } catch (e: Exception) {
                _cartProducts.value = emptyList()
                _totalPrice.value = 0
            }
        }
    }

    private fun calculateTotalPrice() {
        _totalPrice.value = _cartProducts.value.sumOf { it.price * it.orderQuantity }
    }

    fun deleteProductFromCart(cartId: Int, userName: String) {
        viewModelScope.launch {
            productRepository.deleteProductFromCart(cartId, userName)
            getCartProducts()
        }
    }
}