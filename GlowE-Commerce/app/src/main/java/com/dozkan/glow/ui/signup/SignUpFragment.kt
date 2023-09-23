package com.dozkan.glow.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dozkan.glow.R
import com.dozkan.glow.common.gone
import com.dozkan.glow.common.viewBinding
import com.dozkan.glow.common.visible
import com.dozkan.glow.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                viewModel.checkInfo(email, password)
            }
        }

        initObservers()
    }

    private fun initObservers() = with(binding) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignUpState.Loading -> progressBar.visible()

                SignUpState.GoToHome -> findNavController().navigate(R.id.action_signUpToHome)

                is SignUpState.Error -> {
                    progressBar.gone()
                }
            }
        }
    }
}