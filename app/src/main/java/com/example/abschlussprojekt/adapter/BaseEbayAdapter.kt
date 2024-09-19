package com.example.abschlussprojekt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.ItemRecsBinding
import com.example.abschlussprojekt.datamodel.remotemodels.shopping.ItemSummary
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel

abstract class BaseEbayAdapter(
    protected var products: List<ItemSummary>,
    protected val recsViewModel: RecommendationViewModel
) : RecyclerView.Adapter<BaseEbayAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemRecsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemRecsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        recsViewModel.product = product
        Log.d("product", "$recsViewModel.product")
        holder.binding.apply {
            tvRecTitle.text = product.title
            tvCurrencyValue.text = product.price.value
            product.image?.let {
                ivRec.load(it.imageUrl)
            } ?: ivRec.setImageResource(R.drawable.avatar)
        }
        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
    /**
     * Aktualisiert die Liste der Produkte.
     *
     * @param newProducts Die neue Liste der Produkte.
     */
    open fun updateProducts(newProducts: List<ItemSummary>) {
        products = newProducts
        notifyDataSetChanged()
    }

    abstract fun onProductClick(product: ItemSummary)
}