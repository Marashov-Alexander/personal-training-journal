package ru.ok.technopolis.training.personal.db.generators

import android.content.Context
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.SubscriptionEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.UserExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
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

        fun getTestUserSubscriptionEntity(): SubscriptionEntity {
            return SubscriptionEntity(1, 10)
        }
        fun getTestAuthorSubscriptionEntity(): SubscriptionEntity {
            return SubscriptionEntity(10, 1)
        }

        fun getTestCategories(context: Context): List<WorkoutCategoryEntity> {
            val stringArray = context.resources.getStringArray(R.array.workout_types)
            return stringArray.map { WorkoutCategoryEntity(it) }
        }

        fun getTestExerciseCategories(context: Context): List<ExerciseCategoryEntity> {
            val stringArray = context.resources.getStringArray(R.array.exercise_types)
            return stringArray.map { ExerciseCategoryEntity(it) }
        }

        fun getTestSports(context: Context): List<WorkoutSportEntity> {
            val stringArray = context.resources.getStringArray(R.array.sport_types)
            return stringArray.map { WorkoutSportEntity(it) }
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
                    1,
                    true,
                    1L
            )
        }

        fun getAuthorTestExercise(): ExerciseEntity {
            return ExerciseEntity(
                    "Author exercise",
                    "Description",
                    1,
                    true,
                    10
            )
        }

        fun getTestMessage(): MessageEntity {
            return MessageEntity(
                    "Test Message",
                    System.currentTimeMillis(),
                    10,
                    1,
                    1,
                    null,
                    true
            )
        }

        fun getTestWorkoutExercise(): WorkoutExerciseEntity {
            return WorkoutExerciseEntity(1, 1)
        }

        fun getTestAuthorWorkoutExercise(): WorkoutExerciseEntity {
            return WorkoutExerciseEntity(2, 2)
        }

        fun getTestUserWorkout(): UserWorkoutEntity {
            return UserWorkoutEntity(1, 1, true)
        }

        fun getTestUserExercise(): UserExerciseEntity {
            return UserExerciseEntity(1, 1, true)
        }

        fun getTestAuthorWorkout(): UserWorkoutEntity {
            return UserWorkoutEntity(10, 2, true)
        }

        fun getTestAuthorExercise(): UserExerciseEntity {
            return UserExerciseEntity(10, 2, true)
        }

    }
}