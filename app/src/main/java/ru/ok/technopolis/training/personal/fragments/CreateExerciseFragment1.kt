package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_new_exercise_1.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortParameterItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder


class CreateExerciseFragment1 : BaseFragment() {

    private var nextStepCard: MaterialCardView? = null
    private var parametersRecycler: RecyclerView? = null
    private var parametersList: ItemsList<ShortParameterItem>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextStepCard = next_step_card
        nextStepCard?.setOnClickListener {
            router?.showNewExercisePage2()
        }

        val parameters = mutableListOf(
                ShortParameterItem("1", "Подходы", "раз", 10, 0, false),
                ShortParameterItem("2", "Повторения", "раз", 20, 0, false),
                ShortParameterItem("3", "Отдых", "сек", 30, 0, true),
                ShortParameterItem("4", "Parameter 4", "Units 4", 40, 0, true),
                ShortParameterItem("5", "Parameter 5", "Units 5", 50, 0, true),
                ShortParameterItem("6", "Невидимый айтем", "???", 0, 0, editable = true, invisible = true)
        )
        parametersList = ItemsList(parameters)

        parametersRecycler = parameters_recycler
        parametersRecycler?.adapter = ParameterAdapter(
                holderType = ParameterViewHolder::class,
                layoutId = R.layout.item_parameter_short,
                dataSource = parametersList!!,
                onClick = {

                }
        )
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        parametersRecycler?.layoutManager = layoutManager

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                if (position + 1 != parametersList?.size()) {
                    parametersList?.remove(position)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(parametersRecycler)

    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_1

}
