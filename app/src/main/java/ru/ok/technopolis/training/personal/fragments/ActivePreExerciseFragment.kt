package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_next_exercise.*
import kotlinx.android.synthetic.main.item_media_viewer.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseMediaEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.DescriptionDialogFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.items.ProgressRectItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.RestCountDownTimer
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ProgressRectAdapter
import ru.ok.technopolis.training.personal.viewholders.ProgressRectViewHolder
import ru.ok.technopolis.training.personal.views.MediaViewerWrapper

class ActivePreExerciseFragment : ExerciseFragment() {

    private var startCard: MaterialCardView? = null
    private var timerValue: TextView? = null
    private var exerciseDescription: TextView? = null
    private var progressRecycler: RecyclerView? = null

    private var timer: CountDownTimer? = null

    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaViewer: MediaViewerWrapper? = null

    private var userId = -1L
    private var currentIndex = -1
    private var restValue = 15
    private lateinit var workoutExercises: LongArray
    private lateinit var counters: IntArray
    private lateinit var progress: IntArray

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = CurrentUserRepository.currentUser.value?.id!!
        requireActivity().let {
            currentIndex = (it.intent.extras?.get(Page.INDEX_ID_KEY)) as Int
            workoutExercises = (it.intent.extras?.get(Page.WORKOUT_EXERCISES_ID_KEY)) as LongArray
            counters = (it.intent.extras?.get(Page.COUNTERS_ID_KEY)) as IntArray
            progress = (it.intent.extras?.get(Page.PROGRESS_ID_KEY)) as IntArray
            restValue = (it.intent.extras?.get(Page.REST_ID_KEY)) as Int
        }
        progress[currentIndex] = ProgressRectItem.STATUS_CURRENT

        val workoutExerciseId = workoutExercises[currentIndex]
        val currentRepeatCounter = counters[currentIndex]
        loadPreExerciseInfo(workoutExerciseId) { wex, exercise, author, mediaData ->
            activity?.base_toolbar?.title = getString(R.string.exercise) + " \"${exercise.name}\" ($currentRepeatCounter/${wex.counter})"
            startCard?.setOnClickListener {
                // TODO: configure userId and workoutId
                println("On start exercise")
                timer?.cancel()
                router?.showActiveExercisePage(currentIndex, workoutExercises, counters, progress)
            }
            mediaViewer = MediaViewerWrapper(
                    this,
                    exercise_image_switcher,
                    no_content,
                    pos_value,
                    pos_card,
                    mediaList
            )
            mediaViewer?.setMediaData(mediaData.map { m -> m.url })

            val description = if (exercise.description.isNullOrBlank()) "Описания нет :(" else exercise.description!!
            exerciseDescription?.setOnClickListener {
                showExerciseDescription(exercise.name, description)
            }

            // TODO: calculate rest timer
            timer = RestCountDownTimer(
                    restValue,
                    onTickListener = { timeLeft ->
                        timerValue?.text = timeLeft
                    },
                    onFinishListener = {
                        println("Слыш, работать")
                    }
            )
            timer?.start()
        }
        exerciseDescription = exercise_description
        startCard = start_card
        timerValue = timer_value

        val progressItems = progress.mapIndexed { index, progress ->
            ProgressRectItem(index.toString(), progress)
        }.toMutableList()
        val progressList = ItemsList(progressItems)

        progressRecycler = progress_recycler
        progressRecycler?.adapter = ProgressRectAdapter(
                holderType = ProgressRectViewHolder::class,
                layoutId = R.layout.item_progress,
                dataSource = progressList,
                onClick = {

                }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        progressRecycler?.layoutManager = layoutManager

    }

    private fun loadPreExerciseInfo(
            workoutExerciseId: Long,
            actionsAfter: (
                    wex: WorkoutExerciseEntity,
                    exercise: ExerciseEntity,
                    author: UserEntity,
                    mediaData: List<ExerciseMediaEntity>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val wex = it.workoutExerciseDao().getById(workoutExerciseId)
                val exercise = it.exerciseDao().getById(wex.exerciseId)
                val mediaList = it.exerciseMediaDao().getByExerciseId(exercise.id)
                val author = it.userDao().getById(exercise.authorId)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(wex, exercise, author, mediaList)
                }
            }

        }
    }


    private fun showExerciseDescription(title: String, description: String) {
        DescriptionDialogFragment(title, description)
                .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_next_exercise
}
