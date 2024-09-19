package com.example.abschlussprojekt.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.FragmentSetupRegisterBinding
import com.example.abschlussprojekt.ui.viewmodels.AuthViewModel


class SetupRegisterFragment : Fragment() {
    private lateinit var binding: FragmentSetupRegisterBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEmailSignIn.setOnClickListener {
            val email = binding.tietProfileName.text.toString()
            val password = binding.tietPassword.text.toString()
            if (email != "" && password != "") {
                authViewModel.firebaseRegister(email, password)
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.setupLoginFragment)
        }

        authViewModel.currentUser.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.addProfileFragment)
            }
        }
    }

}