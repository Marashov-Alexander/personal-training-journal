package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortParameterItem
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder
import kotlin.reflect.KClass

class ParameterAdapter(
    holderType: KClass<out ParameterViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<ShortParameterItem>,
    private val onEdit: (ShortParameterItem) -> Unit,
    onClick: (ShortParameterItem) -> Unit = {}
) : BaseListAdapter<ShortParameterItem>(holderType, layoutId, dataSource, onClick) {
    override fun onBindViewHolder(holder: BaseViewHolder<ShortParameterItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        (holder as ParameterViewHolder).setOnEditListener { onEdit.invoke(item) }
    }
}
