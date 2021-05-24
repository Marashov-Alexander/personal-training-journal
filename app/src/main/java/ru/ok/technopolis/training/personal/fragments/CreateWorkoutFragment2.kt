package ru.ok.technopolis.training.personal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_new_workout_2.*
import kotlinx.android.synthetic.main.item_media_loader.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.ParameterDialogFragment
import ru.ok.technopolis.training.personal.fragments.dialogs.PlanWorkoutDialog
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.views.MediaLoaderWrapper


class CreateWorkoutFragment2 : BaseFragment(), PlanWorkoutDialog.PlanWorkoutListener {

    private var prevStepCard: MaterialCardView? = null
    private var nextStepCard: MaterialCardView? = null
    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())
    private var mediaLoader: MediaLoaderWrapper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.base_toolbar?.title = getString(R.string.workout_creation)
        prevStepCard = prev_step_card
        nextStepCard = next_step_card

        nextStepCard?.setOnClickListener {
            PlanWorkoutDialog(WorkoutEntity("name", "12:00", 3,"description"), this)
                    .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
        }

        prevStepCard?.setOnClickListener {
            router?.goToPrevFragment()
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

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_workout_2

}
