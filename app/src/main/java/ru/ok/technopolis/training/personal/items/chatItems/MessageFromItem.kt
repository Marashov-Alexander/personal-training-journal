package ru.ok.technopolis.training.personal.items.chatItems

import android.view.View
import androidx.annotation.NonNull
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_element_message_from.view.*
import kotlinx.android.synthetic.main.item_message_from.view.message_text
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.lifecycle.Router
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortExerciseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder
import java.sql.Time
import java.text.DateFormat

class MessageFromItem(@NonNull val message: MessageEntity, private val router: Router) : Item<GroupieViewHolder>() {
    private val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    override fun getLayout(): Int = R.layout.item_element_message_from

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text.text = message.text
        val prof = loadSender(message.senderId)
        viewHolder.itemView.sender_name.text = prof.name
        viewHolder.itemView.message_send_time.text = formatter.format(message.timestamp)
        viewHolder.itemView.request.visibility = View.GONE
        when {
            message.userWorkoutId != null -> {
                workoutDummy(viewHolder)
            }
            message.userExerciseId != null -> {
                exDummy(viewHolder)
            }
//        val imUrl = message.senderId.pictureUrlStr
//            viewHolder.itemView.sender_icon.setImageURI(imUrl)
        }
        viewHolder.itemView.sender_icon.setImageURI(prof.pictureUrlStr)
    }

    private fun loadSender(id: Long): ProfileItem {
        val list = listOf("Легкая атлетика", "Бейсбол", "Теннис")
        return ProfileItem("1234", id, "Иванов Иван", list, true, null, 5, 10, 23, 6)
    }

    private fun workoutDummy(viewHolder: GroupieViewHolder) {
        val workout = mutableListOf(ShortWorkoutItem(message.userWorkoutId.toString(),"kk", "category", "sport", 0, 0.0))
        val workoutsList = ItemsList(workout)
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = { workoutItem ->
                    println("workout ${workoutItem.id} clicked")
                },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router.showWorkoutPage(workoutItem.id.toLong())
                }
        )
        viewHolder.itemView.message_element.adapter = workoutsAdapter
    }

    private fun exDummy(viewHolder: GroupieViewHolder) {
        val ex = mutableListOf(ShortExerciseItem(message.userExerciseId.toString(),"kk", "category", 0, 0.0))
        val exList = ItemsList(ex)
        val workoutsAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exList,
                onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router.showExercisePage(workoutItem.id.toLong())
                }
        )
        viewHolder.itemView.message_element.adapter = workoutsAdapter
    }
}
