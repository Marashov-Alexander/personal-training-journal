package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_short_exercise.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ShortExerciseItem
import kotlin.math.roundToInt

class ShortExerciseViewHolder (
        itemView: View
) : BaseViewHolder<ShortExerciseItem>(itemView) {
    private var imageCard: CardView = itemView.short_ex_image_card
    private var image: ImageView = itemView.short_ex_image
    private var name: TextView = itemView.short_ex_name
    private var category: TextView = itemView.short_ex_category
    private var downloadsIcon: ImageView = itemView.short_ex_downloads
    private var downloads: TextView = itemView.ex_downloads_number
    private var starIcon: ImageView = itemView.short_ex_star
    private var rank: TextView = itemView.ex_rank_number
    override fun bind(item: ShortExerciseItem) {
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

    fun update(item: ShortExerciseItem) {
        name.text = item.exercise.name
        category.text = item.category
        rank.text = item.rank.toString()
        downloads.text = item.downloadsNumber.toString()
        setImage(item.url)
    }
    private fun setImage(url: String){
        Glide
                .with(imageCard.short_ex_image)
                .asDrawable()
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_access_time_black_24dp)
                .error(R.drawable.image_running_person)
                .into(imageCard.short_ex_image)
    }

    fun setOnStartClickListener(onStart: () -> Unit) {
        if (itemView.visibility == View.VISIBLE) {
            imageCard.setOnClickListener { onStart() }
            image.setOnClickListener { onStart() }
        }
    }
}