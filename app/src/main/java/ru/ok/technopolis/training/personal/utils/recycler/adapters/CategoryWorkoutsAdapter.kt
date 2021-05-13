package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList

import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.CategoryWorkoutsViewHolder
import kotlin.reflect.KClass

class CategoryWorkoutsAdapter (
        holderType: KClass<out CategoryWorkoutsViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<CategoryWorkoutsItem>,
        onClick: (CategoryWorkoutsItem) -> Unit = {},
        private val onStart: (CategoryWorkoutsItem) -> Unit = {}
) : BaseListAdapter<CategoryWorkoutsItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<CategoryWorkoutsItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        (holder as CategoryWorkoutsViewHolder).setOnStartClickListener {
            onStart.invoke(item)
        }
    }
}