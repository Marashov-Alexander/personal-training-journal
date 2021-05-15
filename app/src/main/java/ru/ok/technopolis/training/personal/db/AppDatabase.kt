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
import ru.ok.technopolis.training.personal.db.dao.DoneExerciseDao
import ru.ok.technopolis.training.personal.db.dao.ExerciseDao
import ru.ok.technopolis.training.personal.db.dao.ExerciseParameterDao
import ru.ok.technopolis.training.personal.db.dao.ExerciseTypeDao
import ru.ok.technopolis.training.personal.db.dao.ParameterDao
import ru.ok.technopolis.training.personal.db.dao.ParameterResultDao
import ru.ok.technopolis.training.personal.db.dao.UserDao
import ru.ok.technopolis.training.personal.db.dao.UserWorkoutDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutDao
import ru.ok.technopolis.training.personal.db.dao.WorkoutExerciseDao
import ru.ok.technopolis.training.personal.db.entity.DoneExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseTypeEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterResultEntity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity
import ru.ok.technopolis.training.personal.db.generators.InitialDataGenerator

@Database(
    entities = [
        ExerciseEntity::class,
        ExerciseParameterEntity::class,
        ExerciseTypeEntity::class,
        ParameterEntity::class,
        UserEntity::class,
        UserWorkoutEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        DoneExerciseEntity::class,
        ParameterResultEntity::class
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
                                instance?.let {
                                    it.exerciseTypeDao().insert(InitialDataGenerator.getExerciseTypes(context))
                                    it.parameterDao().insert(InitialDataGenerator.getParameters(context))
                                }
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
    abstract fun exerciseTypeDao(): ExerciseTypeDao
    abstract fun parameterDao(): ParameterDao
    abstract fun userDao(): UserDao
    abstract fun userWorkoutDao(): UserWorkoutDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun doneExerciseDao(): DoneExerciseDao
    abstract fun parameterResultDao(): ParameterResultDao
}