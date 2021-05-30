package ru.ok.technopolis.training.personal.fragments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_plan_workout.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.UserWorkoutEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.PlanDayItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.PlanDayAdapter
import ru.ok.technopolis.training.personal.viewholders.PlanDayViewHolder
import java.sql.Time
import java.text.DateFormat
import java.time.temporal.ChronoUnit


class PlanWorkoutDialog(
        private val userWorkout: UserWorkoutEntity,
        private val savePlanListener: (UserWorkoutEntity, () -> Unit) -> Unit
) : DialogFragment() {
    private var workoutRepetitionSpinner: Spinner? = null
    private var everyDayPart: ConstraintLayout? = null

    private var setNotification: CheckBox? = null
    private var notificationTimePicker: LinearLayout? = null
    private var minutesHoursSwitcher: ConstraintLayout? = null
    private var planButton: Button? = null
    private var hoursMinutesSwitch: ConstraintLayout? = null
    val myCalendar: Calendar = Calendar.getInstance()

    private var daysRecyclerView: RecyclerView? = null

    private val MAX_NOTIFICATION_TIME = 60
    private val MIN_NOTIFICATION_TIME = 0

    private var isEveryKDays = true
    private lateinit var dayElements: MutableList<PlanDayItem>
    private val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

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

            val startAt = view.start_at_edit_text
            setNotification = view.set_notification
            setNotification?.isChecked = userWorkout.notifyMinutes != 0
            notificationTimePicker = view.notification_time_picker
            minutesHoursSwitcher = view.minutes_hours_switcher
            planButton = view.plan_button
            val nDayInput = view.n_day_input
            nDayInput.inputType = InputType.TYPE_CLASS_NUMBER

            val plusButton = view.increment
            val minusButton = view.decrement
            val notificationTime = view.display_value
            notificationTime.inputType = InputType.TYPE_CLASS_NUMBER
            var notificationTimeValue = if (userWorkout.isScheduled()) {
                notificationTime.setText(userWorkout.notifyMinutes.toString())
                userWorkout.notifyMinutes
            } else {
                Integer.parseInt(notificationTime.text.toString())
            }
            plusButton.setOnClickListener {
                if (notificationTimeValue < MAX_NOTIFICATION_TIME) {
                    notificationTimeValue += 1
                    notificationTime.setText(notificationTimeValue.toString())
                }
            }

            minusButton.setOnClickListener {
                if (notificationTimeValue > MIN_NOTIFICATION_TIME) {
                    notificationTimeValue -= 1
                    notificationTime.setText(notificationTimeValue.toString())
                }
                if (notificationTimeValue == 0) {
                    setNotification?.isChecked = false
                }
            }

            hoursMinutesSwitch = view.minutes_hours_switcher
            var flag = true
            hoursMinutesSwitch?.setOnClickListener {
                flag = !flag
                val minutes = view.minutes
                val hours = view.hours
                if (flag) {
                    minutes.setText(R.string.h)
                    hours.setText(R.string.min)
                } else {
                    hours.setText(R.string.h)
                    minutes.setText(R.string.min)
                }
            }

            fillDays(userWorkout, activity)
            workoutRepetitionSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
                @SuppressLint("SimpleDateFormat", "SetTextI18n")
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {

                    when (position) {
                        0 -> {
                            isEveryKDays = true
                            everyDayPart?.visibility = View.VISIBLE
                            daysRecyclerView?.visibility = View.INVISIBLE
                            val hour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
                            val minute: Int = myCalendar.get(Calendar.MINUTE)
                            val startAtValue = userWorkout.getPlannedTime(0)
                            if (startAtValue == "" || userWorkout.restDays == null) {
                                startAt?.setText("$hour:$minute")
                            } else {
                                startAt?.setText(startAtValue)
                            }

                            val everyDay = view.every_day_checkbox
                            everyDay.setOnCheckedChangeListener { compoundButton, b ->
                                nDayInput.setText(R.string._1)
                                nDayInput.isEnabled = !nDayInput.isEnabled
                            }

                            startAt.setOnClickListener {

                                val mTimePicker: TimePickerDialog
                                mTimePicker = TimePickerDialog(activity, { timePicker, selectedHour, selectedMinute ->
                                    startAt?.setText(fixTime(selectedHour, selectedMinute))
                                }, hour, minute, true)
                                mTimePicker.show()
                            }
                        }
                        1 -> {
                            isEveryKDays = false
                            everyDayPart?.visibility = View.INVISIBLE
                            daysRecyclerView?.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }

            planButton?.setOnClickListener {
                if (isEveryKDays) {
                    val restDays = nDayInput.text.toString().toInt()
                    userWorkout.weekdaysMask = null
                    userWorkout.restDays = restDays
                    userWorkout.plannedTimes = startAt.text.toString()
                } else {
                    val mask = dayElements.map { it.checked }.toBooleanArray()
                    val plannedTimes = dayElements
                            .map { formatter.format(it.time) }
                            .reduce { acc, str -> "$acc*$str" }
                    userWorkout.plannedTimes = plannedTimes
                    userWorkout.setWeekdaysMask(mask)
                    userWorkout.restDays = null
                }
                userWorkout.timestampFrom = System.currentTimeMillis()
                userWorkout.notifyMinutes = if (setNotification?.isChecked == true) {
                    notificationTimeValue
                } else {
                    0
                }
                savePlanListener(userWorkout) {
                    dialog.dismiss()
                    println(userWorkout)
                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    @SuppressLint("SetTextI18n")
    private fun fillDays(userWorkoutEntity: UserWorkoutEntity, activity: FragmentActivity) {

        val weekdaysMask =
                if (userWorkoutEntity.weekdaysMask == null)
                    BooleanArray(7) { false }
                else
                    userWorkoutEntity.getWeekdaysArray()!!
        val weekdays = requireContext().resources.getStringArray(R.array.weekdays)
        dayElements = weekdaysMask.mapIndexed { index, checked ->
            val savedTime = userWorkout.getPlannedTime(index)
            val time = if (savedTime == "") {
                Time(System.currentTimeMillis())
            } else {
                Time(formatter.parse(savedTime).time)
            }
            PlanDayItem(
                    index.toString(),
                    weekdays[index],
                    time,
                    checked
            )
        }.toMutableList()

        val daysList = ItemsList(dayElements)
        val daysAdapter = PlanDayAdapter(
                holderType = PlanDayViewHolder::class,
                layoutId = R.layout.item_day,
                dataSource = daysList,
                onClick = { item ->
                },
                onStart = { editText: EditText, item: PlanDayItem ->
                    val hour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
                    val minute: Int = myCalendar.get(Calendar.MINUTE)

                    val mTimePicker: TimePickerDialog
                    mTimePicker = TimePickerDialog(activity, { timePicker, selectedHour, selectedMinute ->
                        val strTime = fixTime(selectedHour, selectedMinute)
                        item.time.minutes = selectedMinute
                        item.time.hours = selectedHour
                        editText.setText(strTime)
                    }, hour, minute, true)
                    mTimePicker.show()
                }
        )
        daysRecyclerView?.adapter = daysAdapter
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        daysRecyclerView?.layoutManager = layoutManager
    }

    private fun fixTime(selectedHour: Int, selectedMinute: Int): String {
        val hours =
                if (selectedHour < 10) {
                    "0$selectedHour"
                } else {
                    "$selectedHour"
                }
        val minutes =
                if (selectedMinute < 10) {
                    "0$selectedMinute"
                } else {
                    "$selectedMinute"
                }
        return "$hours:$minutes"
    }
}