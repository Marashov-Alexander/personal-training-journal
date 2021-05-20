package ru.ok.technopolis.training.personal.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_exercise_description.view.*
import ru.ok.technopolis.training.personal.R

class DescriptionDialogFragment(private val title: String, private val description: String): DialogFragment() {

    private var titleTextView: TextView? = null
    private var descriptionTextView: TextView? = null
    private var closeBtn: MaterialCardView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.item_exercise_description, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            closeBtn = view.close_card
            closeBtn!!.setOnClickListener {
                dialog.dismiss()
            }
            titleTextView = view.exercise_name
            titleTextView?.text = title
            descriptionTextView = view.exercise_description
            descriptionTextView?.text = description

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}