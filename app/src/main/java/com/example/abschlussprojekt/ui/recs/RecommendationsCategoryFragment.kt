package com.example.abschlussprojekt.ui.recs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.abschlussprojekt.adapter.EbayAdapter
import com.example.abschlussprojekt.adapter.MapsAdapter
import com.example.abschlussprojekt.databinding.FragmentRecommendationsCategoryBinding
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.ItemSummary
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel

class RecommendationsCategoryFragment : Fragment() {
    private val args: RecommendationsCategoryFragmentArgs by navArgs()

    private var buttonType: Int = 0
    private lateinit var binding: FragmentRecommendationsCategoryBinding
    private val uiViewModel: UIViewModel by activityViewModels()
    private val recommendationViewModel: RecommendationViewModel by activityViewModels()
    private lateinit var ebayAdapter: EbayAdapter
    private lateinit var mapsAdapter: MapsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            buttonType = args.buttonType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationsCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (buttonType) {
            1 -> recommendationViewModel.cardSuggestions.observe(viewLifecycleOwner) { cardData ->
                displayContent1(
                    cardData.itemSummaries,
                    view
                )
            }

            2 -> recommendationViewModel.giftResultSuggestion.observe(viewLifecycleOwner) { giftData ->
                displayContent2(
                    giftData.itemSummaries,
                    view
                )
            }

            3 -> recommendationViewModel.restaurantResults.observe(viewLifecycleOwner) { restaurantData ->
                displayContent3(
                    restaurantData,
                    view
                )
            }

            4 -> recommendationViewModel.activitiesResults.observe(viewLifecycleOwner) { activitiesData ->
                displayContent4(
                    activitiesData,
                    view
                )
            }
        }
    }


    /**
     * Zeigt die Inhalte für Kartenempfehlungen an.
     * @param itemSummaries Die Liste der Artikelzusammenfassungen.
     * @param view Die erstellte View.
     */
    private fun displayContent1(itemSummaries: List<ItemSummary>, view: View) {
        val recyclerView = binding.rvCategory
        ebayAdapter = EbayAdapter(itemSummaries, recommendationViewModel)
        recyclerView.adapter = ebayAdapter
        ebayAdapter.updateProducts(itemSummaries)
        uiViewModel.setUpToolbar(this, view, "Karten Empfehlungen")
    }

    /**
     * Zeigt die Inhalte für Geschenkempfehlungen an.
     * @param itemSummaries Die Liste der Artikelzusammenfassungen.
     * @param view Die erstellte View.
     */
    private fun displayContent2(itemSummaries: List<ItemSummary>, view: View) {
        val recyclerView = binding.rvCategory
        ebayAdapter = EbayAdapter(itemSummaries, recommendationViewModel)
        recyclerView.adapter = ebayAdapter
        ebayAdapter.updateProducts(itemSummaries)
        uiViewModel.setUpToolbar(this, view, "Geschenk Empfehlungen")
    }

    /**
     * Zeigt die Inhalte für Restaurantempfehlungen an.
     * @param placeSummary Die Liste der Orte mit Fotos.
     * @param view Die erstellte View.
     */
    private fun displayContent3(placeSummary: List<PlaceWithPhoto>, view: View) {
        val recyclerView = binding.rvCategory
        mapsAdapter = MapsAdapter(placeSummary, recommendationViewModel, 1)
        recyclerView.adapter = mapsAdapter
        mapsAdapter.updatePlaces(placeSummary)
        uiViewModel.setUpToolbar(this, view, "Restaurant Empfehlungen")
    }

    /**
     * Zeigt die Inhalte für Aktivitätsempfehlungen an.
     * @param placeSummary Die Liste der Orte mit Fotos.
     * @param view Die erstellte View.
     */
    private fun displayContent4(placeSummary: List<PlaceWithPhoto>, view: View) {
        val recyclerView = binding.rvCategory
        mapsAdapter = MapsAdapter(placeSummary, recommendationViewModel, 2)
        recyclerView.adapter = mapsAdapter
        mapsAdapter.updatePlaces(placeSummary)
        uiViewModel.setUpToolbar(this, view, "Unternehmungs Empfehlungen")
    }
}