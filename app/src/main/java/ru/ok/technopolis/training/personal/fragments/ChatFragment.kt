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
import ru.ok.technopolis.training.personal.db.AppDatabase
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageFromItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageToItem
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.MESSAGE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.OPPONENT_ID_KEY
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository

class ChatFragment : UserFragment() {
    private var dialog: RecyclerView? = null
    private var attachButton: ImageButton? = null
    private var messageText: EditText? = null
    private var sendButton: ImageButton? = null
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    private var userId: Long = -1L

    private var messagesList: MutableList<MessageEntity>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = CurrentUserRepository.currentUser.value!!.id
        dialog = view.dialog
        attachButton = view.attach_icon
        messageText = view.message_input
        sendButton = view.send_icon
        val opponentId = (activity?.intent?.extras?.get(OPPONENT_ID_KEY) as Long)

        val messageId = (activity?.intent?.extras?.get(MESSAGE_ID_KEY) )as Long?
        getUser(opponentId) {opponent ->
            activity?.base_toolbar?.title = getString(R.string.chat) + " \"${opponent.name}\""
        }

        showMessages(opponentId)
        if (messageId != null) {
            sendShareMessage(messageId)
        }
        sendButton?.setOnClickListener {
            performSendMessage(userId, opponentId)
        }
    }

    //WIP: load from db
    private fun showMessages(opponentId: Long) {
        adapter = GroupAdapter<GroupieViewHolder>()
        GlobalScope.launch(Dispatchers.IO) {
            database?.let { appDatabase ->
                // TODO: SOCKET
                messagesList = appDatabase.messageDao().getDialog(userId, opponentId)
                val user = appDatabase.userDao().getById(opponentId)
                messagesList!!.sortBy { it.timestamp }
                for (message in messagesList!!) {
                    if (message.senderId == userId) {
                        formToMessage(message, appDatabase)
                    } else {
                        formFromMessage(message, user, appDatabase)
                    }
                }
                withContext(Dispatchers.Main) {
                    dialog?.scrollToPosition(adapter.itemCount - 1)
                    dialog?.adapter = adapter
                }
            }
        }
    }

    private fun formToMessage(message: MessageEntity, appDatabase: AppDatabase) {
        when {
            message.userWorkoutId != null -> {
                val elem = formWorkoutItem(appDatabase, message)
                adapter.add(MessageToItem(message, elem, null, router!!))
            }
            message.userExerciseId != null -> {
                val elem = formExerciseItem(appDatabase, message)
                adapter.add(MessageToItem(message, null, elem, router!!))
            }
            else -> {
                adapter.add(MessageToItem(message, null, null, router!!))
            }
        }
    }

    private fun formFromMessage(message: MessageEntity, sender: UserEntity, appDatabase: AppDatabase) {
        when {
            message.userWorkoutId != null -> {
                val elem = formWorkoutItem(appDatabase, message)
                adapter.add(MessageFromItem(message,sender, elem, null, router!!))
            }
            message.userExerciseId != null -> {
                val elem = formExerciseItem(appDatabase, message)
                adapter.add(MessageFromItem(message, sender,null, elem, router!!))
            }
            else -> {
                adapter.add(MessageFromItem(message, sender,null, null, router!!))
            }
        }
    }

    private fun formWorkoutItem(appDatabase: AppDatabase, message: MessageEntity): ShortWorkoutItem {
        val workout = appDatabase.workoutDao().getById(message.userWorkoutId!!)
        val category = appDatabase.workoutCategoryDao().getById(workout.categoryId)
        val sport = appDatabase.workoutSportDao().getById(workout.sportId)
        var image = appDatabase.workoutMediaDao().getByFirstWorkoutId(workout.id)
        if (image.isNullOrBlank()) {
            image = " "
        }
        val downloadsNumber = 0
        val rank = 0.0
        val elem = ShortWorkoutItem(
                workout.id.toString(),
                workout,
                category.name,
                sport.name,
                downloadsNumber,
                rank,
                image
        )
        return elem
    }

    private fun formExerciseItem(appDatabase: AppDatabase, message: MessageEntity): ShortExerciseItem {
        val exercise = appDatabase.exerciseDao().getById(message.userExerciseId!!)
        val category = appDatabase.exerciseCategoryDao().getById(exercise.categoryId)
        var image = appDatabase.exerciseMediaDao().getFirstByExerciseId(exercise.id)
        if (image.isNullOrBlank()) {
            image = " "
        }
        val downloadsNumber = 0
        val rank = 0.0
        val elem = ShortExerciseItem(
                exercise.id.toString(),
                exercise,
                category.name,
                downloadsNumber,
                rank,
                image
        )
        return elem
    }

    private fun sendShareMessage(messageId: Long) {
        val message = messageText?.text?.toString()
        if (!message.isNullOrBlank()) {
            GlobalScope.launch(Dispatchers.IO) {
                database?.let { appDatabase ->
                    val messageEntity = appDatabase.messageDao().getById(messageId)
                        formToMessage(messageEntity, appDatabase)
                        messageText?.text?.clear()
                        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                        dialog?.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
    }

    private fun performSendMessage(userId: Long, opponentId: Long) {
        val message = messageText?.text?.toString()
        if (!message.isNullOrBlank()) {
            GlobalScope.launch(Dispatchers.IO) {
                database?.let { appDatabase ->
                    val messageEntity = MessageEntity(
                            message, System.currentTimeMillis(), userId, opponentId, null, null, true
                    )
                    messageEntity.id = appDatabase.messageDao().insert(messageEntity)
                    withContext(Dispatchers.Main) {
//                    progressBar?.visibility = View.GONE
                        formToMessage(messageEntity, appDatabase)
                        messageText?.text?.clear()
                        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                        dialog?.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }

        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_chat
}
