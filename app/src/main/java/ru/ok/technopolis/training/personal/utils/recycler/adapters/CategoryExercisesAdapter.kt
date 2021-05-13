package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.CategoryExerciseItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.CategoryExerciseViewHolder
import kotlin.reflect.KClass

class CategoryExercisesAdapter (
        holderType: KClass<out CategoryExerciseViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<CategoryExerciseItem>,
        onClick: (CategoryExerciseItem) -> Unit = {},
        private val onStart: (CategoryExerciseItem) -> Unit = {}
) : BaseListAdapter<CategoryExerciseItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<CategoryExerciseItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        (holder as CategoryExerciseViewHolder).setOnStartClickListener {
            onStart.invoke(item)
        }
    }
}