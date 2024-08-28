package com.faisal.yolov8tflite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat


@SuppressLint("ClickableViewAccessibility")
class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var bounds = Rect()

    private var selectedBoundingBox: BoundingBox? = null

    init {
        initPaints()
        setOnTouchListener { _, event ->
            handleTouchEvent(event)
        }
    }

    fun clear() {
        results = listOf()
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        selectedBoundingBox = null
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

//        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }

    private fun getBoxColor(clsName: String): Int {
        return when (clsName) {
            "iqlab" -> Color.RED
            "ikhfa syafawi" -> Color.BLUE
            "idghom mimi" -> Color.GREEN
            else -> Color.YELLOW
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        results.forEach { bbox ->
            val left = bbox.x1 * width
            val top = bbox.y1 * height
            val right = bbox.x2 * width
            val bottom = bbox.y2 * height

            boxPaint.color = getBoxColor(bbox.clsName)
            canvas.drawRect(left, top, right, bottom, boxPaint)
            val drawableText = "${bbox.clsName} ${String.format("%.2f", bbox.cnf)}"

            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()
            canvas.drawRect(
                left,
                top,
                left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )
            canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
        }
    }

    fun setResults(boundingBoxes: List<BoundingBox>) {
        results = boundingBoxes
        invalidate()
    }

    private fun handleTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                selectedBoundingBox = results.find { bbox ->
                    val left = bbox.x1 * width
                    val top = bbox.y1 * height
                    val right = bbox.x2 * width
                    val bottom = bbox.y2 * height
                    RectF(left, top, right, bottom).contains(x, y)
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                selectedBoundingBox?.let { bbox ->
                    when (bbox.clsName) {
                        "iqlab" -> {
                            val intent = Intent(context, IqlabDetail::class.java)
                            context.startActivity(intent)
                        }
                        "ikhfa syafawi" -> {
                            val intent = Intent(context, IkhfakSyafawi::class.java)
                            context.startActivity(intent)
                        }
                        "idghom mimi" -> {
                            val intent = Intent(context, IdghomMisli::class.java)
                            context.startActivity(intent)
                        }
                    }
                    // Kirim imagePath ke activity lain
//                    (context as MainActivity).captureAndCropImage(bbox)
                }
                selectedBoundingBox = null
                invalidate()
            }
        }
        return true
    }


    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}