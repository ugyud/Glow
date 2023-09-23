package com.dozkan.glow.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dozkan.glow.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel by viewModels<SplashFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            viewModel.checkUserLogin()
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is SplashState.GoToHome -> {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                }

                SplashState.GoToSignIn -> {
                    findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                }
            }
        }
    }
}