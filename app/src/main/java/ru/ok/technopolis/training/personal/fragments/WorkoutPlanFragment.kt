package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_workout_plan.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.DayItem
import ru.ok.technopolis.training.personal.items.EventColor
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.DayListAdapter
import ru.ok.technopolis.training.personal.viewholders.DayViewHolder
import java.sql.Date

class WorkoutPlanFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.days_recycler_view

        val itemsList = ItemsList(mutableListOf(
                DayItem("0", Date(System.currentTimeMillis()), "Пн", isChosen = false, isToday = false, event = EventColor.RED),
                DayItem("1", Date(System.currentTimeMillis()), "Вт", isChosen = false, isToday = false, event = EventColor.NONE),
                DayItem("2", Date(System.currentTimeMillis()), "Ср", isChosen = true, isToday = false, event = EventColor.GREEN),
                DayItem("3", Date(System.currentTimeMillis()), "Чт", isChosen = false, isToday = true, event = EventColor.NONE),
                DayItem("4", Date(System.currentTimeMillis()), "Пт", isChosen = false, isToday = false, event = EventColor.WHITE),
                DayItem("5", Date(System.currentTimeMillis()), "Сб", isChosen = false, isToday = false, event = EventColor.NONE),
                DayItem("6", Date(System.currentTimeMillis()), "Вс", isChosen = false, isToday = false, event = EventColor.NONE)
        ))

        val dayAdapter = DayListAdapter(
                holderType = DayViewHolder::class,
                layoutId = R.layout.day_item,
                dataSource = itemsList,
                onClick = {dayItem ->
                    println("WOW")
                }
        )

        recyclerView?.adapter = dayAdapter
        recyclerView?.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
//        recyclerView?.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))
    }


    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_plan
}
