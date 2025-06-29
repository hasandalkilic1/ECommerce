package com.example.ecommerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productRepository: ProductsRepositoryImpl) :
    ViewModel() {
    val totalPrice = MutableLiveData(0)
    private var addJob: Job? = null

    fun addProductToCart(product: ShoppingCartProduct) {
        if (addJob?.isActive == true) return

        addJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentCart = try {
                    productRepository.getCartProducts()
                } catch (e: Exception) {
                    emptyList<ShoppingCartProduct>()
                }

                val existingProduct = currentCart.find { it.ad == product.ad }

                if (existingProduct != null) {
                    productRepository.deleteProductFromCart(existingProduct.sepetId, "Hasan")
                }

                val updatedProduct = if (existingProduct != null) {
                    existingProduct.copy(siparisAdeti = existingProduct.siparisAdeti + product.siparisAdeti)
                } else {
                    product
                }

                productRepository.addProductToCart(updatedProduct)
            } catch (e: Exception) {
                Log.e("ProductDetailViewModel", "Error adding product to cart", e)
            }
        }
    }

    fun calculateTotalPrice(price: Int, quantity: Int) {
        totalPrice.value = price * quantity
    }
}