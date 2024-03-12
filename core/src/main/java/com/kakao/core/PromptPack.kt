package com.kakao.core

object PromptPack {

  const val DRAWN_BY_CRAYON = "drawn by kids with crayon, ((Drawn)), Drawings, Painting"

  object NegativePrompt {
    const val LOW_QUALITY = "out of frame, low resolution, blurry, worst quality, fuzzy, lowres, text, low quality, normal quality, signature, watermark, grainy"
    const val LOW_REALITY = "((Drawn)), Drawings, Painting, art, low resolution, bad proportions, cropped, duplicate, malformed, cropped, pixelated, sketch, grayscale"
    const val NOT_HUMAN = "poorly drawn hands, poorly drawn feet, poorly drawn face, out of frame, body out of frame, watermark, distorted face, bad anatomy"
    const val MISSING_BODY = "missing anatomy, missing body, missing face, missing legs, missing fingers, missing feet, missing toe, fewer digits, extra limbs"
    const val EXTRA_BODY = "extra anatomy, extra face, extra arms, extra fingers, extra hands, extra legs, extra feet, extra toe, mutated hands"
    const val UGLY = "ugly, mutilated, disfigured, mutation, bad proportions, cropped head, cross-eye, mutilated, distorted eyes, strabismus, skin blemishes"
  }

  const val MIN_WIDTH_DIVIDED_BY_8 = 48
  const val MIN_HEIGHT_DIVIDED_BY_8 = 48
  const val MAX_WIDTH_DIVIDED_BY_8 = 80
  const val MAX_HEIGHT_DIVIDED_BY_8 = 80

  fun String.splitWithDelimiters() = this.split(", ", ",", " ")
}