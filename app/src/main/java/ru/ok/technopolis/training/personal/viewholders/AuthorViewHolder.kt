package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.item_authors.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList

import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.AuthorCategoryAdapter

class AuthorViewHolder (
        itemView: View
) : BaseViewHolder<ProfileItem>(itemView) {
    private var authorIcon: SimpleDraweeView = itemView.author_icon
    private var authorName: TextView = itemView.author_name
    private var authorCategories: RecyclerView = itemView.author_categories
    private var authorSendMessage: ImageButton = itemView.send_message_icon
    private var separator: View = itemView.author_separator


    override fun bind(item: ProfileItem) {
        if (item.isUser) {
            itemView.visibility = View.INVISIBLE
        } else {
            itemView.visibility = View.VISIBLE
            update(item)
        }
    }

    fun update(item: ProfileItem) {
        authorName.text = item.name
        authorIcon.setImageURI(item.pictureUrlStr)
        val workoutsLayoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        authorCategories.layoutManager = workoutsLayoutManager
        val sports = ItemsList(item.sports as MutableList<*>)
        val adapter = AuthorCategoryAdapter(
                holderType = AuthorCategoryViewHolder::class,
                layoutId = R.layout.item_author_category,
                dataSource = sports as ItemsList<String>
        )
        authorCategories.adapter = adapter
    }

    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == View.VISIBLE) {
            authorSendMessage.setOnClickListener { onStart() }
            authorIcon.setOnClickListener { onStart() }
            authorName.setOnClickListener{onStart()}
        }
    }
}
