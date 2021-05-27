package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ParameterItem
import ru.ok.technopolis.training.personal.viewholders.ActiveParameterViewHolder
import kotlin.reflect.KClass

class ActiveParameterAdapter(
    holderType: KClass<out ActiveParameterViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<ParameterItem>,
    onClick: (ParameterItem) -> Unit = {}
) : BaseListAdapter<ParameterItem>(holderType, layoutId, dataSource, onClick)
