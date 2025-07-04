package com.example.ecommerce.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(private val productRepository: ProductsRepositoryImpl) :
    ViewModel() {
    val cartProducts = MutableLiveData<List<ShoppingCartProduct>>(emptyList())
    val totalPrice = MutableLiveData(0)

    private var updateJob: Job? = null

    init {
        getCartProducts()
    }

    fun getCartProducts() {
        viewModelScope.launch {
            try {
                cartProducts.value = productRepository.getCartProducts() ?: emptyList()
                sortProductsAlphabetically()
                calculateTotalPrice()
            } catch (e: Exception) {
                cartProducts.value = emptyList()
                totalPrice.value = 0
            }
        }
    }

    private fun sortProductsAlphabetically() {
        cartProducts.value = cartProducts.value?.sortedBy { it.brand.lowercase() }
    }

    fun updateProductQuantity(product: ShoppingCartProduct, newQuantity: Int) {
        if (updateJob?.isActive == true) return

        updateJob = viewModelScope.launch {
            try {
                productRepository.deleteProductFromCart(product.cartId, product.userName)

                val updatedProduct = product.copy(orderQuantity = newQuantity)
                productRepository.addProductToCart(
                    updatedProduct
                )

                cartProducts.value = productRepository.getCartProducts() ?: emptyList()
                sortProductsAlphabetically()
                calculateTotalPrice()
            } catch (e: Exception) {
                cartProducts.value = emptyList()
                totalPrice.value = 0
            }
        }
    }

    private fun calculateTotalPrice() {
        totalPrice.value = cartProducts.value?.sumOf { it.price * it.orderQuantity }
    }

    fun deleteProductFromCart(cartId: Int, userName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            productRepository.deleteProductFromCart(cartId, userName)
            getCartProducts()
        }
    }
}