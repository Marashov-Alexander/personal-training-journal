package ru.ok.technopolis.training.personal.lifecycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import ru.ok.technopolis.training.personal.activities.BaseFragmentActivity
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.AUTHOR_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.OPPONENT_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.PAGE_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.USER_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import ru.ok.technopolis.training.personal.utils.logger.Logger
import kotlin.reflect.full.createInstance

class Router(private val activity: Activity) {

    fun showLoginPage() {
        showPage(Page.Activity.Login)
    }

    fun showWorkoutPlanPage() {
        showPage(Page.Fragment.WorkoutPlan)
    }

    fun showProfilePage() {
        showPage(Page.Fragment.Profile)
    }

    fun showChatsPage() {
        showPage(Page.Fragment.Chats)
    }

    fun showNewWorkoutPage(workoutId: Long) {
        val bundle = Bundle(1)
        bundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.NewWorkout, bundle)
    }

    fun showNewWorkoutPage2(workoutId: Long) {
        val bundle = Bundle(1)
        bundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.NewWorkout2, bundle)
    }

    fun showNewExercisePage1(userId: Long, workoutId: Long, exerciseId: Long) {
        val bundle = Bundle(2)
        bundle.putLong(USER_ID_KEY, userId)
        bundle.putLong(WORKOUT_ID_KEY, workoutId)
        bundle.putLong(EXERCISE_ID_KEY, exerciseId)
        showPage(Page.Fragment.NewExercise1, bundle)
    }

    fun showNewExercisePage2() {
        showPage(Page.Fragment.NewExercise2)
    }

    fun showWorkoutPage(workoutId: Long) {
        val workoutIdBundle = Bundle(1)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.Workout, workoutIdBundle)
    }

    fun showWorkoutProgressPage() {
        showPage(Page.Fragment.WorkoutProgress)
    }

    fun showAuthorPage(authorId: Long) {
        val authorIdBundle = Bundle(1)
        authorIdBundle.putLong(AUTHOR_ID_KEY, authorId)
        showPage(Page.Fragment.Author, authorIdBundle)
    }

    fun showSubscribersPage(userId: Long) {
        val userIdBundle = Bundle(1)
        userIdBundle.putLong(USER_ID_KEY, userId)
        showPage(Page.Fragment.ViewSubscribers, userIdBundle)
    }

    fun showSubscriptionsPage(userId: Long) {
        val userIdBundle = Bundle(1)
        userIdBundle.putLong(USER_ID_KEY, userId)
        showPage(Page.Fragment.ViewSubscriptions, userIdBundle)
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

    fun showActivePreExercisePage(userId: Long, workoutId: Long) {
        val workoutIdBundle = Bundle(2)
        workoutIdBundle.putLong(USER_ID_KEY, userId)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.ActivePreExercise, workoutIdBundle)
    }

    fun showWorkoutDonePage(userId: Long, workoutId: Long) {
        val workoutIdBundle = Bundle(2)
        workoutIdBundle.putLong(USER_ID_KEY, userId)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.WorkoutDone, workoutIdBundle)
    }

    fun showExercisePage(exerciseId: Long) {
        val exerciseIdBundle = Bundle(1)
        exerciseIdBundle.putLong(EXERCISE_ID_KEY, exerciseId)
        showPage(Page.Fragment.Exercise, exerciseIdBundle)
    }

    fun showChatPage(chatId: Long){
        val chatIdBundle = Bundle(1)
        chatIdBundle.putLong(OPPONENT_ID_KEY, chatId)
        showPage(Page.Fragment.Chat, chatIdBundle)
    }

    fun showResultsPage() {
        showPage(Page.Fragment.Results)
    }

    fun showNavigationPage() {
        showPage(Page.Fragment.Navigation)
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