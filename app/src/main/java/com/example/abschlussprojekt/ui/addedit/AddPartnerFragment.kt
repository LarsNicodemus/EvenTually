package com.example.abschlussprojekt.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.FragmentAddPartnerBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalPartnerProfile
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.utils.ChipUtils
import com.example.abschlussprojekt.utils.SpinnerUtils
import com.example.abschlussprojekt.utils.SpinnerUtils.setSpinnerToValue
import com.example.abschlussprojekt.utils.SpinnerUtils.setupSpinner
import com.example.abschlussprojekt.utils.TimeUtils
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ListenerRegistration

class AddPartnerFragment : Fragment() {
    private lateinit var binding: FragmentAddPartnerBinding
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
        binding = FragmentAddPartnerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editPartnerUI()
        snapShotListener()
    }

    /**
     * Setzt einen Snapshot-Listener, um Änderungen im Partnerprofil zu überwachen.
     */
    private fun snapShotListener() {
        partnerProfileListenerRegistration =
            userViewModel.partnerRef?.addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val partnerProfile = snapshot.toObject(GlobalPartnerProfile::class.java)
                    binding.tietPartnerName.setText(partnerProfile?.name)
                    binding.tietPartnerAge.setText(partnerProfile?.birthDate.toString())
                    setSpinnerToValue(binding.genderSpinnerPartner, partnerProfile?.gender)
                    if (partnerProfile?.imageString != "") {
                        profileLink = partnerProfile?.imageString!!
                        binding.ivPartnerProfile.load(profileLink)
                    }
                    binding.cgLikes.checkedChipIds.forEach {
                        val chip = binding.cgLikes.findViewById<Chip>(it)
                        if (chip.text in partnerProfile.likes)
                            chip.isChecked = true
                    }
                    binding.cgFood.checkedChipIds.forEach {
                        val chip = binding.cgFood.findViewById<Chip>(it)
                        if (chip.text in partnerProfile.foodPreferences)
                            chip.isChecked = true
                    }
                    binding.cgActivity.checkedChipIds.forEach {
                        val chip = binding.cgActivity.findViewById<Chip>(it)
                        if (chip.text in partnerProfile.activityPreferences)
                            chip.isChecked = true
                    }
                }
            }
    }

    /**
     * Initialisiert die UI-Elemente und deren Listener.
     */
    private fun editPartnerUI() {
        setupSpinner(
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

        binding.btPartnerSave.setOnClickListener {
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
            findNavController().navigate(AddPartnerFragmentDirections.actionAddPartnerFragmentToAddEventFragment())
        }
        binding.btPartnerBack.setOnClickListener {
            findNavController().navigate(AddPartnerFragmentDirections.actionAddPartnerFragmentToAddProfileFragment())
        }
    }
}