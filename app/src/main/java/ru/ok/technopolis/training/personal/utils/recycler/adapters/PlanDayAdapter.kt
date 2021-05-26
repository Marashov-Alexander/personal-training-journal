package ru.ok.technopolis.training.personal.utils.recycler.adapters

import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes

import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.PlanDayItem
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.PlanDayViewHolder

import kotlin.reflect.KClass
//WARNING: usage of public val that should be private
class PlanDayAdapter (
        holderType: KClass<out PlanDayViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<PlanDayItem>,
        onClick: (PlanDayItem) -> Unit = {},
        private val onStart: (EditText) -> Unit = {}
) : BaseListAdapter<PlanDayItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<PlanDayItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        //WARNING: usage of public val that should be private
        (holder as PlanDayViewHolder).setClickListener {
            onStart.invoke(holder.time)
        }
    }
}