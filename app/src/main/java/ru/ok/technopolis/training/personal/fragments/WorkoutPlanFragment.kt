package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
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
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.items.*
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.DayListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ScheduledWorkoutListAdapter
import ru.ok.technopolis.training.personal.utils.recycler.listeners.InfinityScrollListener
import ru.ok.technopolis.training.personal.viewholders.DayViewHolder
import ru.ok.technopolis.training.personal.viewholders.ScheduledWorkoutViewHolder
import java.sql.Date
import java.sql.Time
import java.text.DateFormatSymbols
import java.util.*

class WorkoutPlanFragment : BaseFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = CurrentUserRepository.currentUser.value!!.id
        recyclerView = view.days_recycler_view
        workoutsRecycler = view.scheduled_workouts_recycler
        addWorkoutButton = view.add_workout_button
        activity?.base_toolbar?.title = getString(R.string.workouts_plan)
        addWorkoutButton?.setOnClickListener {
            createNewWorkout(userId) { workoutId: Long ->
                router?.showNewWorkoutPage(workoutId)
            }
        }

        calendar.time = Date(System.currentTimeMillis())
        calendar.add(Calendar.DAY_OF_MONTH, -daysBefore)

        for (i in 0..7) {
            pushDay(false)
            pushDay(true)
        }

        val itemsList = SingleSelectableList(daysMutableList)
        itemsList.select( itemsList.items.find { it.isToday }!! )
        val dayAdapter = DayListAdapter(
                holderType = DayViewHolder::class,
                layoutId = R.layout.day_item,
                dataSource = itemsList,
                onClick = { dayItem ->
                    itemsList.select(dayItem)
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

        for (i in 1..3) pushWorkout(i, false)
        pushWorkout(4, true)
        val workoutsList = ItemsList(workoutsMutableList)
        val workoutAdapter = ScheduledWorkoutListAdapter(
                holderType = ScheduledWorkoutViewHolder::class,
                layoutId = R.layout.scheduled_workout_item,
                dataSource = workoutsList,
                onClick = { workoutItem ->
                    println("workout ${workoutItem.id} clicked")
                    router?.showWorkoutPage(workoutItem.id.toLong())
                },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    // TODO: configure userId and workoutId
                    router?.showActivePreExercisePage(0, 0)
                }
        )
        workoutsRecycler?.adapter = workoutAdapter
        val workoutLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        workoutsRecycler?.layoutManager = workoutLayoutManager
    }

    private fun createNewWorkout(userId: Long, actionsAfter: (Long) -> Unit?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val newWorkout = WorkoutEntity("", "", 1L, 1L, 1, false, userId)
                newWorkout.id = it.workoutDao().insert(newWorkout)
                val newUserWorkout = UserWorkoutEntity(userId, newWorkout.id, true, System.currentTimeMillis())
                newUserWorkout.id = it.userWorkoutDao().insert(newUserWorkout)
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(newWorkout.id)
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
                        event = EventColor.GREEN
                )
        )
    }

    private fun pushWorkout(id: Int, invisible: Boolean) {
        workoutsMutableList.add(
                ScheduledWorkoutItem(
                        id.toString(),
                        Time(System.currentTimeMillis()),
                        "Моя тренировочка",
                        "Общеукрепляющая",
                        "ОФП",
                        "40 минут",
                        true,
                        invisible
                )
        )
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_plan
}
