package ru.ok.technopolis.training.personal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.ok.technopolis.training.personal.db.converter.DateConverter
import ru.ok.technopolis.training.personal.db.dao.ExerciseCategoryDao
import ru.ok.technopolis.training.personal.db.dao.ExerciseDao
import ru.ok.technopolis.training.personal.db.dao.ExerciseMediaDao
import ru.ok.technopolis.training.personal.db.dao.ExerciseParameterDao
import ru.ok.technopolis.training.personal.db.dao.LevelExerciseParameterDao
import ru.ok.technopolis.training.personal.db.dao.MessageDao
import ru.ok.technopolis.training.personal.db.dao.ParameterDao
import ru.ok.technopolis.training.personal.db.dao.ParameterResultDao
import ru.ok.technopolis.training.personal.db.dao.SubscriptionDao
import ru.ok.technopolis.training.personal.db.dao.UserDao
import ru.ok.technopolis.training.personal.db.dao.UserExerciseDao
import ru.ok.technopolis.training.personal.db.dao.UserLevelDao
import ru.ok.technopolis.training.personal.db.dao.UserWorkoutDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutCategoryDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutExerciseDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutMediaDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutSportDao
import ru.ok.technopolis.training.personal.db.entity.ExerciseCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseMediaEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.LevelExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterResultEntity
import ru.ok.technopolis.training.personal.db.entity.SubscriptionEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.UserExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.UserLevelEntity
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutCategoryEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutMediaEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutSportEntity
import ru.ok.technopolis.training.personal.db.generators.InitialDataGenerator

@Database(
        entities = [
            ExerciseEntity::class,
            ExerciseParameterEntity::class,
            ExerciseCategoryEntity::class,
            LevelExerciseParameterEntity::class,
            MessageEntity::class,
            ParameterEntity::class,
            ParameterResultEntity::class,
            UserEntity::class,
            UserExerciseEntity::class,
            UserLevelEntity::class,
            UserWorkoutEntity::class,
            WorkoutCategoryEntity::class,
            WorkoutEntity::class,
            WorkoutExerciseEntity::class,
            WorkoutSportEntity::class,
            SubscriptionEntity::class,
            WorkoutMediaEntity::class,
            ExerciseMediaEntity::class
        ],
        version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME: String = "workoutJournalDB"

        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (instance != null) {
                return instance
            }

            return synchronized(this) {
                if (instance != null) {
                    instance
                } else {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            GlobalScope.launch(Dispatchers.IO) {
                                instance?.parameterDao()?.insert(InitialDataGenerator.getParameters(context))
                                instance?.userDao()?.insert(InitialDataGenerator.getTestUser())
                                instance?.userDao()?.insert(InitialDataGenerator.getTestAuthor())
                                instance?.userDao()?.insert(InitialDataGenerator.getTestAuthor2())
                                instance?.workoutCategoryDao()?.insert(InitialDataGenerator.getTestCategories(context))
                                instance?.workoutSportDao()?.insert(InitialDataGenerator.getTestSports(context))
                                instance?.exerciseCategoryDao()?.insert(InitialDataGenerator.getTestExerciseCategories(context))
                                instance?.workoutDao()?.insert(InitialDataGenerator.getTestWorkout())
                                instance?.subscriptionDao()?.insert(InitialDataGenerator.getTestUserSubscriptionEntity())
                                instance?.subscriptionDao()?.insert(InitialDataGenerator.getTestAuthorSubscriptionEntity())
                                instance?.workoutDao()?.insert(InitialDataGenerator.getAuthorTestWorkout())
                                instance?.exerciseDao()?.insert(InitialDataGenerator.getTestExercise())
                                instance?.exerciseDao()?.insert(InitialDataGenerator.getAuthorTestExercise())
                                instance?.workoutExerciseDao()?.insert(InitialDataGenerator.getTestWorkoutExercise())
                                instance?.workoutExerciseDao()?.insert(InitialDataGenerator.getTestAuthorWorkoutExercise())
                                instance?.userWorkoutDao()?.insert(InitialDataGenerator.getTestUserWorkout())
                                instance?.userWorkoutDao()?.insert(InitialDataGenerator.getTestAuthorWorkout())
                                instance?.userExerciseDao()?.insert(InitialDataGenerator.getTestUserExercise())
                                instance?.userExerciseDao()?.insert(InitialDataGenerator.getTestAuthorExercise())
                                instance?.messageDao()?.insert(InitialDataGenerator.getTestMessage())
                                instance?.messageDao()?.insert(InitialDataGenerator.getTestMessage2())

                            }
                        }
                    })
                            .build()
                    instance
                }
            }
        }
    }

    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseParameterDao(): ExerciseParameterDao
    abstract fun exerciseCategoryDao(): ExerciseCategoryDao
    abstract fun levelExerciseParameterDao(): LevelExerciseParameterDao
    abstract fun messageDao(): MessageDao
    abstract fun parameterDao(): ParameterDao
    abstract fun parameterResultDao(): ParameterResultDao
    abstract fun userDao(): UserDao
    abstract fun userExerciseDao(): UserExerciseDao
    abstract fun userLevelDao(): UserLevelDao
    abstract fun userWorkoutDao(): UserWorkoutDao
    abstract fun workoutCategoryDao(): WorkoutCategoryDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun workoutSportDao(): WorkoutSportDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun workoutMediaDao(): WorkoutMediaDao
    abstract fun exerciseMediaDao(): ExerciseMediaDao
}