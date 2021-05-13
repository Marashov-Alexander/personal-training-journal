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
import ru.ok.technopolis.training.personal.items.CategoryExerciseItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryExercisesAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryExerciseViewHolder
import java.sql.Time

const val ARG_OBJECT = "object"

class LibraryExercisesFragment : Fragment() {
    private var recycler: RecyclerView? = null
    private var workoutsMutableList = mutableListOf<ShortExerciseItem>()
    private var workoutsMutableList2 = mutableListOf<ShortExerciseItem>()
    private var workoutsMutableList3 = mutableListOf<ShortExerciseItem>()
    private var categoyElem = mutableListOf<CategoryExerciseItem>()

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
    }

    private fun exDummyToRecView() {
        pushExercise(0, "Мое упражнение", "Кардио", "Офп", 200, 4.8)
        pushExercise(1, "Бег на месте", "Кардио", "Офп", 100, 4.5)
        pushExercise(2, "Приседания", "Силовые", "Базовые", 30, 5.0)

        pushExercise2(3, "Мое упражнение", "Кардио", "Офп", 8, 3.0)
        pushExercise2(4, "Бег на месте", "Кардио", "Офп", 6, 5.0)
        pushExercise2(5, "Бег на месте 2", "Кардио", "Офп", 7, 5.0)
        pushExercise2(6, "Бег на месте 3", "Кардио", "Офп", 6, 4.0)


        pushExercise3(5, "Приседания", "Силовые", "Базовые", 3, 5.0)
        pushExercise3(6, "Приседания 2", "Силовые", "Базовые", 6, 4.5)

        pushCategory(0,"Популярное", workoutsMutableList)
        pushCategory(1,"Кардио", workoutsMutableList2)
        pushCategory(2,"Силовые", workoutsMutableList3)

        val categories = ItemsList(categoyElem)
        val catAdapter = CategoryExercisesAdapter(
                holderType = CategoryExerciseViewHolder::class,
                layoutId = R.layout.item_library_elements,
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
    private fun pushExercise(id: Int, name: String, category: String, description: String,  sharedNumber: Int, rank: Double) {
        workoutsMutableList.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
        )
    }

    private fun pushExercise2(id: Int, name: String, category: String, description: String,  sharedNumber: Int, rank: Double) {
        workoutsMutableList2.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
        )
    }
    private fun pushExercise3(id: Int, name: String, category: String, description: String,  sharedNumber: Int, rank: Double) {
        workoutsMutableList3.add(
                ShortExerciseItem(id.toString(), Time(System.currentTimeMillis()), name, category, description, true, sharedNumber, rank)
        )
    }

    private fun pushCategory(id: Int, name: String, workouts: List<ShortExerciseItem>) {
        categoyElem.add(CategoryExerciseItem(id.toString(), name, workouts))
    }


}