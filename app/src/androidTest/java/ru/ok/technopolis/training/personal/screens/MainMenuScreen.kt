package ru.ok.technopolis.training.personal.screens

import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers
import ru.ok.technopolis.training.personal.R


class MainMenuScreen {

    fun chooseDate(day: Int, month: Int, year: Int): MainMenuScreen {
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
                .perform(PickerActions.setDate(year, month, day))
        return this
    }

    fun getWorkoutList(): ViewInteraction {
        return onView(withId(R.id.workout_list))
    }

    fun pressAddWorkoutButton(): EditWorkoutScreen {
        onView(withId(R.id.add_workout_button)).perform(click())
        return EditWorkoutScreen()
    }

    fun clickWorkoutItem(name: String): EditWorkoutScreen {
        scrollToWorkout(name).check(matches(isDisplayed()))
        onView(withText(name)).perform(click())
        return EditWorkoutScreen()
    }

    fun scrollToWorkout(name: String): ViewInteraction {
        return try {
            onView(withId(R.id.workout_list))
                    .perform(scrollTo<RecyclerView.ViewHolder>(
                            hasDescendant(withText(name)))
                    )
        } catch (e: PerformException) {
            onView(withText(name))
        }
    }
}