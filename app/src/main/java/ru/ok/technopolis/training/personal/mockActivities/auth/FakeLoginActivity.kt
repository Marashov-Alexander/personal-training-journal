package ru.ok.technopolis.training.personal.mockActivities.auth

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog

import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Response
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.activities.BaseActivity
import ru.ok.technopolis.training.personal.api.Api
import ru.ok.technopolis.training.personal.api.ApiUtils
import ru.ok.technopolis.training.personal.dto.UserDto
import ru.ok.technopolis.training.personal.repository.AuthRepository
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.logger.Logger
import java.net.HttpURLConnection

class FakeLoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        remember_tv.setOnClickListener {
            remember_checkbox.isChecked = !remember_checkbox.isChecked
        }

        not_exist_acc_tv.setOnClickListener {
            router?.showRegistrationPage()
        }

        confirm_button.setOnClickListener {
            val token = ApiUtils.encodeEmailAndPasswordToAuthorizationHeader(email_et.text.toString(), password_et.text.toString())
            taskContainer.add(
                    Api.fakeLogin(token).subscribe(
                            { onResponse(it, token) },
                            { onFail(it) }
                    )
            )
        }
    }

    private fun onResponse(response: Response<UserDto>, token: String) {
        when (response.code()) {
            HttpURLConnection.HTTP_OK -> {
                AuthRepository.doOnLogin(this, token, remember_checkbox.isChecked,
                        response.body()?.toUserInfo() ?: CurrentUserRepository.CURRENT_USER_EMPTY)
                Logger.d(this, "successfully login with code ${response.code()}")
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                MaterialDialog(this).show {
                    title(R.string.cannot_login)
                    message(R.string.incorrect_email_or_password)
                    negativeButton(R.string.close) {
                        it.cancel()
                    }
                }
                Logger.d(this, response.code())
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                MaterialDialog(this).show {
                    title(R.string.cannot_login)
                    message(R.string.server_not_available)
                    negativeButton(R.string.close) {
                        it.cancel()
                    }
                }
                Logger.d(this, response.code())
            }
            else -> {
                MaterialDialog(this).show {
                    title(R.string.cannot_login)
                    message(R.string.server_not_available)
                    negativeButton(R.string.close) {
                        it.cancel()
                    }
                }
                Logger.d(this, "unsupported code ${response.code()}")
            }
        }
    }

    private fun onFail(throwable: Throwable) {
        MaterialDialog(this).show {
            title(R.string.cannot_login)
            message(R.string.failed_login)
            negativeButton(R.string.close) {
                it.cancel()
            }
        }
        Logger.e(this, "Login failed : ${throwable.message}")
    }

    override fun getActivityLayoutId() = R.layout.activity_login
}
