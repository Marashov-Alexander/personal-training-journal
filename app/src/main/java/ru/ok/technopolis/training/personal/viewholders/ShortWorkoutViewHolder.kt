package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.item_short_workout.view.*
import ru.ok.technopolis.training.personal.items.ShortWorkoutItem
import kotlin.math.roundToInt

class ShortWorkoutViewHolder (
        itemView: View
) : BaseViewHolder<ShortWorkoutItem>(itemView) {

    private var imageCard: CardView = itemView.short_workout_image_card
    private var image: ImageView = itemView.short_workout_image
    private var name: TextView = itemView.short_workout_name
    private var category: TextView = itemView.short_workout_category
    private var sport: TextView = itemView.short_workout_sport
    private var downloadsIcon: ImageView = itemView.short_workout_downloads
    private var downloads: TextView = itemView.downloads_number
    private var starIcon: ImageView = itemView.short_workout_star
    private var rank: TextView = itemView.rank_number

    override fun bind(item: ShortWorkoutItem) {
        if (item.downloadsNumber == 0) {
            downloads.visibility = View.GONE
            downloadsIcon.visibility = View.GONE
        } else {
            downloads.visibility = View.VISIBLE
            downloadsIcon.visibility = View.VISIBLE
        }

        if (item.rank.roundToInt() == 0) {
            rank.visibility = View.GONE
            starIcon.visibility = View.GONE
        } else {
            rank.visibility = View.VISIBLE
            starIcon.visibility = View.VISIBLE
        }

        update(item)
    }

    fun update(item: ShortWorkoutItem) {
        name.text = item.name
        category.text = item.category
        sport.text = item.sport
        rank.text = item.rank.toString()
        downloads.text = item.downloadsNumber.toString()
    }

    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == View.VISIBLE) {
            imageCard.setOnClickListener { onStart() }
            image.setOnClickListener { onStart() }
        }
    }

}