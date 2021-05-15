package ru.ok.technopolis.training.personal.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ProgressItem
import kotlin.math.max as max1

class ProgressChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private val itemWidth: Int
    private val originalData: List<ProgressItem>
    private val goalValue: Float
    private val goalMeasureUnit: String

    private val roundedFillPaint = Paint()
    private val roundedBorderPaint = Paint()
    private val strokePaint = Paint()
    private val fillPaint = Paint()

    private val redPaint = Paint()
    private val greenPaint = Paint()

    private val regularTextPaint = Paint()
    private val italicTextPaint = Paint()
    private val boldTextPaint = Paint()

    private val goalLinePaint = Paint()

    private val path = Path()
    private val textRect = Rect()

    private var startMoveX = 0f
    private var chartOffset = 0f
    private var maxOffset = 0f

    companion object {
        private const val DEFAULT_ITEM_WIDTH_DP = 20
        private const val DEFAULT_ITEM_COLOR = Color.LTGRAY
        private const val DEFAULT_GOOD_COLOR = Color.GREEN
        private const val DEFAULT_BAD_COLOR = Color.RED
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val DEFAULT_STROKE_COLOR = Color.BLACK
        private const val DEFAULT_STROKE_WIDTH_DP = 2
        private const val DEFAULT_SPACE_PROPORTION = 0.4f
        private const val DEFAULT_GOAL_LINE_COLOR = Color.BLUE
    }

    init {
        val displayMetrics = context.resources.displayMetrics
        var itemWidthFromAttr = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_WIDTH_DP.toFloat(), displayMetrics) + 0.5f).toInt()
        var itemColorFromAttr = DEFAULT_ITEM_COLOR
        var goodColorFromAttr = DEFAULT_GOOD_COLOR
        var badColorFromAttr = DEFAULT_BAD_COLOR
        var textColorFromAttr = DEFAULT_TEXT_COLOR
        var itemStrokeColorAttr = DEFAULT_STROKE_COLOR
        var itemStrokeWidthAttr = DEFAULT_STROKE_WIDTH_DP
        var goalLineAttr = DEFAULT_GOAL_LINE_COLOR

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressChartView)
            itemWidthFromAttr = typedArray.getDimensionPixelSize(R.styleable.ProgressChartView_itemWidth, itemWidthFromAttr)
            itemColorFromAttr = typedArray.getColor(R.styleable.ProgressChartView_itemColor, itemColorFromAttr)
            goodColorFromAttr = typedArray.getColor(R.styleable.ProgressChartView_goodColor, goodColorFromAttr)
            badColorFromAttr = typedArray.getColor(R.styleable.ProgressChartView_badColor, badColorFromAttr)
            textColorFromAttr = typedArray.getColor(R.styleable.ProgressChartView_textColor, textColorFromAttr)
            itemStrokeColorAttr = typedArray.getColor(R.styleable.ProgressChartView_strokeColor, itemStrokeColorAttr)
            itemStrokeWidthAttr = typedArray.getColor(R.styleable.ProgressChartView_strokeWidth, itemStrokeWidthAttr)
            goalLineAttr = typedArray.getColor(R.styleable.ProgressChartView_goalLineColor, goalLineAttr)
            typedArray.recycle()
        }

        itemWidth = itemWidthFromAttr
        originalData = listOf(
            ProgressItem(60f, "17.05"),
            ProgressItem(80f, "18.05", "14:00"),
            ProgressItem(70f, "19.05"),
            ProgressItem(50f, "20.05"),
            ProgressItem(80f, "21.05"),
            ProgressItem(90f, "22.05"),
            ProgressItem(80f, "23.05"),
            ProgressItem(85f, "24.05"),
            ProgressItem(75f, "25.05"),
            ProgressItem(90f, "26.05")
        )
        goalValue = 100f
        goalMeasureUnit = "%"

        fillPaint.style = Paint.Style.FILL
        fillPaint.color = itemColorFromAttr

        redPaint.style = Paint.Style.FILL
        redPaint.color = badColorFromAttr

        greenPaint.style = Paint.Style.FILL
        greenPaint.color = goodColorFromAttr

        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = itemStrokeColorAttr
        strokePaint.strokeWidth = itemStrokeWidthAttr.toFloat()

        regularTextPaint.style = Paint.Style.FILL
        regularTextPaint.textSize = itemWidth / context.resources.displayMetrics.scaledDensity
        regularTextPaint.color = textColorFromAttr
        regularTextPaint.textAlign = Paint.Align.LEFT

        italicTextPaint.style = Paint.Style.FILL
        italicTextPaint.textSize = itemWidth / context.resources.displayMetrics.scaledDensity
        italicTextPaint.color = textColorFromAttr
        italicTextPaint.textAlign = Paint.Align.LEFT
        italicTextPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)

        boldTextPaint.style = Paint.Style.FILL
        boldTextPaint.color = textColorFromAttr
        boldTextPaint.textAlign = Paint.Align.CENTER
        boldTextPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        goalLinePaint.style = Paint.Style.STROKE
        goalLinePaint.color = goalLineAttr
        goalLinePaint.strokeWidth = itemStrokeWidthAttr * 2f

        roundedBorderPaint.style = Paint.Style.STROKE
        roundedBorderPaint.color = itemStrokeColorAttr
        roundedBorderPaint.strokeWidth = itemStrokeWidthAttr * 2f

        roundedFillPaint.style = Paint.Style.FILL
        roundedFillPaint.color = Color.WHITE

        setOnTouchListener { _, motion ->
            val rawX = motion.rawX
            when (motion.action) {
                MotionEvent.ACTION_DOWN -> {
                    startMoveX = rawX
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val delta = rawX - startMoveX
                    chartOffset -= delta
                    if (chartOffset < 0f) {
                        chartOffset = 0f
                    }
                    if (chartOffset > maxOffset) {
                        chartOffset = maxOffset
                    }
                    invalidate()
                    startMoveX = rawX
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val rawWidth = (originalData.size * itemWidth * (1f + DEFAULT_SPACE_PROPORTION) - itemWidth + paddingLeft + paddingRight).toInt()
        val width = resolveSize(rawWidth, widthMeasureSpec)
        val itemCount = ((measuredWidth + itemWidth - paddingLeft - paddingRight) / (itemWidth * (1f + DEFAULT_SPACE_PROPORTION))).toInt() - 1
        maxOffset = itemWidth * max1(0, originalData.size - itemCount) * (1f + DEFAULT_SPACE_PROPORTION) + itemWidth / 2f
        println("itemCount=$itemCount, length=${originalData.size}, maxOffset=$maxOffset")
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (originalData.isEmpty()) {
            return
        }

        val maxValue: Float = max1(originalData.maxBy { it.value }!!.value, goalValue)
        val borderValue = maxValue * 1.25f
        val measuredHeight = measuredHeight - paddingTop - paddingBottom
        val chartHeight = measuredHeight * 0.8f
        val chartStartX = (paddingStart + itemWidth).toFloat()
        val chartEndX = (width - paddingEnd).toFloat()

        boldTextPaint.textSize = 2 * itemWidth / context.resources.displayMetrics.scaledDensity

        // рамка графика (заливка)
        val radius = itemWidth / 2f
        canvas.drawRoundRect(chartStartX, strokePaint.strokeWidth, chartEndX, paddingTop + chartHeight, radius, radius, roundedFillPaint)

        path.reset()
        path.addRoundRect(chartStartX, strokePaint.strokeWidth, chartEndX, chartHeight, radius, radius, Path.Direction.CW)
        path.addRect(chartStartX, chartHeight, chartEndX, measuredHeight.toFloat(), Path.Direction.CW)
        canvas.save()
        canvas.clipPath(path)

        var currentX = chartStartX + itemWidth / 2 - chartOffset
        var prevValueHeight = 0f
        for (data in originalData) {
            val height = data.value / borderValue * chartHeight
            val deltaHeight = if (prevValueHeight > 0.01f) height - prevValueHeight else 0f
            val startY = paddingTop + chartHeight - height
            val endY = startY + height
            val endX = (currentX + itemWidth)

            if (deltaHeight < 0) {
                val redStartY = paddingTop + chartHeight - prevValueHeight
                canvas.drawRect(currentX, redStartY, endX, endY, redPaint)
            }

            canvas.drawRect(currentX, startY, endX, endY, fillPaint)

            if (deltaHeight > 0) {
                val greenStartY = paddingTop + chartHeight - height
                val greenEndY = greenStartY + deltaHeight
                canvas.drawRect(currentX, greenStartY, endX, greenEndY, greenPaint)
            }

            canvas.drawRect(currentX, startY, endX, endY, strokePaint)

            regularTextPaint.getTextBounds(data.date, 0, data.date.length, textRect)
            canvas.drawText(data.date, currentX, endY + textRect.height() * (1f + DEFAULT_SPACE_PROPORTION), regularTextPaint)
            data.time?.let { text ->
                canvas.drawText(text, currentX, endY + 2f * textRect.height() * (1f + DEFAULT_SPACE_PROPORTION), italicTextPaint)
            }
            currentX += (itemWidth * (1f + DEFAULT_SPACE_PROPORTION)).toInt()
            prevValueHeight = height
        }
        canvas.restore()

        // линия цели
        val startStopY = paddingTop + chartHeight - goalValue / borderValue * chartHeight
        canvas.drawLine(chartStartX, startStopY, chartEndX, startStopY, goalLinePaint)
        // текст цели
        regularTextPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("$goalValue $goalMeasureUnit ", chartEndX, startStopY * 0.99f, regularTextPaint)
        regularTextPaint.textAlign = Paint.Align.LEFT

        // рамка графика (обводка)
        canvas.drawRoundRect(chartStartX, strokePaint.strokeWidth, chartEndX, paddingTop + chartHeight, radius, radius, roundedBorderPaint)

        // надпись оси
        canvas.save()
        canvas.rotate(-90f)
        canvas.drawText("Прогресс", -(paddingTop + chartHeight) / 2f, chartStartX / 2f  , boldTextPaint)
        canvas.restore()
    }
}