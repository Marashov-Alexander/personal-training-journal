package ru.ok.technopolis.training.personal.items.chatItems

import android.view.View
import androidx.annotation.NonNull
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_element_message_from.view.*
import kotlinx.android.synthetic.main.item_message_to.view.message_send_time
import kotlinx.android.synthetic.main.item_message_to.view.message_text
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import ru.ok.technopolis.training.personal.viewholders.ShortExerciseViewHolder
import ru.ok.technopolis.training.personal.viewholders.ShortWorkoutViewHolder
import java.sql.Time
import java.text.DateFormat

class MessageToItem(@NonNull val message: MessageEntity)  : Item<GroupieViewHolder>() {
    private val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
    override fun getLayout(): Int = R.layout.item_element_message_to

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_text.text = message.text
        viewHolder.itemView.message_send_time.text = formatter.format(message.sendTime)
        viewHolder.itemView.request.visibility = View.GONE
        when {
            message.workoutId != null -> {
                workoutDummy(viewHolder)
            }
            message.exerciseId != null -> {
                exDummy(viewHolder)
            }
            message.requestId != null -> {
                viewHolder.itemView.request.visibility = View.VISIBLE
            }
        }
    }
        private fun workoutDummy(viewHolder: GroupieViewHolder) {
            val workout = mutableListOf(ShortWorkoutItem(message.workoutId.toString(), Time(System.currentTimeMillis()), "kk", "category", "sport", "40 min", true, 0, 0.0, false))
            val workoutsList = ItemsList(workout)
            val workoutsAdapter = ShortWorkoutListAdapter(
                    holderType = ShortWorkoutViewHolder::class,
                    layoutId = R.layout.item_short_workout,
                    dataSource = workoutsList,
                    onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                    onStart = { workoutItem ->
                        println("workout ${workoutItem.id} started")
                    }
            )
            viewHolder.itemView.message_element.adapter = workoutsAdapter
        }
        private fun exDummy(viewHolder: GroupieViewHolder) {
            val ex = mutableListOf(ShortExerciseItem(message.exerciseId.toString(), Time(System.currentTimeMillis()), "kk", "category", "sport",  true, 0, 0.0))
            val exList = ItemsList(ex)
            val workoutsAdapter = ShortExerciseListAdapter(
                    holderType = ShortExerciseViewHolder::class,
                    layoutId = R.layout.item_short_exercice,
                    dataSource = exList,
                    onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                    onStart = { workoutItem ->
                        println("workout ${workoutItem.id} started")
                    }
            )
            viewHolder.itemView.message_element.adapter = workoutsAdapter
        }
}