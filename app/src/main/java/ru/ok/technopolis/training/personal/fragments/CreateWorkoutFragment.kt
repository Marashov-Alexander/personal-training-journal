package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_new_workout_1.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ExerciseItem
import ru.ok.technopolis.training.personal.items.ExercisesList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ExerciseAdapter
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder


class CreateWorkoutFragment : BaseFragment() {

    private var workoutName: TextInputLayout? = null
    private var exercisesRecycler: RecyclerView? = null
    private var actionButton: FloatingActionButton? = null
    private var exercisesList: ExercisesList? = null
    private var nextStepCard: MaterialCardView? = null
    private var addExerciseButton: FloatingActionButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addExerciseButton = add_exercise_button
        workoutName = input_workout_name
        exercisesRecycler = exercises_recycler
        actionButton = add_exercise_button
        nextStepCard = next_step_card
        nextStepCard?.setOnClickListener {
            router?.showNewWorkoutPage2()
        }

        addExerciseButton?.setOnClickListener {
            router?.showNewExercisePage1()
        }

        exercisesList = ExercisesList(mutableListOf(
                ExerciseItem("1", 1, "Упражнение 1", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("2", 1, "Упражнение 2", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("3", 1, "Упражнение 3", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("4", 1, "Упражнение 4", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("5", 1, "Упражнение 5", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("6", 1, "Упражнение 6", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("7", 1, "Упражнение 7", "5 подходов, 5 повторений", supersetGroupId = null),
                ExerciseItem("8", 1, "Упражнение 8", "5 подходов, 5 повторений", supersetGroupId = null)
        ))

        val adapter = ExerciseAdapter(
                holderType = ExerciseItemViewHolder::class,
                layoutId = R.layout.item_exercise,
                dataSource = exercisesList!!,
                onClick = { exercise ->
                    print("Exercise $exercise clicked")
                },
                onLongExerciseClick = { item, itemView ->
                    val popup = PopupMenu(requireContext(), itemView)
                    popup.inflate(R.menu.exercise_menu)
                    popup.setOnMenuItemClickListener(getMenuItemClickListener(item))
                    popup.show()
                }
        )
        exercisesRecycler?.adapter = adapter
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        exercisesRecycler?.layoutManager = layoutManager

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                exercisesList?.remove(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(exercisesRecycler)
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_workout_1

    private fun getMenuItemClickListener(item: ExerciseItem): PopupMenu.OnMenuItemClickListener {
        return PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.superset_item -> {
                    exercisesList?.createSuperset()
                    actionButton?.setImageResource(R.drawable.ic_baseline_check_24)
                    actionButton?.setOnClickListener {
                        exercisesList?.saveSuperset()
                        actionButton?.setImageResource(R.drawable.ic_add_black_24dp)
                    }
                }
                R.id.remove_exercise_item -> exercisesList?.remove(item)
            }
            true
        }
    }
}
