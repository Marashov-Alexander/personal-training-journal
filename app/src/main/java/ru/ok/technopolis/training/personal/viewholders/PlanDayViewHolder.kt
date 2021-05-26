package ru.ok.technopolis.training.personal.viewholders

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import kotlinx.android.synthetic.main.item_day.view.*
import kotlinx.android.synthetic.main.item_plan_workout.view.*
import ru.ok.technopolis.training.personal.items.PlanDayItem
import java.text.DateFormat
import java.text.DateFormat.*

class PlanDayViewHolder (
itemView: View
) : BaseViewHolder<PlanDayItem>(itemView) {
    private val datBox: CheckBox = itemView.day

    //WARNING: usage of public val that should be private
    val time: EditText = itemView.day_start_at_edit_text

    private val formatter: DateFormat = getTimeInstance(SHORT)
    override fun bind(item: PlanDayItem) {
        datBox.text = item.name
        time.setText(formatter.format(item.time))
    }
    fun update(item: PlanDayItem) {
        datBox.text = item.name
        time.setText(formatter.format(item.time))
    }
    fun setClickListener(onStart: () -> Unit) {
       time.setOnClickListener {onStart.invoke() }
    }

}