package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_number.view.*
import kotlinx.android.synthetic.main.item_workout.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryWorkoutsAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryWorkoutsViewHolder
import java.sql.Time

//const val ARG_OBJECT = "object"

class CategoryWorkoutsFragment : Fragment() {

    private var recycler: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var workoutsMutableList2 = mutableListOf<ShortWorkoutItem>()
    private var workoutsMutableList3 = mutableListOf<ShortWorkoutItem>()
    private var workoutsMutableList4 = mutableListOf<ShortWorkoutItem>()
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

        pushWorkout(0, "Тренировка 1", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout2(0, "Тренировка 1", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout2(1, "Тренировка 2", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout2(2, "Тренировка 3", "Кардио", "", "Легкая атлетика", 0, 0.0)

        pushWorkout3(1, "Тренировка 2", "Силовая", "", "Легкая атлетика", 0, 0.0)
        pushWorkout3(1, "Тренировка 3", "Силовая", "", "Легкая атлетика", 0, 0.0)
        pushWorkout(0, "Любимая тренировка", "Круговая", "", "Легкая атлетика", 0, 0.0)
        pushWorkout3(1, "Тренировка 1", "Силовая", "", "Легкая атлетика", 0, 0.0)

        pushCategory(0,"Популярное", workoutsMutableList)
        pushCategory(1,"Кардио", workoutsMutableList)
        pushCategory(2,"Силовые", workoutsMutableList)
        pushCategory(3,"Круговые", workoutsMutableList)

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


    private fun pushWorkout(id: Int, name: String, category: String, description: String, sport: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), name, category, sport, "40 min", true, sharedNumber, rank, false)
        )
    }
    private fun pushWorkout2(id: Int, name: String, category: String, description: String, sport: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList2.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), name, category, sport, "40 min", true, sharedNumber, rank, false)
        )
    }
    private fun pushWorkout3(id: Int, name: String, category: String, description: String, sport: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList3.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), name, category, sport, "40 min", true, sharedNumber, rank, false)
        )
    }
    private fun pushWorkout4(id: Int, name: String, category: String, description: String, sport: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList4.add(
                ShortWorkoutItem(id.toString(), Time(System.currentTimeMillis()), name, category, sport, "40 min", true, sharedNumber, rank, false)
        )
    }
    private fun pushCategory(id: Int, name: String, workouts: List<ShortWorkoutItem>) {
        categoryElem.add(CategoryWorkoutsItem(id.toString(), name, workouts))
    }


}