package ru.ok.technopolis.training.personal.screens

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.utils.getText

class EditExerciseScreen {

    fun createExercise(
            exerciseName: String
    ): EditWorkoutScreen {
        return enterExerciseName(exerciseName)
                .pressSaveButton()
    }

    fun enterExerciseName(name: String): EditExerciseScreen {
        onView(ViewMatchers.withId(R.id.exercise_name)).perform(ViewActions.typeText(name))
        return this
    }

    fun getExerciseName(): String? {
        return getText(ViewMatchers.withId(R.id.exercise_name))
    }

    fun pressSaveButton(): EditWorkoutScreen {
        onView(ViewMatchers.withId(R.id.apply_changes)).perform(ViewActions.click())
        Espresso.closeSoftKeyboard()
        return EditWorkoutScreen()
    }

    fun getExerciseItem(exerciseName: String): ViewInteraction {
        return onView(ViewMatchers.withText(exerciseName))
    }

}
