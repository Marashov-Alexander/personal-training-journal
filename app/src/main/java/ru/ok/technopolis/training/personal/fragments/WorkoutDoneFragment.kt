package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.dialogs.CongratulationsDialogFragment

class WorkoutDoneFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CongratulationsDialogFragment()
            .show(requireActivity().supportFragmentManager, "CongratulationsDialogFragment")
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_result
}
