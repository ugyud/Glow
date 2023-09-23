package com.dozkan.glow.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dozkan.glow.common.gone
import com.dozkan.glow.common.visible
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(), FavoritesAdapter.FavoriteProductListener {

    private lateinit var binding : FragmentFavoritesBinding
    private val favAdapter by lazy { FavoritesAdapter(this) }
    private val viewModel by viewModels<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteProducts()
        binding.rvFavorite.adapter = favAdapter
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.favState.observe(viewLifecycleOwner) {state ->
            when(state) {
                is FavState.Data -> {
                    rvFavorite.visible()
                    favAdapter.submitList(state.products)
                }
                is FavState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                    rvFavorite.gone()
                }
            }
        }
    }

    override fun onFavButtonClick(product: ProductUI) {
        viewModel.deleteFromFavorites(product)
    }

}