package ru.ok.technopolis.training.personal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_new_exercise_2.*
import kotlinx.android.synthetic.main.item_media_loader.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseMediaEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.items.MuscleItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.views.MediaLoaderWrapper
import ru.ok.technopolis.training.personal.views.MultiSpinner


class CreateExerciseFragment2 : ExerciseFragment(), MultiSpinner.MultiSpinnerListener {

    private var prevStepCard: MaterialCardView? = null
    private var nextStepCard: MaterialCardView? = null
    private lateinit var muscleGroupsLabel: TextView
    private lateinit var exerciseDescription: EditText
    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaLoader: MediaLoaderWrapper? = null
    private var muscleGroupsValue: MultiSpinner? = null

    private val muscles = mutableListOf(
        MuscleItem("1", "Руки бицепс", false),
        MuscleItem("2", "Руки трицепс", false),
        MuscleItem("3", "Предплечье", false),
        MuscleItem("4", "Пресс", false),
        MuscleItem("5", "Кардио", false),
        MuscleItem("6", "Растяжка", false),
        MuscleItem("7", "Грудь", false),
        MuscleItem("8", "Спина", false),
        MuscleItem("9", "Ноги", false),
        MuscleItem("10", "Ягодичные мышцы", false),
        MuscleItem("11", "Дельты", false)
    )

        var workoutId: Long = -1L
        var exerciseId: Long = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.base_toolbar?.title = getString(R.string.exercise_creation)

        activity?.let {
            workoutId = (it.intent.extras?.get(Page.WORKOUT_ID_KEY)) as Long
            exerciseId = (it.intent.extras?.get(Page.EXERCISE_ID_KEY)) as Long
        }

        exerciseDescription = exercise_description
        nextStepCard = next_step_card
        prevStepCard = prev_step_card
        muscleGroupsValue = muscle_groups_value
        muscleGroupsLabel = muscle_groups_label

        val userId = CurrentUserRepository.currentUser.value!!.id
        loadExerciseInfo(userId, workoutId, exerciseId) { exercise, category, author, userLevel, levelsMap, maxLevel, mediaData ->
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
            mediaLoader?.setMediaData(mediaData.map { m -> m.url })
            exerciseDescription.setText(exercise.description)

            val defaultText = exercise.muscles
            // TODO: parse muscles from exercise.muscles
            muscleGroupsValue?.setItems(muscles.map {it.name}, defaultText, this, muscles.map {it.checked}.toBooleanArray())

            prevStepCard?.setOnClickListener {
                router?.goToPrevFragment()
            }
            nextStepCard?.setOnClickListener {
                exercise.description = exerciseDescription.text.toString()
                val musclesCollection = muscles.filter {it.checked}
                if (musclesCollection.isEmpty()) {
                    exercise.muscles = "Не указано"
                } else {
                    exercise.muscles = muscles.map { it.name }.reduce { acc, str -> "$acc, $str" }
                }
                saveExerciseInfo(
                        exercise,
                        mediaLoader!!.getLoadedData().map { str -> ExerciseMediaEntity(str, true, exerciseId) }
                ) {
                    // TODO: congratulations and navigate to place where called
                    router?.showExercisePage(exerciseId)
                }
            }
        }



    }

    private fun saveExerciseInfo(
            // объект упражнения с обновлёнными полями
            exercise: ExerciseEntity,
            mediaData: List<ExerciseMediaEntity>,
            actionsAfter: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            database?.let { database ->
                database.exerciseDao().update(exercise)
                database.exerciseMediaDao().deleteByExercise(exerciseId)
                val ids = database.exerciseMediaDao().insert(mediaData)
                for (i in mediaData.indices) {
                    mediaData[i].id = ids[i]
                }
            }
            withContext(Dispatchers.Main) {
                actionsAfter.invoke()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaLoader?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDetach() {
        super.onDetach()
        mediaLoader?.onDetach()
    }

    override fun onItemsSelected(selected: BooleanArray?) {
        selected?.forEachIndexed { ind, b ->
            muscles[ind].checked = b
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_2

}
