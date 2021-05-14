package ru.ok.technopolis.training.personal.viewholders

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.item_active_exercise_element.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.EQUALS_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.GREATER_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.LESS_BETTER
import kotlin.math.abs

class ActiveExerciseViewHolder(
    itemView: View
) : BaseViewHolder<ParameterEntity>(itemView) {

    private var name: TextView = itemView.parameter_name
    private var goal: EditText = itemView.parameter_value
    private var value: EditText = itemView.parameter_value_result
    private var unit1: TextView = itemView.parameter_unit
    private var unit2: TextView = itemView.parameter_unit_result
    private var progress: TextView = itemView.parameter_progress

    private var goalValue: Float = 0f

    override fun bind(item: ParameterEntity) {
        name.text = item.name

        unit1.text = item.measureUnit
        unit2.text = item.measureUnit
        goalValue = item.value
        value.setText("")
        goal.setText(item.value.toString())
        goal.isEnabled = false
        progress.text = ""

        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val newValue: Float? = s.toString().toFloatOrNull()
                    if (newValue != null) {
                        item.value = newValue
                        if (goalValue > 0.000001) {
                            val percents = when (item.resultType) {
                                LESS_BETTER -> {
                                    goalValue * 100f / item.value
                                }
                                EQUALS_BETTER -> {
                                    100f - abs(item.value - goalValue) * 100f / goalValue
                                }
                                GREATER_BETTER -> {
                                    item.value * 100f / goalValue
                                }
                                else -> {
                                    0f
                                }
                            }
                            progress.text = String.format(itemView.resources.getString(R.string.parameter_progress), percents)
                            if (percents > 75f) {
                                progress.setTextColor(Color.GREEN)
                            } else {
                                progress.setTextColor(Color.BLACK)
                            }
                        } else {
                            progress.text = ""
                        }
                    }
                } else {
                    progress.text = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
