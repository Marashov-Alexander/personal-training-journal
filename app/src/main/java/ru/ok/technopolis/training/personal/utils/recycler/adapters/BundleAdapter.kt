package ru.ok.technopolis.training.personal.utils.recycler.adapters

import android.view.View
import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import kotlin.reflect.KClass

class BundleAdapter(
    holderType: KClass<out BundleItemViewHolder>,
    @LayoutRes layoutId: Int,
    dataSource: ItemsList<BundleItem>,
    onClick: (BundleItem) -> Unit = {},
    private val onBundleClick: (BundleItem, View) -> Unit = { _, _ -> }
) : BaseListAdapter<BundleItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<BundleItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        val bundleHolder = (holder as BundleItemViewHolder)

        bundleHolder.setOnClickListener { view ->
            onBundleClick.invoke(item, view)
        }
    }
}
