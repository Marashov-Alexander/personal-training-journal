package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_new_exercise_1.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.ParameterDialogFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ShortParameterItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder


class CreateExerciseFragment1 : BaseFragment(), ParameterDialogFragment.ParameterDialogListener {

    private var nextStepCard: MaterialCardView? = null
    private var parametersRecycler: RecyclerView? = null
    private var parametersList: ItemsList<ShortParameterItem>? = null
    private var addParameterBtn: FloatingActionButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextStepCard = next_step_card
        nextStepCard?.setOnClickListener {
            router?.showNewExercisePage2()
        }
        addParameterBtn = add_parameter_button
        addParameterBtn?.setOnClickListener {
            ParameterDialogFragment(ParameterEntity("", ""), this)
                .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
        }

        val parameters = mutableListOf<ShortParameterItem>()
        for (i in 1..7) {
            parameters.add(ShortParameterItem(i.toString(), ParameterEntity("Параметр $i", "ед. изм."), true))
        }
        parameters.add(ShortParameterItem("0", null, editable = false, invisible = true))

        parametersList = ItemsList(parameters)

        parametersRecycler = parameters_recycler
        parametersRecycler?.adapter = ParameterAdapter(
                holderType = ParameterViewHolder::class,
                layoutId = R.layout.item_parameter_short,
                dataSource = parametersList!!,
                onEdit = {
                    ParameterDialogFragment(it.parameter!!, this)
                        .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
                },
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

    override fun onSaveClick(item: ParameterEntity) {
        println(item.toString())
    }

}
