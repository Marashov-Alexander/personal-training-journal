package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_bundle.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.BundleItem


class BundleItemViewHolder(
        itemView: View
) : BaseViewHolder<BundleItem>(itemView) {

    private val bundleCard: MaterialCardView = itemView.bundle_card
    private val title: TextView = itemView.title

    override fun bind(item: BundleItem) {
        title.text = item.title
        setIsSelected(item.isChosen)
    }

    fun setIsSelected(selected: Boolean) {
        if (selected) {
            bundleCard.background.setTint(itemView.context.getColor(R.color.light_cyanide))
        } else {
            bundleCard.background.setTint(itemView.context.getColor(R.color.white))
        }
    }

    fun setOnClickListener(onClickListener: (View) -> Unit) {
        bundleCard.setOnClickListener(onClickListener)
    }

}