package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.element_exercise_parameters.*
import kotlinx.android.synthetic.main.fragment_view_exercise.view.*
import kotlinx.android.synthetic.main.item_media_viewer.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.dialogs.DescriptionDialogFragment
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.logger.Logger
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.views.ExerciseParametersWrapper
import java.sql.Time

class ExerciseViewFragment : ExerciseFragment() {
    private var exerciseShortInfoRecycler: RecyclerView? = null
    private var imageSwitcher: RecyclerView? = null
    private var startButton: ImageView? = null
    private var downloadsNumber: TextView? = null
    private var raiting: TextView? = null
    private var level: Spinner? = null
    private var authorName: TextView? = null
    private var parametersRecycler: RecyclerView? = null
    private var info: MaterialCardView? = null

    private var exercise: ShortExerciseItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val exerciseId = (activity?.intent?.extras?.get(Page.EXERCISE_ID_KEY) as Long)
        val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY) as? Long)

        super.onViewCreated(view, savedInstanceState)
        exerciseShortInfoRecycler = view.exercise_scroll_info
        startButton = view.workout_start_icon
        imageSwitcher = view.exercise_image_switcher
        downloadsNumber = view.downloads_number
        raiting = view.rank_number
        level = level_value
        authorName = view.author_name
        parametersRecycler = parameters_recycler
        info = view.info_card
        val userId = CurrentUserRepository.currentUser.value?.id!!

        loadExerciseInfo(userId, workoutId, exerciseId) { exercise, author, userLevel, levelsMap, maxLevel ->
            setWorkoutDummy()

            ExerciseParametersWrapper(
                this,
                parameters_recycler,
                add_parameter_button,
                increase_level,
                decrease_level,
                level_value,
                levelsMap = levelsMap,
                userLevel = userLevel,
                maxLevel = maxLevel,
                editable = false
            )
        }
    }


    private fun setWorkoutDummy(){
        val exerciseId = (activity?.intent?.extras?.get(Page.EXERCISE_ID_KEY) as Long)
        exercise = ShortExerciseItem(exerciseId.toString(), Time(System.currentTimeMillis()), "name", "category", "sport", 123, 3.5)
        activity?.base_toolbar?.title = getString(R.string.exercise) + " \"${exercise?.name}\" "
        raiting?.text = exercise?.rank.toString()
        downloadsNumber?.text = exercise?.downloadsNumber.toString()
//        val isLocal = false
//        if (!isLocal) {
//            shareButton?.visibility = View.INVISIBLE
//            startButton?.visibility = View.INVISIBLE
//        }

        info?.setOnClickListener {
            showExerciseDescription(exercise!!.name, exercise!!.description)
        }

        //TODO:load author from db
//        authorName?.text =
        setWorkoutShortInfo()
    }

    private fun setWorkoutShortInfo(){
        val itemsList = SingleSelectableList(mutableListOf(
                BundleItem("0", 0, exercise?.category.toString())
        ))
        val exerciseAdapter = BundleAdapter(
                holderType = BundleItemViewHolder::class,
                layoutId = R.layout.item_bundle,
                dataSource = itemsList,
                onBundleClick = { bundleItem, _ ->
                    println("Bundle $bundleItem clicked")
                }
        )
        exerciseShortInfoRecycler?.adapter = exerciseAdapter
        val layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        exerciseShortInfoRecycler?.layoutManager = layoutManager
    }

    private fun showExerciseDescription(title: String, description: String) {
        DescriptionDialogFragment(title, description)
                .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Logger.d(this, "onCreateOptionsMenu")
        inflater.inflate(R.menu.change_menu, menu)

        val button: MenuItem = menu.findItem(R.id.change_button)

//        if (exercise not in currentUser.exercises) {
        button.setIcon(R.drawable.ic_add_black_24dp)
//        }

        button.setOnMenuItemClickListener {
//             if (exercise not in currentUser.exercises) {

            true
        }

    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_view_exercise

}