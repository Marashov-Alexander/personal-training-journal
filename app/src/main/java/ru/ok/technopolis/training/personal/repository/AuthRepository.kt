package ru.ok.technopolis.training.personal.repository

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.activities.BaseActivity
import ru.ok.technopolis.training.personal.db.entity.UserEntity
import ru.ok.technopolis.training.personal.model.UserInfo

object AuthRepository {

    const val TRAINING_PREFERENCE = "Training"
    const val USER_TOKEN = "user_token_key"
    const val USER_INFO_KEY = "user_info_key"

    fun doOnLogin(activity: BaseActivity, token: String, needRemember: Boolean, userInfo: UserInfo) {
        activity.apply {
            CurrentUserRepository.currentUser.value = userInfo
            GlobalScope.launch(Dispatchers.IO) {
                var user = database?.userDao()?.getByEmail(userInfo.email)
                if (user == null) {
                    user = UserEntity(
                        userInfo.firstName,
                        userInfo.lastName,
                        userInfo.fatherName,
                        userInfo.email,
                        "Male",
                        userInfo.pictureUrlStr,
                        userInfo.id
                    )
                    database?.userDao()?.insert(user)
                } else {
                    user.firstName = userInfo.firstName
                    user.lastName = userInfo.lastName
                    user.fatherName = userInfo.fatherName
                    user.email = userInfo.email
                    user.avatarUrl = userInfo.pictureUrlStr
                    user.serverId = userInfo.id
                    database?.userDao()?.update(user)
                }
                withContext(Dispatchers.Main) {
                    if (needRemember) {
                        getSharedPreferences(TRAINING_PREFERENCE, Context.MODE_PRIVATE).edit().putString(USER_TOKEN, token).apply()
                        getSharedPreferences(TRAINING_PREFERENCE, Context.MODE_PRIVATE).edit().putString(USER_INFO_KEY, Gson().toJson(user)).apply()
                    }
                    router?.showWorkoutPlanPage()
                    finish()
                }
            }
        }
    }

    fun doOnLogout(activity: BaseActivity) {
        activity.apply {
            CurrentUserRepository.currentUser.value = null
            getSharedPreferences("Training", Context.MODE_PRIVATE).edit().remove(USER_TOKEN).apply()
            getSharedPreferences("Training", Context.MODE_PRIVATE).edit().remove(USER_INFO_KEY).apply()
            router?.showLoginPage()
            finish()
        }
    }
}