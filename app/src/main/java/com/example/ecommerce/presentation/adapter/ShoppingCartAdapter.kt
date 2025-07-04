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
import java.text.NumberFormat
import java.util.Locale

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

        val formatter = NumberFormat.getInstance(Locale("tr", "TR"))

        design.tvShoppingCartItemBrand.text = product.brand
        design.tvShoppingCartItemName.text = product.name
        design.tvShoppingCartItemPrice.text = "${formatter.format(product.price)} TL"
        design.tvShoppingCartItemCount.text = "${product.orderQuantity}"
        design.tvShoppingCartItemTotalPrice.text = "${formatter.format(product.price * product.orderQuantity)} TL"

        design.ivShoppingCartItemDelete.setOnClickListener {
            Snackbar.make(it, "Do you want to delete ${product.name} ?", Snackbar.LENGTH_SHORT)
                .setAction("Yes") {
                    viewModel.deleteProductFromCart(product.cartId, product.userName)
                }.show()
        }

        design.btShoppingCartDecrease.setOnClickListener {
            if (product.orderQuantity > 1) {
                val newQuantity = product.orderQuantity - 1
                viewModel.updateProductQuantity(product, newQuantity)
            }
        }

        design.btShoppingCartIncrease.setOnClickListener {
            val newQuantity = product.orderQuantity + 1
            viewModel.updateProductQuantity(product, newQuantity)
        }

        Glide.with(context).load("http://kasimadalan.pe.hu/urunler/resimler/${product.image}")
            .override(64, 64).into(design.ivShoppingCartItemImage)
    }

    fun updateProducts(newList: List<ShoppingCartProduct>) {
        (products as MutableList).clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }
}