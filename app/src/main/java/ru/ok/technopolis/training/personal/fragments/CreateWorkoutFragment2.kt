package ru.ok.technopolis.training.personal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_new_workout_2.*
import kotlinx.android.synthetic.main.item_media_loader.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.PlanWorkoutDialog
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.views.MediaLoaderWrapper
import kotlin.math.max
import kotlin.math.min


class CreateWorkoutFragment2 : WorkoutFragment() {

    private var prevStepCard: MaterialCardView? = null
    private var nextStepCard: MaterialCardView? = null
    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaLoader: MediaLoaderWrapper? = null

    private lateinit var difficultyPlus: FloatingActionButton
    private lateinit var difficultyMinus: FloatingActionButton
    private lateinit var difficultyValue: TextView
    private lateinit var categorySpinner: Spinner
    private lateinit var sportSpinner: Spinner
    private lateinit var workoutDescription: EditText

    private var userId = -1L
    private var workoutId = -1L
    private lateinit var workout: WorkoutEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutId = (requireActivity().intent.extras?.get(Page.WORKOUT_ID_KEY)) as Long
        userId = CurrentUserRepository.currentUser.value!!.id
        activity?.base_toolbar?.title = getString(R.string.workout_creation)

        prevStepCard = prev_step_card
        nextStepCard = next_step_card
        difficultyMinus = decrease_difficulty
        difficultyPlus = increase_difficulty
        difficultyValue = difficulty_value
        categorySpinner = workout_type_spinner
        sportSpinner = sport_type_spinner
        workoutDescription = workout_description

        loadWorkoutInfo(userId, workoutId, loadCategories = true, loadSports = true) { workoutEntity, userWorkout, workoutCategoryEntity, workoutSportEntity, exercises, author, categories, sports ->
            this.workout = workoutEntity
            setDifficulty(this.workout.difficulty)
            difficultyPlus.setOnClickListener { changeDifficulty(1) }
            difficultyMinus.setOnClickListener { changeDifficulty(-1) }
            workoutDescription.setText(workout.description)

            nextStepCard?.setOnClickListener {
                workout.description = workoutDescription.text.toString()
                saveWorkoutInfo(workout) {
                    router?.showWorkoutPlanPage()
                    PlanWorkoutDialog(userWorkout!!, this::savePlan)
                            .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
                }
            }

            prevStepCard?.setOnClickListener { router?.goToPrevFragment() }

            mediaLoader = MediaLoaderWrapper(
                    this,
                    exercise_image_switcher,
                    no_content,
                    edit_content_btn,
                    remove_content_btn,
                    pos_value,
                    pos_card,
                    mediaList
            )

            val categoryAdapter = ArrayAdapter<WorkoutCategoryEntity>(requireContext(), R.layout.spinner_item, categories)
            categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            categorySpinner.adapter = categoryAdapter
            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    workout.categoryId = categories[position].id
                }
            }
            categorySpinner.setSelection(categories.indexOfFirst {it.id == workout.categoryId})

            val sportAdapter = ArrayAdapter<WorkoutSportEntity>(requireContext(), R.layout.spinner_item, sports)
            sportAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            sportSpinner.adapter = sportAdapter
            sportSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    workout.sportId = sports[position].id
                }
            }
            sportSpinner.setSelection(sports.indexOfFirst {it.id == workout.sportId})
        }

    }

    private fun savePlan(userWorkout: UserWorkoutEntity, actionsAfter: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                it.userWorkoutDao().update(userWorkout)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke()
                }
            }
        }
    }

    private fun saveWorkoutInfo(workout: WorkoutEntity, actionsAfter: (Long) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                it.workoutDao().update(workout)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workoutId)
                }
            }
        }
    }

    private fun changeDifficulty(delta: Int) {
        workout.difficulty = max(min(workout.difficulty + delta, 10), 1)
        setDifficulty(workout.difficulty)
    }

    private fun setDifficulty(value: Int) {
        difficultyValue.text = value.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaLoader?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDetach() {
        super.onDetach()
        mediaLoader?.onDetach()
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_workout_2

}
