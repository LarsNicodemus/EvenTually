package com.example.abschlussprojekt.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.databinding.FragmentSettingsEventBinding
import com.example.abschlussprojekt.datamodel.localmodels.DateCalculator
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalEventProfile
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.google.firebase.firestore.ListenerRegistration


class SettingsEventFragment : Fragment() {
    private lateinit var binding: FragmentSettingsEventBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private var eventListenerRegistration: ListenerRegistration? = null
    private lateinit var dateCalculator: DateCalculator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsEventBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateCalculator = DateCalculator()
        snapShotListener()
        binding.btEventEdit.setOnClickListener {
            findNavController().navigate(SettingsEventFragmentDirections.actionEventFragmentToEditEventFragment())
        }
        uiViewModel.setUpToolbar(this, view, "Euer Jahrestag")
    }

    //TODO
    private fun snapShotListener() {
        eventListenerRegistration = userViewModel.eventRef?.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                val eventProfile = snapshot.toObject(GlobalEventProfile::class.java)
                val eventDateString = eventProfile?.startDate
                if (eventDateString != null) {
                    dateCalculator.calculateAndUpdate(eventDateString) { countdown, timeSinceEvent ->
                        updateCountdownUI(countdown)
                        updateTimeSinceEventUI(timeSinceEvent)
                    }
                }
                binding.tvBeginningInput.text = eventProfile?.startDate
                binding.ivEventAdd.load(eventProfile?.imageString)
            }
        }
    }

    private fun updateCountdownUI(countdown: String) {
        binding.tvCountdownTimeInput.text = countdown
    }

    private fun updateTimeSinceEventUI(timeSinceEvent: String) {
        val components = timeSinceEvent.split(", ")
        for (component in components) {
            val parts = component.trim().split(" ")
            when {
                parts[1].startsWith("Jahr") -> binding.tvYears.text = "${parts[0]} Jahre"
                parts[1].startsWith("Monat") -> binding.tvMonths.text = "${parts[0]} Monate"
                parts[1].startsWith("Tag") -> binding.tvDays.text = "${parts[0]} Tage"
                parts[1].startsWith("Stunde") -> binding.tvHours.text = "${parts[0]} Stunden"
                parts[1].startsWith("Minute") -> binding.tvMinutes.text = "${parts[0]} Minuten"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::dateCalculator.isInitialized) {
            dateCalculator.stopUpdates()
        }
        eventListenerRegistration?.remove()

    }

}