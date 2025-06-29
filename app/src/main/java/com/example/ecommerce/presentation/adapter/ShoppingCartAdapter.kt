package com.example.ecommerce.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.databinding.ShoppingCartItemBinding
import com.example.ecommerce.presentation.viewmodel.ShoppingCartViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.StringFormat

class ShoppingCartAdapter(
    private val context: Context,
    private val products: List<ShoppingCartProduct>,
    private val viewModel: ShoppingCartViewModel
) :
    RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>() {

    inner class ShoppingCartViewHolder(val binding: ShoppingCartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        return ShoppingCartViewHolder(
            ShoppingCartItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val product = products[position]
        val design = holder.binding
        design.tvShoppingCartItemBrand.text = product.marka
        design.tvShoppingCartItemName.text = product.ad
        design.tvShoppingCartItemPrice.text = "${product.fiyat} TL"
        design.tvShoppingCartItemCount.text = "${product.siparisAdeti}"
        design.tvShoppingCartItemTotalPrice.text = "${product.fiyat * product.siparisAdeti} TL"

        design.ivShoppingCartItemDelete.setOnClickListener {
            Snackbar.make(it, "Do you want to delete ${product.ad} ?", Snackbar.LENGTH_SHORT)
                .setAction("Yes") {
                    viewModel.deleteProductFromCart(product.sepetId, product.kullaniciAdi)
                }.show()
        }

        design.btShoppingCartDecrease.setOnClickListener {
            if (product.siparisAdeti > 1) {
                val newQuantity = product.siparisAdeti - 1
                viewModel.updateProductQuantity(product, newQuantity)
            }
        }

        design.btShoppingCartIncrease.setOnClickListener {
            val newQuantity = product.siparisAdeti + 1
            viewModel.updateProductQuantity(product, newQuantity)
        }

        Glide.with(context).load("http://kasimadalan.pe.hu/urunler/resimler/${product.resim}")
            .override(64, 64).into(design.ivShoppingCartItemImage)
    }

    fun updateProducts(newList: List<ShoppingCartProduct>) {
        (products as MutableList).clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }
}