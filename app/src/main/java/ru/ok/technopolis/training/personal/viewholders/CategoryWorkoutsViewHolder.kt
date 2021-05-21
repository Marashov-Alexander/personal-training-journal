package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_library_elements.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortWorkoutListAdapter

class CategoryWorkoutsViewHolder(
        itemView: View
) :BaseViewHolder<CategoryWorkoutsItem>(itemView){

    private var categoryName: TextView = itemView.navigation_category_name
    private var workoutsList: RecyclerView = itemView.navigation_category_elements
    private var viewWorkout: (Long) -> Unit = {}

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
                onClick = {},
                onStart = { workoutItem ->
                    viewWorkout.invoke(workoutItem.id.toLong())
                }
        )
        workoutsList.adapter = workoutsAdapter
    }


    fun setOnStartClickListener(onStart: (Long) -> Unit) {
        this.viewWorkout = onStart
    }

}