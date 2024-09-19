package com.example.abschlussprojekt.adapter

import android.util.Log
import androidx.navigation.findNavController
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.ItemSummary
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.example.abschlussprojekt.ui.recs.RecommendationsCategoryFragmentDirections

class EbayAdapter(
    products: List<ItemSummary>,
    recsViewModel: RecommendationViewModel
) : BaseEbayAdapter(products, recsViewModel) {

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val product = products[position]
        holder.binding.cvRec.setOnClickListener {
            recsViewModel.product = product
            Log.d("product", "${recsViewModel.product}")
            val action = RecommendationsCategoryFragmentDirections.actionRecommendationsCategoryFragmentToRecommendationDetailFragment2(1,2)
            holder.itemView.findNavController().navigate(action)
        }
    }

    /**
     * Aktualisiert die Liste der Produkte.
     *
     * @param newProducts Die neue Liste der Produkte.
     */
    override fun updateProducts(newProducts: List<ItemSummary>) {
        products = newProducts
        notifyDataSetChanged()
    }
    override fun onProductClick(product: ItemSummary) {
        // Implementiere die spezifische Klick-Aktion für EbayAdapter
    }
}