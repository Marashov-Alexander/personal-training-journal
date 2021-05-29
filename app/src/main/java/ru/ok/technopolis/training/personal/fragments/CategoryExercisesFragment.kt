package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_number.view.navigation_view_main_block
import kotlinx.android.synthetic.main.item_personal_elements.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryExercisesAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryExerciseViewHolder

class CategoryExercisesFragment : CategoryExerciseFragment() {
    private var recycler: RecyclerView? = null
    private var addButton: FloatingActionButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        addButton = view.add_element_button
        loadExercises()
        addButton?.setOnClickListener {
            //TODO:create new exercise here
            router?.showNewExercisePage1(1, 1, 1)
        }
    }

    override fun getFragmentLayoutId() = R.layout.item_personal_elements

    private fun loadExercises() {
        val userId = CurrentUserRepository.currentUser.value?.id
        loadCategoryExercises(userId!!, false) { elementsList ->
            val categories = ItemsList(elementsList)
            val catAdapter = CategoryExercisesAdapter(
                    holderType = CategoryExerciseViewHolder::class,
                    layoutId = R.layout.item_library_elements,
                    dataSource = categories,
                    onClick = { exerciseItem -> println("workout ${exerciseItem.id} clicked") },
                    onStart = { item ->
                        println("workout ${item} started")
                        router?.showExercisePage(item)
                    }
            )
            recycler?.adapter = catAdapter
            val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            recycler?.layoutManager = workoutsLayoutManager
        }
    }

}