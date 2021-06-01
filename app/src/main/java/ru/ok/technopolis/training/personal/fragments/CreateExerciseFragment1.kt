package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.element_exercise_parameters.*
import kotlinx.android.synthetic.main.fragment_new_exercise_1.*
import kotlinx.android.synthetic.main.fragment_new_exercise_1.next_step_card
import kotlinx.android.synthetic.main.fragment_new_workout_1.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserLevelEntity
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_CREATING_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.views.ExerciseParametersWrapper


class CreateExerciseFragment1 : ExerciseFragment() {

    private var userId: Long = -1L
    private var workoutId: Long? = null
    private var exerciseId: Long = -1L
    private var isCreating: Boolean = false
    private lateinit var exercise: ExerciseEntity
    private var userLevel: UserLevelEntity? = null

    private var nextStepCard: MaterialCardView? = null
    private lateinit var nameTextView: AutoCompleteTextView
    private lateinit var exerciseTypeSpinner: Spinner
    private lateinit var parametersWrapper: ExerciseParametersWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            workoutId = (it.intent.extras?.get(WORKOUT_ID_KEY)) as Long
            exerciseId = (it.intent.extras?.get(EXERCISE_ID_KEY)) as Long
            isCreating = (it.intent.extras?.get(EXERCISE_CREATING_ID_KEY)) as Boolean
        }
        if (workoutId == 0L ) {
            workoutId = null
        }

        if (isCreating) {
            activity?.base_toolbar?.title = getString(R.string.exercise_creation)
        } else {
            activity?.base_toolbar?.title = getString(R.string.exercise_edit)
        }

        userId = CurrentUserRepository.currentUser.value!!.id

        loadExerciseInfo(userId, workoutId, exerciseId) { exercise, category, author, userLevel, levelsMap, maxLevel, mediaData ->

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
                    router?.showNewExercisePage2(workoutId, exerciseId)
                }
            }

            this.userLevel = userLevel
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

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v.id == add_parameter_button.id) {
            menu.add(0, 0, 0, v.resources.getString(R.string.create_new)).setOnMenuItemClickListener {
                parametersWrapper.createNewParameter()
                true
            }
            GlobalScope.launch(Dispatchers.IO) {
                database?.let {
                    val parameters = it.parameterDao().getAll()
                    withContext(Dispatchers.Main) {
                        for (parameter in parameters) {
                            menu.add(1, parameter.id.toInt(), 0, parameter.name).setOnMenuItemClickListener {
                                parametersWrapper.addParameter(parameter, exercise)
                                true
                            }
                        }
                    }
                }
            }
        }

    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_1

}
