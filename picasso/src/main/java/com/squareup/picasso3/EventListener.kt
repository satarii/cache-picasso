package com.squareup.picasso3

import android.graphics.Bitmap
import androidx.core.graphics.BitmapCompat
import java.io.Closeable

interface EventListener : Closeable {
  fun performCacheHit()
  fun performCacheMiss()
  fun performDownloadFinished(size: Long)
  fun performBitmapDecoded(bitmap: Bitmap)
  fun performBitmapTransformed(bitmap: Bitmap)
  override fun close() = Unit
}

class StatsEventListener : EventListener {
  private var cacheHits = 0L
  private var cacheMisses = 0L
  private var totalDownloadSize = 0L
  private var totalOriginalBitmapSize = 0L
  private var totalTransformedBitmapSize = 0L

  private var averageDownloadSize = 0.0
  private var averageOriginalBitmapSize = 0.0
  private var averageTransformedBitmapSize = 0.0

  private var downloadCount = 0
  private var originalBitmapCount = 0
  private var transformedBitmapCount = 0

  override fun performCacheHit() {
    cacheHits++
  }

  override fun performCacheMiss() {
    cacheMisses++
  }

  override fun performDownloadFinished(size: Long) {
    downloadCount++
    totalDownloadSize += size
    averageDownloadSize = average(downloadCount, totalDownloadSize)
  }

  override fun performBitmapDecoded(bitmap: Bitmap) {
    val bitmapSize = BitmapCompat.getAllocationByteCount(bitmap)

    originalBitmapCount++
    totalOriginalBitmapSize += bitmapSize
    averageOriginalBitmapSize = average(originalBitmapCount, totalOriginalBitmapSize)
  }

  override fun performBitmapTransformed(bitmap: Bitmap) {
    val bitmapSize = BitmapCompat.getAllocationByteCount(bitmap)

    transformedBitmapCount++
    totalTransformedBitmapSize += bitmapSize
    averageTransformedBitmapSize = average(originalBitmapCount, totalTransformedBitmapSize)
  }

  private fun average(
    count: Int,
    totalSize: Long
  ): Double = totalSize * 1.0 / count
}