package ru.ok.technopolis.training.personal.viewholders

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import kotlinx.android.synthetic.main.item_exercise_element.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.EQUALS_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.GREATER_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.LESS_BETTER

class ExerciseElementViewHolder(
    itemView: View
) : BaseViewHolder<ParameterEntity>(itemView) {

    private var title: EditText = itemView.title
    private var value: EditText = itemView.value
    private var units: Spinner = itemView.units
    private var chosenUnit: EditText = itemView.chosen_unit
    private var type: ImageView = itemView.result_type
    private var delete: ImageView = itemView.delete_parameter_button
    private var item: ParameterEntity? = null

    override fun bind(item: ParameterEntity) {
        this.item = item
        this.item?.let { newItem ->

            units.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (units.selectedItem.toString() != "") {
                        chosenUnit.setText(units.selectedItem.toString())
                    }
                }
            }

            title.setText(newItem.name)
            value.setText(newItem.value.toString())
            type.setOnCreateContextMenuListener { menu, _, _ ->
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
            typeMenuItemClicked(newItem.resultType)
            type.setOnClickListener {
                it.showContextMenu()
            }
            title.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty()) {
                        newItem.name = s.toString()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            value.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty()) {
                        val newValue: Float? = s.toString().toFloatOrNull()
                        if (newValue != null) {
                            newItem.value = newValue
                        }
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            chosenUnit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty()) {
                        newItem.measureUnit = s.toString()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            chosenUnit.setText(newItem.measureUnit)
        }
    }

    fun setClickListener(onDeleteClick: (View) -> Unit) {
        delete.setOnClickListener(onDeleteClick)
    }

    private fun typeMenuItemClicked(id: Int) {
        item?.resultType = id
        when (id) {
            GREATER_BETTER -> {
                type.setImageResource(R.drawable.ic_arrow_up)
                type.rotation = 0f
            }
            LESS_BETTER -> {
                type.setImageResource(R.drawable.ic_arrow_up)
                type.rotation = 180f
            }
            EQUALS_BETTER -> {
                type.setImageResource(R.drawable.equalsign)
                type.rotation = 0f
            }
        }
    }
}
