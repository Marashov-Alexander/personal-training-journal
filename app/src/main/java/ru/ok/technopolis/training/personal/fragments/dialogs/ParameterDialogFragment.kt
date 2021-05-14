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

class ParameterDialogFragment(private val item: ParameterEntity, private val listener: ParameterDialogListener): DialogFragment() {

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

            saveBtn = view.save_card
            saveBtn!!.setOnClickListener {
                if (updateParameterEntity(view)) {
                    listener.onSaveClick(item)
                    dialog.dismiss()
                }
            }

            val units = view.units
            val chosenUnit = view.chosen_unit
            units.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (units.selectedItem.toString() != "") {
                        chosenUnit.setText(units.selectedItem.toString())
                    }
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
            typeMenuItemClicked(item.resultType)
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
            it.value.setText(item.value.toString())
            it.title.setText(item.name)
            it.chosen_unit.setText(item.measureUnit)
            typeMenuItemClicked(item.resultType)
            it.show_in_description_checkbox.isChecked = item.showInDescription
            it.stopwatch_switch.isChecked = (item.input == ParameterEntity.INPUT_STOPWATCH)
            it.timer_switch.isChecked = (item.input == ParameterEntity.INPUT_TIMER)
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

            item.measureUnit = measureUnit
            item.name = name
            item.resultType = inputTypeId
            item.value = value!!
            item.showInDescription = it.show_in_description_checkbox.isChecked
            item.input = when {
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
        fun onSaveClick(item: ParameterEntity)
    }
}