package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.view.View.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.CornerFamily
import kotlinx.android.synthetic.main.item_exercise.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ExerciseItem


class ExerciseItemViewHolder(
        itemView: View
) : BaseViewHolder<ExerciseItem>(itemView) {

    private var icon: ImageView = itemView.icon
    private var customCheckbox: FloatingActionButton = itemView.custom_checkbox
    private var title: TextView = itemView.title
    private var description: TextView = itemView.description
    private var supersetCounterBackground: CardView = itemView.superset_counter_background
    private var supersetBackground: CardView = itemView.superset_background
    private var supersetCounter: EditText = itemView.superset_counter
    private var exerciseBackground: MaterialCardView = itemView.exercise_background
    private var cornerRadius: Float = 0f

    override fun bind(item: ExerciseItem) {
        cornerRadius = itemView.resources.getDimension(R.dimen.superset_corner_radius)
        title.text = item.title
        description.text = item.description
        supersetCounter.setText("${item.counter}")
        setCounter(item.counterVisibility)
        setColor(itemView.context.getColor(item.getColorId()))
        setCornerMode(item.cornerMode)
        setItemMode(item.itemMode())
        exerciseBackground.setOnClickListener {
            item.checked?.let { checked ->
                item.checked = !checked
                setItemChecked(!checked)
            }
        }
        item.checked?.let { checked ->
            setItemChecked(checked)
        }
    }

    private fun setItemChecked(checked: Boolean) {
        if (checked) {
            customCheckbox.background?.setTint(itemView.context.getColor(R.color.green))
        } else {
            customCheckbox.background?.setTint(itemView.context.getColor(R.color.white))
        }
    }

    private fun supersetModeOn(): ExerciseItemViewHolder {
        customCheckbox.visibility = INVISIBLE
        return this
    }

    private fun supersetModeOff(): ExerciseItemViewHolder {
        customCheckbox.visibility = INVISIBLE
        return this
    }

    private fun editableMode(): ExerciseItemViewHolder {
        customCheckbox.visibility = VISIBLE

        return this
    }

    private fun setItemMode(mode: ItemMode): ExerciseItemViewHolder {
        when (mode) {
            ItemMode.EDITABLE -> editableMode()
            ItemMode.SIMPLE -> supersetModeOff()
            ItemMode.SUPERSET -> supersetModeOn()
        }
        return this
    }

    private fun setCounter(counterVisibility: Int): ExerciseItemViewHolder {
        supersetCounter.visibility = counterVisibility
        supersetCounterBackground.visibility = counterVisibility
        supersetBackground.visibility = counterVisibility
        return this
    }

    private fun setCornerMode(cornerMode: CornerMode): ExerciseItemViewHolder {
        val (topLeft, topRight, bottomRight, bottomLeft) =
                when (cornerMode) {
                    CornerMode.ALL -> listOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
                    CornerMode.BOTTOM -> listOf(0f, 0f, cornerRadius, cornerRadius)
                    CornerMode.TOP -> listOf(cornerRadius, cornerRadius, 0f, 0f)
                    CornerMode.NONE -> listOf(0f, 0f, 0f, 0f)
                }
        exerciseBackground.shapeAppearanceModel = exerciseBackground.shapeAppearanceModel
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, topLeft)
                .setTopRightCorner(CornerFamily.ROUNDED, topRight)
                .setBottomRightCorner(CornerFamily.ROUNDED, bottomRight)
                .setBottomLeftCorner(CornerFamily.ROUNDED, bottomLeft)
                .build()
        return this
    }

    private fun setColor(color: Int): ExerciseItemViewHolder {
        exerciseBackground.background.setTint(color)
        supersetBackground.background.setTint(color)
        return this
    }

    fun setLongClickListener(onLongClick: (View) -> Unit) {
        exerciseBackground.setOnLongClickListener {
            onLongClick.invoke(it)
            true
        }
    }

    enum class CornerMode {
        ALL, NONE, TOP, BOTTOM
    }

    enum class ItemMode {
        SUPERSET, SIMPLE, EDITABLE
    }

}