package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_author_category.view.*

class AuthorCategoryViewHolder (
        itemView: View
) : BaseViewHolder<String>(itemView) {
    private var categoryName: TextView = itemView.author_category_name


    override fun bind(item: String) {
        categoryName.text = item
    }
}