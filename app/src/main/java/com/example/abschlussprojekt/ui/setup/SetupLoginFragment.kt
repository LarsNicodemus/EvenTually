package com.example.abschlussprojekt.ui.setup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojekt.databinding.FragmentSetupLoginBinding
import com.example.abschlussprojekt.ui.viewmodels.AuthViewModel


class SetupLoginFragment : Fragment() {
    private lateinit var binding: FragmentSetupLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            "Login",
            "${authViewModel.currentUser.value?.uid} ${authViewModel.currentUser.value?.email}"
        )
        observeCurrentUser()
        binding.btnEmailSignIn.setOnClickListener {
            val email = binding.tietProfileName.text.toString()
            val password = binding.tietPassword.text.toString()
            if (email != "" && password != "") {
                authViewModel.firebaseLogin(email, password)
                Log.d(
                    "LoginButton",
                    "${authViewModel.currentUser.value?.uid} ${authViewModel.currentUser.value?.email}"
                )
            }
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(SetupLoginFragmentDirections.actionSetupLoginFragmentToSetupRegisterFragment2())
        }
        binding.btnReset.setOnClickListener {
            val email = binding.tietProfileName.text.toString()
            authViewModel.firebaseResetPassword(email)
            Toast.makeText(requireContext(), "Rücksetzungslink gesendet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeCurrentUser() {
        authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                findNavController().navigate(SetupLoginFragmentDirections.actionSetupLoginFragmentToHomeFragment())
                Log.d("LoginObserver", "${user.uid} ${user.email}")
            }
        }
    }

}