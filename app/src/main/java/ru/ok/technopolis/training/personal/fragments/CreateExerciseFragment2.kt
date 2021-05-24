package ru.ok.technopolis.training.personal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_new_exercise_2.*
import kotlinx.android.synthetic.main.item_media_loader.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.views.MediaLoaderWrapper


class CreateExerciseFragment2 : BaseFragment() {

    private var prevStepCard: MaterialCardView? = null
    private var nextStepCard: MaterialCardView? = null
    private lateinit var muscleGroupsLabel: TextView
    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaLoader: MediaLoaderWrapper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.base_toolbar?.title = getString(R.string.exercise_creation)
        nextStepCard = next_step_card
        prevStepCard = prev_step_card
        prevStepCard?.setOnClickListener {
            router?.goToPrevFragment()
        }
        muscleGroupsLabel = muscle_groups_label
        muscleGroupsLabel.setOnClickListener {

        }

        mediaLoader = MediaLoaderWrapper(
            this,
            exercise_image_switcher,
            no_content,
            edit_content_btn,
            remove_content_btn,
            pos_value,
            pos_card,
            mediaList
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mediaLoader?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDetach() {
        super.onDetach()
        mediaLoader?.onDetach()
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_2

}
