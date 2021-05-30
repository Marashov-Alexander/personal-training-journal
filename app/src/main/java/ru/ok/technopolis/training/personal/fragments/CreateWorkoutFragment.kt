package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_new_workout_1.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.items.ExerciseItem
import ru.ok.technopolis.training.personal.items.ExercisesList
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ExerciseAdapter
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder


class CreateWorkoutFragment : WorkoutFragment() {

    private var workoutName: TextInputLayout? = null
    private var exercisesRecycler: RecyclerView? = null
    private var exercisesList: ExercisesList? = null
    private var nextStepCard: MaterialCardView? = null
    private var addExerciseButton: FloatingActionButton? = null

    private var userId = -1L
    private var workoutId = -1L

    private var chooseMode: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutId = (requireActivity().intent.extras?.get(Page.WORKOUT_ID_KEY)) as Long
        userId = CurrentUserRepository.currentUser.value!!.id

        activity?.base_toolbar?.title = getString(R.string.workout_creation)
        addExerciseButton = add_exercise_button
        workoutName = input_workout_name
        exercisesRecycler = exercises_recycler
        nextStepCard = next_step_card

        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        exercisesRecycler?.layoutManager = layoutManager

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val exerciseItem = exercisesList!!.items[position]
                removeExercise(exerciseItem) {
                    exercisesList?.remove(exerciseItem)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(exercisesRecycler)

        loadWorkoutInfo(workoutId) { workout, category, sport, exercises, author ->
            nextStepCard?.setOnClickListener {
                saveWorkoutInfo { workoutId ->
                    // TODO: go to next step
//                    router?.showNewWorkoutPage2(workoutId)
                }
            }

            addExerciseButton?.setOnClickListener {
                createNewExercise(exercisesList!!.items.size) { exerciseId: Long ->
                    router?.showNewExercisePage1(userId, workoutId, exerciseId)
                }
            }

            exercisesList = ExercisesList(exercises)
            val adapter = ExerciseAdapter(
                holderType = ExerciseItemViewHolder::class,
                layoutId = R.layout.item_exercise,
                dataSource = exercisesList!!,
                onClick = { exercise ->
                    print("Exercise $exercise clicked")
                },
                onView = {exerciseItem ->
                    if (!chooseMode) {
                        print("View exercise $exerciseItem clicked")
                        router?.showExercisePage(exerciseItem.exercise.id)
                    }
                    chooseMode
                },
                onEdit = {exerciseItem: ExerciseItem ->
                    if (!chooseMode) {
                        print("View exercise $exerciseItem clicked")
                        router?.showNewExercisePage1(userId, workoutId, exerciseItem.exercise.id)
                    }
                    chooseMode
                },
                onLongExerciseClick = { item, itemView ->
                    val popup = PopupMenu(requireContext(), itemView)
                    popup.inflate(R.menu.exercise_menu)
                    popup.setOnMenuItemClickListener(getMenuItemClickListener(item))
                    popup.show()
                }
            )
            exercisesRecycler?.adapter = adapter
        }
    }

    private fun createNewExercise(orderNumber: Int, actionsAfter: (Long) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val newExercise = ExerciseEntity("", "", 1, false, userId)
                newExercise.id = it.exerciseDao().insert(newExercise)
                val newWorkoutExercise = WorkoutExerciseEntity(workoutId, newExercise.id, orderNumber)
                newWorkoutExercise.id = it.workoutExerciseDao().insert(newWorkoutExercise)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(newExercise.id)
                }
            }
        }
    }

    private fun removeExercise(exerciseItem: ExerciseItem, actionsAfter: () -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.workoutExerciseDao().delete(exerciseItem.workoutExercise)
            withContext(Dispatchers.Main) {
                actionsAfter.invoke()
            }
        }
    }

    private fun saveWorkoutInfo(actionsAfter: (Long) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {

                var orderNumber = 1
                exercisesList?.items?.forEach { exerciseItem ->
                    val exercise = exerciseItem.exercise
                    val workoutExercise = exerciseItem.workoutExercise
                    if (workoutExercise.supersetGroupId == null) {
                        workoutExercise.counter = 1
                    }
                    workoutExercise.orderIndex = orderNumber
                    orderNumber++
                    println("On save $exercise: $workoutExercise")
                    it.workoutExerciseDao().update(workoutExercise)
                    it.exerciseDao().update(exercise)
                }

                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workoutId)
                }
            }
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_workout_1

    private fun getMenuItemClickListener(item: ExerciseItem): PopupMenu.OnMenuItemClickListener {
        return PopupMenu.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.superset_item -> {
                    chooseMode = true
                    exercisesList?.newSupersetMode()
                    addExerciseButton?.setImageResource(R.drawable.ic_baseline_check_24)
                    addExerciseButton?.setOnClickListener {
                        chooseMode = false
                        exercisesList?.supersetFromChosen()
                        addExerciseButton?.setImageResource(R.drawable.ic_add_black_24dp)
                        addExerciseButton?.setOnClickListener {
                            createNewExercise(exercisesList!!.items.size) { exerciseId: Long ->
                                router?.showNewExercisePage1(userId, workoutId, exerciseId)
                            }
                        }
                    }
                }
                R.id.remove_exercise_item -> exercisesList?.remove(item)
            }
            true
        }
    }
}
