package com.example.abschlussprojekt.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.databinding.FragmentSettingsProfileBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalProfile
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.google.firebase.firestore.ListenerRegistration


class SettingsProfileFragment : Fragment() {
    private lateinit var binding: FragmentSettingsProfileBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var profileListenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snapShotListener()
        binding.btProfileEdit.setOnClickListener {
            findNavController().navigate(SettingsProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }
        uiViewModel.setUpToolbar(this, view, "Profile")
    }

    //TODO
    private fun snapShotListener() {
        profileListenerRegistration =
            userViewModel.profileRef?.addSnapshotListener { snapShot, error ->
                if (error == null && snapShot != null) {
                    val profile = snapShot.toObject(GlobalProfile::class.java)
                    binding.apply {
                        tvNameInput.text = profile?.name
                        tvAgeInput.text = profile?.age.toString()
                        tvGenderInput.text = profile?.gender
                        ivProfile.load(profile?.imageString)

                    }
                }
            }
    }

}