package com.example.abschlussprojekt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.adapter.SettingsAdapter
import com.example.abschlussprojekt.databinding.FragmentSettingsBinding
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.example.abschlussprojekt.ui.viewmodels.AuthViewModel
import com.example.abschlussprojekt.utils.OnSettingClickListener

class SettingsFragment : Fragment(), OnSettingClickListener {
    private lateinit var binding: FragmentSettingsBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by activityViewModels()
    private lateinit var adapter: SettingsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.rvSettings

        uiViewModel.settings.observe(viewLifecycleOwner) { settings ->
            adapter = SettingsAdapter(settings.toMutableList(), uiViewModel, this)
            recyclerView.adapter = adapter
            adapter.updateSettings(settings)

        }
        binding.button.setOnClickListener {
            authViewModel.firebaseLogout()
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
        }
    }

    /**
     * Navigiert zu verschiedenen Fragmenten basierend auf der übergebenen `fragmentId`.
     * @param fragmentId Die ID des Fragments, zu dem navigiert werden soll.
     */
    override fun onSettingClicked(fragmentId: Int) {
        val navController = findNavController()
        when (fragmentId) {
            0 -> navController.navigate(R.id.action_settingsFragment_to_profileFragment)
            1 -> navController.navigate(R.id.action_settingsFragment_to_partnerFragment)
            2 -> navController.navigate(R.id.action_settingsFragment_to_eventFragment)
            3 -> navController.navigate(R.id.action_settingsFragment_to_reminderFragment)
            4 -> navController.navigate(R.id.action_settingsFragment_to_recsFragment)
            else -> navController.navigate(R.id.action_settingsFragment_self)
        }
    }


}