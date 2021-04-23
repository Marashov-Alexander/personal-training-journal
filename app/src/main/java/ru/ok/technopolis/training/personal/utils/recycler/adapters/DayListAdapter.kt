package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.DayItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.DayViewHolder
import ru.ok.technopolis.training.personal.viewholders.ExerciseViewHolder
import kotlin.reflect.KClass

class DayListAdapter(
        holderType: KClass<out DayViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<DayItem>,
        onClick: (DayItem) -> Unit = {}
) : BaseListAdapter<DayItem>(holderType, layoutId, dataSource, onClick) {

//    override fun onBindViewHolder(holder: BaseViewHolder<DayItem>, position: Int) {
//        super.onBindViewHolder(holder, position)
//        val item = data[position]
//        (holder as DayViewHolder).setOnClickListener {
//            println("WOW")
//            item.isChosen = true
//            holder.update(item)
//        }
//        (holder as DayViewHolder).setClickListener { onClick.invoke(item) }
//    }
}