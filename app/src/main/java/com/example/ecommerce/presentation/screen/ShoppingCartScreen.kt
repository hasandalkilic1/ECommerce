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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.databinding.ShoppingCartScreenBinding
import com.example.ecommerce.presentation.adapter.ShoppingCartAdapter
import com.example.ecommerce.presentation.viewmodel.ShoppingCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ShoppingCartScreen : Fragment() {
    private lateinit var binding: ShoppingCartScreenBinding
    private lateinit var viewModel: ShoppingCartViewModel
    private lateinit var adapter: ShoppingCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ShoppingCartScreenBinding.inflate(inflater, container, false)

        val formatter = NumberFormat.getInstance(Locale("tr", "TR"))

        adapter = ShoppingCartAdapter(requireContext(), mutableListOf(), viewModel)
        binding.rvCartProducts.adapter = adapter
        binding.rvCartProducts.layoutManager = LinearLayoutManager(requireContext())

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        collectCartProducts()
        collectTotalPrice(formatter)

        return binding.root
    }

    private fun collectCartProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collect { products ->
                    adapter.updateProducts(products)
                }
            }
        }
    }

    private fun collectTotalPrice(formatter: NumberFormat) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect {
                    binding.tvCardTotalPrice.text = "Toplam: ${formatter.format(it)} TL"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ShoppingCartViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartProducts()
    }
}