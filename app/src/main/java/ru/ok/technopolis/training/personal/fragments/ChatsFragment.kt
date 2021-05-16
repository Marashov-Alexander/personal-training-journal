package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_chats.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ChatItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ChatAdapter
import ru.ok.technopolis.training.personal.viewholders.ChatViewHolder
import java.sql.Time

class ChatsFragment : BaseFragment() {
    private var chatsRecycler: RecyclerView? = null

    private var chatsMutableList = mutableListOf<ChatItem>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsRecycler = view.chats_list
        val list = listOf("Легкая атлетика", "Бейсбол", "Теннис")
        val prof = ProfileItem("1234", 123,"Иванов Иван", list, true, null, 5, 10, 23,6)
        chatsMutableList.clear()
        pushChat(0,"Dev", "Null", 5, prof)
        pushChat(1,"Java", "Forever", 0, prof)
        pushChat(2,"Kotlin", "Null", 10, prof)
        val chats = ItemsList(chatsMutableList)
        val chatAdapter = ChatAdapter(
                holderType = ChatViewHolder::class,
                layoutId = R.layout.item_chat,
                dataSource = chats,
                onClick = {item-> println("item ${item.id} clicked")
                router?.showChatPage(item.chatId)},
                onStart = { item ->
                    println("item ${item.id} started")
                    router?.showChatPage(item.chatId)
                }
        )
        chatsRecycler?.adapter = chatAdapter
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chatsRecycler?.layoutManager = workoutsLayoutManager

    }

    private fun pushChat(id: Int, name: String, description: String, unreadMessages: Int, profile: ProfileItem) {
        chatsMutableList.add(ChatItem(id.toString(), id.toLong(), name, null, description, unreadMessages, Time(System.currentTimeMillis()), mutableListOf<ProfileItem>(),profile ))

    }

    override fun getFragmentLayoutId() = R.layout.fragment_chats

}