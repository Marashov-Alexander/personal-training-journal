package ru.ok.technopolis.training.personal.screens

import android.widget.TimePicker
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import okhttp3.internal.immutableListOf
import org.hamcrest.Matchers
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.utils.setChecked
import ru.ok.technopolis.training.personal.utils.Weekday
import ru.ok.technopolis.training.personal.utils.getText
import ru.ok.technopolis.training.personal.utils.isChecked


class EditWorkoutScreen {

    private val lst = immutableListOf(R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5, R.id.day6, R.id.day7)

    fun createWorkout(
            workoutName: String,
            weekdaysMask: Int,
            timetableHours: Int,
            timetableMinutes: Int
    ): MainMenuScreen {
        return setDaysChecked(weekdaysMask)
                .setTimetableTime(timetableHours, timetableMinutes)
                .enterWorkoutName(workoutName)
                .pressSaveButton()
    }

    private fun setTimetableTime(hours: Int, minutes: Int): EditWorkoutScreen {
        onView(withId(R.id.choose_workout_time))
                .perform(click())
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
                .perform(PickerActions.setTime(hours, minutes))
        onView(withId(android.R.id.button1))
                .perform(click())
        return this
    }

    private fun setDaysChecked(weekdaysMask: Int): EditWorkoutScreen {
        for (weekday in Weekday.values()) {
            val checked = weekdaysMask and weekday.value != 0
            setDayChecked(weekday.ordinal, checked)
        }
        return this
    }

    private fun setDayChecked(weekday: Int, checked: Boolean): EditWorkoutScreen {
        assert(Weekday.MONDAY.ordinal <= weekday)
        assert(weekday <= Weekday.SUNDAY.ordinal)

        onView(withId(lst[weekday]))
                .perform(setChecked(checked))
        return this
    }

    fun enterWorkoutName(name: String): EditWorkoutScreen {
        onView(withId(R.id.workout_name)).perform(ViewActions.typeText(name))
        return this
    }

    fun getWorkoutName(): String? {
        return getText(withId(R.id.workout_name))
    }

    fun pressSaveButton(): MainMenuScreen {
        onView(withId(R.id.apply_changes)).perform(click())
        Espresso.closeSoftKeyboard()
        return MainMenuScreen()
    }

    fun getWeekdaysMask(): Int {
        var result = 0
        for (i in lst.indices) {
            if (isChecked(withId(lst[i])) == true) {
                result += 1 shl i
            }
        }
        return result
    }

    fun getTimetableTime(): String? {
        return getText(withId(R.id.choose_workout_time))
    }

    fun pressAddExerciseButton(menuItemText: String): EditExerciseScreen {
        onView(withId(R.id.add_exercise_button))
                .perform(click())
        onView(withText(menuItemText))
                .check(matches(isDisplayed()))
                .perform(click())
        return EditExerciseScreen()
    }

    fun pressAddExerciseButton(menuItemTextId: Int): EditExerciseScreen {
        onView(withId(R.id.add_exercise_button))
                .perform(click())
        onView(withText(menuItemTextId))
                .perform(click())
        return EditExerciseScreen()
    }

}
