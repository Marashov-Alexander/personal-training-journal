package ru.ok.technopolis.training.personal.viewholders

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class RoundedImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        drawable?.let {
            when (drawable) {
                is BitmapDrawable -> {
                    val radius = 0.2f
                    val bitmap = drawable.bitmap
                    val resourceId = RoundedBitmapDrawableFactory.create(resources, bitmap)
                    resourceId.cornerRadius = bitmap.width * radius
                    super.setImageDrawable(resourceId)
                }
                is VectorDrawable -> {
                    // do nothing?
                }
            }
        }
    }


}