package ru.ok.technopolis.training.personal.views

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter


class MultiSpinner : androidx.appcompat.widget.AppCompatSpinner, OnMultiChoiceClickListener, DialogInterface.OnCancelListener {
    private var items: List<String>? = null
    private lateinit var selected: BooleanArray
    private var defaultText: String? = null
    private var listener: MultiSpinnerListener? = null

    constructor(context: Context?) : super(context)
    constructor(arg0: Context?, arg1: AttributeSet?) : super(arg0, arg1)
    constructor(arg0: Context?, arg1: AttributeSet?, arg2: Int) : super(arg0, arg1, arg2)

    override fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean) {
        selected[which] = isChecked
    }

    override fun onCancel(dialog: DialogInterface?) { // refresh text on spinner
        val spinnerBuffer = StringBuffer()
        var someSelected = false
        for (i in items!!.indices) {
            if (selected[i]) {
                spinnerBuffer.append(items!![i])
                spinnerBuffer.append(", ")
                someSelected = true
            }
        }
        var spinnerText: String?
        if (someSelected) {
            spinnerText = spinnerBuffer.toString()
            if (spinnerText.length > 2) spinnerText = spinnerText.substring(0, spinnerText.length - 2)
        } else {
            spinnerText = defaultText
        }
        val adapter = ArrayAdapter(context,
            R.layout.simple_spinner_item, arrayOf(spinnerText))
        setAdapter(adapter)
        listener!!.onItemsSelected(selected)
    }

    override fun performClick(): Boolean {
        super.performClick()
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMultiChoiceItems(
            items!!.toTypedArray(), selected, this)
        builder.setPositiveButton(R.string.ok
        ) { dialog, _ -> dialog.cancel() }
        builder.setOnCancelListener(this)
        builder.show()
        return true
    }

    fun setItems(items: List<String>, defaultText: String?,
                 listener: MultiSpinnerListener?, selected: BooleanArray? = null) {
        this.items = items
        this.defaultText = defaultText
        this.listener = listener
        // none selected by default
        if (selected == null) {
            this.selected = BooleanArray(items.size)
        } else {
            this.selected = selected
            for (i in this.selected.indices) this.selected[i] = false
        }
        // all text on the spinner
        val adapter = ArrayAdapter(context,
            R.layout.simple_spinner_item, arrayOf(defaultText))
        setAdapter(adapter)
    }

    interface MultiSpinnerListener {
        fun onItemsSelected(selected: BooleanArray?)
    }
}