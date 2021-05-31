package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_workout_plan.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.*
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.DayListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ScheduledWorkoutListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.listeners.InfinityScrollListener
import ru.ok.technopolis.training.personal.viewholders.DayViewHolder
import ru.ok.technopolis.training.personal.viewholders.ScheduledWorkoutViewHolder
import java.sql.Date
import java.text.DateFormatSymbols
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.Calendar.SUNDAY
import kotlin.random.Random.Default.nextInt

class WorkoutPlanFragment : WorkoutFragment() {

    private var recyclerView: RecyclerView? = null
    private var workoutsRecycler: RecyclerView? = null
    private var addWorkoutButton: FloatingActionButton? = null

    private val calendar: Calendar = Calendar.getInstance()
    private val symbols: DateFormatSymbols = DateFormatSymbols()
    private val dayNames: Array<String> = symbols.shortWeekdays

    private var daysBefore = 0
    private var daysAfter = 0

    private val daysMutableList = mutableListOf<DayItem>()
    private val workoutsMutableList = mutableListOf<ScheduledWorkoutItem>()

    private lateinit var workoutsList: ItemsList<ScheduledWorkoutItem>
    private lateinit var hintText: TextView
    private lateinit var hintArrow: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = CurrentUserRepository.currentUser.value!!.id
        recyclerView = view.days_recycler_view
        workoutsRecycler = view.scheduled_workouts_recycler
        addWorkoutButton = view.add_workout_button
        hintText = view.hint_text
        hintArrow = view.hint_arrow
        activity?.base_toolbar?.title = getString(R.string.workouts_plan)
        addWorkoutButton?.setOnClickListener {
            createNewWorkout(userId) { workoutId: Long ->
                router?.showNewWorkoutPage(workoutId, true)
            }
        }

        calendar.time = Date(System.currentTimeMillis())
        calendar.add(Calendar.DAY_OF_MONTH, -daysBefore)

        for (i in 0..7) {
            pushDay(false)
            pushDay(true)
        }

        val itemsList = SingleSelectableList(daysMutableList)
        val dayAdapter = DayListAdapter(
                holderType = DayViewHolder::class,
                layoutId = R.layout.day_item,
                dataSource = itemsList,
                onClick = { dayItem ->
                    itemsList.select(dayItem)
                    loadScheduledWorkouts(dayItem.date)
                }
        )
        recyclerView?.adapter = dayAdapter
        val dayLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        recyclerView?.layoutManager = dayLayoutManager
        recyclerView?.scrollToPosition(daysBefore - 1)
        recyclerView?.addOnScrollListener(InfinityScrollListener(
                { ahead ->
                    pushDay(ahead)
                }, daysMutableList))

        workoutsList = ItemsList(workoutsMutableList)
        val workoutAdapter = ScheduledWorkoutListAdapter(
                holderType = ScheduledWorkoutViewHolder::class,
                layoutId = R.layout.scheduled_workout_item,
                dataSource = workoutsList,
                onClick = { workoutItem ->
                    println("workout ${workoutItem.id} clicked")
                    router?.showWorkoutPage(workoutItem.workout.id)
                },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")

                    createExercisesList(workoutItem.workout.id) { workoutExercises, currentCounters, progress ->
                        router?.showActivePreExercisePage(0, workoutExercises, currentCounters, progress, 15)
                    }
                }
        )
        workoutsRecycler?.adapter = workoutAdapter
        val workoutLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        workoutsRecycler?.layoutManager = workoutLayoutManager

        val todayItem = itemsList.items.find { it.isToday }!!
        itemsList.select(todayItem)
        loadScheduledWorkouts(todayItem.date)
    }

    private fun loadScheduledWorkouts(date: java.util.Date) {
        calendar.time = date
        val weekday = if (calendar.firstDayOfWeek == SUNDAY) {
            calendar.get(Calendar.DAY_OF_WEEK) - 1 // from 0 to 6
        } else {
            (calendar.get(Calendar.DAY_OF_WEEK) - 1 + 6) % 7 // from 0 to 6
        }
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val all = it.userWorkoutDao().getAll()
                val filtered = all.filter { userWorkout ->
                    var result = false
                    if (userWorkout.restDays != null) {
                        val createdDate = Date(userWorkout.timestampFrom)
                        val daysDelta = ChronoUnit.DAYS.between(
                                date.toInstant().truncatedTo(ChronoUnit.DAYS),
                                createdDate.toInstant().truncatedTo(ChronoUnit.DAYS)
                        )
                        result = daysDelta % userWorkout.restDays!!.toLong() == 0L
                    }
                    if (!result) {
                        if (userWorkout.weekdaysMask != null) {
                            println("mask=${userWorkout.weekdaysMask!!}, bit=$weekday, and=${userWorkout.weekdaysMask!! and (1 shl weekday)}")
                        }
                        result = userWorkout.weekdaysMask != null && (userWorkout.weekdaysMask!! and (1 shl weekday)) != 0
                    }
                    result
                }.map { userWorkout ->
                    val workout = it.workoutDao().getById(userWorkout.workoutId)
                    val category = it.workoutCategoryDao().getById(workout.categoryId)
                    val sport = it.workoutSportDao().getById(workout.sportId)
                    val timeStart = if (userWorkout.weekdaysMask == null) {
                        userWorkout.getPlannedTime(0)
                    } else {
                        userWorkout.getPlannedTime(weekday)
                    }
                    // TODO: check if workout already done
                    ScheduledWorkoutItem(
                            nextInt().toString(),
                            workout,
                            userWorkout,
                            category,
                            sport,
                            timeStart,
                            false
                    )
                }.toMutableList()
                withContext(Dispatchers.Main) {
                    if (filtered.isEmpty()) {
                        hintText.visibility = VISIBLE
                        hintArrow.visibility = VISIBLE
                    } else {
                        hintText.visibility = INVISIBLE
                        hintArrow.visibility = INVISIBLE
                    }
                    workoutsList.setData(filtered)
                }
            }
        }
    }

    private fun pushDay(ahead: Boolean) {
        val index = if (ahead) daysMutableList.size else 0
        val dayId = if (ahead) daysAfter++ else ++daysBefore
        calendar.time = Date(System.currentTimeMillis())
        if (ahead) {
            calendar.add(Calendar.DAY_OF_MONTH, dayId)
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -dayId)
        }
        daysMutableList.add(
                index,
                DayItem(
                        dayId.toString(),
                        calendar.time,
                        dayNames[calendar.get(Calendar.DAY_OF_WEEK)],
                        isChosen = false,
                        isToday = dayId == 0,
                        event = EventColor.NONE
                )
        )
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_plan
}
