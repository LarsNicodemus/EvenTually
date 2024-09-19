package com.example.abschlussprojekt.adapter

import androidx.navigation.findNavController
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.example.abschlussprojekt.ui.main.RecommendationsFragmentDirections
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.google.android.libraries.places.api.model.Place

class PlacesAdapter(
    places: List<PlaceWithPhoto>,
    recsViewModel: RecommendationViewModel
) : BasePlacesAdapter(places, recsViewModel) {

    override fun onBindViewHolder(holder: BasePlacesAdapter.PlaceViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val place = places[position]
        holder.binding.cvRec.setOnClickListener {
        recsViewModel.place = place
            val action = RecommendationsFragmentDirections.actionRecommendationsOverviewFragmentToRecommendationDetailFragment(1,1)
            holder.itemView.findNavController().navigate(action)
        }
    }

    /**
     * Gibt den Text für einen Ort zurück.
     *
     * @param place Der Ort.
     * @param placetype Der Typ des Ortes.
     * @return Der Text für den Ort.
     */
    override fun getTextForPlace(place: Place, placetype: Int): String {
        return recsViewModel.getTextForPlace(place)
    }

    /**
     * Aktualisiert die Liste der Orte.
     *
     * @param newPlaces Die neue Liste der Orte.
     */
    override fun updatePlaces(newPlaces: List<PlaceWithPhoto>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}