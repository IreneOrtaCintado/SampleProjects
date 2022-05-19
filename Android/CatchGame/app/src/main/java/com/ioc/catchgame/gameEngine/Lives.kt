package com.ioc.catchgame.gameEngine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.ioc.catchgame.R
import com.ioc.catchgame.rendering.world2screen

class Lives(private val context: Context) {
    val width = 60
    val height = 55
    val imageRect: Rect = Rect(0, 0, width, height)

    /**
     *  Returns the image to be drawn on the screen on Bitmap format.
     */
    fun getBitmap(): Bitmap? {
        return AppCompatResources.getDrawable(this.context, R.drawable.ic_heart_icon)
            ?.toBitmap(width, height)
    }

    /**
     * Returns the position on screen of a heart representing a life.
     * The position is adjusted depending on the life number.
     */
    fun getLifeDestinationRect(lives: Int): Rect {

        val left: Int = world2screen(((lives - 1) * width)+(15*lives))  //  starting horizontal position (margin = 15)
        val top: Int = world2screen(30)
        val right: Int = world2screen((lives * width)+(15*lives))       //  end horizontal position (margin = 15)
        val bottom: Int = world2screen(height + 30)

        return Rect(left, top, right, bottom)
    }
}