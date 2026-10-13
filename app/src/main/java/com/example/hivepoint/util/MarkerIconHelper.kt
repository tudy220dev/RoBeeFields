package com.example.hivepoint.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object MarkerIconHelper {

    fun createBeehiveMarkerDrawable(context: Context, statusColorHex: String = "#FBC02D"): Drawable {
        val size = 96
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#33000000")
            style = Paint.Style.FILL
        }

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor(statusColorHex)
            style = Paint.Style.FILL
        }

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#3E2723")
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 42f
            textAlign = Paint.Align.CENTER
        }

        canvas.drawCircle(size / 2f, size / 2f + 4f, size / 2.4f, outerPaint)
        canvas.drawCircle(size / 2f, size / 2f, size / 2.5f, circlePaint)
        canvas.drawCircle(size / 2f, size / 2f, size / 2.5f, borderPaint)
        canvas.drawText("🐝", size / 2f, size / 2f + 14f, textPaint)

        return BitmapDrawable(context.resources, bitmap)
    }

    fun createApiaryMarkerDrawable(context: Context, apiaryName: String = "Stupină"): Drawable {
        val size = 112
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#44000000")
            style = Paint.Style.FILL
        }

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#FF8F00")
            style = Paint.Style.FILL
        }

        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#3E2723")
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }

        val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 46f
            textAlign = Paint.Align.CENTER
        }

        canvas.drawCircle(size / 2f, size / 2f + 4f, size / 2.3f, shadowPaint)
        canvas.drawCircle(size / 2f, size / 2f, size / 2.4f, bgPaint)
        canvas.drawCircle(size / 2f, size / 2f, size / 2.4f, borderPaint)
        canvas.drawText("🏠", size / 2f, size / 2f + 16f, iconPaint)

        return BitmapDrawable(context.resources, bitmap)
    }

    fun createGoogleBlueDotDrawable(context: Context): Drawable {
        val size = 64
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val haloPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#334285F4")
            style = Paint.Style.FILL
        }

        val whiteBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }

        val blueCorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#4285F4")
            style = Paint.Style.FILL
        }

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, haloPaint)
        canvas.drawCircle(size / 2f, size / 2f, size / 3.2f, whiteBorderPaint)
        canvas.drawCircle(size / 2f, size / 2f, size / 4.5f, blueCorePaint)

        return BitmapDrawable(context.resources, bitmap)
    }
}
