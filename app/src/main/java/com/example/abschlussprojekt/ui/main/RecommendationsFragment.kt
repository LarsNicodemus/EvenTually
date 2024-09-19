package com.example.abschlussprojekt.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojekt.adapter.EbayCardAdapter
import com.example.abschlussprojekt.adapter.EbayGiftAdapter
import com.example.abschlussprojekt.adapter.PlacesAdapter
import com.example.abschlussprojekt.adapter.RestaurantsAdapter
import com.example.abschlussprojekt.databinding.FragmentRecommendationsBinding
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalRecommendation
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch


class RecommendationsFragment : Fragment() {
    private lateinit var binding: FragmentRecommendationsBinding
    private val recommendationViewModel: RecommendationViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var giftsAdapter: EbayGiftAdapter
    private lateinit var cardsAdapter: EbayCardAdapter
    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var restaurantsAdapter: RestaurantsAdapter
    private lateinit var geoPoint: GeoPoint
    private lateinit var foodPreferences: List<String>
    private lateinit var activityPreferences: List<String>
    private var likes = emptyList<String>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationsBinding.inflate(layoutInflater)
        setupAdapters()
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDataAndExecute()
        binding.tvMoreCards.setOnClickListener { openDetailFragment(1) }
        binding.tvMoreGifts.setOnClickListener { openDetailFragment(2) }
        binding.tvMoreRestaurants.setOnClickListener { openDetailFragment(3) }
        binding.tvMoreActivities.setOnClickListener { openDetailFragment(4) }
    }


    /**
     * Initialisiert die Adapter für die RecyclerViews.
     */
    private fun setupAdapters() {
        giftsAdapter = EbayGiftAdapter(emptyList(), recommendationViewModel)
        cardsAdapter = EbayCardAdapter(emptyList(), recommendationViewModel)
        placesAdapter = PlacesAdapter(emptyList(), recommendationViewModel)
        restaurantsAdapter = RestaurantsAdapter(emptyList(), recommendationViewModel)

        binding.rvRecsGifts.adapter = giftsAdapter
        binding.rvRecsCards.adapter = cardsAdapter
        binding.rvRecsActivities.adapter = placesAdapter
        binding.rvRecsRestaurants.adapter = restaurantsAdapter
    }

    /**
     * Setzt die Observer für die LiveData-Objekte.
     */
    private fun setupObservers() {
        recommendationViewModel.giftResultSuggestion.observe(viewLifecycleOwner) { giftData ->
            giftsAdapter.updateProducts(giftData.itemSummaries)
        }

        recommendationViewModel.cardSuggestions.observe(viewLifecycleOwner) { cardData ->
            cardsAdapter.updateProducts(cardData.itemSummaries)
        }

        recommendationViewModel.activitiesResults.observe(viewLifecycleOwner) { activityData ->
            placesAdapter.updatePlaces(activityData)
        }

        recommendationViewModel.restaurantResults.observe(viewLifecycleOwner) { restaurantData ->
            restaurantsAdapter.updatePlaces(restaurantData)
        }
    }

    /**
     * Lädt die Daten und führt die entsprechenden API-Aufrufe aus.
     */
    private fun loadDataAndExecute() {
        recommendationViewModel.fetchRecsData()
        userViewModel.fetchPartnerProfileData()
        userViewModel.fetchProfileData()

        userViewModel.partnerData.observe(viewLifecycleOwner) { partnerData ->
            partnerData?.let {
                foodPreferences = it.foodPreferences
                activityPreferences = it.activityPreferences
                likes = it.likes
            }
        }

        userViewModel.profileData.observe(viewLifecycleOwner) { profileData ->
            profileData?.location?.let {
                geoPoint = it
                val location = LatLng(geoPoint.latitude, geoPoint.longitude)
                recommendationViewModel.getCityName(location)
            }
        }
        recommendationViewModel.recommendationData.observe(viewLifecycleOwner) { recsData ->
            Log.d("RecommendationFragment", "Received recommendation data: $recsData")
            recsData?.let {
                checkDataAndExecute(it)
                updateViews(
                    it.presentSelected,
                    binding.rvRecsGifts,
                    binding.tvTitleGifts,
                    binding.tvMoreGifts
                )
                updateViews(
                    it.giftCardSelected,
                    binding.rvRecsCards,
                    binding.tvTitleCards,
                    binding.tvMoreCards
                )
                updateViews(
                    it.restaurantSelected,
                    binding.rvRecsRestaurants,
                    binding.tvTitleRestaurants,
                    binding.tvMoreRestaurants
                )
                updateViews(
                    it.activitySelected,
                    binding.rvRecsActivities,
                    binding.tvTitleActivities,
                    binding.tvMoreActivities
                )
            }
        }
    }

    /**
     * Aktualisiert die Sichtbarkeit und Aktivierung der Views basierend auf den Empfehlungen.
     * @param isSelected Ob die Empfehlung ausgewählt ist.
     * @param recyclerView Die RecyclerView für die Empfehlung.
     * @param titleView Die Titel-View für die Empfehlung.
     * @param moreView Die "Mehr"-View für die Empfehlung.
     */
    private fun updateViews(
        isSelected: Boolean?,
        recyclerView: View,
        titleView: View,
        moreView: View
    ) {
        val visibility = if (isSelected == true) View.VISIBLE else View.GONE
        val isEnabled = isSelected == true

        recyclerView.visibility = visibility
        titleView.visibility = visibility
        moreView.visibility = visibility

        recyclerView.isEnabled = isEnabled
        titleView.isEnabled = isEnabled
        moreView.isEnabled = isEnabled
    }

    /**
     * Überprüft die Daten und führt die entsprechenden API-Aufrufe aus.
     * @param recsData Die Empfehlung-Daten.
     */
    private fun checkDataAndExecute(recsData: GlobalRecommendation?) {
        if (::foodPreferences.isInitialized && ::activityPreferences.isInitialized && ::geoPoint.isInitialized && recsData != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (recsData.restaurantSelected) {
                    launch { executeRestaurantAPICall() }
                }

                if (recsData.activitySelected) {
                    launch { executeActivitiesAPICall() }
                }

                if (recsData.presentSelected) {
                    launch { executeGiftAPICall() }
                }

                if (recsData.giftCardSelected) {
                    launch { executeCardAPICall() }
                }
            }
        }
    }

    /**
     * Führt den API-Aufruf für Restaurants aus.
     */
    private fun executeRestaurantAPICall() {
        recommendationViewModel.searchRestaurantWithPhoto(foodPreferences)
    }

    /**
     * Führt den API-Aufruf für Aktivitäten aus.
     */
    private fun executeActivitiesAPICall() {
        recommendationViewModel.searchActivitiesWithPhoto(activityPreferences)
    }

    /**
     * Führt den API-Aufruf für Geschenke aus.
     */
    private fun executeGiftAPICall() {
        recommendationViewModel.getGiftSuggestions(likes)
    }

    /**
     * Führt den API-Aufruf für Karten aus.
     */
    private fun executeCardAPICall() {
        recommendationViewModel.searchCards(likes)
    }

    /**
     * Öffnet das Detail-Fragment basierend auf dem Button-Typ.
     * @param buttonType Der Typ des Buttons.
     */
    private fun openDetailFragment(buttonType: Int) {
        val action =
            RecommendationsFragmentDirections.actionRecommendationsOverviewFragmentToRecommendationsCategoryFragment(
                buttonType
            )
        findNavController().navigate(action)
    }
}
