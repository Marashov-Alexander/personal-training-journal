package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_number.view.navigation_view_main_block
import kotlinx.android.synthetic.main.item_personal_elements.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.items.CategoryWorkoutsItem
import ru.ok.technopolis.training.personal.items.ExerciseItem
import ru.ok.technopolis.training.personal.items.ExercisesList
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.CategoryWorkoutsAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ExerciseAdapter
import ru.ok.technopolis.training.personal.viewholders.CategoryWorkoutsViewHolder
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder
import java.sql.Time
import kotlin.random.Random

class CategoryWorkoutsFragment : CategoryWorkoutFragment() {

    private var recycler: RecyclerView? = null
    private var addButton: FloatingActionButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.navigation_view_main_block
        addButton = view.add_element_button
        exDummyToRecView()
        addButton?.setOnClickListener {
            // TODO: create new workout here
            router?.showNewWorkoutPage(1)
        }
    }

    override fun getFragmentLayoutId() = R.layout.item_personal_elements

    private fun exDummyToRecView() {
        val userId = CurrentUserRepository.currentUser.value?.id
                loadCategoryWorkouts(userId!!, false) { elementsList ->
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