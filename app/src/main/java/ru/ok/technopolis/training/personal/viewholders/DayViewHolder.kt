package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.day_item.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.DayItem
import ru.ok.technopolis.training.personal.items.EventColor
import java.text.DateFormat
import java.text.DateFormat.getDateInstance

class DayViewHolder(
    itemView: View
) : BaseViewHolder<DayItem>(itemView) {

    private var eventStatus: View = itemView.event_status
    private var dayBackground: View = itemView.highlighted_icon
    private var weekdayButton: TextView = itemView.weekday_button
    private var weekdayDate: TextView = itemView.weekday_date
    private var dayLine: View = itemView.day_line

    private val formatter: DateFormat = getDateInstance(DateFormat.SHORT)

    override fun bind(item: DayItem) {
        print("Bind item $item")
        update(item)
    }

    fun update(item: DayItem) {
        weekdayButton.text = item.name
        when (item.event) {
            EventColor.RED -> eventStatus.setBackgroundResource(R.drawable.day_red_event)
            EventColor.GREEN -> eventStatus.setBackgroundResource(R.drawable.day_green_event)
            EventColor.WHITE -> eventStatus.setBackgroundResource(R.drawable.day_event)
            EventColor.NONE -> eventStatus.background = null
        }
        when {
            item.isChosen -> {
                dayBackground.setBackgroundResource(R.drawable.day_highlithed_background)
                weekdayButton.setBackgroundResource(R.drawable.day_highlited_circle)
                weekdayDate.text = formatter.format(item.date)
                dayLine.setBackgroundResource(R.color.magic_mint)
            }
            item.isToday -> {
                dayBackground.background = null
                weekdayButton.setBackgroundResource(R.drawable.day_today_circle)
                weekdayDate.text = formatter.format(item.date).substringBeforeLast('.')
                dayLine.setBackgroundResource(R.color.white)
            }
            else -> {
                dayBackground.background = null
                weekdayButton.setBackgroundResource(R.drawable.day_circle)
                weekdayDate.text = formatter.format(item.date).substringBeforeLast('.')
                dayLine.setBackgroundResource(R.color.white)
            }
        }
    }
}