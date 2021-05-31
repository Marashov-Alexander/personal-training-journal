package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.element_exercise_parameters.*
import kotlinx.android.synthetic.main.fragment_view_exercise.view.*
import kotlinx.android.synthetic.main.fragment_view_exercise.view.author_name
import kotlinx.android.synthetic.main.fragment_view_exercise.view.downloads_number
import kotlinx.android.synthetic.main.fragment_view_exercise.view.info_card
import kotlinx.android.synthetic.main.fragment_view_exercise.view.rank_number
import kotlinx.android.synthetic.main.fragment_view_exercise.view.share_card
import kotlinx.android.synthetic.main.item_media_viewer.*
import kotlinx.android.synthetic.main.item_media_viewer.view.*
import kotlinx.android.synthetic.main.scheduled_workout_item.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.DescriptionDialogFragment
import ru.ok.technopolis.training.personal.fragments.dialogs.ShareDialog
import ru.ok.technopolis.training.personal.items.*
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.logger.Logger
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.views.ExerciseParametersWrapper
import ru.ok.technopolis.training.personal.views.MediaViewerWrapper

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
    private var shareButton: CardView? = null

    private var exercise: ShortExerciseItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val exerciseId = (activity?.intent?.extras?.get(Page.EXERCISE_ID_KEY)) as Long
        val workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY) as? Long)

        super.onViewCreated(view, savedInstanceState)
        exerciseShortInfoRecycler = view.exercise_scroll_info
        startButton = workout_start_icon
        imageSwitcher = view.exercise_image_switcher
        downloadsNumber = view.downloads_number
        raiting = view.rank_number
        level = level_value
        authorName = view.author_name
        parametersRecycler = parameters_recycler
        info = view.info_card
        val userId = CurrentUserRepository.currentUser.value?.id!!
        shareButton = view.share_card
        shareButton?.setOnClickListener {
//            router?.shareElement(exerciseId, null)
            ShareDialog(exerciseId, false)
                    .show(requireActivity().supportFragmentManager, "ShareDialog")
        }

        loadExerciseInfo(userId, workoutId, exerciseId) { exercise, category, author, userLevel, levelsMap, maxLevel, mediaData ->
            setWorkoutDummy(exercise, author, category)

            val mediaList = ItemsList<MediaItem>(mutableListOf())
            val mediaViewer = MediaViewerWrapper(
                    this,
                    exercise_image_switcher,
                    no_content,
                    pos_value,
                    pos_card,
                    mediaList
            )
            mediaViewer.setMediaData(mediaData.map { m -> m.url })

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


    private fun setWorkoutDummy(exerciseEntity: ExerciseEntity, author: UserEntity, categoryEntity: ExerciseCategoryEntity){
        val exerciseId = (activity?.intent?.extras?.get(Page.EXERCISE_ID_KEY) as Long)

//        exercise = ShortExerciseItem(exerciseId.toString(), exerciseEntity, categoryEntity.name, 0, 0.0)
        activity?.base_toolbar?.title = getString(R.string.exercise) + " \"${exercise?.exercise?.name}\" "
        raiting?.text = (0.0).toString()
        downloadsNumber?.text = exercise?.downloadsNumber.toString()
        info?.setOnClickListener {
            showExerciseDescription(exercise!!.exercise.name, exercise!!.exercise.description.toString())
        }

        authorName?.text =  author.firstName
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