package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_workout_progress.*
import okhttp3.internal.immutableListOf
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.controllers.ButtonGroupController
import ru.ok.technopolis.training.personal.items.BundleItem
import ru.ok.technopolis.training.personal.items.ProgressItem
import ru.ok.technopolis.training.personal.items.SelectableItem
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.BundleAdapter
import ru.ok.technopolis.training.personal.viewholders.BundleItemViewHolder
import ru.ok.technopolis.training.personal.views.CustomScrollView
import ru.ok.technopolis.training.personal.views.ProgressChartView
import ru.ok.technopolis.training.personal.views.SelectableButtonWrapper

class WorkoutProgressFragment : BaseFragment() {

    private var scrollView: CustomScrollView? = null
    private var chart: ProgressChartView? = null
    private var buttonGroup: ButtonGroupController? = null
    private var exerciseRecycler: RecyclerView? = null

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

        exerciseRecycler = exercises_recycler
        val itemsList = SingleSelectableList(mutableListOf(
            BundleItem("0", 0, "Все"),
            BundleItem("1", 1, "Упражнение 1"),
            BundleItem("2", 2, "Упражнение 2"),
            BundleItem("3", 3, "Упражнение 3")
        ))
        val exerciseAdapter = BundleAdapter(
            holderType = BundleItemViewHolder::class,
            layoutId = R.layout.item_bundle,
            dataSource = itemsList,
            onBundleClick = { bundleItem, _ ->
                println("Bundle $bundleItem clicked")
                itemsList.select(bundleItem)
            }
        )
        exerciseRecycler?.adapter = exerciseAdapter
        val layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        exerciseRecycler?.layoutManager = layoutManager
        
        val data = immutableListOf(
            ProgressItem(60f, "17.05"),
            ProgressItem(80f, "18.05", "14:00"),
            ProgressItem(70f, "19.05"),
            ProgressItem(50f, "20.05"),
            ProgressItem(80f, "21.05"),
            ProgressItem(90f, "22.05"),
            ProgressItem(80f, "23.05"),
            ProgressItem(85f, "24.05"),
            ProgressItem(75f, "25.05"),
            ProgressItem(90f, "26.05")
        )

        chart?.bindData(
            data,
            100f,
            "% "
        )

        val selectableList = SingleSelectableList(mutableListOf(SelectableItem("0"), SelectableItem("1"), SelectableItem("2"), SelectableItem("3")))

        buttonGroup = ButtonGroupController(
            selectableList,
            listOf(
                SelectableButtonWrapper(day_mode, day_chosen, day_line) {
                    println("Clicked 0")
                    selectableList.select(0)
                    switchChartMode(ProgressChartView.ChartMode.DAY)
                },
                SelectableButtonWrapper(week_mode, week_chosen, week_line) {
                    println("Clicked 1")
                    selectableList.select(1)
                    switchChartMode(ProgressChartView.ChartMode.WEEK)
                },
                SelectableButtonWrapper(month_mode, month_chosen, month_line) {
                    println("Clicked 2")
                    selectableList.select(2)
                    switchChartMode(ProgressChartView.ChartMode.MONTH)
                },
                SelectableButtonWrapper(year_mode, year_chosen, year_line) {
                    println("Clicked 2")
                    selectableList.select(3)
                    switchChartMode(ProgressChartView.ChartMode.YEAR)
                }
            )
        )
        selectableList.select(0)
    }

    override fun onDetach() {
        buttonGroup?.detach()
        super.onDetach()
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_workout_progress
}
