package ru.ok.technopolis.training.personal.viewholders

import android.text.Editable
import android.text.TextWatcher
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
    private var editBtn: FloatingActionButton = itemView.edit
    private var customCheckbox: FloatingActionButton = itemView.custom_checkbox
    private var title: TextView = itemView.title
    private var description: TextView = itemView.description
    private var supersetCounterBackground: CardView = itemView.superset_counter_background
    private var supersetBackground: CardView = itemView.superset_background
    private var supersetCounter: EditText = itemView.superset_counter
    private var exerciseBackground: MaterialCardView = itemView.exercise_background
    private var cornerRadius: Float = 0f

    private lateinit var chooseListener: () -> Unit

    // TODO: обновлять item при изменениях?
    override fun bind(item: ExerciseItem) {
        cornerRadius = itemView.resources.getDimension(R.dimen.superset_corner_radius)
        title.text = item.exercise.name
        description.text = item.description
        supersetCounter.setText("${item.workoutExercise.counter}")
        setCounter(item.counterVisibility)
        setColor(itemView.context.getColor(item.getColorId()))
        setCornerMode(item.cornerMode)
        setItemMode(item.itemMode())
        item.checked?.let { checked ->
            setItemChecked(checked)
        }
        chooseListener = {
            item.checked?.let { checked ->
                item.checked = !checked
                setItemChecked(!checked)
            }
        }
        supersetCounter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val newValue: Int? = s.toString().toIntOrNull()
                    if (newValue != null) {
                        item.workoutExercise.counter = newValue
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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
        editBtn.visibility = VISIBLE
        return this
    }

    private fun supersetModeOff(): ExerciseItemViewHolder {
        customCheckbox.visibility = INVISIBLE
        editBtn.visibility = VISIBLE
        return this
    }

    private fun editableMode(): ExerciseItemViewHolder {
        customCheckbox.visibility = VISIBLE
        editBtn.visibility = INVISIBLE
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

    fun setOnEditListener(onClick: (View) -> Unit) {
        editBtn.setOnClickListener(onClick)
    }

    fun setOnViewClickListener(onClick: () -> Boolean) {
        exerciseBackground.setOnClickListener {
            val chooseMode = onClick()
            if (chooseMode) {
                chooseListener.invoke()
            }
        }
    }

    enum class CornerMode {
        ALL, NONE, TOP, BOTTOM
    }

    enum class ItemMode {
        SUPERSET, SIMPLE, EDITABLE
    }

}