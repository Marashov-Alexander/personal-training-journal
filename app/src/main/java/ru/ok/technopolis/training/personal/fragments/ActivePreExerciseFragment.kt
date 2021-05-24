package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_next_exercise.*
import kotlinx.android.synthetic.main.item_media_viewer.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.dialogs.DescriptionDialogFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.items.ProgressRectItem
import ru.ok.technopolis.training.personal.utils.RestCountDownTimer
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ProgressRectAdapter
import ru.ok.technopolis.training.personal.viewholders.ProgressRectViewHolder
import ru.ok.technopolis.training.personal.views.MediaViewerWrapper

class ActivePreExerciseFragment : BaseFragment() {

    private var startCard: MaterialCardView? = null
    private var timerValue: TextView? = null
    private var exerciseDescription: TextView? = null
    private var progressRecycler: RecyclerView? = null

    private var timer: CountDownTimer? = null

    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaViewer: MediaViewerWrapper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCard = start_card
        startCard?.setOnClickListener {
            // TODO: configure userId and workoutId
            println("On start exercise")
            timer?.cancel()
            router?.showActiveExercisePage(0, 0)
        }

        mediaViewer = MediaViewerWrapper(
            this,
            exercise_image_switcher,
            no_content,
            pos_value,
            pos_card,
            mediaList
        )

        exerciseDescription = exercise_description
        exerciseDescription?.setOnClickListener {
            showExerciseDescription("Title", "Description")
        }

        timerValue = timer_value
        timer = RestCountDownTimer(
            15,
            onTickListener = { timeLeft ->
                timerValue?.text = timeLeft
            },
            onFinishListener = {
                println("Слыш, работать")
            }
        )
        timer?.start()

        val progressItems = mutableListOf(
            ProgressRectItem("1", ProgressRectItem.ProgressStatus.PASSED),
            ProgressRectItem("2", ProgressRectItem.ProgressStatus.PASSED),
            ProgressRectItem("3", ProgressRectItem.ProgressStatus.FAILED),
            ProgressRectItem("4", ProgressRectItem.ProgressStatus.PASSED),
            ProgressRectItem("5", ProgressRectItem.ProgressStatus.CURRENT),
            ProgressRectItem("6", ProgressRectItem.ProgressStatus.FUTURE),
            ProgressRectItem("7", ProgressRectItem.ProgressStatus.FUTURE),
            ProgressRectItem("8", ProgressRectItem.ProgressStatus.FUTURE),
            ProgressRectItem("9", ProgressRectItem.ProgressStatus.FUTURE)
        )
        val progressList = ItemsList(progressItems)

        progressRecycler = progress_recycler
        progressRecycler?.adapter = ProgressRectAdapter(
            holderType = ProgressRectViewHolder::class,
            layoutId = R.layout.item_progress,
            dataSource = progressList,
            onClick = {

            }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        progressRecycler?.layoutManager = layoutManager

    }

    private fun showExerciseDescription(title: String, description: String) {
        DescriptionDialogFragment(title, description)
            .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_next_exercise
}
