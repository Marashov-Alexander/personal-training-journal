package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_number.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryExercisesAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryExerciseViewHolder

class LibraryExercisesFragment : CategoryExerciseFragment() {

    private var recycler: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        loadExercises()
    }

    private fun loadExercises() {
        val userId = CurrentUserRepository.currentUser.value?.id
        loadCategoryExercises(userId!!, true) { elementsList ->
            val categories = ItemsList(elementsList)
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
            val workoutsLayoutManager = LinearLayoutManager(
                    activity,
                    RecyclerView.VERTICAL,
                    false)
            recycler?.layoutManager = workoutsLayoutManager
        }
    }

    override fun getFragmentLayoutId() = R.layout.fragment_number
}