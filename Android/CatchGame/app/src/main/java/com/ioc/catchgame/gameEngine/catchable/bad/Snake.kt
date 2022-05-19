package com.ioc.catchgame.gameEngine.catchable.bad

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.ioc.catchgame.R
import com.ioc.catchgame.gameEngine.catchable.BadStuff

class Snake(context: Context) : BadStuff(context) {
    override var width: Int = 180
    override var height: Int = 270

    override var imageRect: Rect = Rect(0, 0, width, height)

    override fun getBitmap(): Bitmap? {
        return AppCompatResources.getDrawable(this.context, R.drawable.ic_bad_snake)
            ?.toBitmap(width,height)
    }
}