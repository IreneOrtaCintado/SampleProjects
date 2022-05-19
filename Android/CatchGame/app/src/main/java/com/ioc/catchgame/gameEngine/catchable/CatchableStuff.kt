package com.ioc.catchgame.gameEngine.catchable

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect

/**
 * Defines a type of object that can be caught.
 */
abstract class CatchableStuff() {
    abstract val context: Context
    open var width = 100
    open var height = 100
    open var imageRect: Rect = Rect(0, 0, width, height)
    var destinationRect: Rect = Rect(0, 0, width, height)
    var posX: Int = 0
    var posY: Int = 0

    abstract fun getBitmap(): Bitmap?
}