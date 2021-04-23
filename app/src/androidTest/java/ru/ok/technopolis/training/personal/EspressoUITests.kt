package ru.ok.technopolis.training.personal

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.ok.technopolis.training.personal.mockActivities.auth.FakeLoginActivity
import ru.ok.technopolis.training.personal.screens.LoginScreen
import ru.ok.technopolis.training.personal.utils.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class EspressoUITests {

    @get:Rule
    val activityRule = ActivityScenarioRule(FakeLoginActivity::class.java)

    private val loginScreen = LoginScreen()

    private val isDisplayed = matches(isDisplayed())
    private val doesNotExists = doesNotExist()

    private val weekdaysEveryday = (1 shl 7) - 1

    @Test
    fun createdExerciseIsAvailableEverywhere() {
        var mainMenuScreen = loginScreen
                .fakeLogin()
        val createWorkoutScreen = mainMenuScreen
                .pressAddWorkoutButton()

        val workoutName = randomWorkoutName()
        val timetableHours = randomHours()
        val timetableMinutes = randomMinutes()
        val weekdaysMask = weekdaysEveryday

        val editExerciseScreen = createWorkoutScreen
                .createWorkout(
                        workoutName,
                        weekdaysMask,
                        timetableHours,
                        timetableMinutes
                )
                .clickWorkoutItem(workoutName)
                .pressAddExerciseButton(R.string.create_new)

        val exerciseName = randomExerciseName()
        mainMenuScreen = editExerciseScreen
                .createExercise(exerciseName)
                .pressSaveButton()

        mainMenuScreen
                .pressAddWorkoutButton()
                .pressAddExerciseButton(exerciseName)
                .getExerciseItem(exerciseName)
                .check(isDisplayed)
    }

    @Test
    fun workoutParametersSavesCorrectly() {
        val mainMenuScreen = loginScreen
                .fakeLogin()
        val createWorkoutScreen = mainMenuScreen
                .pressAddWorkoutButton()

        val workoutName = randomWorkoutName()
        val timetableHours = randomHours()
        val timetableMinutes = randomMinutes()
        val weekdaysMask = weekdaysEveryday

        val editWorkoutScreen = createWorkoutScreen
                .createWorkout(
                        workoutName,
                        weekdaysMask,
                        timetableHours,
                        timetableMinutes
                )
                .clickWorkoutItem(workoutName)

        assertThat(editWorkoutScreen.getWorkoutName(), equalTo(workoutName))
        assertThat(editWorkoutScreen.getWeekdaysMask(), equalTo(weekdaysMask))
        assertThat(editWorkoutScreen.getTimetableTime(), equalTo("$timetableHours:$timetableMinutes"))
    }

    @Test
    fun calendarShowWorkoutsCorrectly() {
        var mainMenuScreen = loginScreen
                .fakeLogin()
        val weekdaysList = listOf(Weekday.values().map {weekday -> weekday.value}, listOf(weekdaysEveryday)).flatten()
        for (weekdaysMask in weekdaysList) {
            Espresso.closeSoftKeyboard()
            val createWorkoutScreen = mainMenuScreen
                    .pressAddWorkoutButton()
            val workoutName = randomWorkoutName()
            val timetableHours = randomHours()
            val timetableMinutes = randomMinutes()
            mainMenuScreen = createWorkoutScreen.createWorkout(
                    workoutName,
                    weekdaysMask,
                    timetableHours,
                    timetableMinutes
            )
            for (pair in getWeek()) {
                val dateTriple = pair.first
                val weekday = pair.second
                val checked = weekdaysMask and weekday.value != 0
                val matcher = if (checked) isDisplayed else doesNotExists
                mainMenuScreen
                        .chooseDate(dateTriple.third, dateTriple.second, dateTriple.first)
                        .scrollToWorkout(workoutName)
                        .check(matcher)
            }
        }
    }
}