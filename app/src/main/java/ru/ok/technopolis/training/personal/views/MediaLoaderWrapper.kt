package ru.ok.technopolis.training.personal.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.disposables.Disposable
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.utils.recycler.LinearHorizontalSpacingDecoration
import ru.ok.technopolis.training.personal.utils.recycler.adapters.MediaAdapter
import ru.ok.technopolis.training.personal.viewholders.MediaViewHolder

class MediaLoaderWrapper(
    private val fragment: BaseFragment,
    mediaRecycler: RecyclerView,
    editContentBtn: FloatingActionButton,
    removeContentBtn: FloatingActionButton,
    private val posValue: TextView,
    private val posCard: MaterialCardView,
    private val mediaList: ItemsList<MediaItem>
) {
    private var hasPosition: Boolean = false
    private var mediaPosition: Int = 0
    private var mediaCount: Int = 0
    private val subscribe: Disposable

    init {
        posCard.visibility = View.INVISIBLE
        posValue.text = ""
        editContentBtn.setImageResource(R.drawable.ic_add_black_24dp)
        editContentBtn.setOnClickListener {
            ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            fragment.startActivityForResult(intent, MediaItem.PICK_IMAGE_REQUEST)
        }
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

        removeContentBtn.setOnClickListener {
            if (mediaList.size() != 0) {
                mediaPosition = Integer.min(mediaList.size() - 1, Integer.max(0, mediaPosition - 1))
                mediaList.remove(mediaPosition)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val hasPos = hasPosition
        hasPosition = true
        if (resultCode == Activity.RESULT_OK && requestCode == MediaItem.PICK_IMAGE_REQUEST) {
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = fragment.context?.contentResolver?.query(uri, filePathColumn, null, null, null)
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val filePath = columnIndex?.let {
                        cursor.getString(it)
                    }
                    cursor?.close()
                    filePath?.let {
                        if (hasPos) {
                            mediaPosition++
                        }
                        mediaList.add(MediaItem((mediaList.size() + 1).toString(), it, MediaItem.DisplayMode.FIT_CENTER))
                    }
                }
            }
        }
    }

    fun onDetach() {
        subscribe.dispose()
    }

}