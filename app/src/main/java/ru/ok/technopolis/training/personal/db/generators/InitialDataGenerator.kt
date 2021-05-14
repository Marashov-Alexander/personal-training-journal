package ru.ok.technopolis.training.personal.db.generators

import android.content.Context
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseTypeEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity

class InitialDataGenerator {
    companion object {

        fun getExerciseTypes(context: Context): List<ExerciseTypeEntity> {
            return listOf(
                ExerciseTypeEntity(context.resources.getString(R.string.strength)),
                ExerciseTypeEntity(context.resources.getString(R.string.dexterity)),
                ExerciseTypeEntity(context.resources.getString(R.string.coordination)),
                ExerciseTypeEntity(context.resources.getString(R.string.endurance)),
                ExerciseTypeEntity(context.resources.getString(R.string.speed)),
                ExerciseTypeEntity(context.resources.getString(R.string.flexibility)),
                ExerciseTypeEntity(context.resources.getString(R.string.concentration)),
                ExerciseTypeEntity(context.resources.getString(R.string.other_type))
            )
        }
        fun getParameters(context: Context): List<ParameterEntity> {
            return listOf(
                ParameterEntity(context.resources.getString(R.string.time), context.resources.getString(R.string.sec)),
                ParameterEntity(context.resources.getString(R.string.distance), context.resources.getString(R.string.m)),
                ParameterEntity(context.resources.getString(R.string.weight), context.resources.getString(R.string.kg))
            )
        }
    }
}