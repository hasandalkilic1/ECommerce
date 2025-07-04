package com.example.ecommerce.presentation.screen

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.MainScreenBinding
import com.example.ecommerce.presentation.adapter.MainScreenProductsAdapter
import com.example.ecommerce.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : Fragment() {
    private lateinit var binding: MainScreenBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainScreenBinding.inflate(inflater, container, false)

        viewModel.products.observe(viewLifecycleOwner) {
            binding.rvMainScreenProducts.adapter =
                MainScreenProductsAdapter(requireContext(), it, viewModel)
        }
        binding.rvMainScreenProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.svMainScreen.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                return true
            }
        })
        binding.floatingActionButton.setOnClickListener {
            it.findNavController().navigate(R.id.toShoppingCartScreen)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: MainViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        binding.svMainScreen.setQuery("", false)
    }
}