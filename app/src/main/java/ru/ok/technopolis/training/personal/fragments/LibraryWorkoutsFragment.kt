package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_number.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryWorkoutsAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryWorkoutsViewHolder
import java.sql.Time

class LibraryWorkoutsFragment : CategoryWorkoutFragment() {

    private var recycler: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        exDummyToRecView()
    }

    override fun getFragmentLayoutId() = R.layout.fragment_number

    private fun exDummyToRecView() {
        val userId = CurrentUserRepository.currentUser.value?.id
        loadCategoryWorkouts(userId!!, true) { elementsList ->
            val categoriesList = ItemsList(elementsList)
            val catAdapter = CategoryWorkoutsAdapter(
                    holderType = CategoryWorkoutsViewHolder::class,
                    layoutId = R.layout.item_library_elements,
                    dataSource = categoriesList,
                    onClick = { workoutItem -> println("Треня ${workoutItem.id} clicked") },
                    onStart = { workoutId->
                        println("Элемент ${workoutId} started")
                        router?.showWorkoutPage(workoutId)
                    }
            )

            recycler?.adapter = catAdapter
            val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            recycler?.layoutManager = workoutsLayoutManager
        }
    }



}