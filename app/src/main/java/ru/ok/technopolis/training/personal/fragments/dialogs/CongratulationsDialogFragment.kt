package ru.ok.technopolis.training.personal.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_congratulations_dialog.view.*
import ru.ok.technopolis.training.personal.R

class CongratulationsDialogFragment : DialogFragment() {

    private var showResultCard: MaterialCardView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.fragment_congratulations_dialog, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            showResultCard = view.show_results_card
            showResultCard!!.setOnClickListener {
                dialog.dismiss()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
