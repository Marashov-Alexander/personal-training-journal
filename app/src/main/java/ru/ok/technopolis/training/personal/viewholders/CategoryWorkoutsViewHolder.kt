package ru.ok.technopolis.training.personal.viewholders

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category_and_elements.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter
import java.sql.Time

class CategoryWorkoutsViewHolder (
        itemView: View
) : BaseViewHolder<CategoryWorkoutsItem>(itemView) {

    private var categoryName: TextView = itemView.navigation_category_name
    private var workoutsList: RecyclerView = itemView.navigation_category_elements

    override fun bind(item: CategoryWorkoutsItem) {
        update(item)
    }

    fun update(item: CategoryWorkoutsItem) {
       categoryName.text = item.name
        val workoutsLayoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        workoutsList.layoutManager = workoutsLayoutManager
        val workouts = ItemsList(item.worlouts.toMutableList())
        val workoutsAdapter = ShortWorkoutListAdapter(
                holderType = ShortWorkoutViewHolder::class,
                layoutId = R.layout.item_short_workout,
                dataSource = workouts,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")},
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                }
        )
        workoutsList.adapter = workoutsAdapter
    }


    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == View.VISIBLE) {
            workoutsList.setOnClickListener { onStart() }
        }
    }
}