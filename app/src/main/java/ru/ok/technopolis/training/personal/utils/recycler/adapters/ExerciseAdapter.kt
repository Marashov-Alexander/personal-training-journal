package ru.ok.technopolis.training.personal.utils.recycler.adapters

import android.view.View
import android.view.View.*
import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ExerciseItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder
import kotlin.reflect.KClass

class ExerciseAdapter(
        holderType: KClass<out ExerciseItemViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<ExerciseItem>,
        onClick: (ExerciseItem) -> Unit = {},
        private val onLongExerciseClick: (ExerciseItem, View) -> Unit = { _, _ -> }
) : BaseListAdapter<ExerciseItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<ExerciseItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        val exerciseHolder = (holder as ExerciseItemViewHolder)
        val prevItem = if (position == 0) null else data[position - 1]
        val nextItem = if (position == data.size - 1) null else data[position + 1]

        item.cornerMode = item.getCornerMode(prevItem, nextItem)
        item.counterVisibility = item.getCounterMode(item.cornerMode)

        exerciseHolder.setLongClickListener { view ->
            onLongExerciseClick.invoke(item, view)
        }
    }
}
