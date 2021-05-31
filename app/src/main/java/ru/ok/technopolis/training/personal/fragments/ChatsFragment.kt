package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_chats.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ChatItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ChatAdapter
import ru.ok.technopolis.training.personal.viewholders.ChatViewHolder
import java.sql.Time
import java.text.DateFormat

class ChatsFragment : UserFragment() {
    private var chatsRecycler: RecyclerView? = null

    private var chatsMutableList = mutableListOf<ChatItem>()
    private val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.base_toolbar?.title = getString(R.string.chats)
        chatsRecycler = view.chats_list
        val userId  = CurrentUserRepository.currentUser.value?.id
        getChatsProfiles(userId!!) { authors ->
            chatsMutableList.clear()
            for (author in authors) {
                //TODO:TIME OF LAST MESSAGE
                getChatLastMessage(author.userId, userId) { message ->
                    chatsMutableList.add(ChatItem(author.id, author.userId, author.name, author.pictureUrlStr, " ", 0, formatter.format(message.timestamp), userId))

                val chats = ItemsList(chatsMutableList)
                val chatAdapter = ChatAdapter(
                        holderType = ChatViewHolder::class,
                        layoutId = R.layout.item_chat,
                        dataSource = chats,
                        onClick = { item ->
                            println("item ${item.id} clicked")
                            router?.showChatPage(item.chatId, null)
                        },
                        onStart = { item ->
                            println("item ${item.id} started")
                            router?.showChatPage(item.chatId, null)
                        }
                )
                chatsRecycler?.adapter = chatAdapter
                val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                chatsRecycler?.layoutManager = workoutsLayoutManager
            }

            }
        }

    }

    override fun getFragmentLayoutId() = R.layout.fragment_chats

}