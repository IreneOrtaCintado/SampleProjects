package com.ioc.catchgame.gameEngine.catchable

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.ioc.catchgame.R

/**
 * Negative objects that should not be caught.
 */
abstract class BadStuff(override val context: Context) : CatchableStuff() {

}