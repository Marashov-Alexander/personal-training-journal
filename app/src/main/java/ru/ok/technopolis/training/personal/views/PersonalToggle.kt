package ru.ok.technopolis.training.personal.views

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.personal_toggle.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import kotlin.math.roundToInt

class PersonalToggle : BaseFragment() {
    private var toggle: View? = null
    private var recyclerView: RecyclerView? = null
    private var flag = true
    private var exInitLeft = 0
    private var exInitRight = 0
    private var workoutInitLeft = 0
    private var workoutInitRight = 0
    private var switcherSize = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggle = view.toggle_background
        recyclerView = view.toggle_recycler
        var touchMode = 0

        loadItems(flag)

        val clickListener = View.OnClickListener {
            toggleSwitch()
        }

        val touchListener = View.OnTouchListener { v, event ->
            val x: Int
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                workoutInitLeft = toggle!!.toggle_workout_side.left
                workoutInitRight = toggle!!.toggle_workout_side.right
                exInitLeft = toggle!!.toggle_ex_side.left
                exInitRight = toggle!!.toggle_ex_side.right
                switcherSize = toggle!!.toggle_switcher.width
                touchMode = 1
            } else if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                x = event.rawX.roundToInt()
                if (flag and (x > workoutInitRight)) {
                    toggle!!.toggle_switcher.right = x
                    touchMode = 2

                } else if (!flag and (x < exInitLeft)) {
                    toggle!!.toggle_switcher.left = x
                    touchMode = 3
                }
            } else if (event.actionMasked == MotionEvent.ACTION_UP) {
                when {
                    touchMode == 1 -> {
                        v.performClick()
                    }
                    ((touchMode == 3) and (toggle!!.toggle_switcher.left < workoutInitRight))
                            or ((touchMode == 2) and (toggle!!.toggle_switcher.right > exInitLeft)) -> {
                        toggleSwitch()
                    }
                    ((touchMode == 2) or (touchMode == 3)) -> {
                        setToggleSwitcherPosition()
                    }
                }
            }
            true
        }

        toggle!!.toggle_workout_side.setOnClickListener(clickListener)
        toggle!!.toggle_ex_side.setOnClickListener(clickListener)
        toggle!!.toggle_workout_side.setOnTouchListener(touchListener)
        toggle!!.toggle_ex_side.setOnTouchListener(touchListener)
        toggle!!.toggle_switcher.setOnTouchListener(touchListener)
        toggle!!.toggle_switcher.setOnTouchListener(touchListener)
    }

    private fun setToggleSwitcherPosition() {
        if (flag) {
            toggle!!.toggle_switcher.left = workoutInitLeft
            toggle!!.toggle_switcher.right = toggle!!.toggle_switcher.left + switcherSize

        } else {
            toggle!!.toggle_switcher.right = exInitRight
            toggle!!.toggle_switcher.left = exInitRight - switcherSize
        }
    }

    private fun toggleSwitch() {
        flag = !flag
        setToggleSwitcherPosition()
        loadItems(flag)
    }

    private fun loadItems(flag: Boolean) {

    }

    override fun getFragmentLayoutId() = R.layout.personal_toggle

}