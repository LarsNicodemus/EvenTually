package com.example.abschlussprojekt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojekt.databinding.ItemRecsPlacesBinding
import com.example.abschlussprojekt.datamodel.remotemodels.places.PlaceWithPhoto
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.google.android.libraries.places.api.model.Place

abstract class BasePlacesAdapter(
    protected var places: List<PlaceWithPhoto>,
    protected val recsViewModel: RecommendationViewModel
) : RecyclerView.Adapter<BasePlacesAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemRecsPlacesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRecsPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        recsViewModel.place = place
        holder.binding.apply {
            tvPlacesTitle.text = place.place?.name
            tvPlacesStatus.text = if (place.place?.let { recsViewModel.isRestaurantOpen(it) } == true) "geöffnet" else "geschlossen"
            tvPlacesKitchen.text = place.place?.let { getTextForPlace(it) }
            tvRatingNumbers.text = place.place?.rating?.toString() ?: "0.0"
            rbPlaces.rating = place.place?.rating?.toFloat() ?: 0.0f
            val photoReference = place.photo
            ivRec.setImageBitmap(photoReference)
        }

    }

    override fun getItemCount(): Int {
        return places.size
    }

    /**
     * Aktualisiert die Liste der Orte.
     *
     * @param newPlaces Die neue Liste der Orte.
     */
    open fun updatePlaces(newPlaces: List<PlaceWithPhoto>) {
        places = newPlaces
        notifyDataSetChanged()
    }
    /**
     * Gibt den Text für einen Ort basierend auf dem Ortstyp zurück.
     *
     * @param place Der Ort.
     * @param placetype Der Typ des Ortes.
     * @return Der Text für den Ort.
     */
    abstract fun getTextForPlace(place: Place, placetype: Int = 1): String

}