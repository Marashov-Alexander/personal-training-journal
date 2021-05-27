package ru.ok.technopolis.training.personal.viewholders

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.item_parameter_short.view.*
import ru.ok.technopolis.training.personal.items.ParameterItem


class ParameterViewHolder(
        itemView: View
) : BaseViewHolder<ParameterItem>(itemView) {

    private val icon: ImageView = itemView.icon
    private val title: TextView = itemView.title
    private val units: TextView = itemView.units
    private val value: EditText = itemView.value
    private val edit: FloatingActionButton = itemView.edit
    private var item: ParameterItem? = null
    private var onValueChanged: ((Float, ParameterItem?) -> Unit)? = null


    init {
        println("wow")
        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val newValue: Float? = s.toString().toFloatOrNull()
                    if (newValue != null) {
                        onValueChanged?.invoke(newValue, item)
                        // this@ParameterViewHolder.item
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun bind(item: ParameterItem) {
        this.item = item
        title.text = item.parameter?.name
        units.text = item.parameter?.measureUnit
        value.setText(item.levelExerciseParameterEntity?.value.toString())
        value.isEnabled = item.editable



        edit.visibility = if (item.editable) VISIBLE else INVISIBLE
        itemView.visibility = if (item.invisible) INVISIBLE else VISIBLE
    }

    fun setOnEditListener(onEditListener: (ParameterItem) -> Unit) {
        item?.let { item ->
            if (item.editable) {
                edit.setOnClickListener {
                    onEditListener.invoke(item)
                }
            }
        }
    }

    fun setOnValueChangedListener(onValueChanged: (Float, ParameterItem?) -> Unit) {
        this.onValueChanged = onValueChanged
    }

}