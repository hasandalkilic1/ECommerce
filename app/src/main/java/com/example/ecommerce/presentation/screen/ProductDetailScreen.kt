package com.example.ecommerce.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.databinding.ProductDetailScreenBinding
import com.example.ecommerce.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ProductDetailScreen : Fragment() {
    private lateinit var binding: ProductDetailScreenBinding
    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProductDetailScreenBinding.inflate(inflater, container, false)

        val bundle: ProductDetailScreenArgs by navArgs()
        val product = bundle.Product

        val formatter = NumberFormat.getInstance(Locale("tr", "TR"))

        Glide.with(requireContext())
            .load("http://kasimadalan.pe.hu/urunler/resimler/${product.image}")
            .into(binding.ivProductImage)

        binding.tvProductName.text = product.name
        binding.tvProductBrand.text = product.brand
        binding.tvProductPrice.text = "${formatter.format(product.price)} TL"
        binding.tvProductCategory.text = product.category

        viewModel.calculateTotalPrice(product.price, 1)


        collectTotalPrice(formatter)

        binding.btAddToCart.setOnClickListener {
            viewModel.addProductToCart(
                ShoppingCartProduct(
                    1,
                    product.name,
                    product.image,
                    product.category,
                    product.price,
                    product.brand,
                    binding.tvTotalUnit.text.toString().toInt(),
                    "Hasan"
                )
            )
        }

        binding.btIncrease.setOnClickListener {
            val currentUnit = binding.tvTotalUnit.text.toString().toInt()
            binding.tvTotalUnit.text = (currentUnit + 1).toString()
            viewModel.calculateTotalPrice(
                product.price,
                binding.tvTotalUnit.text.toString().toInt()
            )
        }

        binding.btDecrease.setOnClickListener {
            val currentUnit = binding.tvTotalUnit.text.toString().toInt()
            if (currentUnit > 1) {
                binding.tvTotalUnit.text = (currentUnit - 1).toString()
            }
            viewModel.calculateTotalPrice(
                product.price,
                binding.tvTotalUnit.text.toString().toInt()
            )
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private fun collectTotalPrice(formatter: NumberFormat) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect { total ->
                    binding.tvTotalPrice.text = "Toplam: ${formatter.format(total)} TL"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProductDetailViewModel by viewModels()
        viewModel = tempViewModel
    }
}