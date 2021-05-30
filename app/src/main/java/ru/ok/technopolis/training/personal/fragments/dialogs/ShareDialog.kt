package ru.ok.technopolis.training.personal.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_share_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.activities.BaseActivity

class ShareDialog(
        private val itemId: Long,
        private val isWorkout: Boolean
) : DialogFragment() {
    private var makePublic: MaterialCardView? = null
    private var sendInMessage: MaterialCardView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.item_share_dialog, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val database = (activity as BaseActivity).database

            makePublic = view.make_public_card
            sendInMessage = view.send_card
            makePublic!!.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    database!!.let {
                        if (isWorkout) {
                            val workout = it.workoutDao().getById(itemId)
                            workout.isPublic = true
                            val updated = it.workoutDao().update(workout)
                        } else {
                            val exercise = it.exerciseDao().getById(itemId)
                            exercise.isPublic = true
                            val updated = it.exerciseDao().update(exercise)
                        }
                    }
                }
                dialog.dismiss()
            }
            sendInMessage!!.setOnClickListener {
                when {
                    isWorkout -> {
                        activity.router?.shareElement(null, itemId)
                    }
                    else -> {
                        activity.router?.shareElement(itemId, null)
                    }
                }
                dialog.dismiss()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
