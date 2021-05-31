package ru.ok.technopolis.training.personal.lifecycle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import ru.ok.technopolis.training.personal.activities.BaseFragmentActivity
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.AUTHOR_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.COUNTERS_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_CREATING_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.OPPONENT_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.INDEX_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.MESSAGE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.PAGE_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.PROGRESS_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.REST_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.USER_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_CREATING_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_EXERCISES_ID_KEY
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

    fun showNewWorkoutPage(workoutId: Long, creating: Boolean) {
        val bundle = Bundle(2)
        bundle.putLong(WORKOUT_ID_KEY, workoutId)
        bundle.putBoolean(WORKOUT_CREATING_ID_KEY, creating)
        showPage(Page.Fragment.NewWorkout, bundle)
    }

    fun showNewWorkoutPage2(workoutId: Long) {
        val bundle = Bundle(1)
        bundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.NewWorkout2, bundle)
    }

    fun showNewExercisePage1(workoutId: Long?, exerciseId: Long, isCreating: Boolean) {
        val bundle = Bundle(3)
        if (workoutId != null) {
            bundle.putLong(WORKOUT_ID_KEY, workoutId)
        } else {
            bundle.putLong(WORKOUT_ID_KEY, 0)
        }
        bundle.putLong(EXERCISE_ID_KEY, exerciseId)
        bundle.putBoolean(EXERCISE_CREATING_ID_KEY, isCreating)
        showPage(Page.Fragment.NewExercise1, bundle)
    }

    fun showNewExercisePage2(workoutId: Long?, exerciseId: Long) {
        val workoutIdBundle = Bundle(2)
        if (workoutId != null) {
            workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        } else {
            workoutIdBundle.putLong(WORKOUT_ID_KEY, 0)
        }
        workoutIdBundle.putLong(EXERCISE_ID_KEY, exerciseId)
        showPage(Page.Fragment.NewExercise2, workoutIdBundle)
    }

    fun showWorkoutPage(workoutId: Long) {
        val workoutIdBundle = Bundle(1)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.Workout, workoutIdBundle)
    }

    fun showWorkoutProgressPage(workoutId: Long) {
        val workoutIdBundle = Bundle(1)
        workoutIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        showPage(Page.Fragment.WorkoutProgress, workoutIdBundle)
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

    fun showActiveExercisePage(currentIndex: Int, workoutExercises: LongArray, counters: IntArray, progress: IntArray) {
        val bundle = Bundle(4)
        bundle.putInt(INDEX_ID_KEY, currentIndex)
        bundle.putLongArray(WORKOUT_EXERCISES_ID_KEY, workoutExercises)
        bundle.putIntArray(COUNTERS_ID_KEY, counters)
        bundle.putIntArray(PROGRESS_ID_KEY, progress)
        showPage(Page.Fragment.ActiveExercise, bundle)
    }

    fun showActivePreExercisePage(
            currentIndex: Int,
            workoutExercises: LongArray,
            counters: IntArray,
            progress: IntArray,
            restValue: Int
    ) {
        val bundle = Bundle(5)
        bundle.putInt(INDEX_ID_KEY, currentIndex)
        bundle.putLongArray(WORKOUT_EXERCISES_ID_KEY, workoutExercises)
        bundle.putIntArray(COUNTERS_ID_KEY, counters)
        bundle.putIntArray(PROGRESS_ID_KEY, progress)
        bundle.putInt(REST_ID_KEY, restValue)
        showPage(Page.Fragment.ActivePreExercise, bundle)
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

    fun showChatPage(chatId: Long, messageId: Long?){
        val chatIdBundle = Bundle(2)
        chatIdBundle.putLong(OPPONENT_ID_KEY, chatId)
        messageId?.or(0L)?.let { chatIdBundle.putLong(MESSAGE_ID_KEY, it) }
        showPage(Page.Fragment.Chat, chatIdBundle)
    }

    fun shareElement(exerciseId: Long?, workoutId: Long?){
        val shareIdBundle = Bundle(2)
        if (exerciseId != null) {
            shareIdBundle.putLong(EXERCISE_ID_KEY, exerciseId)
        } else {
            shareIdBundle.putLong(EXERCISE_ID_KEY, 0)
        }
        if (workoutId != null) {
            shareIdBundle.putLong(WORKOUT_ID_KEY, workoutId)
        } else {
            shareIdBundle.putLong(WORKOUT_ID_KEY, 0)
        }
        showPage(Page.Fragment.Share, shareIdBundle)
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