package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ParameterItem
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder
import kotlin.reflect.KClass

class ParameterAdapter(
    holderType: KClass<out ParameterViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<ParameterItem>,
    private val onEdit: (ParameterItem) -> Unit,
    private val onValueChanged: (Float, ParameterItem?) -> Unit = {_, _ -> },
    onClick: (ParameterItem) -> Unit = {}
) : BaseListAdapter<ParameterItem>(holderType, layoutId, dataSource, onClick) {
    override fun onBindViewHolder(holder: BaseViewHolder<ParameterItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        (holder as ParameterViewHolder).setOnEditListener(onEdit)
        holder.setOnValueChangedListener(onValueChanged)
    }
}
