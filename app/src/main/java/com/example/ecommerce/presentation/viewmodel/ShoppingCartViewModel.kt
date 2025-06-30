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
        cartProducts.value = cartProducts.value?.sortedBy { it.marka.lowercase() }
    }

    fun updateProductQuantity(product: ShoppingCartProduct, newQuantity: Int) {
        if (updateJob?.isActive == true) return

        updateJob = viewModelScope.launch {
            try {
                // 1. Önce ürünü sil
                productRepository.deleteProductFromCart(product.sepetId, product.kullaniciAdi)

                // 2. Yeni adetle tekrar ekle
                val updatedProduct = product.copy(siparisAdeti = newQuantity)
                productRepository.addProductToCart(
                    updatedProduct
                )

                // 3. Güncel listeyi tekrar al
                cartProducts.value = productRepository.getCartProducts() ?: emptyList()
                sortProductsAlphabetically()
                // 4. Toplam fiyatı yeniden hesapla
                calculateTotalPrice()
            } catch (e: Exception) {
                // Hata durumunda listeyi sıfırlamak isteyebilirsin
                cartProducts.value = emptyList()
                totalPrice.value = 0
            }
        }
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