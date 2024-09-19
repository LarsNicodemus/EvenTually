package com.example.abschlussprojekt.ui.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.FragmentSettingsRecsBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalRecommendation
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.google.firebase.firestore.ListenerRegistration


class SettingsRecsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsRecsBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val recsViewModel: RecommendationViewModel by activityViewModels()
    private var recsListenerRegistration: ListenerRegistration? = null
    private val recs = mutableMapOf(
        "cards" to false,
        "gifts" to false,
        "restaurants" to false,
        "activities" to false
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsRecsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiViewModel.setUpToolbar(this, view, "Empfehlungseinstellung")
        snapShotListener()
        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            swGreetingCards.setOnCheckedChangeListener { _, isChecked ->
                updateRecs("giftCardSelected", isChecked)
            }
            swGifts.setOnCheckedChangeListener { _, isChecked ->
                updateRecs("presentSelected", isChecked)
            }
            swRestaurants.setOnCheckedChangeListener { _, isChecked ->
                updateRecs("restaurantSelected", isChecked)
            }
            swActivities.setOnCheckedChangeListener { _, isChecked ->
                updateRecs("activitySelected", isChecked)
            }
            seekBarDistance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.tvDistance.text = getString(R.string.distance_in_km, progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Optional: Etwas tun, wenn die Benutzerinteraktion mit der SeekBar beginnt
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.progress?.let {
                        recsViewModel.firebaseUpdatePartialRecs("distance", it)
                    }
                }
            })
        }
    }

    private fun snapShotListener() {
        recsListenerRegistration =
            recsViewModel.recsRef?.addSnapshotListener { snapShot, error ->
                if (error == null || snapShot != null) {
                    snapShot?.toObject(GlobalRecommendation::class.java).let { rec ->
                        if (rec != null) {
                            recs["cards"] = rec.giftCardSelected
                            recs["gifts"] = rec.presentSelected
                            recs["restaurants"] = rec.restaurantSelected
                            recs["activities"] = rec.activitySelected
                            setupSwitches()
                            binding.seekBarDistance.progress = rec.distance
                            binding.tvDistance.text =
                                getString(R.string.distance_in_km, rec.distance)

                        }

                    }
                }
            }
    }

    private fun setupSwitches() {
        binding.apply {
            swGreetingCards.isChecked = recs["cards"] ?: false
            swGifts.isChecked = recs["gifts"] ?: false
            swRestaurants.isChecked = recs["restaurants"] ?: false
            swActivities.isChecked = recs["activities"] ?: false
        }
    }

    private fun updateRecs(key: String, isChecked: Boolean) {
        recs[key] = isChecked
        recsViewModel.firebaseUpdatePartialRecs(key, isChecked)
    }

    override fun onDestroy() {
        super.onDestroy()
        recsListenerRegistration?.remove()
    }
}

