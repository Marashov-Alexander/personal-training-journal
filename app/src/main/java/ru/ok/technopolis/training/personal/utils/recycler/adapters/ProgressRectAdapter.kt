package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProgressRectItem
import ru.ok.technopolis.training.personal.viewholders.ProgressRectViewHolder
import kotlin.reflect.KClass

class ProgressRectAdapter(
    holderType: KClass<out ProgressRectViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<ProgressRectItem>,
    onClick: (ProgressRectItem) -> Unit = {}
) : BaseListAdapter<ProgressRectItem>(holderType, layoutId, dataSource, onClick)
