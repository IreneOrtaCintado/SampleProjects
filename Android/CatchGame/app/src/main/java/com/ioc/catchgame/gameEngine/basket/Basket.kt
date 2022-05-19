package com.ioc.catchgame.gameEngine.basket

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.ioc.catchgame.R

class Basket(context: Context) {
    var context = context
    val width: Int = 220
    val height: Int = 200
    val rectangle: Rect = Rect(0, 0, width, height)

    fun getBitmap(): Bitmap? {
        return AppCompatResources.getDrawable(this.context, R.drawable.ic_basket_picnic)
            ?.toBitmap(width, height)
    }
}