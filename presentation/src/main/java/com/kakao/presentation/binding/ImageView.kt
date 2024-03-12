package com.kakao.presentation.binding

import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.kakao.presentation.R

@BindingAdapter("image_src")
fun loadImage(imageView: ImageView, src: String) {
  imageView.load(src) {
    placeholder(R.drawable.img_image_loading)
    error(R.drawable.img_image_failure)
  }
}

@BindingAdapter("image_adjustSrc")
fun loadAdjustlImage(imageView: ImageView, src: String) {
  imageView.setImageResource(R.drawable.img_image_loading)
  imageView.adjustViewBounds = true
  val imageRequest = ImageRequest.Builder(imageView.context)
    .data(src)
    .target { drawable ->
      val width = drawable.intrinsicWidth
      val height = drawable.intrinsicHeight

      if (width < height) {
        imageView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        imageView.layoutParams.height = imageView.height
      } else {
        imageView.layoutParams.width = imageView.width
        imageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
      }
      imageView.load(drawable)
    }.build()

  val imageLoader = ImageLoader(imageView.context)
  imageLoader.enqueue(imageRequest)
}

@BindingAdapter("image_isCorrect")
fun setBorder(imageView: ImageView, isCorrect: Boolean) {
  val attrValue = TypedValue()
  val colorId = when (isCorrect) {
    true -> com.google.android.material.R.attr.colorPrimary
    false -> com.google.android.material.R.attr.colorError
  }
  imageView.context.theme.resolveAttribute(colorId, attrValue, true)
  imageView.setBackgroundColor(ContextCompat.getColor(imageView.context, attrValue.resourceId))
}

@BindingAdapter("emoji")
fun setEmoji(view: ImageView, isCorrect: Boolean) {
  if (isCorrect) view.setImageResource(R.drawable.img_happy)
  else view.setImageResource(R.drawable.img_sad)
}