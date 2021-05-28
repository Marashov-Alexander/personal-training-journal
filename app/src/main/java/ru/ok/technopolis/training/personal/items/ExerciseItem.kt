package ru.ok.technopolis.training.personal.items

import android.view.View
import android.view.View.GONE
import okhttp3.internal.immutableListOf
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId
import ru.ok.technopolis.training.personal.viewholders.ExerciseItemViewHolder

data class ExerciseItem(

    override val id: String,
    val exercise: ExerciseEntity,
    // id суперсета, к которому относится упражнение. Если null, то упражнение не включено в суперсет. По этому полю определяется цвет
    var workoutExercise: WorkoutExerciseEntity

) : WithId {
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
        return if (workoutExercise.supersetGroupId == null) {
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
        val isSuperset = workoutExercise.supersetGroupId != null
        val prevDisconnected = prevItem == null || prevItem.workoutExercise.supersetGroupId != workoutExercise.supersetGroupId
        val nextDisconnected = nextItem == null || nextItem.workoutExercise.supersetGroupId != workoutExercise.supersetGroupId
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
        return if (workoutExercise.supersetGroupId == null) {
            transparentColorId
        } else {
            colorIds[workoutExercise.supersetGroupId!! % colorIds.size]
        }
    }

    fun isSingleExerciseSuperset(prev: ExerciseItem?, next: ExerciseItem?): Boolean {
        val prevDisconnected = prev == null || prev.workoutExercise.supersetGroupId != workoutExercise.supersetGroupId
        val nextDisconnected = next == null || next.workoutExercise.supersetGroupId != workoutExercise.supersetGroupId
        return prevDisconnected && nextDisconnected
    }
}
