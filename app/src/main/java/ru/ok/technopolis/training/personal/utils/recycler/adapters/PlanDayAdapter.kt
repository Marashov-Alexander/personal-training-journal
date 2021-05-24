package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes

import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.PlanDayItem
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.PlanDayViewHolder

import kotlin.reflect.KClass

class PlanDayAdapter (
        holderType: KClass<out PlanDayViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<PlanDayItem>,
        onClick: (PlanDayItem) -> Unit = {}
) : BaseListAdapter<PlanDayItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<PlanDayItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
    }
}