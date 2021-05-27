package ru.ok.technopolis.training.personal.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.items.ChatItem
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageFromItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageToItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import java.sql.Time

class ChatFragment : BaseFragment() {
    private var dialog: RecyclerView? = null
    private var attachButton: ImageButton? = null
    private var messageText: EditText? = null
    private var sendButton: ImageButton? = null
    private var chat: ChatItem? = null
    private var prof: ProfileItem? = null
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = view.dialog
        attachButton = view.attach_icon
        messageText = view.message_input
        sendButton = view.send_icon

        val chatId = (activity?.intent?.extras?.get(Page.CHAT_ID_KEY) as Long)
        val list = listOf("Легкая атлетика", "Бейсбол", "Теннис")
        prof = ProfileItem("1234", 123, "Иванов Иван", list, true, null, 5, 10, 23, 6)
        chat = ChatItem(chatId.toString(), chatId, "Dev", null, "Null", 5, Time(System.currentTimeMillis()), mutableListOf<ProfileItem>(), prof!!)
        activity?.base_toolbar?.title = getString(R.string.chat) + " \"${chat!!.name}\""
        addDummyMessages()
        sendButton?.setOnClickListener {
            print { "WE ARE SENDING//////////////////////////////" }
            performSendMessage()
        }
        dialog?.scrollToPosition(adapter.itemCount - 1)
    }


    //WIP: load from db
    private fun showMessages() {
        GlobalScope.launch(Dispatchers.IO) {
            activity?.runOnUiThread {
//                progressBar?.visibility = View.VISIBLE
            }
        }
    }


    private fun addDummyMessages() {

        val oldMessageTo = chat?.chatId?.let { MessageEntity("lsls", System.currentTimeMillis(), 12, it, 1, null) }
        val oldMessageFrom = prof?.userId?.let { chat?.chatId?.let { it1 -> MessageEntity("fff", System.currentTimeMillis(), it, it1, null, 1) } }
        oldMessageFrom?.let { MessageFromItem(it, router!!) }?.let { adapter.add(it) }
        oldMessageTo?.let { MessageToItem(it, router!!) }?.let { adapter.add(it) }
        val oldMessageTo2 = chat?.chatId?.let { MessageEntity("lslkkkkkkkkkkkkkkkkkkkks", System.currentTimeMillis(), 12, it, null, null) }
        val oldMessageFrom2 = prof?.userId?.let { chat?.chatId?.let { it1 -> MessageEntity("ffkkkkkkkkkkkkkkkfffffffffffffffffffffffffffffkkkkkkkkkf", System.currentTimeMillis(), it, it1, null, null) } }
        oldMessageFrom2?.let { MessageFromItem(it, router!!) }?.let { adapter.add(it) }
        oldMessageTo2?.let { MessageToItem(it, router!!) }?.let { adapter.add(it) }
        dialog?.adapter = adapter
    }

    private fun performSendMessage() {
        val message = messageText?.text?.toString()
        if (!message.isNullOrBlank()) {
            GlobalScope.launch(Dispatchers.IO) {
                val messageEntity = prof?.userId?.let {
                    chat?.chatId?.let { it1 ->
                        MessageEntity(
                                message, System.currentTimeMillis(), it, it1, null, null
                        )
                    }
                }
                messageEntity?.id = messageEntity?.let { database?.messageDao()?.insert(it) }!!
            }
        }


//        val lasMessage = database!!.messageDao().getAll().last()
        val lastMessage = chat?.chatId?.let { message?.let { it1 -> MessageEntity(it1, System.currentTimeMillis(), 12, it, null, null) } }
        lastMessage?.let { MessageToItem(it, router!!) }?.let { adapter.add(it) }
        dialog?.adapter = adapter
        messageText?.text?.clear()
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
        dialog?.scrollToPosition(adapter.itemCount - 1)
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_chat
}