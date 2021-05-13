package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.AuthorCategoryViewHolder
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import kotlin.reflect.KClass

class AuthorCategoryAdapter(
        holderType: KClass<out AuthorCategoryViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<String>,
        onClick: (String) -> Unit = {},
        private val onStart: (String) -> Unit = {}
) : BaseListAdapter<String>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
//        (holder as AuthorCategoryViewHolder).setOnStartClickListener {
//            onStart.invoke(item)
//        }
    }
}