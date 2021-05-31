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
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ExerciseResultsHelper
import ru.ok.technopolis.training.personal.items.SelectableItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.views.CustomScrollView
import ru.ok.technopolis.training.personal.views.ProgressChartView
import ru.ok.technopolis.training.personal.views.SelectableButtonWrapper

class WorkoutProgressFragment : WorkoutFragment() {

    private var scrollView: CustomScrollView? = null
    private var chart: ProgressChartView? = null
    private var buttonGroup: ButtonGroupController? = null
    private var exerciseRecycler: RecyclerView? = null

    private lateinit var chartMode: ProgressChartView.ChartMode
    private lateinit var currentExerciseResult: ExerciseResultsHelper
    private lateinit var exercisesResults: List<ExerciseResultsHelper>
    private lateinit var workoutEntity: WorkoutEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollView = scroll_view
        chart = progress_chart
        exerciseRecycler = exercises_recycler
        activity?.base_toolbar?.title = getString(R.string.workout_progress)
        val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY)) as Long
        val userId = CurrentUserRepository.currentUser.value!!.id

        loadWorkoutProgress(userId, workoutId) { workoutEntity, exercisesResults ->
            this.chartMode = ProgressChartView.ChartMode.DAY
            this.workoutEntity = workoutEntity
            this.exercisesResults = exercisesResults
            chart?.setScrollLockListener(
                    lockListener = {
                        scrollView?.isEnableScrolling = false
                    },
                    unlockListener = {
                        scrollView?.isEnableScrolling = true
                    }
            )

            val exercisesBundles = exercisesResults.map { exerciseResult ->
                BundleItem(exerciseResult.exercise.id.toString(), exerciseResult.exercise.id, exerciseResult.exercise.name)
            }.toMutableList()

            val itemsList = SingleSelectableList(exercisesBundles)

            val exerciseAdapter = BundleAdapter(
                    holderType = BundleItemViewHolder::class,
                    layoutId = R.layout.item_bundle,
                    dataSource = itemsList,
                    onBundleClick = { bundleItem, _ ->
                        println("Bundle $bundleItem clicked")
                        itemsList.select(bundleItem)
                        currentExerciseResult = exercisesResults.find {erh -> erh.exercise.id == bundleItem.itemId}!!
                        setResults(chartMode, currentExerciseResult)
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
                                chartMode = ProgressChartView.ChartMode.DAY
                                setResults(chartMode, currentExerciseResult)
                            },
                            SelectableButtonWrapper(week_mode, week_chosen, week_line) {
                                println("Clicked 1")
                                selectableList.select(1)
                                chartMode = ProgressChartView.ChartMode.WEEK
                                setResults(chartMode, currentExerciseResult)
                            },
                            SelectableButtonWrapper(month_mode, month_chosen, month_line) {
                                println("Clicked 2")
                                selectableList.select(2)
                                chartMode = ProgressChartView.ChartMode.MONTH
                                setResults(chartMode, currentExerciseResult)
                            },
                            SelectableButtonWrapper(year_mode, year_chosen, year_line) {
                                println("Clicked 3")
                                selectableList.select(3)
                                chartMode = ProgressChartView.ChartMode.YEAR
                                setResults(chartMode, currentExerciseResult)
                            }
                    )
            )
            selectableList.select(0)
            itemsList.select(0)

            chart?.invalidate()
        }
    }

    private fun setResults(chartMode: ProgressChartView.ChartMode, exerciseResultsHelper: ExerciseResultsHelper) {
        val results = exerciseResultsHelper.getResults(chartMode)
        chart?.bindData(results, 100f, "% ")
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
                val exercises: MutableList<ExerciseEntity> = workoutExercisesByWorkout.map { workoutExercise ->
                        it.exerciseDao().getById(workoutExercise.exerciseId)
                    }.toMutableList()

                val exercisesResults =exercises.map {exercise ->
                    val resultsList = it.resultsDao().getByWorkoutAndUserAndExercise(workoutId, userId, exercise.id)
                    ExerciseResultsHelper(
                            exercise,
                            resultsList
                    )
                }.toMutableList()
                exercisesResults.add(
                        0,
                        ExerciseResultsHelper(
                                ExerciseEntity("Все", null, categoryId = 1, isPublic = false, authorId = userId, id = 0),
                                it.resultsDao().getByWorkoutAndUser(workoutId, userId)
                        )
                )
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(workout, exercisesResults)
                }
            }
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_progress
}
