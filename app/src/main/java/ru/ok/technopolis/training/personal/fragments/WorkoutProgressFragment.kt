package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_workout_progress.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.views.CustomScrollView
import ru.ok.technopolis.training.personal.views.ProgressChartView

class WorkoutProgressFragment : BaseFragment() {

    private var scrollView: CustomScrollView? = null
    private var chart: ProgressChartView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollView = scroll_view
        chart = progress_chart
        chart?.setScrollLockListener(
            lockListener = {
                scrollView?.isEnableScrolling = false
            },
            unlockListener = {
                scrollView?.isEnableScrolling = true
            }
        )
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_progress
}
