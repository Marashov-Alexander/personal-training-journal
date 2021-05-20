package ru.ok.technopolis.training.personal.utils

import android.os.CountDownTimer

class RestCountDownTimer(
    seconds: Int,
    private val onTickListener: (timeLeft: String) -> Unit = {},
    private val onFinishListener: () -> Unit = {}
) : CountDownTimer(seconds * 1000L, 1000L) {

    override fun onFinish() {
        onFinishListener.invoke()
    }

    override fun onTick(millisUntilFinished: Long) {
        val seconds = millisUntilFinished / 1000 % 60
        val minutes = millisUntilFinished / 1000 / 60 % 60
        val hours = millisUntilFinished / 1000 / 60 / 60
        val timeLeft = "${if (hours < 10) "0" else ""}$hours" +
            ":${if (minutes < 10) "0" else ""}$minutes" +
            ":${if (seconds < 10) "0" else ""}$seconds"
        onTickListener.invoke(timeLeft)
    }

}
