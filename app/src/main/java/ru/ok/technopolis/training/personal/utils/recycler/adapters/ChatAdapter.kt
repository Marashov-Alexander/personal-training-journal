package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.annotation.LayoutRes
import ru.ok.technopolis.training.personal.items.ChatItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.viewholders.BaseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ChatViewHolder
import kotlin.reflect.KClass

class ChatAdapter (
        holderType: KClass<out ChatViewHolder>,
        @LayoutRes layoutId: Int,
        dataSource: ItemsList<ChatItem>,
        onClick: (ChatItem) -> Unit = {},
        private val onStart: (ChatItem) -> Unit = {}
) : BaseListAdapter<ChatItem>(holderType, layoutId, dataSource, onClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<ChatItem>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        (holder as ChatViewHolder).setOnStartClickListener {
            onStart.invoke(item)
        }
    }
}