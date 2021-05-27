package ru.ok.technopolis.training.personal.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_parameter_full.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.EQUALS_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.GREATER_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.LESS_BETTER
import ru.ok.technopolis.training.personal.items.ParameterItem

class ParameterDialogFragment(
    private val item: ParameterItem,
    private val listener: ParameterDialogListener
): DialogFragment() {

    private var inputType: ImageView? = null
    private var saveBtn: MaterialCardView? = null
    private var inputTypeId: Int = GREATER_BETTER

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.item_parameter_full, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            resetErrors(view)

            val units = view.units
            val chosenUnit = view.chosen_unit
            units.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                private var loaded = false
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (units.selectedItem.toString() != "" && loaded) {
                        chosenUnit.setText(units.selectedItem.toString())
                    }
                    loaded = true
                }
            }

            saveBtn = view.save_card
            saveBtn!!.setOnClickListener {
                if (updateParameterEntity(view)) {
                    listener.onSaveParameterClick(item)
                    dialog.dismiss()
                }
            }

            inputType = view.input_type
            inputType!!.setOnCreateContextMenuListener { menu, _, _ ->
                menu?.add(0, GREATER_BETTER, 0, R.string.greater_better)?.setOnMenuItemClickListener {
                    typeMenuItemClicked(it.itemId)
                    true
                }
                menu?.add(0, LESS_BETTER, 0, R.string.less_better)?.setOnMenuItemClickListener {
                    typeMenuItemClicked(it.itemId)
                    true
                }
                menu?.add(0, EQUALS_BETTER, 0, R.string.equals_better)?.setOnMenuItemClickListener {
                    typeMenuItemClicked(it.itemId)
                    true
                }
            }
            typeMenuItemClicked(item.parameter.resultType)
            inputType!!.setOnClickListener {
                it.showContextMenu()
            }

            view.stopwatch_switch.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    view.timer_switch.isChecked = false
                }
            }

            view.timer_switch.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    view.stopwatch_switch.isChecked = false
                }
            }

            bindParameter(view)

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun resetErrors(view: View?) {
        view?.let {
            it.units.background.setTint(Color.GRAY)
            it.title.backgroundTintList = null
            it.value.backgroundTintList = null
        }
    }

    private fun bindParameter(view: View?) {
        view?.let {
            it.value.setText(item.levelExerciseParameterEntity.value.toString())
            it.title.setText(item.parameter.name)
            typeMenuItemClicked(item.parameter.resultType)
            it.show_in_description_checkbox.isChecked = item.parameter.showInDescription
            it.stopwatch_switch.isChecked = (item.parameter.input == ParameterEntity.INPUT_STOPWATCH)
            it.timer_switch.isChecked = (item.parameter.input == ParameterEntity.INPUT_TIMER)
            it.chosen_unit.setText(item.parameter.measureUnit)
        }
    }

    private fun updateParameterEntity(view: View?): Boolean {
        view?.let {
            val value = it.value.text.toString().toFloatOrNull()
            val name = it.title.text.toString()
            val measureUnit = it.chosen_unit.text.toString()
            var error = false
            if (value == null) {
                error = true
                it.value.background.setTint(Color.RED)
            }
            if (name.isBlank()) {
                error = true
                it.title.background.setTint(Color.RED)
            }
            if (measureUnit.isBlank()) {
                error = true
                it.units.background.setTint(Color.RED)
            }

            if (error) {
                return false
            }

            item.parameter.measureUnit = measureUnit
            item.parameter.name = name
            item.parameter.resultType = inputTypeId
            item.levelExerciseParameterEntity.value = value!!
            item.parameter.showInDescription = it.show_in_description_checkbox.isChecked
            item.parameter.input = when {
                it.stopwatch_switch.isChecked -> ParameterEntity.INPUT_STOPWATCH
                it.timer_switch.isChecked -> ParameterEntity.INPUT_TIMER
                else -> ParameterEntity.INPUT_SIMPLE
            }
            return true
        }
        return false
    }

    private fun typeMenuItemClicked(id: Int) {
        inputTypeId = id
        when (id) {
            GREATER_BETTER -> {
                inputType?.setImageResource(R.drawable.ic_arrow_up)
                inputType?.rotation = 0f
            }
            LESS_BETTER -> {
                inputType?.setImageResource(R.drawable.ic_arrow_up)
                inputType?.rotation = 180f
            }
            EQUALS_BETTER -> {
                inputType?.setImageResource(R.drawable.equalsign)
                inputType?.rotation = 0f
            }
        }
    }

    interface ParameterDialogListener {
        fun onSaveParameterClick(item: ParameterItem)
    }
}