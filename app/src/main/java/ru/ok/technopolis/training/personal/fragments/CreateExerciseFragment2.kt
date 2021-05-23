package ru.ok.technopolis.training.personal.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_new_exercise_2.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import ru.ok.technopolis.training.personal.items.MediaItem.Companion.PICK_IMAGE_REQUEST
import ru.ok.technopolis.training.personal.utils.recycler.LinearHorizontalSpacingDecoration
import ru.ok.technopolis.training.personal.utils.recycler.adapters.MediaAdapter
import ru.ok.technopolis.training.personal.viewholders.MediaViewHolder
import java.lang.Integer.max
import java.lang.Integer.min


class CreateExerciseFragment2 : BaseFragment() {

    private var prevStepCard: MaterialCardView? = null
    private var nextStepCard: MaterialCardView? = null
    private var mediaRecycler: RecyclerView? = null
    private lateinit var editContentBtn: FloatingActionButton
    private lateinit var removeContentBtn: FloatingActionButton
    private lateinit var muscleGroupsLabel: TextView
    private lateinit var posValue: TextView
    private lateinit var posCard: MaterialCardView
    private val mediaList: ItemsList<MediaItem> = ItemsList(mutableListOf())

    private var hasPosition: Boolean = false
    private var mediaPosition: Int = 0
    private var mediaCount: Int = 0
    private var subscribe: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.base_toolbar?.title = getString(R.string.exercise_creation)
        nextStepCard = next_step_card
        prevStepCard = prev_step_card
        prevStepCard?.setOnClickListener {
            router?.goToPrevFragment()
        }
        editContentBtn = edit_content_btn
        removeContentBtn = remove_content_btn
        posCard = pos_card
        posCard.visibility = INVISIBLE
        posValue = pos_value
        posValue.text = ""
        muscleGroupsLabel = muscle_groups_label
        muscleGroupsLabel.setOnClickListener {

        }

        editContentBtn.setImageResource(R.drawable.ic_add_black_24dp)
        editContentBtn.setOnClickListener {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        subscribe = mediaList.sizeChangedSubject().subscribe { size ->
            if (size == 0) {
                posCard.visibility = INVISIBLE
                hasPosition = false
            } else {
                mediaCount = size
                posValue.text = "${mediaPosition + 1}/$mediaCount"
                posCard.visibility = VISIBLE
            }
        }
        mediaRecycler = exercise_image_switcher
        mediaRecycler?.let { recycler ->
            recycler.adapter = MediaAdapter(
                MediaViewHolder::class,
                R.layout.item_media,
                mediaList,
                onImageClick = { mediaItem, _ ->
                    println("${mediaItem.id} clicked")
                }
            )
            val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            recycler.layoutManager = layoutManager
            val spacing = resources.getDimensionPixelSize(R.dimen.dimens_16dp)
            recycler.addItemDecoration(LinearHorizontalSpacingDecoration(spacing))
            val pager = PagerSnapHelper()
            pager.attachToRecyclerView(recycler)

            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mediaPosition = layoutManager.findFirstVisibleItemPosition()
                        posValue.text = "${mediaPosition + 1}/$mediaCount"
                        posCard.visibility = VISIBLE
                    }
                }
            })

            removeContentBtn.setOnClickListener {
                if (mediaList.size() != 0) {
                    mediaPosition = min(mediaList.size() - 1, max(0, mediaPosition - 1))
                    mediaList.remove(mediaPosition)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val hasPos = hasPosition
        hasPosition = true
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = context?.contentResolver?.query(uri, filePathColumn, null, null, null)
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

    override fun onDetach() {
        super.onDetach()
        subscribe?.dispose()
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_2

}
