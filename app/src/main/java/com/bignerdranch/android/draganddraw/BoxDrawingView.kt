package com.bignerdranch.android.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class BoxDrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
  companion object {
    private const val TAG = "BowDrawingView"
    @ColorRes
    private const val BOX_COLOR_RES = R.color.box_color
    @ColorRes
    private const val BACKGROUND_COLOR_RES = R.color.background_color
  }

  private var currentBox: Box? = null
  private val boxen = mutableListOf<Box>()
  private val boxPaint = Paint().apply {
    color = ContextCompat.getColor(context, BOX_COLOR_RES)
  }
  private val backgroundPaint = Paint().apply {
    color = ContextCompat.getColor(context, BACKGROUND_COLOR_RES)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val current = PointF(event.x, event.y)
    var action = ""
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        action = "ACTION_DOWN"
        // Reset drawing state
        currentBox = Box(current).also { boxen.add(it) }
      }
      MotionEvent.ACTION_MOVE -> {
        action = "ACTION_MOVE"
        updateCurrentBox(current)
      }
      MotionEvent.ACTION_UP -> {
        action = "ACTION_UP"
        updateCurrentBox(current)
        currentBox = null
      }
      MotionEvent.ACTION_CANCEL -> {
        action = "ACTION_CANCEL"
        currentBox = null
      }
    }
    Log.i(TAG, "$action at x=${current.x}, y=${current.y}")
    return true
  }

  private fun updateCurrentBox(current: PointF) {
    currentBox?.let {
      it.end = current
      invalidate()
    }
  }

  override fun onDraw(canvas: Canvas) {
    canvas.drawPaint(backgroundPaint)
    boxen.forEach { box -> canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint) }
  }
}
