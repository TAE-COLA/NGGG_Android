package com.kakao.presentation.utility

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StickyHeaderItemDecoration(
  private val sectionCallback: SectionCallback
) : RecyclerView.ItemDecoration() {

  interface SectionCallback {
    fun isHeader(position: Int): Boolean
    fun getHeaderLayoutView(list: RecyclerView, position: Int): View?
  }

  override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    super.onDrawOver(canvas, parent, state)

    val topChild = parent.getChildAt(0) ?: return
    val topChildPosition = parent.getChildAdapterPosition(topChild).apply {
      if (this == RecyclerView.NO_POSITION) return
    }

    val currentHeader = sectionCallback.getHeaderLayoutView(parent, topChildPosition) ?: return
    fixLayoutSize(parent, currentHeader)

    val childInContact = getChildInContact(parent, currentHeader) ?: return
    val childAdapterPosition = parent.getChildAdapterPosition(childInContact).apply {
      if (this == RecyclerView.NO_POSITION) return
    }

    if (sectionCallback.isHeader(childAdapterPosition)) moveHeader(canvas, currentHeader, childInContact)
    else drawHeader(canvas, currentHeader)
  }

  private fun getChildInContact(parent: RecyclerView, currentHeader: View): View? {
    for (index in 0 until parent.childCount) {
      val child = parent.getChildAt(index)
      if (currentHeader.bottom in child.top until child.bottom) return child
    }
    return null
  }

  private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View) {
    canvas.run {
      save()
      translate(0f, nextHeader.top - currentHeader.height.toFloat())
      currentHeader.draw(this)
      restore()
    }
  }

  private fun drawHeader(canvas: Canvas, header: View) {
    canvas.run {
      save()
      translate(0f, 0f)
      header.draw(this)
      restore()
    }
  }

  private fun fixLayoutSize(parent: ViewGroup, view: View) {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
    val childWidth  = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
    val childHeight = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)
    view.measure(childWidth, childHeight)
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
  }
}