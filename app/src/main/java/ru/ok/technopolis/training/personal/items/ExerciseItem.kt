package ru.ok.technopolis.training.personal.items

import android.view.View
import android.view.View.GONE
import okhttp3.internal.immutableListOf
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.interfaces.WithId
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder

data class ExerciseItem(

    override val id: String,
    val iconId: Int,
    val title: String,
    val description: String,
    // id суперсета, к которому относится упражнение. Если null, то упражнение не включено в суперсет. По этому полю определяется цвет
    var supersetGroupId: Int?

) : WithId {
    var counter: Int? = null
    var checked: Boolean? = null
    var counterVisibility: Int = GONE
    var cornerMode: ExerciseItemViewHolder.CornerMode = ExerciseItemViewHolder.CornerMode.ALL

    companion object {
        const val transparentColorId = R.color.transparent
        val colorIds = immutableListOf(
                R.color.periwinkle,
                R.color.light_mallow,
                R.color.magic_mint,
                R.color.gray_4,
                R.color.pang
        )
    }

    fun itemMode(): ExerciseItemViewHolder.ItemMode {
        return if (supersetGroupId == null) {
            if (checked == null) {
                ExerciseItemViewHolder.ItemMode.SIMPLE
            } else {
                ExerciseItemViewHolder.ItemMode.EDITABLE
            }
        } else {
            ExerciseItemViewHolder.ItemMode.SUPERSET
        }
    }

    fun getCounterMode(cornerMode: ExerciseItemViewHolder.CornerMode): Int =
            when (cornerMode) {
                ExerciseItemViewHolder.CornerMode.BOTTOM -> View.INVISIBLE
                ExerciseItemViewHolder.CornerMode.ALL -> View.INVISIBLE
                ExerciseItemViewHolder.CornerMode.TOP -> View.VISIBLE
                ExerciseItemViewHolder.CornerMode.NONE -> View.INVISIBLE
            }

    fun getCornerMode(prevItem: ExerciseItem?, nextItem: ExerciseItem?): ExerciseItemViewHolder.CornerMode {
        val isSuperset = supersetGroupId != null
        val prevDisconnected = prevItem == null || prevItem.supersetGroupId != supersetGroupId
        val nextDisconnected = nextItem == null || nextItem.supersetGroupId != supersetGroupId
        val isFirst = isSuperset && prevDisconnected
        val isLast = isSuperset && nextDisconnected
        val isMiddle = !isFirst && !isLast && isSuperset && !prevDisconnected && !nextDisconnected

        return when {
            isFirst && !isLast -> ExerciseItemViewHolder.CornerMode.TOP
            isLast && !isFirst -> ExerciseItemViewHolder.CornerMode.BOTTOM
            isMiddle -> ExerciseItemViewHolder.CornerMode.NONE
            else -> ExerciseItemViewHolder.CornerMode.ALL
        }
    }

    fun getColorId(): Int {
        return if (supersetGroupId == null) {
            transparentColorId
        } else {
            colorIds[supersetGroupId!! % colorIds.size]
        }
    }

    fun isSingleExerciseSuperset(prev: ExerciseItem?, next: ExerciseItem?): Boolean {
        val prevDisconnected = prev == null || prev.supersetGroupId != supersetGroupId
        val nextDisconnected = next == null || next.supersetGroupId != supersetGroupId
        return prevDisconnected && nextDisconnected
    }
}
