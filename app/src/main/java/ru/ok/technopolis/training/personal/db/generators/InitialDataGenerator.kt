package ru.ok.technopolis.training.personal.db.generators

import android.content.Context
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity

class InitialDataGenerator {
    companion object {
        fun getParameters(context: Context): List<ParameterEntity> {
            return listOf(
                ParameterEntity(context.resources.getString(R.string.time), context.resources.getString(R.string.sec)),
                ParameterEntity(context.resources.getString(R.string.distance), context.resources.getString(R.string.m)),
                ParameterEntity(context.resources.getString(R.string.weight), context.resources.getString(R.string.kg))
            )
        }
        fun getTestUser(): UserEntity {
            return UserEntity(
                "Tester",
                "Testov",
                "Testovich",
                "test@test.test",
                "Mail",
                null,
                1
            )
        }

        fun getTestAuthor(): UserEntity {
            return UserEntity(
                    "Author",
                    "Testov",
                    "Testovich",
                    "test@test.test",
                    "Mail",
                    null,
                    10
            )
        }

        fun getTestCategory(): WorkoutCategoryEntity {
            return WorkoutCategoryEntity(
                    "category"
            )
        }

        fun getTestSport(): WorkoutSportEntity {
            return WorkoutSportEntity(
                    "sport"
            )
        }

        fun getTestWorkout(): WorkoutEntity {
            return WorkoutEntity(
                "My workout 1",
                "Description",
                1,
                1,
                0,
                true,
                1
            )
        }

        fun getAuthorTestWorkout(): WorkoutEntity {
            return WorkoutEntity(
                    "Author workout",
                    "Description",
                    1,
                    1,
                    0,
                    true,
                    10
            )
        }

        fun getTestExercise(): ExerciseEntity {
            return ExerciseEntity(
                "My exercise 1",
                "Description",
                "My category",
                true,
                1L
            )
        }

        fun getTestWorkoutExercise(): WorkoutExerciseEntity {
            return WorkoutExerciseEntity(1, 1)
        }

    }
}