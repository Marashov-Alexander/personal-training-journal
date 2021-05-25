package ru.ok.technopolis.training.personal.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_active_exercise.*
import kotlinx.android.synthetic.main.item_media_viewer.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ActiveParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ActiveParameterViewHolder
import ru.ok.technopolis.training.personal.views.MediaViewerWrapper

class ActiveExerciseFragment : BaseFragment() {

    private var exerciseProgressText: TextView? = null
    private var repeatsDone: View? = null
    private var repeatsLeft: View? = null
    private var parametersRecycler: RecyclerView? = null

    private var nextCard: MaterialCardView? = null
    private var nextTextView: TextView? = null

    private var skipCard: MaterialCardView? = null
    private var skipTextView: TextView? = null

    private var repeatsAllCount = 10
    private var repeatsDoneCount = 0

    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaViewer: MediaViewerWrapper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseProgressText = exercise_progress_text
        repeatsDone = repeats_done
        repeatsLeft = repeats_left
        parametersRecycler = parameters_recycler
        nextCard = next_card
        nextTextView = next_text
        skipCard = skip_card
        skipTextView = skip_text

        mediaViewer = MediaViewerWrapper(
            this,
            exercise_image_switcher,
            no_content,
            pos_value,
            pos_card,
            mediaList
        )

        setRepeatsProgress(repeatsDoneCount, repeatsAllCount)
        nextCard?.setOnClickListener {
            setRepeatsProgress(++repeatsDoneCount, repeatsAllCount)
        }
        skipCard?.setOnClickListener {
            setRepeatsProgress(++repeatsDoneCount, repeatsAllCount)
        }

        val parameters = mutableListOf(
            ParameterEntity("Параметр 1", "Ед. измерения", value = 3f),
            ParameterEntity("Параметр 2", "Ед. измерения", value = 3f),
            ParameterEntity("Параметр 3", "Ед. измерения", value = 3f),
            ParameterEntity("Параметр 4", "Ед. измерения"),
            ParameterEntity("Параметр 5", "Ед. измерения")
        )
        val parametersList = ItemsList(parameters)

        parametersRecycler = parameters_recycler
        parametersRecycler?.adapter = ActiveParameterAdapter(
            holderType = ActiveParameterViewHolder::class,
            layoutId = R.layout.item_parameter_writer,
            dataSource = parametersList,
            onClick = {

            }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecycler?.layoutManager = layoutManager
    }

    private fun setRepeatsProgress(repeatsDoneCount: Int, repeatsAllCount: Int) {
        val donePercents = 100f * repeatsDoneCount / repeatsAllCount
        val leftPercents = 100f - donePercents
        val leftParams = repeatsLeft?.layoutParams as? ConstraintLayout.LayoutParams
        leftParams?.horizontalWeight = leftPercents
        val doneParams = repeatsDone?.layoutParams as? ConstraintLayout.LayoutParams
        doneParams?.horizontalWeight = donePercents
        exerciseProgressText?.text = String.format(requireContext().getString(R.string.repeats), repeatsDoneCount, repeatsAllCount)
        checkExerciseDone()
    }

    private fun checkExerciseDone() {
        if (repeatsDoneCount == repeatsAllCount) {
            nextTextView?.text = "Завершить"
            nextTextView?.setTextColor(Color.WHITE)

            nextCard?.setOnClickListener {
                router?.showWorkoutDonePage(0, 0)
            }
            nextCard?.setCardBackgroundColor(requireContext().getColor(R.color.light_blue))
            nextCard?.strokeColor = requireContext().getColor(R.color.dark_cyanide)

            skipTextView?.text = "Ещё подход!"
            skipCard?.setOnClickListener {
                setRepeatsProgress(++repeatsDoneCount, repeatsAllCount)
            }
        }
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_active_exercise
}
