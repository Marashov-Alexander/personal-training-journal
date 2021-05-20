package ru.ok.technopolis.training.personal.viewholders

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.item_parameter_writer.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.EQUALS_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.GREATER_BETTER
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity.Companion.LESS_BETTER
import kotlin.math.abs

class ActiveParameterViewHolder(
    itemView: View
) : BaseViewHolder<ParameterEntity>(itemView) {

    private var title: TextView = itemView.title
    private var value: EditText = itemView.value
    private var units: TextView = itemView.units
    private var goalStatus: TextView = itemView.goal_status

    private var goalValue: Float = 0f
    private var item: ParameterEntity? = null

    override fun bind(item: ParameterEntity) {
        this.item = item
        title.text = item.name

        units.text = item.measureUnit
        goalValue = item.value
        value.setText(goalValue.toString())
        goalStatus.text = ""
        updateGoalStatus(item.value)

        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val newValue: Float? = s.toString().toFloatOrNull()
                    updateGoalStatus(newValue)
                } else {
                    goalStatus.text = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateGoalStatus(newValue: Float?) {
        item?.let { item ->
            if (newValue == null) {
                goalStatus.text = ""
            } else {
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
                    if (percents > 9999f) {
                        goalStatus.text = ""
                        return
                    }
                    goalStatus.text = String.format(itemView.resources.getString(R.string.parameter_progress), percents)
                    if (percents > 75f) {
                        goalStatus.setTextColor(itemView.context.getColor(R.color.bright_green))
                    } else {
                        goalStatus.setTextColor(Color.BLACK)
                    }
                }
            }
        }
    }
}
