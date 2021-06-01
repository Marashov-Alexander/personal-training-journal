package ru.ok.technopolis.training.personal.lifecycle

import ru.ok.technopolis.training.personal.activities.auth.LoginActivity
import ru.ok.technopolis.training.personal.fragments.ProfileFragment
import ru.ok.technopolis.training.personal.mockActivities.auth.FakeLoginActivity
import ru.ok.technopolis.training.personal.activities.auth.RegistrationActivity
import ru.ok.technopolis.training.personal.activities.settings.AccountSettingsActivity
import ru.ok.technopolis.training.personal.activities.settings.SettingsActivity
import ru.ok.technopolis.training.personal.fragments.*
import ru.ok.technopolis.training.personal.fragments.ShareFragment
import ru.ok.technopolis.training.personal.fragments.settings.AccountSettingsFragment
import java.io.Serializable
import kotlin.reflect.KClass

sealed class Page : Serializable {

    sealed class Activity : Page() {

        abstract val clazz: KClass<out android.app.Activity>

        object Login : Activity() {
            override val clazz = LoginActivity::class
        }


        object Settings : Activity() {
            override val clazz = SettingsActivity::class
        }

        object AccountSettings : Activity() {
            override val clazz = AccountSettingsActivity::class
        }

        object Registration : Activity() {
            override val clazz = RegistrationActivity::class
        }
    }

    sealed class Fragment : Page() {

        abstract val clazz: KClass<out BaseFragment>

        object AccountSettings : Fragment() {
            override val clazz = AccountSettingsFragment::class
        }

        object Results : Fragment() {
            override val clazz = StatisticsMainFragment::class
        }

        object Navigation : Fragment() {
            override val clazz = NavigationFragment::class
        }

        object ActiveExercise : Fragment() {
            override val clazz = ActiveExerciseFragment::class
        }

        object ActivePreExercise : Fragment() {
            override val clazz = ActivePreExerciseFragment::class
        }

        object WorkoutDone : Fragment() {
            override val clazz = WorkoutDoneFragment::class
        }

        object NewWorkout : Fragment() {
            override val clazz = CreateWorkoutFragment::class
        }

        object NewWorkout2 : Fragment() {
            override val clazz = CreateWorkoutFragment2::class
        }

        object NewExercise1 : Fragment() {
            override val clazz = CreateExerciseFragment1::class
        }

        object NewExercise2 : Fragment() {
            override val clazz = CreateExerciseFragment2::class
        }

        object WorkoutProgress: Fragment() {
            override val clazz = WorkoutProgressFragment::class
        }

        object WorkoutPlan : Fragment() {
            override val clazz = WorkoutPlanFragment::class
        }

        object Profile : Fragment() {
            override val clazz = ProfileFragment::class
        }

        object Chats: Fragment() {
            override val clazz = ChatsFragment::class
        }

        object Exercise : Fragment() {
            override val clazz = ExerciseViewFragment::class
        }

        object Chat: Fragment() {
            override val clazz = ChatFragment::class
        }

        object Share: Fragment() {
            override val clazz = ShareFragment::class
        }

        object Workout : Fragment() {
            override val clazz = WorkoutViewFragment::class
        }

        object Author : Fragment() {
            override val clazz = ViewAuthorFragment::class
        }

        object TrainingView : Fragment() {
            override val clazz = TrainingViewFragment::class
        }

        object ViewSubscribers: Fragment() {
            override val clazz = ViewSubscribersFragment::class
        }

        object ViewSubscriptions: Fragment() {
            override val clazz = ViewSubscriptionsFragment::class

        }
    }

    companion object {
        const val PAGE_KEY = "PAGE"
        const val USER_ID_KEY = "USER_ID"
        const val WORKOUT_ID_KEY = "WORKOUT_ID"
        const val WORKOUT_CREATING_ID_KEY = "WORKOUT_CREATING_ID"
        const val EXERCISE_CREATING_ID_KEY = "EXERCISE_CREATING_ID"
        const val EXERCISE_ID_KEY = "EXERCISE_ID"
        const val AUTHOR_ID_KEY = "AUTHOR_ID"
        const val OPPONENT_ID_KEY = "OPPONENT_ID"
        const val MESSAGE_ID_KEY = "MESSAGE_ID"
        const val INDEX_ID_KEY = "INDEX_ID"
        const val WORKOUT_EXERCISES_ID_KEY = "WORKOUT_EXERCISES_ID"
        const val COUNTERS_ID_KEY = "COUNTERS_ID"
        const val PROGRESS_ID_KEY = "PROGRESS_ID"
        const val REST_ID_KEY = "REST_ID"
    }
}