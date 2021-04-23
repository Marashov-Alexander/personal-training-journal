package ru.ok.technopolis.training.personal.lifecycle

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_base_fragment.*
import kotlinx.android.synthetic.main.activity_base_fragment.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.activities.BaseFragmentActivity
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.PAGE_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.USER_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import ru.ok.technopolis.training.personal.utils.logger.Logger
import kotlin.reflect.full.createInstance
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

class Router(private val activity: Activity) {

    fun showLoginPage() {
        showPage(Page.Activity.Login)
    }

    fun showCalendarPage() {
        showPage(Page.Fragment.Calendar)
    }

    fun showWorkoutPlanPage() {
        showPage(Page.Fragment.WorkoutPlan)
    }

    fun showWorkoutPage(workoutId: Long) {
        val workoutIdBundle = Bundle(1)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.Workout, workoutIdBundle)
    }

    fun showTrainingViewPage() {
        showPage(Page.Fragment.TrainingView)
    }

    fun showActiveExercisePage(userId: Long, workoutId: Long) {
        val workoutIdBundle = Bundle(2)
        workoutIdBundle.putLong(USER_ID_KEY, userId)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.ActiveExercise, workoutIdBundle)
    }

    fun showExercisePage(exerciseId: Long) {
        val exerciseIdBundle = Bundle(1)
        exerciseIdBundle.putLong(EXERCISE_ID_KEY, exerciseId)
        showPage(Page.Fragment.Exercise, exerciseIdBundle)
    }

    fun showResultsPage() {
        showPage(Page.Fragment.Results)
    }

    fun showSettingsPage() {
        showPage(Page.Activity.Settings)
    }

    fun showAccountSettingsPage() {
        showPage(Page.Activity.AccountSettings)
    }

    fun showAccountSettingsSubPage() {
        showPage(Page.Fragment.AccountSettings)
    }

    fun showRegistrationPage() {
        showPage(Page.Activity.Registration)
    }

    private fun showPage(page: Page, bundle: Bundle? = null) {
        Logger.d(this, "showPage $page")
        when (page) {
            is Page.Activity -> {
                println("Activity")
                showActivity(page, bundle)
            }
            is Page.Fragment -> {
                println("Fragment")
                if (activity is BaseFragmentActivity) {
                    println("(BaseFrAct)")
                    replaceFragment(page, bundle)
                } else {
                    println("(ActWithFr)")
                    showActivityWithFragment(page, bundle)
                }
            }
        }
    }

    private fun showActivity(page: Page.Activity, bundle: Bundle?) {
        val intent = Intent(activity, page.clazz.java)
        bundle?.let { intent.putExtras(it) }
        activity.startActivity(intent)
    }

    private fun showActivityWithFragment(page: Page.Fragment, bundle: Bundle?) {
        val intent = Intent(activity, BaseFragmentActivity::class.java)
        intent.putExtra(PAGE_KEY, page)
        bundle?.let { intent.putExtras(it) }
        activity.startActivity(intent)
    }

    private fun replaceFragment(page: Page.Fragment, bundle: Bundle?) {
        bundle?.let { activity.intent.putExtras(it) }
        (activity as? BaseFragmentActivity)?.setFragment(page.clazz.createInstance())
    }

    fun goToPrevFragment() {
        (activity as? BaseFragmentActivity)?.setPrevFragment()
    }
}