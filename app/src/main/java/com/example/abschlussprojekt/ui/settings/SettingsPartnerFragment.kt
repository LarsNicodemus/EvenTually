package com.example.abschlussprojekt.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.databinding.FragmentSettingsPartnerBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalPartnerProfile
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.google.firebase.firestore.ListenerRegistration


class SettingsPartnerFragment : Fragment() {
    private lateinit var binding: FragmentSettingsPartnerBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var partnerProfileListenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsPartnerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snapShotListener()
        binding.btProfileEdit.setOnClickListener {
            findNavController().navigate(SettingsPartnerFragmentDirections.actionPartnerFragmentToEditPartnerFragment())
        }
        uiViewModel.setUpToolbar(this, view, "Partner Profile")
    }

    //TODO
    private fun snapShotListener() {
        partnerProfileListenerRegistration =
            userViewModel.partnerRef?.addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val partnerProfile = snapshot.toObject(GlobalPartnerProfile::class.java)
                    binding.apply {
                        tvNameInput.text = partnerProfile?.name
                        tvAgeInput.text = partnerProfile?.age.toString()
                        tvGenderInput.text = partnerProfile?.gender
                        tvLikesInput.text = partnerProfile?.likes?.joinToString(separator = ", ")
                        tvFoodPreferencesInput.text =
                            partnerProfile?.foodPreferences?.joinToString(separator = ", ")
                        tvActivityPreferencesInput.text =
                            partnerProfile?.activityPreferences?.joinToString(separator = ", ")
                        ivProfile.load(partnerProfile?.imageString)
                    }
                }
            }
    }
}