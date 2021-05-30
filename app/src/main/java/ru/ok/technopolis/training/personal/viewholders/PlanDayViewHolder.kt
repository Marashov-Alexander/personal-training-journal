package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import kotlinx.android.synthetic.main.item_day.view.*
import ru.ok.technopolis.training.personal.items.PlanDayItem
import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.text.DateFormat.getTimeInstance

class PlanDayViewHolder (
itemView: View
) : BaseViewHolder<PlanDayItem>(itemView) {
    private val datBox: CheckBox = itemView.day

    //WARNING: usage of public val that should be private
    val time: EditText = itemView.day_start_at_edit_text
    var localItem: PlanDayItem? = null

    private val formatter: DateFormat = getTimeInstance(SHORT)
    override fun bind(item: PlanDayItem) {
        localItem = item
        datBox.text = item.name
        time.setText(formatter.format(item.time))
        datBox.isChecked = item.checked
        datBox.setOnCheckedChangeListener { _, checked ->
            item.checked = checked
        }
    }
    fun update(item: PlanDayItem) {
        datBox.text = item.name
        time.setText(formatter.format(item.time))
        datBox.isChecked = item.checked
    }
    fun setClickListener(onStart: (EditText, PlanDayItem) -> Unit) {
       time.setOnClickListener {onStart.invoke(time, localItem!!) }
    }

}