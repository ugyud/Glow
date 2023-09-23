package com.dozkan.glow.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dozkan.glow.R
import com.dozkan.glow.common.gone
import com.dozkan.glow.common.viewBinding
import com.dozkan.glow.common.visible
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel: SearchViewModel by viewModels()

    private val searchProductsAdapter by lazy { SearchProductAdapter(::onProductClick, ::onFavoriteClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearchProducts.adapter = searchProductsAdapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchProduct(newText)
                    return false
                }
            })
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Loading -> {
                    progressBar.visible()
                }

                is SearchState.Success -> {
                    searchProductsAdapter.submitList(state.products)
                    progressBar.gone()
                }

                is SearchState.EmptyScreen -> {
                    progressBar.gone()
                }

                is SearchState.Error -> TODO()
            }
        }
    }

    private fun onProductClick(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    private fun onFavoriteClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }
}