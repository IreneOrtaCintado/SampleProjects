package com.ioc.catchgame.rendering

import kotlin.math.min

//  SCREEN
var screenWidth: Int = 0
var screenHeight: Int = 0

//  WORLD
var worldHeight: Int = 1600
var worldWidth: Int = 1000

//  SCALE
var scale: Float = 0F

/**
 * Translates a coordinate in the world to a coordinate in the screen
 */
fun world2screen(worldCoord: Int?): Int {
    if (worldCoord != null) {
        return (worldCoord * scale).toInt()
    }
    return 0
}

/**
 * Translates a coordinate in the screen to a coordinate in the world
 */
fun screen2world(screenCoord: Int?): Int {
    if (screenCoord != null) {
        return (screenCoord / scale).toInt()
    }
    return 0
}


/**
 * Sets the scale to adjust the world proportions to fit in the screen.
 * Calculates the coefficient necessary to fit the width of the world to the width of the screen,
 * and the same with the height.
 * Uses the smallest of them, so it fits.
 */
fun setScale() {
    var scaleX: Float = screenWidth.toFloat() / worldWidth.toFloat()
    var scaleY: Float = screenHeight.toFloat() / worldHeight.toFloat()

    scale = min(scaleX, scaleY)
}