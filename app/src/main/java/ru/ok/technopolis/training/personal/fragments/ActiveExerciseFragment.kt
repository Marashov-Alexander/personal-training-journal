package ru.ok.technopolis.training.personal.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_active_exercise.*
import kotlinx.android.synthetic.main.item_media_viewer.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ResultEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.items.ParameterItem
import ru.ok.technopolis.training.personal.items.ProgressRectItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ActiveParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ActiveParameterViewHolder
import ru.ok.technopolis.training.personal.views.MediaViewerWrapper

class ActiveExerciseFragment : ExerciseFragment() {

    private var exerciseProgressText: TextView? = null
    private var repeatsDone: View? = null
    private var repeatsLeft: View? = null
    private var parametersRecycler: RecyclerView? = null

    private var nextCard: MaterialCardView? = null
    private var nextTextView: TextView? = null

    private var skipCard: MaterialCardView? = null
    private var skipTextView: TextView? = null

    private var repeatsAllCount = -1
    private var repeatsSkippedCount = 0
    private var repeatsDoneCount = 0

    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaViewer: MediaViewerWrapper? = null

    private var workoutId = -1L
    private var userId = -1L
    private var currentIndex = -1
    private lateinit var workoutExercises: LongArray
    private lateinit var counters: IntArray
    private lateinit var progress: IntArray

    private var restValue: Float = 15.0f

    private lateinit var parametersList: ItemsList<ParameterItem>

    private var restParam: ParameterItem? = null
    private var repeatsParam: ParameterItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = CurrentUserRepository.currentUser.value?.id!!
        requireActivity().let {
            currentIndex = (it.intent.extras?.get(Page.INDEX_ID_KEY)) as Int
            workoutExercises = (it.intent.extras?.get(Page.WORKOUT_EXERCISES_ID_KEY)) as LongArray
            counters = (it.intent.extras?.get(Page.COUNTERS_ID_KEY)) as IntArray
            progress = (it.intent.extras?.get(Page.PROGRESS_ID_KEY)) as IntArray
        }

        val workoutExerciseId = workoutExercises[currentIndex]
        val currentRepeatCounter = counters[currentIndex]
        loadWorkoutExerciseInfo(workoutExerciseId) { workoutExercise: WorkoutExerciseEntity ->
            this.workoutId = workoutExercise.workoutId
            loadExerciseInfo(
                    userId,
                    workoutExercise.workoutId,
                    workoutExercise.exerciseId
            ) { exercise, category, author, userLevel, levelsMap, maxLevel, mediaData ->
                activity?.base_toolbar?.title = getString(R.string.exercise) + " \"${exercise.name}\" ($currentRepeatCounter/${workoutExercise.counter})"
                // TODO: load repeats count from parameters
                mediaViewer = MediaViewerWrapper(
                        this,
                        exercise_image_switcher,
                        no_content,
                        pos_value,
                        pos_card,
                        mediaList
                )
                mediaViewer?.setMediaData(mediaData.map { m -> m.url })

                val parameters = levelsMap.getOrDefault(userLevel!!.level, mutableListOf())
                val ordinaryParams = parameters.filter {par -> par.parameter.parameterType == ParameterEntity.PARAMETER_ORDINARY}.toMutableList()
                val goals = ordinaryParams.map { parItem -> parItem.levelExerciseParameterEntity.value }
                restParam = parameters.firstOrNull { par -> par.parameter.parameterType == ParameterEntity.PARAMETER_REST }
                repeatsParam = parameters.firstOrNull { par -> par.parameter.parameterType == ParameterEntity.PARAMETER_REPEATS }
                parametersList.setData(ordinaryParams)

                restValue = restParam?.levelExerciseParameterEntity?.value ?: 15.0f
                repeatsAllCount = repeatsParam?.levelExerciseParameterEntity?.value?.toInt() ?: 1

                setRepeatsProgress(repeatsDoneCount, repeatsAllCount)
                checkExerciseDone()
                nextCard?.setOnClickListener {
                    val results = parametersList.items.mapIndexed { index, parItem ->
                        val exerciseParameterId = parItem.levelExerciseParameterEntity.exerciseParameterId
                        val goal = goals[index]
                        val result = parItem.levelExerciseParameterEntity.value
                        ResultEntity(
                                userId,
                                exerciseParameterId,
                                workoutId,
                                goal,
                                result,
                                parItem.parameter.resultType,
                                System.currentTimeMillis()
                        )
                    }
                    createResultEntity(results) {
                        // TODO: have a rest :)
                        setRepeatsProgress(++repeatsDoneCount, repeatsAllCount)
                        checkExerciseDone()
                    }
                }
                skipCard?.setOnClickListener {
                    // TODO: create progress entity
                    repeatsSkippedCount++
                    setRepeatsProgress(++repeatsDoneCount, repeatsAllCount)
                    checkExerciseDone()
                }
            }
        }
        exerciseProgressText = exercise_progress_text
        repeatsDone = repeats_done
        repeatsLeft = repeats_left
        parametersRecycler = parameters_recycler
        nextCard = next_card
        nextTextView = next_text
        skipCard = skip_card
        skipTextView = skip_text


