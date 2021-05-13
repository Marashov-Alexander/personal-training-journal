package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_number.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryWorkoutsAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryWorkoutsViewHolder
import java.sql.Time

class LibraryWorkoutsFragment : Fragment() {

    private var recycler: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var categoryElem = mutableListOf<CategoryWorkoutsItem>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        exDummyToRecView()
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
        }
//        val textView: TextView = view.findViewById(R.id.textView)
//        textView.text = getInt(ARG_OBJECT).toString()
    }

    private fun exDummyToRecView() {
        for (i in 1..5) pushWorkout(i)
        pushCategory(0,"ssfsf", workoutsMutableList)
        pushCategory(1,"ssfbdbfdfsf", workoutsMutableList)
        pushCategory(2,"ssfsdfdfdf", workoutsMutableList)
        pushCategory(3,"ssfsgdfdfdff", workoutsMutableList)

        val categories = ItemsList(categoryElem)
        val catAdapter = CategoryWorkoutsAdapter(
                holderType = CategoryWorkoutsViewHolder::class,
                layoutId = R.layout.item_category_and_elements,
                dataSource = categories,
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")},
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                }
        )
        recycler?.adapter = catAdapter
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler?.layoutManager = workoutsLayoutManager
    }


    private fun pushWorkout(id: Int) {
        workoutsMutableList.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), "MYвшпвшпвкпиквпшкивпквпвпквпивчмпч MY", "kardio", "ofp", "", true, 12,4.0, false)
        )
    }
    private fun pushCategory(id: Int, name: String, workouts: List<ShortWorkoutItem>) {
        categoryElem.add(CategoryWorkoutsItem(id.toString(), name, workouts))
    }


}