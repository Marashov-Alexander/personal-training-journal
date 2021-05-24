package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import kotlinx.android.synthetic.main.item_day.view.*
import ru.ok.technopolis.training.personal.items.PlanDayItem
import java.text.DateFormat
import java.text.DateFormat.*

class PlanDayViewHolder (
itemView: View
) : BaseViewHolder<PlanDayItem>(itemView) {
    private val datBox: CheckBox = itemView.day
    private val time: EditText = itemView.day_start_at_edit_text

    private val formatter: DateFormat = getTimeInstance(SHORT)

    override fun bind(item: PlanDayItem) {
        datBox.text = item.name
        time.setText(formatter.format(item.time))
    }
    fun update(item: PlanDayItem) {
        datBox.text = item.name
        time.setText(formatter.format(item.time))
    }
    fun setClickListener(onClickListener: (View) -> Unit) {
       time.setOnClickListener { onClickListener(time) }
    }

}