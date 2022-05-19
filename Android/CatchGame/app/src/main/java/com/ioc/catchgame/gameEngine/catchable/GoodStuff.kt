package com.ioc.catchgame.gameEngine.catchable

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.ioc.catchgame.R

/**
 * Positive objects that should be caught.
 */
abstract class GoodStuff(override val context: Context) : CatchableStuff() {

    abstract val points: Int

}