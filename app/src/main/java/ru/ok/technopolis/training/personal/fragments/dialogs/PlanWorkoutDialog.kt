package ru.ok.technopolis.training.personal.fragments.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.dsl.genericFastAdapter
import kotlinx.android.synthetic.main.item_plan_workout.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.WorkoutEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.PlanDayItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.PlanDayAdapter
import ru.ok.technopolis.training.personal.viewholders.PlanDayViewHolder
import java.sql.Time


class PlanWorkoutDialog(private val workout: WorkoutEntity, private val listener: PlanWorkoutListener) : DialogFragment() {
    private var workoutRepetitionSpinner: Spinner? = null
    private var everyDayPart: ConstraintLayout? = null
//    private var oneDayPart: ConstraintLayout? = null

    private var setNotification: CheckBox? = null
    private var notificationTimePicker: LinearLayout? = null
    private var minutesHoursSwitcher: ConstraintLayout? = null
    private var planButton: Button? = null
    private var hoursMinutesSwitch: ConstraintLayout? = null
    val myCalendar: Calendar = Calendar.getInstance()

    private var daysRecyclerView: RecyclerView? = null

    private val MAX_NOTIFICATION_TIME = 60
    private val MIN_NOTIFICATION_TIME = 1

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
//            oneDayPart = view.one_date

            setNotification = view.set_notification
            notificationTimePicker = view.notification_time_picker
            minutesHoursSwitcher = view.minutes_hours_switcher
            planButton = view.plan_button
            val nDayInput = view.n_day_input
            nDayInput.inputType = InputType.TYPE_CLASS_NUMBER


            val plusButton = view.increment
            val minusButton = view.decrement
            val notificationTime = view.display_value
            notificationTime.inputType = InputType.TYPE_CLASS_NUMBER
            var notificationTimeValue = Integer.parseInt(notificationTime.text.toString())
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

            fillDays(activity)
            workoutRepetitionSpinner?.onItemSelectedListener = object : OnItemSelectedListener {
                @SuppressLint("SimpleDateFormat", "SetTextI18n")
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {

                    when (position) {
                        0 -> {
                            everyDayPart?.visibility = View.VISIBLE
//                            oneDayPart?.visibility = View.INVISIBLE
                            daysRecyclerView?.visibility = View.INVISIBLE
                            val startAt = view.start_at_edit_text
                            val hour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
                            val minute: Int = myCalendar.get(Calendar.MINUTE)
                            startAt?.setText("$hour:$minute")

                            val everyDay = view.every_day_checkbox
                            everyDay.setOnCheckedChangeListener { compoundButton, b ->
                                nDayInput.setText(R.string._1)
                                nDayInput.isEnabled = !nDayInput.isEnabled
                            }

                            startAt.setOnClickListener {

                                val mTimePicker: TimePickerDialog
                                mTimePicker = TimePickerDialog(activity, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                                    startAt?.setText("$selectedHour:$selectedMinute")
                                }, hour, minute, true)
                                mTimePicker.show()
                            }
                        }
                        1 -> {
                            everyDayPart?.visibility = View.INVISIBLE
//                            oneDayPart?.visibility = View.INVISIBLE
                            daysRecyclerView?.visibility = View.VISIBLE
                        }
//                        else -> {
//                            everyDayPart?.visibility = View.INVISIBLE
//                            oneDayPart?.visibility = View.VISIBLE
//                            daysRecyclerView?.visibility = View.INVISIBLE
//                            val dateText = view.date_text
//
//                            val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
//                                myCalendar[Calendar.YEAR] = year
//                                myCalendar[Calendar.MONTH] = monthOfYear
//                                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
//                                val myFormat = "dd.MM.yyyy"
//
//                                val sdf = SimpleDateFormat(myFormat)
//
//                                dateText.text = sdf.format(myCalendar.time)
//                            }
//
//                            dateText.setOnClickListener {
//                                DatePickerDialog(activity, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
//                                        myCalendar[Calendar.DAY_OF_MONTH]).show()
//                            }
//                            val dateIcon = view.calendar_icon
//                            dateIcon.setOnClickListener {
//                                DatePickerDialog(activity, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
//                                        myCalendar[Calendar.DAY_OF_MONTH]).show()
//                            }
//                            val startAt = view.date_start_at_edit_text
//
//                            val hour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
//                            val minute: Int = myCalendar.get(Calendar.MINUTE)
//                            startAt?.setText("$hour:$minute")
//                            startAt.setOnClickListener {
//                                val hour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
//                                val minute: Int = myCalendar.get(Calendar.MINUTE)
//
//                                val mTimePicker: TimePickerDialog
//                                mTimePicker = TimePickerDialog(activity, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//                                    startAt?.setText("$selectedHour:$selectedMinute")
//                                }, hour, minute, true)
//                                mTimePicker.show()
//                            }
//                        }
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }

            planButton?.setOnClickListener {
//                if (updateWorkoutEntity(view)) {
//                    listener.onSaveClick(item)
                    dialog.dismiss()
//                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    @SuppressLint("SetTextI18n")
    private fun fillDays(activity: FragmentActivity) {
        val daysElem = mutableListOf(
                PlanDayItem("0", getString(R.string.monday), Time(System.currentTimeMillis())),
                PlanDayItem("1", getString(R.string.tuesday), Time(System.currentTimeMillis())),
                PlanDayItem("2", getString(R.string.wednesday), Time(System.currentTimeMillis())),
                PlanDayItem("3", getString(R.string.thursday), Time(System.currentTimeMillis())),
                PlanDayItem("4", getString(R.string.friday), Time(System.currentTimeMillis())),
                PlanDayItem("5", getString(R.string.saturday), Time(System.currentTimeMillis())),
                PlanDayItem("6", getString(R.string.sunday), Time(System.currentTimeMillis()))
        )

        val daysList = ItemsList(daysElem)
        val daysAdapter = PlanDayAdapter(
                holderType = PlanDayViewHolder::class,
                layoutId = R.layout.item_day,
                dataSource = daysList,
                onClick = { item ->
                },
                onStart = { item ->
                    val hour: Int = myCalendar.get(Calendar.HOUR_OF_DAY)
                    val minute: Int = myCalendar.get(Calendar.MINUTE)

                    val mTimePicker: TimePickerDialog
                    mTimePicker = TimePickerDialog(activity, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                        item.setText("$selectedHour:$selectedMinute")
                    }, hour, minute, true)
                    mTimePicker.show()
                }
        )
        daysRecyclerView?.adapter = daysAdapter
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        daysRecyclerView?.layoutManager = layoutManager
    }

    interface PlanWorkoutListener {

    }
}