package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_parameter_short.view.*
import ru.ok.technopolis.training.personal.items.ShortParameterItem


class ParameterViewHolder(
        itemView: View
) : BaseViewHolder<ShortParameterItem>(itemView) {

    private val icon: ImageView = itemView.icon
    private val title: TextView = itemView.title
    private val units: TextView = itemView.units
    private val value: EditText = itemView.value
    private val edit: ImageView = itemView.edit

    override fun bind(item: ShortParameterItem) {
        title.text = item.name
        units.text = item.unitName
        value.setText(item.value.toString())
        value.isEnabled = item.editable
        edit.visibility = if (item.editable) VISIBLE else INVISIBLE
        itemView.visibility = if (item.invisible) INVISIBLE else VISIBLE
    }

}