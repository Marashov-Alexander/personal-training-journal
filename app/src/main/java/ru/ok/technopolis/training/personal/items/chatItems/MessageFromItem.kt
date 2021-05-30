package ru.ok.technopolis.training.personal.items.chatItems

import android.view.View
import androidx.annotation.NonNull
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_element_message_from.view.*
import kotlinx.android.synthetic.main.item_message_from.view.message_text
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
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

class MessageFromItem(@NonNull val message: MessageEntity, private val sender: UserEntity, private val workout: ShortWorkoutItem?, private val ex: ShortExerciseItem?, private val router: Router) : Item<GroupieViewHolder>() {
    private val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    override fun getLayout(): Int = R.layout.item_element_message_from

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text.text = message.text
        val name = sender.firstName + " ${sender.fatherName}"
        viewHolder.itemView.sender_name.text = name
        viewHolder.itemView.message_send_time.text = formatter.format(message.timestamp)
        viewHolder.itemView.request.visibility = View.GONE
        when {
            message.userWorkoutId != null -> {
                workoutDummy(viewHolder, workout!!)
            }
            message.userExerciseId != null -> {
                exDummy(viewHolder, ex!!)
            }
//        val imUrl = message.senderId.pictureUrlStr
//            viewHolder.itemView.sender_icon.setImageURI(imUrl)
        }
        viewHolder.itemView.sender_icon.setImageURI(sender.avatarUrl)
    }

    private fun workoutDummy(viewHolder: GroupieViewHolder, workout: ShortWorkoutItem) {
        val workoutsList = ItemsList(mutableListOf(workout))
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workoutsList,
                onClick = { workoutItem ->
                    println("workout ${workoutItem.id} clicked")
                },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router.showWorkoutPage(workoutItem.workout.id)
                }
        )
        viewHolder.itemView.message_element.adapter = workoutsAdapter
    }

    private fun exDummy(viewHolder: GroupieViewHolder, ex: ShortExerciseItem) {
        val exList = ItemsList(mutableListOf(ex))
        val workoutsAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercise,
                dataSource = exList,
                onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router.showExercisePage(workoutItem.exercise.id)
                }
        )
        viewHolder.itemView.message_element.adapter = workoutsAdapter
    }
}
