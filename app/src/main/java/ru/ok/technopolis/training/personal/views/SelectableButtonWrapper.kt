package ru.ok.technopolis.training.personal.views

import android.view.View
import android.widget.TextView
import ru.ok.technopolis.training.personal.R

class SelectableButtonWrapper(
    private val title: TextView,
    private val background: View,
    private val underline: View,
    onClickListener: () -> Unit = {}
) {
    init {
        background.setOnClickListener{onClickListener.invoke()}
    }

    fun setSelected(selected: Boolean) {
        println("Title=${title.text}, selected=${selected}")
        if (selected) {
            background.setBackgroundResource(R.drawable.day_highlithed_background)
            underline.setBackgroundResource(R.color.magic_mint)
        } else {
            background.background = null
            underline.setBackgroundResource(R.color.white)
        }
    }
}