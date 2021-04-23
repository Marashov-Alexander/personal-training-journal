package ru.ok.technopolis.training.personal

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.ok.technopolis.training.personal.mockActivities.auth.FakeLoginActivity
import ru.ok.technopolis.training.personal.screens.LoginScreen

@RunWith(AndroidJUnit4::class)
@LargeTest
class Tests {

    @get:Rule
    val activityRule = ActivityScenarioRule(FakeLoginActivity::class.java)
    private val loginScreen = LoginScreen()

    @Test
    fun mainLabelExists() {
        loginScreen.getMainLabel()
            .check(matches(allOf(
                isDisplayed(),
                withSubstring("YOUR"),
                withText(R.string.login_title)
            )))
    }

    @Test
    fun loginIsCorrect() {
        loginScreen
            .typeLogin("test@test.test")
            .typePassword("qwerty")
            .pressConfirmButton()
            .getWorkoutList()
            .check(matches(isDisplayed()))
    }

    @Test
    fun userCanCreateWorkout() {
        val uniqueName = "Name" + (Math.random() * 10000) % 10000
        val createWorkoutScreen = loginScreen
            .pressConfirmButton()
            .pressAddWorkoutButton()
            .enterWorkoutName(uniqueName)
        createWorkoutScreen
            .pressSaveButton()
            .scrollToWorkout(uniqueName)
            .check(matches(isDisplayed()))
    }
}