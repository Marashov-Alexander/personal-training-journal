package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_view_exercise.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.ParameterDialogFragment
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import ru.ok.technopolis.training.personal.items.ShortParameterItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder

import java.sql.Time

class ExerciseViewFragment : BaseFragment(), ParameterDialogFragment.ParameterDialogListener {
    private var exerciseShortInfoRecycler: RecyclerView? = null
    private var imageSwitcher: ImageSwitcher? = null
    private var startButton: ImageView? = null
    private var shareButton: ImageView? = null
    private var downloadsNumber: TextView? = null
    private var raiting: TextView? = null
    private var level: Spinner? = null
    private var nextSwitch: ImageView? =null
    private var prevSwitch: ImageView? = null
    private var authorName: TextView? = null
    private var redactorName: TextView? = null
    private var parametersRecycler: RecyclerView? = null

    private var exercise: ShortExerciseItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseShortInfoRecycler = view.exercise_scroll_info
        startButton = view.workout_start_icon
        shareButton = view.share_icon
        imageSwitcher = view.exercise_image_switcher
        downloadsNumber = view.downloads_number
        raiting = view.rank_number
        level = view.level_value
        nextSwitch = view.next_image_btn
        prevSwitch = view.prev_image_btn
        authorName = view.author_name
        redactorName = view.redactor_name
        parametersRecycler = view.exercise_param_list
        loadDummy()
    }

    private fun loadDummy() {
        setWorkoutDummy()

        val parameters = mutableListOf<ShortParameterItem>()
        for (i in 1..7) {
            parameters.add(ShortParameterItem(i.toString(), ParameterEntity("Параметр $i", "ед. изм."), false))
        }
        parameters.add(ShortParameterItem("0", null, editable = false, invisible = true))

        val parametersList = ItemsList(parameters)

        parametersRecycler?.adapter = ParameterAdapter(
                holderType = ParameterViewHolder::class,
                layoutId = R.layout.item_parameter_short,
                dataSource = parametersList,
                onEdit = {
                },
                onClick = {workoutItem -> println("workout ${workoutItem.id} clicked")
                }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecycler?.layoutManager = layoutManager
    }

    private fun setWorkoutDummy(){
        val workoutId = (activity?.intent?.extras?.get(Page.EXERCISE_ID_KEY) as Long)
        exercise = ShortExerciseItem(workoutId.toString(), Time(System.currentTimeMillis()), "name", "category", "sport", false, 123, 3.5)
        activity?.base_toolbar?.title = getString(R.string.exercise) + " \"${exercise?.name}\" "
        raiting?.text = exercise?.rank.toString()
        downloadsNumber?.text = exercise?.downloadsNumber.toString()
        val isLocal = false
        if (!isLocal) {
            shareButton?.visibility = View.INVISIBLE
            startButton?.visibility = View.INVISIBLE
        }
        //TODO:load author and redactor from db
//        authorName?.text =
//        redactorName?.text =
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

    override fun getFragmentLayoutId(): Int = R.layout.fragment_view_exercise

    override fun onSaveClick(item: ParameterEntity) {
        println(item.toString())
    }
}