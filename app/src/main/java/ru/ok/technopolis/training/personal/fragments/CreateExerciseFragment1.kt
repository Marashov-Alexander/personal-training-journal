package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.element_exercise_parameters.*
import kotlinx.android.synthetic.main.fragment_new_exercise_1.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserLevelEntity
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.USER_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import ru.ok.technopolis.training.personal.views.ExerciseParametersWrapper


class CreateExerciseFragment1 : ExerciseFragment() {

    private var userId: Long = -1L
    private var workoutId: Long = -1L
    private var exerciseId: Long = -1L
    private lateinit var exercise: ExerciseEntity
    private lateinit var userLevel: UserLevelEntity

    private var nextStepCard: MaterialCardView? = null
    private lateinit var nameTextView: AutoCompleteTextView
    private lateinit var exerciseTypeSpinner: Spinner
    private lateinit var parametersWrapper: ExerciseParametersWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.base_toolbar?.title = getString(R.string.exercise_creation)
        activity?.let {
            userId = (it.intent.extras?.get(USER_ID_KEY)) as Long
            workoutId = (it.intent.extras?.get(WORKOUT_ID_KEY)) as Long
            exerciseId = (it.intent.extras?.get(EXERCISE_ID_KEY)) as Long
        }


        loadExerciseInfo(userId, workoutId, exerciseId) { exercise, author, redactor, userLevel, levelsMap, maxLevel ->

            nameTextView.setText(exercise.name)
            nextStepCard?.setOnClickListener {
                // сохранение информации об упражнении
                this.exercise.name = nameTextView.text.toString()
                // сохранение упражнения в базу данных
                saveChanges(
                    this.exercise,
                    parametersWrapper.levelsCount,
                    parametersWrapper.levelsMap,
                    parametersWrapper.removedParameters
                ) {
                    router?.showNewExercisePage2()
                }
            }

            this.userLevel = userLevel!!
            this.exercise = exercise

            parametersWrapper = ExerciseParametersWrapper(
                this,
                parameters_recycler,
                add_parameter_button,
                increase_level,
                decrease_level,
                level_value,
                levelsMap = levelsMap,
                userLevel = userLevel,
                maxLevel = maxLevel,
                editable = true
            )
        }

        exerciseTypeSpinner = exercise_type_spinner
        nameTextView = name
        nextStepCard = next_step_card
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_1

}
