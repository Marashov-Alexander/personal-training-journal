package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_number.view.navigation_view_main_block
import kotlinx.android.synthetic.main.item_personal_elements.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryExerciseItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryExercisesAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryExerciseViewHolder
import java.sql.Time

class CategoryExercisesFragment : BaseFragment() {
    private var recycler: RecyclerView? = null
    private var addButton: FloatingActionButton? = null
    private var workoutsMutableList = mutableListOf<ShortExerciseItem>()
    private var workoutsMutableList2 = mutableListOf<ShortExerciseItem>()
    private var workoutsMutableList3 = mutableListOf<ShortExerciseItem>()
    private var categoryElem = mutableListOf<CategoryExerciseItem>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        addButton = view.add_element_button
        exDummyToRecView()
        addButton?.setOnClickListener {
            router?.showNewExercisePage1(1, 1, 1)
        }
    }

    override fun getFragmentLayoutId() = R.layout.item_personal_elements

    private fun exDummyToRecView() {
        pushExercise(0, "Мое упражнение", "Кардио", "Офп", 0, 0.0)

        pushExercise2(3, "Мое упражнение", "Кардио", "Офп", 0, 0.0)
        pushExercise2(4, "Бег на месте", "Кардио", "Офп", 0, 0.0)
        pushExercise2(5, "Бег на месте 2", "Кардио", "Офп", 0, 0.0)
        pushExercise2(6, "Бег на месте 3", "Кардио", "Офп", 0, 0.0)


        pushExercise3(5, "Приседания", "Силовые", "Базовые", 0, 0.0)
        pushExercise3(6, "Приседания 2", "Силовые", "Базовые", 0, 0.0)

        pushCategory(0, "Популярное", workoutsMutableList)
        pushCategory(1, "Кардио", workoutsMutableList2)
        pushCategory(2, "Силовые", workoutsMutableList3)

        val categories = ItemsList(categoryElem)
        val catAdapter = CategoryExercisesAdapter(
                holderType = CategoryExerciseViewHolder::class,
                layoutId = R.layout.item_library_elements,
                dataSource = categories,
                onClick = { workoutItem -> println("workout ${workoutItem.id} clicked") },
                onStart = { workoutItem ->
                    println("workout ${workoutItem} started")
                    router?.showExercisePage(workoutItem)
                }
        )
        recycler?.adapter = catAdapter
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler?.layoutManager = workoutsLayoutManager
    }

    private fun pushExercise(id: Int, name: String, category: String, description: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
        )
    }

    private fun pushExercise2(id: Int, name: String, category: String, description: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList2.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
        )
    }

    private fun pushExercise3(id: Int, name: String, category: String, description: String, sharedNumber: Int, rank: Double) {
        workoutsMutableList3.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
        )
    }

    private fun pushCategory(id: Int, name: String, workouts: List<ShortExerciseItem>) {
        categoryElem.add(CategoryExerciseItem(id.toString(), name, workouts))
    }

}