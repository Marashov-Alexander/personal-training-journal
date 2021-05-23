package ru.ok.technopolis.training.personal.views

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.reactivex.disposables.Disposable
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.utils.recycler.LinearHorizontalSpacingDecoration
import ru.ok.technopolis.training.personal.utils.recycler.adapters.MediaAdapter
import ru.ok.technopolis.training.personal.viewholders.MediaViewHolder

open class MediaViewerWrapper(
    fragment: BaseFragment,
    mediaRecycler: RecyclerView,
    private val posValue: TextView,
    private val posCard: MaterialCardView,
    private val mediaList: ItemsList<MediaItem>
) {
    protected var hasPosition: Boolean = false
    protected var mediaPosition: Int = 0
    protected var mediaCount: Int = 0
    private val subscribe: Disposable

    init {
        posCard.visibility = View.INVISIBLE
        posValue.text = ""
        subscribe = mediaList.sizeChangedSubject().subscribe { size ->
            if (size == 0) {
                posCard.visibility = View.INVISIBLE
                hasPosition = false
            } else {
                mediaCount = size
                posValue.text = "${mediaPosition + 1}/$mediaCount"
                posCard.visibility = View.VISIBLE
            }
        }
        mediaRecycler.adapter = MediaAdapter(
            MediaViewHolder::class,
            R.layout.item_media,
            mediaList,
            onImageClick = { mediaItem, _ ->
                println("${mediaItem.id} clicked")
            }
        )
        val layoutManager = LinearLayoutManager(fragment.context, RecyclerView.HORIZONTAL, false)
        mediaRecycler.layoutManager = layoutManager
        val spacing = fragment.resources.getDimensionPixelSize(R.dimen.dimens_16dp)
        mediaRecycler.addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
        val pager = PagerSnapHelper()
        pager.attachToRecyclerView(mediaRecycler)

        mediaRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mediaList.size() > 0) {
                        mediaPosition = layoutManager.findFirstVisibleItemPosition()
                        posValue.text = "${mediaPosition + 1}/$mediaCount"
                        posCard.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    fun onDetach() {
        subscribe.dispose()
    }

}