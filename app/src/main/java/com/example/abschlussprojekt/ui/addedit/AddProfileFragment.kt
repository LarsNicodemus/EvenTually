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
import com.example.abschlussprojekt.databinding.FragmentAddProfileBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalProfile
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.utils.SpinnerUtils
import com.example.abschlussprojekt.utils.SpinnerUtils.setupSpinner
import com.example.abschlussprojekt.utils.SpinnerUtils.setupSpinnerListener
import com.example.abschlussprojekt.utils.TimeUtils
import com.google.firebase.firestore.ListenerRegistration


class AddProfileFragment : Fragment() {
    private lateinit var binding: FragmentAddProfileBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private var selectedGender = ""

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                userViewModel.firebaseUploadProfileImage(uri)
            }
        }
    private var profileLink = ""
    private var profileListenerRegistration: ListenerRegistration? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editUINew()
        snapShotListener()
    }

    /**
     * Setzt einen Snapshot-Listener, um Änderungen im Profil zu überwachen.
     */
    private fun snapShotListener() {
        profileListenerRegistration =
            userViewModel.profileRef?.addSnapshotListener { snapShot, error ->
                if (error == null && snapShot != null) {
                    val profile = snapShot.toObject(GlobalProfile::class.java)
                    binding.tietProfileName.setText(profile?.name)
                    binding.tietProfileAge.setText(profile?.birthDate)
                    SpinnerUtils.setSpinnerToValue(binding.genderSpinner, profile?.gender)
                    if (profile?.imageString != "") {
                        profileLink = profile?.imageString!!
                        binding.ivProfilePic.load(profileLink)
                    }
                }
            }
    }

    /**
     * Initialisiert die UI-Elemente und deren Listener.
     */
    private fun editUINew() {

        setupSpinner()
        TimeUtils.setupDateInputListener(binding.tietProfileAge)
        binding.cvProfilePic.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.btProfileSave.setOnClickListener {
            val name = binding.tietProfileName.text.toString()
            val birthdate = binding.tietProfileAge.text.toString()
            val age = TimeUtils.calculateAgeFromDateString(birthdate)
            val gender = selectedGender
            userViewModel.firebaseUpdateProfile(
                GlobalProfile(
                    name,
                    birthdate,
                    age,
                    gender,
                    profileLink
                )
            )
            findNavController().navigate(AddProfileFragmentDirections.actionAddProfileFragmentToAddPartnerFragment())
        }
        binding.btProfileBack.setOnClickListener {
            findNavController().navigate(AddProfileFragmentDirections.actionAddProfileFragmentToSetupRegisterFragment())
        }
    }

    /**
     * Setzt den Spinner für die Geschlechtsauswahl.
     */
    private fun setupSpinner() {
        setupSpinner(
            requireContext(),
            binding.genderSpinner,
            R.array.spinner_items_gender,
            R.layout.item_spinner,
            R.layout.item_spinner_dropdown
        )
        setupSpinnerListener(binding.genderSpinner,
            onItemSelected = { selectedGender = it },
            onNothingSelected = { /* Do nothing */ })
    }

}