package com.kakao.presentation.utility

import android.content.Context
import com.kakao.presentation.R
import com.kakao.presentation.model.ImageUIModel
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.DefaultTemplate
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

fun ShareClient.Companion.share(context: Context, feed: DefaultTemplate) {
  if (instance.isKakaoTalkSharingAvailable(context)) {
    shareWithKakaoTalk(context, feed)
  } else {
    shareWithBrowser(context, feed)
  }
}

private fun ShareClient.Companion.shareWithKakaoTalk(context: Context, feed: DefaultTemplate) {
  instance.shareDefault(context, feed) { sharingResult, error ->
    if (error != null) throw error
    else if (sharingResult != null) {
      context.startActivity(sharingResult.intent)
    }
  }
}

private fun ShareClient.Companion.shareWithBrowser(context: Context, feed: DefaultTemplate) {
  val shareUrl = WebSharerClient.instance.makeDefaultUrl(feed)
  KakaoCustomTabsClient.openWithDefault(context, shareUrl)
}

fun ImageUIModel.makeFeed(context: Context): FeedTemplate = FeedTemplate(
  content = Content(
    title = context.getString(R.string.share_feed_title),
    description = context.getString(R.string.share_feed_description),
    imageUrl = url,
    link = Link(
      webUrl = "https://developers.kakao.com",
      mobileWebUrl = "https://developers.kakao.com"
    )
  ),
  buttons = listOf(
    Button(
      context.getString(R.string.share_feed_button),
      Link(
        androidExecutionParams = mapOf("imageId" to id)
      )
    )
  )
)