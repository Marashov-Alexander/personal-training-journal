package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_view_workout.view.*
import kotlinx.android.synthetic.main.item_media_viewer.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.DescriptionDialogFragment
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ExerciseItem
import ru.ok.technopolis.training.personal.items.ExercisesList
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.items.chatItems.MessageFromItem
import ru.ok.technopolis.training.personal.items.chatItems.MessageToItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ExerciseAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder
import java.sql.Time

class WorkoutViewFragment : BaseFragment() {
    private var workoutShortInfoRecycler: RecyclerView? = null
    private var imageSwitcher: RecyclerView? = null
    private var startButton: ImageView? = null
    private var shareText: TextView? = null
    private var shareButton: ImageView? = null
    private var downloadsNumber: TextView? = null
    private var raiting: TextView? = null
    private var difficulty: TextView? = null
    private var nextSwitch: ImageView? =null
    private var prevSwitch: ImageView? = null
    private var authorName: TextView? = null
    private var redactorName: TextView? = null
    private var exerciseRecycler: RecyclerView? = null
    private var exercisesList: ExercisesList? =null
    private var info: ConstraintLayout? = null

    private var workout: ShortWorkoutItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutShortInfoRecycler = view.workout_scroll_info
        startButton = view.workout_start_icon
        shareButton = view.share_icon
        imageSwitcher = view.exercise_image_switcher
        downloadsNumber = view.downloads_number
        raiting = view.rank_number
        difficulty = view.difficulty_value
        authorName = view.author_name
        redactorName = view.redactor_name
        exerciseRecycler = view.workout_ex_list
        info = view.info_card

        shareText = view.share_text

        loadDummy()
    }


    private fun loadDummy() {

        GlobalScope.launch(Dispatchers.IO) {
            val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY) as Long)
            var workoutEntity : WorkoutEntity? = null
            database?.let { appDatabase ->
                // TODO: DO SOMETHING
                val email = CurrentUserRepository.currentUser.value!!.email
                val user = database!!.userDao().getByEmail(email)
                val userId = user.id
                if(appDatabase.workoutDao().getById(workoutId) == null) {
                    if (appDatabase.workoutCategoryDao().getById(1) == null) {
                        val categoryID = appDatabase.workoutCategoryDao().insert(WorkoutCategoryEntity("category", id=1))
                        val dummyWorkoutId = appDatabase.workoutDao().insert(WorkoutEntity("DUMMY", "description", categoryID, "sport", 3, false, userId, id = workoutId, redactorId = null))
                        val dummyExercise = ExerciseEntity("name", "description", "category", false, userId, redactorId = null)
                        val dummyExerciseId = appDatabase.exerciseDao().insert(dummyExercise)
                        appDatabase.workoutExerciseDao().insert(WorkoutExerciseEntity(dummyWorkoutId, dummyExerciseId))
                    }
                }
                val category = appDatabase.workoutCategoryDao().getById(1)

                val exercisesIds = appDatabase.workoutExerciseDao().getAllByWorkout(workoutId)
                val exercises = mutableListOf<ExerciseItem>()
                for (exId in exercisesIds) {
                    val exercise = appDatabase.exerciseDao().getById(exId.exerciseId)
                    exercises.add(ExerciseItem(exercise.id.toString(), 0, exercise.name, exercise.description.toString(), 0))
                    workoutEntity = appDatabase.workoutDao().getById(workoutId)

                }
                exercisesList = ExercisesList(exercises)
                withContext(Dispatchers.Main) {
                    setWorkoutDummy(workoutEntity!!, category)

                    val adapter = ExerciseAdapter(
                            holderType = ExerciseItemViewHolder::class,
                            layoutId = R.layout.item_exercise,
                            dataSource = exercisesList!!,
                            onClick = { exercise ->
                                print("Exercise $exercise clicked")
                                router?.showExercisePage(exercise.id.toLong())
                            },
                            onStart = {exercise ->
                                print("Exercise $exercise started")
                                router?.showExercisePage(exercise.id.toLong())
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
        }



//        exercisesList = ExercisesList(mutableListOf(
//                ExerciseItem("1", 1, "Упражнение 1", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("2", 1, "Упражнение 2", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("3", 1, "Упражнение 3", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("4", 1, "Упражнение 4", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("5", 1, "Упражнение 5", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("6", 1, "Упражнение 6", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("7", 1, "Упражнение 7", "5 подходов, 5 повторений", supersetGroupId = null),
//                ExerciseItem("8", 1, "Упражнение 8", "5 подходов, 5 повторений", supersetGroupId = null)
//        ))

    }

    private fun setWorkoutDummy(workout: WorkoutEntity, category: WorkoutCategoryEntity){
//        val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY) as Long)
//        workout = ShortWorkoutItem(workoutId.toString(), Time(System.currentTimeMillis()), "name", "description","category", "sport", "40 min",  123, 3.5, false, false)
        activity?.base_toolbar?.title = getString(R.string.workout) + " \"${workout?.name}\" "
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
        //TODO:load author and redactor from db
//        authorName?.text =
//        redactorName?.text =
        setWorkoutShortInfo(workout, category)
    }

    private fun showExerciseDescription(title: String, description: String) {
        DescriptionDialogFragment(title, description)
                .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

    private fun setWorkoutShortInfo(workout: WorkoutEntity, category: WorkoutCategoryEntity){
        val itemsList = SingleSelectableList(mutableListOf(
//                BundleItem("0", 0, "Сложность 3"),
                BundleItem("0", 0, category.name),
                BundleItem("1", 1, workout.sport.toString())
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