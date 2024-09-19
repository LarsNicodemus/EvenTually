package com.example.abschlussprojekt.adapter

import androidx.navigation.findNavController
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.example.abschlussprojekt.ui.main.RecommendationsFragmentDirections
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.google.android.libraries.places.api.model.Place

class RestaurantsAdapter(
    places: List<PlaceWithPhoto>,
    recsViewModel: RecommendationViewModel
) : BasePlacesAdapter(places, recsViewModel) {

    override fun onBindViewHolder(holder: BasePlacesAdapter.PlaceViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val place = places[position]
        holder.binding.cvRec.setOnClickListener {
            recsViewModel.place = place
            val action =
                RecommendationsFragmentDirections.actionRecommendationsOverviewFragmentToRecommendationDetailFragment(
                    2,1
                )
            holder.itemView.findNavController().navigate(action)
        }
    }

    /**
     * Gibt den Text für ein Restaurant zurück.
     *
     * @param place Das Restaurant.
     * @param placetype Der Typ des Ortes.
     * @return Der Text für das Restaurant.
     */
    override fun getTextForPlace(place: Place, placetype: Int): String {
        return recsViewModel.getTextForRestaurant(place)
    }

    /**
     * Aktualisiert die Liste der Restaurants.
     *
     * @param newPlaces Die neue Liste der Restaurants.
     */
    override fun updatePlaces(newPlaces: List<PlaceWithPhoto>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}