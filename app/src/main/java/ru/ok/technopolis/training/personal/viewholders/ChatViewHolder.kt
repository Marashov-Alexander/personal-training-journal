package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.item_chat.view.*
import ru.ok.technopolis.training.personal.items.ChatItem
import java.text.DateFormat


class ChatViewHolder (
        itemView: View
) : BaseViewHolder<ChatItem>(itemView) {

    private var chatIcon: SimpleDraweeView = itemView.chat_icon
    private var chatName: TextView = itemView.chat_name
    private var chatInfo: TextView = itemView.chat_info
    private var lastMessageTime: TextView = itemView.last_message_time
    private var unreadMessageNumber: TextView = itemView.unread_messages_number

    private val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
    override fun bind(item: ChatItem) {
        update(item)
    }

    fun update(item: ChatItem) {
        chatIcon.setImageURI(item.pictureUrlStr)
        chatName.text = item.name
        chatInfo.text = item.description
        lastMessageTime.text = formatter.format(item.lastMessageTime)
        unreadMessageNumber.text = item.unreadMessages.toString()
        if (item.unreadMessages == 0) {
            unreadMessageNumber.visibility = View.INVISIBLE
        }
    }

    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == View.VISIBLE) {
            chatInfo.setOnClickListener { onStart() }
            chatName.setOnClickListener { onStart() }
            chatIcon.setOnClickListener{onStart()}
            lastMessageTime.setOnClickListener{onStart()}
            unreadMessageNumber.setOnClickListener{onStart()}
        }
    }

}
