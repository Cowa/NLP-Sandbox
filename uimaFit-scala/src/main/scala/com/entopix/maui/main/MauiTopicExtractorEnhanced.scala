package com.entopix.maui.main

/**
 * A simple wrapper to be able to change `topicsPerDocument` attribute when outside of `com.entopix.maui.main` #maui
 */
class MauiTopicExtractorEnhanced extends MauiTopicExtractor {
  def setTopicsPerDocument(n: Int) = topicsPerDocument = n
}
