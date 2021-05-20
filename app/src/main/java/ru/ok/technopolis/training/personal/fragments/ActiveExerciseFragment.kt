package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_active_exercise.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ActiveParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ActiveParameterViewHolder

class ActiveExerciseFragment : BaseFragment() {

    private var exerciseProgressText: TextView? = null
    private var repeatsDone: View? = null
    private var repeatsLeft: View? = null
    private var parametersRecycler: RecyclerView? = null
    private var nextCard: MaterialCardView? = null

    private var repeatsAllCount = 10
    private var repeatsDoneCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exerciseProgressText = exercise_progress_text
        repeatsDone = repeats_done
        repeatsLeft = repeats_left
        parametersRecycler = parameters_recycler
        nextCard = next_card

        setRepeatsProgress(repeatsDoneCount, repeatsAllCount)
        nextCard?.setOnClickListener {
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
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_active_exercise
}
