package com.example.ecommerce.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.databinding.ProductDetailScreenBinding
import com.example.ecommerce.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailScreen : Fragment() {
    private lateinit var binding: ProductDetailScreenBinding
    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductDetailScreenBinding.inflate(inflater, container, false)

        val bundle: ProductDetailScreenArgs by navArgs()
        val product = bundle.Product

        Glide.with(requireContext())
            .load("http://kasimadalan.pe.hu/urunler/resimler/${product.resim}")
            .into(binding.ivProductImage)
        binding.tvProductName.text = product.ad
        binding.tvProductBrand.text = product.marka
        binding.tvProductPrice.text = "${product.fiyat} TL"
        binding.tvProductCategory.text = product.kategori

        viewModel.calculateTotalPrice(product.fiyat, 1)

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text = "Toplam: $it TL"
        }

        binding.btAddToCart.setOnClickListener {
            viewModel.addProductToCart(
                ShoppingCartProduct(
                    1,
                    product.ad,
                    product.resim,
                    product.kategori,
                    product.fiyat,
                    product.marka,
                    binding.tvTotalUnit.text.toString().toInt(),
                    "Hasan"
                )
            )
        }

        binding.btIncrease.setOnClickListener {
            val currentUnit = binding.tvTotalUnit.text.toString().toInt()
            binding.tvTotalUnit.text = (currentUnit + 1).toString()
            viewModel.calculateTotalPrice(
                product.fiyat,
                binding.tvTotalUnit.text.toString().toInt()
            )
        }

        binding.btDecrease.setOnClickListener {
            val currentUnit = binding.tvTotalUnit.text.toString().toInt()
            if (currentUnit > 1) {
                binding.tvTotalUnit.text = (currentUnit - 1).toString()
            }
            viewModel.calculateTotalPrice(
                product.fiyat,
                binding.tvTotalUnit.text.toString().toInt()
            )
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProductDetailViewModel by viewModels()
        viewModel = tempViewModel
    }
}