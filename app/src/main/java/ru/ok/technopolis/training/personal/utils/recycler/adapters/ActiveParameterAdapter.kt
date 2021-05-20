package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.items.ItemsList
//import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ActiveParameterViewHolder
import kotlin.reflect.KClass

class ActiveParameterAdapter(
    holderType: KClass<out ActiveParameterViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<ParameterEntity>,
    onClick: (ParameterEntity) -> Unit = {}
) : BaseListAdapter<ParameterEntity>(holderType, layoutId, dataSource, onClick) {
//    override fun onBindViewHolder(holder: BaseViewHolder<ParameterEntity>, position: Int) {
//        super.onBindViewHolder(holder, position)
//        val item = data[position]
//    }
}
