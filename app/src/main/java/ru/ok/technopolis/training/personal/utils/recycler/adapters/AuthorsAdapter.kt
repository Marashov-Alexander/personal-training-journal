package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.viewholders.AuthorViewHolder
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import kotlin.reflect.KClass

class AuthorsAdapter(
        holderType: KClass<out AuthorViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<ProfileItem>,
        onClick: (ProfileItem) -> Unit = {},
        private val onStart: (ProfileItem) -> Unit = {}
) : BaseListAdapter<ProfileItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<ProfileItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        (holder as AuthorViewHolder).setOnStartClickListener {
            onStart.invoke(item)
        }
    }
}