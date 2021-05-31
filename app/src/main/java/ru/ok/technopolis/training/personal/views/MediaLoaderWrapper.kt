package ru.ok.technopolis.training.personal.views

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.disposables.Disposable
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.MediaItem
import kotlin.random.Random


class MediaLoaderWrapper(
    private val fragment: BaseFragment,
    private val mediaRecycler: RecyclerView,
    noMediaContent: TextView,
    editContentBtn: FloatingActionButton,
    removeContentBtn: FloatingActionButton,
    posValue: TextView,
    posCard: MaterialCardView,
    private val mediaList: ItemsList<MediaItem>
) : MediaViewerWrapper(fragment, mediaRecycler, noMediaContent, posValue, posCard, mediaList) {

    private val subscribe: Disposable

    init {
        editContentBtn.setOnClickListener {
//            ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            fragment.startActivityForResult(intent, MediaItem.PICK_IMAGE_REQUEST)
        }
        removeContentBtn.setOnClickListener {
            if (mediaList.size() != 0) {
                mediaPosition = Integer.min(mediaList.size() - 1, Integer.max(0, mediaPosition - 1))
                mediaList.remove(mediaPosition)
            }
        }
        removeContentBtn.visibility = INVISIBLE
        noMediaContent.text = fragment.context?.resources?.getString(R.string.load_media)
        subscribe = mediaList.sizeChangedSubject().subscribe { size ->
            if (size == 0) {
                removeContentBtn.visibility = INVISIBLE
            } else {
                removeContentBtn.visibility = VISIBLE
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == MediaItem.PICK_IMAGE_REQUEST) {
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    loadMedia(uri)
                }
            }
            if (data?.clipData == null) {
                data?.data?.let { uri ->
                    loadMedia(uri)
                }
            }
        }
    }

    private fun loadMedia(uri: Uri) {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = fragment.context?.contentResolver?.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let {
            cursor.getString(it)
        }
        cursor?.close()
        filePath?.let {
            val item = MediaItem((Random.nextInt()).toString(), it, MediaItem.DisplayMode.FIT_CENTER)
            mediaList.addLast(item)
            mediaRecycler.smoothScrollToPosition(mediaList.size() - 1)
        }
    }

    fun getLoadedData(): List<String> {
        return mediaList.items.map { mediaItem -> mediaItem.uri }
    }

}