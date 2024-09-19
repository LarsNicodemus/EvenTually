package com.example.abschlussprojekt.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojekt.databinding.FragmentSettingsReminderBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalReminder
import com.example.abschlussprojekt.ui.viewmodels.NotificationViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.ListenerRegistration


class SettingsReminderFragment : Fragment() {
    private lateinit var binding: FragmentSettingsReminderBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val notificationViewModel: NotificationViewModel by activityViewModels()
    private var reminderListenerRegistration: ListenerRegistration? = null
    private val reminders = mutableMapOf(
        "monthlyReminder" to false,
        "yearlyReminder" to false,
        "sevenDaysReminder" to false,
        "fiveDaysReminder" to false,
        "threeDaysReminder" to false,
        "oneDayReminder" to false,
        "anniversaryReminder" to false
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsReminderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snapShotListener()
        uiViewModel.setUpToolbar(this, view, "Erinnerungseinstellungen")
        setupSwitches()
        setupListeners()
        binding.btReminderSave.setOnClickListener {

            notificationViewModel.scheduleSyncWorker(requireContext())
            findNavController().navigateUp()

        }
    }


    private fun timePicker(hour: Int, minute: Int) {

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(hour)
            .setMinute(minute)
            .setTitleText("Wähle die Zeit")
            .build()


        val showTimePickerButton = binding.showTimePickerButton
        showTimePickerButton.setOnClickListener {
            picker.show(parentFragmentManager, "MATERIAL_TIME_PICKER")
        }


        picker.addOnPositiveButtonClickListener {
            val selectedHour = picker.hour
            val selectedMinute = picker.minute
            notificationViewModel.firebaseUpdatePartialReminder("reminderTimeHour", selectedHour)
            notificationViewModel.firebaseUpdatePartialReminder(
                "reminderTimeMinute",
                selectedMinute
            )
        }
    }

    private fun setupSwitches() {
        binding.apply {
            swMonthly.isChecked = reminders["monthlyReminder"] ?: false
            swYearly.isChecked = reminders["yearlyReminder"] ?: false
            val yearlyEnabled = reminders["yearlyReminder"] ?: false
            arrayOf(sw7days, sw5days, sw3days, sw1day, swAnniversary).forEach {
                it.isEnabled = yearlyEnabled
            }
            sw7days.isChecked = reminders["sevenDaysReminder"] ?: false
            sw5days.isChecked = reminders["fiveDaysReminder"] ?: false
            sw3days.isChecked = reminders["threeDaysReminder"] ?: false
            sw1day.isChecked = reminders["oneDayReminder"] ?: false
            swAnniversary.isChecked = reminders["anniversaryReminder"] ?: false
        }
    }

    private fun setupListeners() {
        binding.apply {
            swMonthly.setOnCheckedChangeListener { _, isChecked ->
                updateReminder(
                    "monthlyReminder",
                    isChecked
                )
            }
            swYearly.setOnCheckedChangeListener { _, isChecked ->
                updateReminder("yearlyReminder", isChecked)
                updateYearlyDependents(isChecked)
            }
            sw7days.setOnCheckedChangeListener { _, isChecked ->
                updateReminder(
                    "sevenDaysReminder",
                    isChecked
                )
            }
            sw5days.setOnCheckedChangeListener { _, isChecked ->
                updateReminder(
                    "fiveDaysReminder",
                    isChecked
                )
            }
            sw3days.setOnCheckedChangeListener { _, isChecked ->
                updateReminder(
                    "threeDaysReminder",
                    isChecked
                )
            }
            sw1day.setOnCheckedChangeListener { _, isChecked ->
                updateReminder(
                    "oneDayReminder",
                    isChecked
                )
            }
            swAnniversary.setOnCheckedChangeListener { _, isChecked ->
                updateReminder(
                    "anniversaryReminder",
                    isChecked
                )
            }
        }
    }

    private fun updateReminder(key: String, isChecked: Boolean) {
        reminders[key] = isChecked
        notificationViewModel.firebaseUpdatePartialReminder(key, isChecked)
    }

    private fun updateYearlyDependents(isEnabled: Boolean) {
        binding.apply {
            arrayOf(sw7days, sw5days, sw3days, sw1day, swAnniversary).forEach {
                it.isEnabled = isEnabled
            }
        }
        if (!isEnabled) {
            listOf(
                "sevenDaysReminder",
                "fiveDaysReminder",
                "threeDaysReminder",
                "oneDayReminder",
                "anniversaryReminder"
            ).forEach {
                updateReminder(it, false)
            }
        }
    }


    private fun snapShotListener() {
        if (notificationViewModel.reminderRef != null) {
            reminderListenerRegistration =
                notificationViewModel.reminderRef?.addSnapshotListener { snapShot, error ->
                    if (error == null && snapShot != null) {
                        snapShot.toObject(GlobalReminder::class.java)?.let { reminder ->
                            Log.d("TAG", "snapShotListener: $reminder")
                            reminders["monthlyReminder"] = reminder.monthlyReminder
                            reminders["yearlyReminder"] = reminder.yearlyReminder
                            reminders["sevenDaysReminder"] = reminder.sevenDaysReminder
                            reminders["fiveDaysReminder"] = reminder.fiveDaysReminder
                            reminders["threeDaysReminder"] = reminder.threeDaysReminder
                            reminders["oneDayReminder"] = reminder.oneDayReminder
                            reminders["anniversaryReminder"] = reminder.anniversaryReminder
                            setupSwitches()
                            binding.showTimePickerButton.text =
                                if (reminder.reminderTimeMinute <= 10) {
                                    "${reminder.reminderTimeHour} : 0${reminder.reminderTimeMinute} Uhr"
                                } else
                                    "${reminder.reminderTimeHour} : ${reminder.reminderTimeMinute} Uhr"
                            timePicker(reminder.reminderTimeHour, reminder.reminderTimeMinute)
                        }
                    }
                }
        }
    }
}