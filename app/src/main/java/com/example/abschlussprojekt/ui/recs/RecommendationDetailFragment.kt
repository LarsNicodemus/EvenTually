package com.example.abschlussprojekt.ui.recs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.FragmentRecommendationDetailBinding
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel


class RecommendationDetailFragment : Fragment() {
    private lateinit var binding: FragmentRecommendationDetailBinding
    private val recommendationViewModel: RecommendationViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by activityViewModels()
    private val args: RecommendationDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val place = recommendationViewModel.place
        val product = recommendationViewModel.product
        val restaurantIndicator =args.placestype
        val typeIndicator = args.recstype
        Log.d("product", "in Fragment:$product in ViewModel:${recommendationViewModel.product}")
        Log.d("place", "in Fragment:$place in ViewModel:${recommendationViewModel.place}")
        if (typeIndicator == 2 && product != null) {
            uiViewModel.setUpToolbar(this, view, product.title)
            binding.apply {
                tvRecCurrency.visibility = View.VISIBLE
                tvCurrencyValue.visibility = View.VISIBLE
                tvRatingNumbers.visibility = View.GONE
                tvPlacesKitchen.visibility = View.GONE
                tvPlacesStatus.visibility = View.GONE
                rbPlaces.visibility = View.GONE
                tvPlacesTitle.text = product.title
                tvCurrencyValue.text = product.price.value
                product.image?.let {
                    ivRec.load(it.imageUrl)
                } ?: ivRec.setImageResource(R.drawable.avatar)
                button2.setOnClickListener {
                    val productUrl = product.itemWebUrl
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(productUrl))
                    startActivity(browserIntent)
                }
            }
        } else if (typeIndicator == 1 && place != null) {
            place.place?.name?.let { uiViewModel.setUpToolbar(this, view, it) }
            binding.apply {
                tvPlacesTitle.text = place.place?.name
                tvPlacesStatus.text =
                    if (place.place?.let { recommendationViewModel.isRestaurantOpen(it) } == true) "geöffnet" else "geschlossen"
                val restaurantDescriptor = place.place?.let { recommendationViewModel.getTextForRestaurant(it) }
                val placeDescriptor = place.place?.let { recommendationViewModel.getTextForPlace(it) }
                if (restaurantIndicator == 2) {
                    tvPlacesKitchen.text = restaurantDescriptor
                } else {
                    tvPlacesKitchen.text = placeDescriptor
                }
                tvRatingNumbers.text = place.place?.rating?.toString() ?: "0.0"
                rbPlaces.rating = place.place?.rating?.toFloat() ?: 0.0f
                val photoReference = place.photo
                ivRec.setImageBitmap(photoReference)
                button2.setOnClickListener {
                    val placeUrl = place.place?.websiteUri
                    if (placeUrl != null) {
                        Log.d("Website", placeUrl.toString())
                        val browserIntent = Intent(Intent.ACTION_VIEW, placeUrl)
                        startActivity(browserIntent)
                    } else {
                        button2.error = "Keine Website verfügbar"
                    }
                }
            }
        }

    }



}