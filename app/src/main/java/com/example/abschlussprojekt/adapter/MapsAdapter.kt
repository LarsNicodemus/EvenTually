package com.example.abschlussprojekt.adapter

import androidx.navigation.findNavController
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.example.abschlussprojekt.ui.recs.RecommendationsCategoryFragmentDirections
import com.google.android.libraries.places.api.model.Place

class MapsAdapter(
    places: List<PlaceWithPhoto>,
    recsViewModel: RecommendationViewModel,
    placetype: Int
) : BasePlacesAdapter(places, recsViewModel) {

    private val typePlace = placetype
    override fun onBindViewHolder(holder: BasePlacesAdapter.PlaceViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val place = places[position]
        holder.binding.cvRec.setOnClickListener {
        recsViewModel.place = place
            val action = RecommendationsCategoryFragmentDirections.actionRecommendationsCategoryFragmentToRecommendationDetailFragment2()
            holder.itemView.findNavController().navigate(action)
        }
    }

    /**
     * Gibt den Text für einen Ort basierend auf dem Ortstyp zurück.
     *
     * @param place Der Ort.
     * @param placetype Der Typ des Ortes.
     * @return Der Text für den Ort.
     */
    override fun getTextForPlace(place: Place, placetype: Int): String {
        return if (placetype == typePlace) {
            recsViewModel.getTextForRestaurant(place)
        } else {
            recsViewModel.getTextForPlace(place)
        }
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