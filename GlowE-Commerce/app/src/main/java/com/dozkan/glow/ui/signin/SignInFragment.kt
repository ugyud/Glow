package com.dozkan.glow.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dozkan.glow.R
import com.dozkan.glow.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()

        viewModel.currentUser?.let {
            findNavController().navigate(R.id.action_signInToHome)
        }

        with(binding) {
            signInButton.setOnClickListener {
                val email = emailText.text.toString()
                val password = passwordText.text.toString()

                if(email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show()
                }
            }
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInToSignUp)
            }
        }

    }

    private fun observeData() = with(binding) {

        viewModel.signInState.observe(viewLifecycleOwner) { state ->

            when (state) {
                is SignInState.Data -> {
                    findNavController().navigate(R.id.action_signInToHome)
                }

                is SignInState.Error -> {
                    Snackbar.make(requireView(), state.throwable.message.orEmpty(), 1000).show()
                }
            }
        }
    }
}