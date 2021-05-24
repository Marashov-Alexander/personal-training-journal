package ru.ok.technopolis.training.personal.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_plan_workout.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.PlanDayItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.PlanDayAdapter
import ru.ok.technopolis.training.personal.viewholders.PlanDayViewHolder
import java.sql.Time

class PlanWorkoutDialog (private val workout: WorkoutEntity, private val listener: PlanWorkoutListener) :DialogFragment() {
    private var workoutRepetitionSpinner: Spinner? = null
    private var everyDayPart: ConstraintLayout? = null
    private var oneDayPart: ConstraintLayout? = null

    private var setNotification: CheckBox? = null
    private var notificationTimePicker: LinearLayout? = null
    private var minutesHoursSwitcher: ConstraintLayout? = null
    private var planButton: Button? = null

    private var daysRecyclerView: RecyclerView? = null

    private val daysNames: Array<String> = arrayOf("Понедельник","Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.item_plan_workout, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            daysRecyclerView = view.choose_days
            workoutRepetitionSpinner = view.workout_repetition_variants
            everyDayPart = view.every_n_day
            oneDayPart = view.one_date

            setNotification = view.set_notification
            notificationTimePicker = view.notification_time_picker
            minutesHoursSwitcher = view.minutes_hours_switcher
            planButton = view.plan_button

            everyDayPart?.visibility = View.INVISIBLE
            oneDayPart?.visibility = View.INVISIBLE
            daysRecyclerView?.visibility = View.VISIBLE

            val daysElem = mutableListOf(
            PlanDayItem("123","Gjyt",  Time(System.currentTimeMillis())),
                    PlanDayItem("124","Gjyt",  Time(System.currentTimeMillis())))

            val daysList = ItemsList(daysElem)
            val daysAdapter = PlanDayAdapter(
                    holderType = PlanDayViewHolder::class,
                    layoutId = R.layout.item_day,
                    dataSource = daysList,
                    onClick = {item -> print("$item")

                    }
            )
            daysRecyclerView?.adapter = daysAdapter
            val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            daysRecyclerView?.layoutManager = layoutManager

//            chooseDay?.ad

//            resetErrors(view)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    private fun resetErrors(view: View?) {
//        view?.let {
//            it.units.background.setTint(Color.GRAY)
//            it.title.backgroundTintList = null
//            it.value.backgroundTintList = null
//        }
//    }

    interface PlanWorkoutListener {

    }
}