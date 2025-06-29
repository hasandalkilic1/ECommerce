package com.example.ecommerce.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.data.model.ShoppingCartProduct
import com.example.ecommerce.databinding.ShoppingCartScreenBinding
import com.example.ecommerce.presentation.adapter.ShoppingCartAdapter
import com.example.ecommerce.presentation.viewmodel.ShoppingCartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartScreen : Fragment() {
    private lateinit var binding: ShoppingCartScreenBinding
    private lateinit var viewModel: ShoppingCartViewModel
    private lateinit var adapter: ShoppingCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShoppingCartScreenBinding.inflate(inflater, container, false)

        viewModel.cartProducts.observe(viewLifecycleOwner) {
            adapter.updateProducts(it)
        }
        adapter = ShoppingCartAdapter(requireContext(), mutableListOf(), viewModel)
        binding.rvCartProducts.adapter = adapter
        binding.rvCartProducts.layoutManager = LinearLayoutManager(requireContext())

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvCardTotalPrice.text = "Toplam: $it TL"
        }

        return binding.root
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