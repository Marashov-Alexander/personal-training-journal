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
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageFromItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageToItem
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.OPPONENT_ID_KEY
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository

class ChatFragment : BaseFragment() {
    private var dialog: RecyclerView? = null
    private var attachButton: ImageButton? = null
    private var messageText: EditText? = null
    private var sendButton: ImageButton? = null
    private val adapter = GroupAdapter<GroupieViewHolder>()

    private var oponent: ProfileItem? = null
    private var prof: ProfileItem? = null

    private var userId: Long? = null

    private var messagesList: MutableList<MessageEntity>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = view.dialog
        attachButton = view.attach_icon
        messageText = view.message_input
        sendButton = view.send_icon
        val opponentId = (activity?.intent?.extras?.get(OPPONENT_ID_KEY) as Long)

        val list = listOf("Легкая атлетика", "Бейсбол", "Теннис")

        prof = ProfileItem("1234", 123, "Иванов Иван", list, true, null, 5, 10, 23, 6)

        oponent = ProfileItem(opponentId.toString(), opponentId, "Иванов Иван", list, true, null, 5, 10, 23, 6)

        activity?.base_toolbar?.title = getString(R.string.chat) + " \"${oponent!!.name}\""
//        addDummyMessages()
        showMessages(opponentId)
        dialog?.scrollToPosition(adapter.itemCount - 1)
        sendButton?.setOnClickListener {
            print { "WE ARE SENDING//////////////////////////////" }
            performSendMessage()
        }
    }


    //WIP: load from db
    private fun showMessages(opponentId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val email = CurrentUserRepository.currentUser.value!!.email
            val user = database!!.userDao().getByEmail(email)
            userId = user.id
            database?.let { appDatabase ->
                // TODO: DO SOMETHING
                messagesList = appDatabase.messageDao().getDialog(userId!!, opponentId)

                withContext(Dispatchers.Main) {
                    messagesList!!.sortBy { it.timestamp }
                    for (message in messagesList!!) {
                        if (message.senderId == userId) {
                            adapter.add(MessageToItem(message, router!!))
                        } else {
                            adapter.add(MessageFromItem(message, router!!))
                        }
                    }
//                messagesList = appDatabase.messageDao().getDialog(chatId)

//                withContext(Dispatchers.Main) {
////                    progressBar?.visibility = View.GONE
                    dialog?.adapter = adapter
                }
            }
        }
    }


//    private fun addDummyMessages() {
//
//        val oldMessageTo = oponent?.userId?.let { MessageEntity("lsls", System.currentTimeMillis(), 12, it, 1, null, true) }
//        val oldMessageFrom = prof?.userId?.let { oponent?.userId?.let { it1 -> MessageEntity("fff", System.currentTimeMillis(), it, it1, null, 1, true) } }
//        oldMessageFrom?.let { MessageFromItem(it, router!!) }?.let { adapter.add(it) }
//        oldMessageTo?.let { MessageToItem(it, router!!) }?.let { adapter.add(it) }
//        val oldMessageTo2 = oponent?.userId?.let { MessageEntity("lslkkkkkkkkkkkkkkkkkkkks", System.currentTimeMillis(), 12, it, null, null, true) }
//        val oldMessageFrom2 = prof?.userId?.let { oponent?.userId?.let { it1 -> MessageEntity("ffkkkkkkkkkkkkkkkfffffffffffffffffffffffffffffkkkkkkkkkf", System.currentTimeMillis(), it, it1, null, null, true) } }
//        oldMessageFrom2?.let { MessageFromItem(it, router!!) }?.let { adapter.add(it) }
//        oldMessageTo2?.let { MessageToItem(it, router!!) }?.let { adapter.add(it) }
//        dialog?.adapter = adapter
//    }

    private fun performSendMessage() {
        val message = messageText?.text?.toString()
        if (!message.isNullOrBlank()) {
            GlobalScope.launch(Dispatchers.IO) {
                val messageEntity = MessageEntity(
                        message, System.currentTimeMillis(), userId!!, userId!!, null, null, true
                )
                messageEntity.id = database?.messageDao()?.insert(messageEntity)!!
                withContext(Dispatchers.Main) {
//                    progressBar?.visibility = View.GONE
                    val messageToItem = MessageToItem(messageEntity, router!!)
                    adapter.add(messageToItem)
                    messageText?.text?.clear()
                    val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    dialog?.scrollToPosition(adapter.itemCount - 1)
                }
            }

        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_chat
}
