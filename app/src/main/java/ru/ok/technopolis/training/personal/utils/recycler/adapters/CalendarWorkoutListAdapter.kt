package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.WorkoutViewHolder
import kotlin.reflect.KClass

class CalendarWorkoutListAdapter(
    holderType: KClass<out WorkoutViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<WorkoutEntity>,
    onClick: (WorkoutEntity) -> Unit = {},
    private val onStartWorkoutClick: (WorkoutEntity) -> Unit = {},
    private val onDeleteWorkoutClick: (WorkoutEntity) -> Unit = {}
) : BaseListAdapter<WorkoutEntity>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<WorkoutEntity>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        (holder as WorkoutViewHolder).setClickListeners(
            { onStartWorkoutClick.invoke(item) },
            { onDeleteWorkoutClick.invoke(item) }
        )
    }
}