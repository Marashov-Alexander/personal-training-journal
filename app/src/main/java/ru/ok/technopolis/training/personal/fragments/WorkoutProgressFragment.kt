package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_workout_progress.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.controllers.ButtonGroupController
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ResultEntity
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ExerciseItem
import ru.ok.technopolis.training.personal.items.ExerciseResultsHelper
import ru.ok.technopolis.training.personal.items.ProgressItem
import ru.ok.technopolis.training.personal.items.SelectableItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.views.CustomScrollView
import ru.ok.technopolis.training.personal.views.ProgressChartView
import ru.ok.technopolis.training.personal.views.SelectableButtonWrapper
import java.lang.Math.abs
import java.sql.Date
import java.text.DateFormat
import kotlin.math.roundToInt
import kotlin.random.Random

class WorkoutProgressFragment : WorkoutFragment() {

    private var scrollView: CustomScrollView? = null
    private var chart: ProgressChartView? = null
    private var buttonGroup: ButtonGroupController? = null
    private var exerciseRecycler: RecyclerView? = null

    private val formatter: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollView = scroll_view
        chart = progress_chart
        exerciseRecycler = exercises_recycler
        activity?.base_toolbar?.title = getString(R.string.workout_progress)
        val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY)) as Long
        val userId = CurrentUserRepository.currentUser.value!!.id

        loadWorkoutProgress(userId, workoutId) { workoutEntity, exercisesResults ->
            chart?.setScrollLockListener(
                    lockListener = {
                        scrollView?.isEnableScrolling = false
                    },
                    unlockListener = {
                        scrollView?.isEnableScrolling = true
                    }
            )

            val exercisesBundles = exercisesResults.map { exerciseResult ->
                BundleItem(exerciseResult.exercise.id, exerciseResult.exercise.exercise.id, exerciseResult.exercise.exercise.name)
            }.toMutableList()
            exercisesBundles.add(0, BundleItem("0", 0, "Все"))

            val itemsList = SingleSelectableList(exercisesBundles)

            val exerciseAdapter = BundleAdapter(
                    holderType = BundleItemViewHolder::class,
                    layoutId = R.layout.item_bundle,
                    dataSource = itemsList,
                    onBundleClick = { bundleItem, _ ->
                        println("Bundle $bundleItem clicked")
                        itemsList.select(bundleItem)
                    }
            )
            exerciseRecycler?.adapter = exerciseAdapter
            val layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            exerciseRecycler?.layoutManager = layoutManager



            val selectableList = SingleSelectableList(mutableListOf(SelectableItem("0"), SelectableItem("1"), SelectableItem("2"), SelectableItem("3")))

            buttonGroup = ButtonGroupController(
                    selectableList,
                    listOf(
                            SelectableButtonWrapper(day_mode, day_chosen, day_line) {
                                println("Clicked 0")
                                selectableList.select(0)
                                switchChartMode(ProgressChartView.ChartMode.DAY)
                            },
                            SelectableButtonWrapper(week_mode, week_chosen, week_line) {
                                println("Clicked 1")
                                selectableList.select(1)
                                switchChartMode(ProgressChartView.ChartMode.WEEK)
                            },
                            SelectableButtonWrapper(month_mode, month_chosen, month_line) {
                                println("Clicked 2")
                                selectableList.select(2)
                                switchChartMode(ProgressChartView.ChartMode.MONTH)
                            },
                            SelectableButtonWrapper(year_mode, year_chosen, year_line) {
                                println("Clicked 2")
                                selectableList.select(3)
                                switchChartMode(ProgressChartView.ChartMode.YEAR)
                            }
                    )
            )
            selectableList.select(0)

            chart?.invalidate()
        }
    }


    fun switchChartMode(mode: ProgressChartView.ChartMode) {
    setResults(mode, exercisesResults)
    }

    private fun setResults(chartMode: ProgressChartView.ChartMode, exerciseResultsHelper: ExerciseResultsHelper) {
        exerciseResultsHelper.getResults(chartMode)
        chart?.bindData(
                results,
                100f,
                "% "
        )
    }

    override fun onDetach() {
        buttonGroup?.detach()
        super.onDetach()
    }

    private fun loadWorkoutProgress(
            userId: Long,
            workoutId: Long,
            actionsAfter: (
                    WorkoutEntity,
                   List<ExerciseResultsHelper>
            ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val workoutExercisesByWorkout = it.workoutExerciseDao().getAllByWorkout(workoutId)
                val workout = it.workoutDao().getById(workoutId)
                val userWorkoutEntity = it.userWorkoutDao().getById(userId, workout.id)
                val progressItems: MutableList<ProgressItem>
                val exercises: MutableList<ExerciseItem>
                    exercises = workoutExercisesByWorkout.map { workoutExercise ->
                        val exercise = it.exerciseDao().getById(workoutExercise.exerciseId)
                        val importantParameters = it.parameterDao().getImportantParameters(exercise.id)
                        val description =
                                if (importantParameters.isEmpty())
                                    ""
                                else
                                    importantParameters.map { info -> "${info.name}: ${info.value} ${info.units}" }.reduce { acc, str -> "$acc, $str" }
                        ExerciseItem(
                                Random.nextInt().toString(),
                                exercise,
                                workoutExercise,
                                description
                        )
                    }.toMutableList()
//                val resultsList = it.resultsDao().getByWorkoutAndUser(workoutId, userId)
                val exercisesResults =exercises.map {exerciseItem ->
                    val resultsList = it.resultsDao().getByWorkoutAndUserAndExercise(workoutId, userId, exerciseItem.exercise.id)
                    ExerciseResultsHelper(
                            exerciseItem,
                            resultsList
                    )
                }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workout, exercisesResults)
                }
            }
        }
    }

    private fun getResult(resultEntity: ResultEntity): Int {
        var percents = when (resultEntity.resultType) {
            ParameterEntity.LESS_BETTER -> {
                resultEntity.goal * 100f / resultEntity.result
            }
            ParameterEntity.EQUALS_BETTER -> {
                100f - abs(resultEntity.result - resultEntity.goal) * 100f / resultEntity.goal
            }
            ParameterEntity.GREATER_BETTER -> {
                resultEntity.result * 100f / resultEntity.goal
            }
            else -> {
                0f
            }
        }
        if (percents.isInfinite()) {
            percents = 100f
        } else if (percents.isNaN()) {
            percents = 0f
        }
        return percents.roundToInt()
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_progress
}