        parametersList = ItemsList(mutableListOf())

        parametersRecycler = parameters_recycler
        parametersRecycler?.adapter = ActiveParameterAdapter(
            holderType = ActiveParameterViewHolder::class,
            layoutId = R.layout.item_parameter_writer,
            dataSource = parametersList,
            onClick = {

            }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecycler?.layoutManager = layoutManager
    }

    private fun createResultEntity(results: List<ResultEntity>, actionsAfter: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val ids = it.resultsDao().insert(results)
                for (i in results.indices) {
                    results[i].id = ids[i]
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke()
                }
            }

        }
    }

    private fun loadWorkoutExerciseInfo(wexId: Long, actionsAfter: (WorkoutExerciseEntity) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val wex = it.workoutExerciseDao().getById(wexId)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(wex)
                }
            }

        }
    }

    private fun setRepeatsProgress(repeatsDoneCount: Int, repeatsAllCount: Int) {
        val donePercents = 100f * repeatsDoneCount / repeatsAllCount
        val leftPercents = 100f - donePercents
        val leftParams = repeatsLeft?.layoutParams as? ConstraintLayout.LayoutParams
        leftParams?.horizontalWeight = leftPercents
        val doneParams = repeatsDone?.layoutParams as? ConstraintLayout.LayoutParams
        doneParams?.horizontalWeight = donePercents
        exerciseProgressText?.text = String.format(requireContext().getString(R.string.repeats), repeatsDoneCount, repeatsAllCount)
    }

    private fun checkExerciseDone() {
        if (repeatsDoneCount == repeatsAllCount) {
            nextTextView?.text = "Завершить"
            nextTextView?.setTextColor(Color.WHITE)

            nextCard?.setOnClickListener {

                if (currentIndex < workoutExercises.size - 1) {
                    // TODO: When we should use STATUS_FAILED?
                    progress[currentIndex] = ProgressRectItem.STATUS_PASSED
                    router?.showActivePreExercisePage(
                            currentIndex + 1,
                            workoutExercises,
                            counters,
                            progress,
                            restValue.toInt()
                    )
                } else {
                    if (repeatsParam == null) {
                        router?.showWorkoutDonePage(userId, workoutId)
                    }
                    repeatsParam?.let { repeatsPar ->
                        val repeatsResult = ResultEntity(
                                userId,
                                repeatsPar.levelExerciseParameterEntity.exerciseParameterId,
                                workoutId,
                                repeatsAllCount.toFloat(),
                                (repeatsDoneCount - repeatsSkippedCount).toFloat(),
                                ParameterEntity.GREATER_BETTER,
                                System.currentTimeMillis()
                        )
                        createResultEntity(listOf(repeatsResult)) {
                            router?.showWorkoutDonePage(userId, workoutId)
                        }
                    }

                }
            }
            nextCard?.setCardBackgroundColor(requireContext().getColor(R.color.light_blue))
            nextCard?.strokeColor = requireContext().getColor(R.color.dark_cyanide)

            skipTextView?.text = "Ещё подход!"
            skipCard?.setOnClickListener {
                setRepeatsProgress(++repeatsDoneCount, repeatsAllCount)
            }
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_active_exercise
}
