package com.example.ecommerce.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(private val productRepository: ProductsRepositoryImpl) :
    ViewModel() {
    val cartProducts = MutableLiveData<List<ShoppingCartProduct>>(emptyList())
    val totalPrice = MutableLiveData(0)

    init {
        getCartProducts()
    }

    fun getCartProducts() {
        viewModelScope.launch {
            try {
                cartProducts.value = productRepository.getCartProducts() ?: emptyList()
                calculateTotalPrice()
            } catch (e: Exception) {
                cartProducts.value = emptyList()
                totalPrice.value = 0
            }
        }
    }

    fun updateProductQuantity(product: ShoppingCartProduct, newQuantity: Int) {
        val updatedList = cartProducts.value?.map {
            if (it.sepetId == product.sepetId) it.copy(siparisAdeti = newQuantity) else it
        } ?: return

        cartProducts.value = updatedList
        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        totalPrice.value = cartProducts.value?.sumOf { it.fiyat * it.siparisAdeti }
    }

    fun deleteProductFromCart(sepetId: Int, kullaniciAdi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            productRepository.deleteProductFromCart(sepetId, kullaniciAdi)
            getCartProducts()
        }
    }
}