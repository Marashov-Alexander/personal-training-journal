package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import kotlinx.android.synthetic.main.item_progress.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ProgressRectItem

class ProgressRectViewHolder(
    itemView: View
) : BaseViewHolder<ProgressRectItem>(itemView) {

    private var rect: View = itemView.progress_item
    private var elevation = rect.elevation

    override fun bind(item: ProgressRectItem) {
        when (item.status) {
            ProgressRectItem.ProgressStatus.PASSED -> {
                rect.setBackgroundColor(itemView.context.getColor(R.color.green_progress))
                rect.elevation = 0f
            }
            ProgressRectItem.ProgressStatus.FAILED -> {
                rect.setBackgroundColor(itemView.context.getColor(R.color.red))
                rect.elevation = 0f
            }
            ProgressRectItem.ProgressStatus.CURRENT -> {
                rect.setBackgroundColor(itemView.context.getColor(R.color.magic_mint))
                rect.elevation = elevation
            }
            ProgressRectItem.ProgressStatus.FUTURE -> {
                rect.setBackgroundColor(itemView.context.getColor(R.color.white))
                rect.elevation = 0f
            }
        }
    }
}
