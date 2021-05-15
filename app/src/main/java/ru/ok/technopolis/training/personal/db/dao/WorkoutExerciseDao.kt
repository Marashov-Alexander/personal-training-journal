package ru.ok.technopolis.training.personal.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.WorkoutExerciseEntity

@Dao
interface WorkoutExerciseDao {
    @Query("SELECT * FROM WorkoutExerciseEntity")
    fun getAll(): List<WorkoutExerciseEntity>

    @Query("SELECT * FROM WorkoutExerciseEntity WHERE workoutId=:workoutId")
    fun getAllByWorkout(workoutId: Long): List<WorkoutExerciseEntity>

    @Query("SELECT * FROM WorkoutExerciseEntity WHERE workoutId=:workoutId AND exerciseId=:exerciseId")
    fun getById(workoutId: Long, exerciseId: Long): WorkoutExerciseEntity

    @Query("SELECT * FROM WorkoutExerciseEntity WHERE serverId=:serverId")
    fun getByServerId(serverId: Long): WorkoutExerciseEntity

    @Query("SELECT * FROM ExerciseEntity AS ee " +
        "INNER JOIN WorkoutExerciseEntity AS wee ON ee.id = wee.exerciseId " +
        "WHERE wee.workoutId = :workoutId AND wee.deleted=0")
    fun getExercisesForWorkout(workoutId: Long): MutableList<ExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workoutExerciseEntity: WorkoutExerciseEntity): Long

    @Insert
    fun insert(workoutExerciseEntityList: List<WorkoutExerciseEntity>): List<Long>

    @Update
    fun update(workoutExerciseEntity: WorkoutExerciseEntity): Int

    @Update
    fun update(workoutExerciseEntityList: List<WorkoutExerciseEntity>): Int

    @Delete
    fun delete(workoutExerciseEntity: WorkoutExerciseEntity): Int

    @Query("DELETE FROM WorkoutExerciseEntity WHERE workoutId=:workoutId AND exerciseId=:exerciseId")
    fun delete(workoutId: Long, exerciseId: Long): Int
}