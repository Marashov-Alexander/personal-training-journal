package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category_and_elements.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryExerciseItem

import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ShortExerciseListAdapter


class CategoryExerciseViewHolder  (
        itemView: View
) : BaseViewHolder<CategoryExerciseItem>(itemView) {

    private var categoryName: TextView = itemView.navigation_category_name
    private var exercisesList: RecyclerView = itemView.navigation_category_elements

    override fun bind(item: CategoryExerciseItem) {
        update(item)
    }

    fun update(item: CategoryExerciseItem) {
        categoryName.text = item.name
        val workoutsLayoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        exercisesList.layoutManager = workoutsLayoutManager
        val exercises = ItemsList(item.exercises.toMutableList())
        val workoutsAdapter = ShortExerciseListAdapter(
                holderType = ShortExerciseViewHolder::class,
                layoutId = R.layout.item_short_exercice,
                dataSource = exercises,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")},
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                }
        )
        exercisesList.adapter = workoutsAdapter
    }


    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == View.VISIBLE) {
            exercisesList.setOnClickListener { onStart() }
        }
    }
}