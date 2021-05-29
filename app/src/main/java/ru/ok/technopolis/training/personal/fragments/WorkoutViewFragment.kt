package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_view_workout.view.*
import kotlinx.android.synthetic.main.item_media_viewer.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.DescriptionDialogFragment
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ExercisesList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ExerciseAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder

class WorkoutViewFragment : WorkoutFragment() {
    private var workoutShortInfoRecycler: RecyclerView? = null
    private var imageSwitcher: RecyclerView? = null
    private var startButton: ImageView? = null
    private var shareText: TextView? = null
    private var downloadsNumber: TextView? = null
    private var raiting: TextView? = null
    private var difficulty: TextView? = null
    private var nextSwitch: ImageView? =null
    private var prevSwitch: ImageView? = null
    private var authorName: TextView? = null
    private var exerciseRecycler: RecyclerView? = null
    private var exercisesList: ExercisesList? = null
    private var info: MaterialCardView? = null

    private var workout: ShortWorkoutItem? = null

    private var workoutId = 1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutShortInfoRecycler = view.workout_scroll_info
        startButton = view.workout_start_icon
        imageSwitcher = view.exercise_image_switcher
        downloadsNumber = view.downloads_number
        raiting = view.rank_number
        difficulty = view.difficulty_value
        authorName = view.author_name
        exerciseRecycler = view.workout_ex_list
        info = view.info_card

        shareText = view.share_text

        loadWorkoutInfo(workoutId) { workout, category, sport, exercises, author ->
            setWorkoutDummy(workout, category, sport)
            exercisesList = ExercisesList(exercises)
            val adapter = ExerciseAdapter(
                holderType = ExerciseItemViewHolder::class,
                layoutId = R.layout.item_exercise,
                dataSource = exercisesList!!,
                onClick = { exercise ->
                    print("Exercise $exercise clicked")
                    router?.showExercisePage(exercise.id.toLong())
                },
                onView = {exercise ->
                    print("View exercise $exercise clicked")
                    router?.showExercisePage(exercise.exercise.id)
                    false
                },
                onLongExerciseClick = {  item, itemView  ->
                    print("Exercise $item clicked")
                }
            )
            exerciseRecycler?.adapter = adapter
            val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            exerciseRecycler?.layoutManager = workoutsLayoutManager
        }
    }

    private fun setWorkoutDummy(workout: WorkoutEntity, category: WorkoutCategoryEntity, sport: WorkoutSportEntity){
//        val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY) as Long)
//        workout = ShortWorkoutItem(workoutId.toString(), Time(System.currentTimeMillis()), "name", "description","category", "sport", "40 min",  123, 3.5, false, false)
        activity?.base_toolbar?.title = getString(R.string.workout) + " \"${workout.name}\" "
//        raiting?.text = workout.
        difficulty?.text = workout.difficulty.toString()
//        downloadsNumber?.text = workout?.downloadsNumber.toString()
//        if (!workout?.private!!) {
//            shareButton?.visibility = View.INVISIBLE
//            startButton?.visibility = View.INVISIBLE
//            shareText?.visibility = View.INVISIBLE
//        }

        info?.setOnClickListener {
            showExerciseDescription(workout.name, workout.description.toString())
        }
        //TODO:load author from db
//        authorName?.text =
        setWorkoutShortInfo(workout, category, sport)
    }

    private fun showExerciseDescription(title: String, description: String) {
        DescriptionDialogFragment(title, description)
                .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

    private fun setWorkoutShortInfo(workout: WorkoutEntity, category: WorkoutCategoryEntity, sport: WorkoutSportEntity){
        val itemsList = SingleSelectableList(mutableListOf(
//                BundleItem("0", 0, "Сложность 3"),
                BundleItem("0", 0, category.name),
                BundleItem("1", 1, sport.name)
        ))
        val exerciseAdapter = BundleAdapter(
                holderType = BundleItemViewHolder::class,
                layoutId = R.layout.item_bundle,
                dataSource = itemsList,
                onBundleClick = { bundleItem, _ ->
                    println("Bundle $bundleItem clicked")
                }
        )
        workoutShortInfoRecycler?.adapter = exerciseAdapter
        val layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        workoutShortInfoRecycler?.layoutManager = layoutManager
    }


    override fun getFragmentLayoutId(): Int = R.layout.fragment_view_workout


}