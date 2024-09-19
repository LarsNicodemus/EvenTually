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
import com.example.abschlussprojekt.databinding.FragmentEditProfileBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalProfile
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.utils.SpinnerUtils
import com.example.abschlussprojekt.utils.TimeUtils
import com.google.firebase.firestore.ListenerRegistration


class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val uiViewModel: UIViewModel by activityViewModels()
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
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editUINew()
        snapShotListener()
        uiViewModel.setUpToolbar(this, view, "Edit Profile")
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
                    binding.tietProfileAge.setText(profile?.birthDate.toString())
                    SpinnerUtils.setSpinnerToValue(binding.genderSpinner, profile?.gender)
                    if (profile?.imageString != "") {
                        profileLink = profile?.imageString!!
                        binding.ivProfile.load(profileLink)
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
            userViewModel.firebaseUpdatePartialProfile("name", name)
            userViewModel.firebaseUpdatePartialProfile("birthDate", birthdate)
            userViewModel.firebaseUpdatePartialProfile("gender", gender)
            userViewModel.firebaseUpdatePartialProfile("age", age)
            userViewModel.firebaseUpdatePartialProfile("imageString", profileLink)
            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToSettingsProfileFragment())
        }
    }

    /**
     * Setzt den Spinner für die Geschlechtsauswahl.
     */
    private fun setupSpinner() {
        SpinnerUtils.setupSpinner(
            requireContext(),
            binding.genderSpinner,
            R.array.spinner_items_gender,
            R.layout.item_spinner,
            R.layout.item_spinner_dropdown
        )

        SpinnerUtils.setupSpinnerListener(
            binding.genderSpinner,
            onItemSelected = { selectedGender = it },
            onNothingSelected = { }
        )
    }
}