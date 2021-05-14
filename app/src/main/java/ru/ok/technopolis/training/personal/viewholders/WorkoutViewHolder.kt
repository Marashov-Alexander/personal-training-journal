package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_workout.view.*
import kotlinx.android.synthetic.main.item_workout_element.view.title
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity

class WorkoutViewHolder(
    itemView: View
) : BaseViewHolder<WorkoutEntity>(itemView) {

    private var startButton: ImageView = itemView.start
    private var deleteButton: ImageView = itemView.delete
    private var time: TextView = itemView.time
    private var title: TextView = itemView.title

    override fun bind(item: WorkoutEntity) {
        time.text = item.plannedTime
        title.text = item.name
    }

    fun setClickListeners(onStartClick: (View) -> Unit, onDeleteClick: (View) -> Unit) {
        startButton.setOnClickListener(onStartClick)
        deleteButton.setOnClickListener(onDeleteClick)
    }
}