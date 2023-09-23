package com.dozkan.glow.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dozkan.glow.common.gone
import com.dozkan.glow.common.visible
import com.dozkan.glow.data.model.response.ProductUI
import com.dozkan.glow.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), AllProductsAdapter.ProductListener, SaleProductsAdapter.ProductListener {

    private lateinit var binding : FragmentHomeBinding
    private val movieAdapter by lazy {AllProductsAdapter(this)}
    private val saleMovieAdapter by lazy { SaleProductsAdapter(this) }
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSecond.adapter = movieAdapter
        viewModel.getProducts()

        binding.rvFirst.adapter = saleMovieAdapter
        viewModel.getSaleProducts()
        observeData()

        logOut()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onProductClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeToDetail(id)
        findNavController().navigate(action)
    }

    override fun onFavoriteButtonClick(product: ProductUI) {
        viewModel.addToFavorites(product)
    }

    private fun observeData() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is HomeState.Data -> {
                    movieAdapter.submitList(state.products)
                    binding.progressBar.gone()
                }
                is HomeState.SaleData -> {
                    saleMovieAdapter.submitList(state.products)
                    binding.progressBar.gone()
                }
                is HomeState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                    binding.progressBar.gone()
                }
                is HomeState.Loading -> {
                    binding.progressBar.visible()
                }
            }
        }
    }

    override fun onSaleClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeToDetail(id)
        findNavController().navigate(action)
    }

    private fun logOut() {
        val showPopUp = PopupMenu(
            context,
            binding.ivProfilIcon
        )

        showPopUp.menu.add(Menu.NONE, 0, 0, "Log Out")

        showPopUp.setOnMenuItemClickListener { menuItem ->
            val id = menuItem.itemId
            if (id == 0) {
                FirebaseAuth.getInstance().signOut()
                val action = HomeFragmentDirections.actionHomeToSignIn()
                findNavController().navigate(action)
            }
            false
        }

        binding.ivProfilIcon.setOnClickListener {
            showPopUp.show()
        }
    }
}