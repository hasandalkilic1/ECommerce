package com.example.ecommerce.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.model.CRUDResponse
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val productRepository: ProductsRepositoryImpl) :
    ViewModel() {
    var products = MutableLiveData<List<Product>>()
    private var addJob: Job? = null

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        CoroutineScope(Dispatchers.Main).launch {
            products.value = productRepository.getProducts()
        }
    }

    fun addProductToCart(product: Product) {
        // Eğer önceki iş hâlâ çalışıyorsa, yenisini başlatma
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

                val productShoppingCart = if (existingProduct != null) {
                    existingProduct.copy(siparisAdeti = existingProduct.siparisAdeti + 1)
                } else {
                    ShoppingCartProduct(
                        sepetId = 1,
                        ad = product.ad,
                        resim = product.resim,
                        kategori = product.kategori,
                        fiyat = product.fiyat,
                        marka = product.marka,
                        siparisAdeti = 1,
                        kullaniciAdi = "Hasan"
                    )
                }

                val response = productRepository.addProductToCart(productShoppingCart)
                Log.d("MainViewModel", "Add to cart success: ${response.success}, message: ${response.message}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error adding product to cart", e)
            }
        }
    }
}