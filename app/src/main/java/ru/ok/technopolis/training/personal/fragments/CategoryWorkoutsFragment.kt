package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_number.view.navigation_view_main_block
import kotlinx.android.synthetic.main.item_personal_elements.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryWorkoutsAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryWorkoutsViewHolder
import java.sql.Time

class CategoryWorkoutsFragment : BaseFragment() {

    private var recycler: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var workoutsMutableList = mutableListOf<ShortWorkoutItem>()
    private var workoutsMutableList2 = mutableListOf<ShortWorkoutItem>()
    private var workoutsMutableList3 = mutableListOf<ShortWorkoutItem>()
    private var workoutsMutableList4 = mutableListOf<ShortWorkoutItem>()
    private var categoryElem = mutableListOf<CategoryWorkoutsItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        addButton = view.add_element_button
        exDummyToRecView()
        addButton?.setOnClickListener {
            router?.showNewWorkoutPage()
        }
    }

    override fun getFragmentLayoutId() = R.layout.item_personal_elements

    private fun exDummyToRecView() {

        pushWorkout(0, "Тренировка 1", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout(0, "Любимая тренировка", "Круговая", "", "Легкая атлетика", 0, 0.0)
        pushWorkout4(0, "Любимая тренировка", "Круговая", "", "Легкая атлетика", 0, 0.0)

        pushWorkout2(0, "Тренировка 1", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout2(1, "Тренировка 2", "Кардио", "", "Легкая атлетика", 0, 0.0)
        pushWorkout2(2, "Тренировка 3", "Кардио", "", "Легкая атлетика", 0, 0.0)

        pushWorkout3(1, "Тренировка 2", "Силовая", "", "Легкая атлетика", 0, 0.0)
        pushWorkout3(1, "Тренировка 3", "Силовая", "", "Легкая атлетика", 0, 0.0)
        pushWorkout3(1, "Тренировка 1", "Силовая", "", "Легкая атлетика", 0, 0.0)

        pushCategory(0, "Популярное", workoutsMutableList)
        pushCategory(1, "Кардио", workoutsMutableList2)
        pushCategory(2, "Силовые", workoutsMutableList3)
        pushCategory(3, "Круговые", workoutsMutableList4)

        val categories = ItemsList(categoryElem)
        val catAdapter = CategoryWorkoutsAdapter(
                holderType = CategoryWorkoutsViewHolder::class,
                layoutId = R.layout.item_library_elements,
                dataSource = categories,
                onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
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