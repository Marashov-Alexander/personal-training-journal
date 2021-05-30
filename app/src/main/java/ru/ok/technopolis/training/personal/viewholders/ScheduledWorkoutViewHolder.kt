package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.scheduled_workout_item.view.*
import ru.ok.technopolis.training.personal.items.ScheduledWorkoutItem

class ScheduledWorkoutViewHolder(
    itemView: View
) : BaseViewHolder<ScheduledWorkoutItem>(itemView) {

    private var doneImage: ImageView = itemView.done_image
    private var time: TextView = itemView.workout_time
    private var name: TextView = itemView.workout_name
    private var category: TextView = itemView.workout_category
    private var sport: TextView = itemView.workout_sport
    private var startCard: MaterialCardView = itemView.workout_start_card
    private var start: TextView = itemView.workout_start

    override fun bind(item: ScheduledWorkoutItem) {
        update(item)
    }

    fun update(item: ScheduledWorkoutItem) {
        time.text = item.timeStart
        name.text = item.workout.name
        category.text = item.category.name
        sport.text = item.sport.name
        doneImage.visibility = if (item.done) VISIBLE else INVISIBLE
    }

    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == VISIBLE) {
            startCard.setOnClickListener { onStart() }
            start.setOnClickListener { onStart() }
        }
    }
}