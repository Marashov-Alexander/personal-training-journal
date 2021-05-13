package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_new_workout_2.*
import ru.ok.technopolis.training.personal.R


class CreateWorkoutFragment2 : BaseFragment() {

    private var prevStepCard: MaterialCardView? = null
    private var nextStepCard: MaterialCardView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prevStepCard = prev_step_card
        nextStepCard = next_step_card
        prevStepCard?.setOnClickListener {
            router?.goToPrevFragment()
        }

    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_workout_2

}
