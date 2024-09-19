package com.example.abschlussprojekt.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.FragmentEditPartnerBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalPartnerProfile
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.utils.ChipUtils
import com.example.abschlussprojekt.utils.SpinnerUtils
import com.example.abschlussprojekt.utils.TimeUtils
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ListenerRegistration


class EditPartnerFragment : Fragment() {
    private lateinit var binding: FragmentEditPartnerBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var selectedGender = ""

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                userViewModel.firebaseUploadPartnerImage(uri)
            }
        }
    private var profileLink = ""
    private var partnerProfileListenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPartnerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snapShotListener()
        editPartnerUI()
        uiViewModel.setUpToolbar(this, view, "Partner Details bearbeiten")
    }

    /**
     * Setzt einen Snapshot-Listener, um Änderungen im Partner Profil zu überwachen.
     */
    private fun snapShotListener() {
        partnerProfileListenerRegistration =
            userViewModel.partnerRef?.addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val partnerProfile = snapshot.toObject(GlobalPartnerProfile::class.java)
                    binding.tietPartnerName.setText(partnerProfile?.name)
                    binding.tietPartnerAge.setText(partnerProfile?.birthDate.toString())
                    SpinnerUtils.setSpinnerToValue(
                        binding.genderSpinnerPartner,
                        partnerProfile?.gender
                    )
                    if (partnerProfile?.imageString != "") {
                        profileLink = partnerProfile?.imageString!!
                        binding.ivPartnerProfile.load(profileLink)
                    }

                    binding.cgLikes.forEach {
                        it as Chip
                        if (it.text in partnerProfile.likes) {
                            it.isChecked = true
                        }
                    }

                    binding.cgFood.forEach {
                        it as Chip
                        if (it.text in partnerProfile.foodPreferences) {
                            it.isChecked = true
                        }
                    }
                    binding.cgActivity.forEach {
                        it as Chip
                        if (it.text in partnerProfile.activityPreferences) {
                            it.isChecked = true
                        }
                    }
                }
            }
    }

    /**
     * Initialisiert die UI-Elemente und deren Listener.
     */
    private fun editPartnerUI() {
        SpinnerUtils.setupSpinner(
            requireContext(),
            binding.genderSpinnerPartner,
            R.array.spinner_items_gender,
            R.layout.item_spinner,
            R.layout.item_spinner_dropdown
        )
        SpinnerUtils.setupSpinnerListener(binding.genderSpinnerPartner,
            onItemSelected = { selectedGender = it },
            onNothingSelected = { /* Do nothing */ })
        TimeUtils.setupDateInputListener(binding.tietPartnerAge)
        binding.ivPartnerProfile.setOnClickListener {
            getContent.launch("image/*")
        }
        val chipGroupLikes = binding.cgLikes
        val chipGroupFoodPref = binding.cgFood
        val chipGroupActivityPref = binding.cgActivity
        ChipUtils.setupChipGroups(
            binding,
            requireContext(),
            chipGroupLikes,
            chipGroupFoodPref,
            chipGroupActivityPref
        )

        binding.btProfileSave.setOnClickListener {
            val name = binding.tietPartnerName.text.toString()
            val birthdate = binding.tietPartnerAge.text.toString()
            val age = TimeUtils.calculateAgeFromDateString(birthdate)
            val gender = selectedGender
            val likes = ChipUtils.getSelectedChips(chipGroupLikes)
            val foodPreferences = ChipUtils.getSelectedChips(chipGroupFoodPref)
            val activityPreferences = ChipUtils.getSelectedChips(chipGroupActivityPref)
            userViewModel.firebaseUpdatePartnerProfile(
                GlobalPartnerProfile(
                    name,
                    birthdate,
                    age,
                    gender,
                    profileLink,
                    likes,
                    foodPreferences,
                    activityPreferences
                )
            )
            findNavController().navigate(EditPartnerFragmentDirections.actionEditPartnerFragmentToSettingsPartnerFragment())
        }
    }
}