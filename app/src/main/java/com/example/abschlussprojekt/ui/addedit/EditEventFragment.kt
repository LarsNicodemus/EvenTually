package com.example.abschlussprojekt.ui.addedit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.databinding.FragmentEditEventBinding
import com.example.abschlussprojekt.datamodel.localmodels.DateCalculator
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalEventProfile
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.example.abschlussprojekt.ui.viewmodels.NotificationViewModel
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.utils.TimeUtils
import com.google.firebase.firestore.ListenerRegistration

class EditEventFragment : Fragment() {
    private lateinit var binding: FragmentEditEventBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val notificationViewModel: NotificationViewModel by activityViewModels()
    private lateinit var dateCalculator: DateCalculator
    private var oldDate = ""
    private var newDate = ""

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                userViewModel.firebaseUploadEventImage(uri)
            }
        }
    private var eventLink = ""
    private var eventListenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateCalculator = DateCalculator()
        editUI()
        snapShotListener()
        uiViewModel.setUpToolbar(this, view, "Edit Event Profile")
    }

    /**
     * Setzt einen Snapshot-Listener, um Änderungen im Event zu überwachen.
     */
    private fun snapShotListener() {
        eventListenerRegistration = userViewModel.eventRef?.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                val eventProfile = snapshot.toObject(GlobalEventProfile::class.java)
                binding.tietEventBeginning.setText(eventProfile?.startDate)
                oldDate = eventProfile?.startDate.toString()
                if (eventProfile?.imageString != "" && eventProfile?.imageString != null) {
                    eventLink = eventProfile.imageString
                    binding.ivEventAdd.load(eventLink)
                    notificationViewModel.updateEventDateString(eventProfile.startDate)
                }
            }
        }
    }

    /**
     * Initialisiert die UI-Elemente und deren Listener.
     */
    private fun editUI() {
        TimeUtils.setupDateInputListener(binding.tietEventBeginning)
        binding.cvEventAdd.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.btEventSave.setOnClickListener {
            val beginningDate = binding.tietEventBeginning.text.toString()
            newDate = beginningDate
            Log.d("EditEventFragment", "oldDate: $oldDate newDate: $newDate")
            if (oldDate != newDate) {
                notificationViewModel.cancelAllScheduledNotifications()
            }
            userViewModel.firebaseUpdatePartialEventProfile("startDate", beginningDate)
            val nextAnniversaryDate = dateCalculator.getNextAnniversaryDate(beginningDate)
            if (nextAnniversaryDate != null) {
                userViewModel.firebaseUpdatePartialEventProfile(
                    "nextAnniversary",
                    nextAnniversaryDate
                )
            }
            userViewModel.firebaseUpdatePartialEventProfile("imageString", eventLink)
            findNavController().navigate(EditEventFragmentDirections.actionEditEventFragmentToSettingsEventFragment())
        }
    }

}