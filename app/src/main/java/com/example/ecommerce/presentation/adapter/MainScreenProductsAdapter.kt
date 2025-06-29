package com.example.ecommerce.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.databinding.MainScreenProductItemBinding
import com.example.ecommerce.presentation.screen.MainScreenDirections
import com.example.ecommerce.presentation.viewmodel.MainViewModel

class MainScreenProductsAdapter(
    private val context: Context,
    private val productList: List<Product>,
    private val viewModel: MainViewModel
) :
    RecyclerView.Adapter<MainScreenProductsAdapter.MainScreenProductViewHolder>() {
    inner class MainScreenProductViewHolder(val binding: MainScreenProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainScreenProductViewHolder {
        return MainScreenProductViewHolder(
            MainScreenProductItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MainScreenProductViewHolder, position: Int) {
        val product = productList[position]
        val design = holder.binding

        design.tvMainScreenProductItemBrand.text = product.marka
        design.tvMainScreenProductItemName.text = product.ad
        design.tvMainScreenProductItemPrice.text = "${product.fiyat} TL"

        design.cvMainScreenProductItem.setOnClickListener {
            it.findNavController().navigate(MainScreenDirections.toProductDetailScreen(product))
        }

        design.ivMainScreenProductItemAddToCart.setOnClickListener {
            viewModel.addProductToCart(product)
        }

        Glide.with(context).load("http://kasimadalan.pe.hu/urunler/resimler/${product.resim}")
            .override(128, 128).into(design.ivMainScreenProductItemImage)

    }
}

