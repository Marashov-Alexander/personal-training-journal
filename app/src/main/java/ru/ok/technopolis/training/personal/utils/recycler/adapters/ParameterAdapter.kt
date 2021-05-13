package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortParameterItem
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder
import kotlin.reflect.KClass

class ParameterAdapter(
    holderType: KClass<out ParameterViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<ShortParameterItem>,
    onClick: (ShortParameterItem) -> Unit = {}
) : BaseListAdapter<ShortParameterItem>(holderType, layoutId, dataSource, onClick)
