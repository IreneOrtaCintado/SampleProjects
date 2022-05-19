package com.ioc.catchgame.rendering

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.ioc.catchgame.R
import com.ioc.catchgame.gameEngine.Lives
import com.ioc.catchgame.gameEngine.basket.Basket
import com.ioc.catchgame.gameEngine.catchable.BadStuff
import com.ioc.catchgame.gameEngine.catchable.CatchableStuff
import com.ioc.catchgame.gameEngine.catchable.GoodStuff
import com.ioc.catchgame.gameEngine.catchable.bad.Snake
import com.ioc.catchgame.gameEngine.catchable.bad.Spider
import com.ioc.catchgame.gameEngine.catchable.fruit.*
import com.ioc.catchgame.gameEngine.scoreFactor

/**
 * Draws game objects.
 */
class GameScreen(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    //  FRUIT
    var goodStuffIO: HashMap<Int, GoodStuff?> = hashMapOf<Int, GoodStuff?>()

    //  ENEMIES
    var badStuffIO: HashMap<Int, BadStuff?> = hashMapOf<Int, BadStuff?>()

    //  BASKET
    private var basketPositionX = worldWidth.div(2)

    //  GAME VALUES
    var scoreIO: Int = 0              //  points earned catching fruit
    var livesIO: Int = 3              //  times you can touch an enemy before triggering a  game over
    var stepsIO: Int = 0              //  falling speed
    var timePlayedIO: Long? = -1

    var addEnemyIO: Boolean = false   //  flag to add a new enemy (simultaneously)
    var addFruitIO: Boolean = true    //  flag to add one more fruit (simultaneously)


    /**
     *  Updates the position of the basket with the input of the user touching the screen.
     */
    override fun onTouchEvent(eventIO: MotionEvent): Boolean {
        val action: Int = eventIO.action
        //  get the position in X of the event
        val x: Int = eventIO.x.toInt()

        //  filter the type of action
        when (action) {
            //  updates position when moving the finger on ths screen (not just touching)
            MotionEvent.ACTION_MOVE -> {
                basketPositionX = x
                //  calls onDraw
                this.invalidate()
            }
        }
        return true
    }

    /**
     * Handles the drawing of all the elements on the screen.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDraw(canvasIO: Canvas) {
        super.onDraw(canvasIO)
        //  objects to paint
        var worldPaintIO: Paint = Paint()
        var objectPaintIO: Paint = Paint()
        var scorePaintIO: Paint = Paint()
        //  colors
        worldPaintIO.color = Color.TRANSPARENT
        worldPaintIO.style = Paint.Style.FILL_AND_STROKE
        scorePaintIO.color = ContextCompat.getColor(context, R.color.white)
        //  text
        val scoreTextSize: Int = 75
        val scoreTypeface = ResourcesCompat.getFont(context, R.font.autour_one)
        scorePaintIO.textAlign = Paint.Align.RIGHT
        scorePaintIO.typeface = scoreTypeface
        scorePaintIO.setShadowLayer(5f, 1.1f, 1.3f, Color.GRAY)
        scorePaintIO.textSize = world2screen(scoreTextSize).toFloat()

        //  WORLD

        //  world area
        val worldWidthOnscreen = world2screen(worldWidth)
        val worldHeightOnScreen = world2screen(worldHeight)
        //  paint world area
        canvasIO.drawRect(Rect(0, 0, worldWidthOnscreen, worldHeightOnScreen), worldPaintIO)

        //  change position of fruit
        addStepToFruits(stepsIO)
        //  change position of enemies
        addStepToEnemies(stepsIO)

        //  if the timer to add fruit sets the flag to true, adds a new fruit to the HashMap
        if (addFruitIO) {
            goodStuffIO?.put(goodStuffIO?.size, pickFruit())
            addFruitIO = false
        }

        //  DRAW FRUITS

        //  draws every fruit in the array of GoodStuff
        for (fruitPair in goodStuffIO) {
            //  gets the fruit object int the HashMap
            var fruit: GoodStuff? = fruitPair.value

            if (fruit != null) {
                //  generate new fruit when not picked (position at the bottom of the screen)
                if (fruit.posY > (worldHeight - fruit.height)) {
                    //  subtracts points for not catching the fruit
                    scoreIO -= (fruit.points * scoreFactor)
                    //  replaces fruit with a new type picked randomly from a list
                    goodStuffIO?.replace(fruitPair.key, pickFruit())
                }

                //  gets the image of the fruit
                var bitMap: Bitmap? = fruit.getBitmap()

                //  creates the rectangle where the fruit image will be placed
                fruit.destinationRect = Rect(
                    world2screen(fruit.posX - (fruit.width / 2)),
                    world2screen(fruit.posY),
                    world2screen(fruit.posX + (fruit.width / 2)),
                    world2screen(fruit.posY + fruit.height)
                )

                //  draws the fruit
                if (bitMap != null) {
                    canvasIO?.drawBitmap(
                        bitMap,
                        fruit.imageRect,
                        fruit.destinationRect,
                        objectPaintIO
                    )
                }
            }
        }

        //  if the timer to add enemies sets the flag to true, adds a new enemy to the HashMap
        if (addEnemyIO) {
            badStuffIO?.set(badStuffIO.size, getBadStuff())
            addEnemyIO = false
        }

        //  DRAW ENEMIES

        //  draws every enemy in the array of BadStuff
        for (enemyPair in badStuffIO) {
            //  gets the enemy object int the HashMap
            var enemy: BadStuff? = enemyPair.value
            if (enemy != null) {
                //  generate new enemy when not picked (position at the bottom of the screen)
                if (enemy.posY > (worldHeight - enemy.height)) {
                    //  replaces enemy with a new type picked randomly from a list
                    badStuffIO.replace(enemyPair.key, getBadStuff())
                }

                // gets the image of the enemy
                var bitMap: Bitmap? = enemy.getBitmap()

                //  creates the rectangle where the enemy image will be placed
                enemy.destinationRect = Rect(
                    world2screen(enemy.posX - (enemy.width / 2)),
                    world2screen(enemy.posY),
                    world2screen(enemy.posX + (enemy.width / 2)),
                    world2screen(enemy.posY + enemy.height)
                )

                //  draws the enemy
                if (bitMap != null) {
                    canvasIO?.drawBitmap(
                        bitMap,
                        enemy.imageRect,
                        enemy.destinationRect,
                        objectPaintIO
                    )
                }
            }
        }

        //  BASKET

        //  create basket
        var basket = Basket(this.context)
        val basketHalfWidth: Int = basket.width / 2
        //  limit movement
        if (basketPositionX < basketHalfWidth) basketPositionX = basketHalfWidth
        else if (basketPositionX > (worldWidth - basketHalfWidth)) basketPositionX =
            worldWidth - basketHalfWidth
        //  basket position rectangle
        var basketDestinationRect = Rect(
            world2screen(basketPositionX - (basketHalfWidth)),
            world2screen(worldHeight - basket.height),
            world2screen(basketPositionX + (basketHalfWidth)),
            world2screen(worldHeight)
        )
        //  get basket image
        var basketBitMap: Bitmap? = basket.getBitmap()

        if (basketBitMap != null) {
            //  draw basket
            canvasIO?.drawBitmap(
                basketBitMap,
                basket.rectangle,
                basketDestinationRect,
                objectPaintIO
            )
        }

        //  SCORE

        //  checks if any fruit has been caught
        for (fruitPair in goodStuffIO) {
            var fruit: GoodStuff? = fruitPair.value
            if (fruit != null) {
                //  checks if the fruit has been caught
                if (Rect.intersects(basketDestinationRect, fruit.destinationRect)) {
                    //  adds points to the score
                    scoreIO += (fruit.points * scoreFactor)
                    //  picks a new fruit
                    goodStuffIO.replace(fruitPair.key, pickFruit())
                }
            }
        }

        //  draws the score on the screen
        canvasIO.drawText(
            scoreIO.toString(),
            world2screen(worldWidth - 20).toFloat(),
            world2screen(scoreTextSize + 10).toFloat(),
            scorePaintIO
        )

        //  LIVES

        //  checks if any enemy has been caught
        for (enemyPair in badStuffIO) {
            var enemy: BadStuff? = enemyPair.value
            if (enemy != null) {
                //  checks if the enemy has been caught
                if (Rect.intersects(basketDestinationRect, enemy.destinationRect)) {
                    //  removes one life
                    livesIO -= 1
                    //  picks a new enemy
                    badStuffIO.replace(enemyPair.key, getBadStuff())
                }
            }
        }

        //  creates Lives object
        var life = Lives(context)
        var lifeBitMap: Bitmap? = life.getBitmap()
        //  paints any available life
        for (i in 1..livesIO) {
            var lifeDestinationRect = life.getLifeDestinationRect(i)
            if (lifeBitMap != null) {
                //  draw image
                canvasIO.drawBitmap(lifeBitMap, life.imageRect, lifeDestinationRect, objectPaintIO)
            }
        }
    }

    /**
     * Returns a random x position in the world
     */
    private fun newSpawnLocation(catchableStuff: CatchableStuff): Int {
        return ((Math.random() * (worldWidth - (catchableStuff.width))) + (catchableStuff.width / 2)).toInt()
    }

    /**
     *  Returns a random picked fruit.
     */
    private fun pickFruit(): GoodStuff {

        val fruitList = listOf(
            Cherry(context),
            Banana(context),
            Grapes(context),
            Orange(context),
            Pineapple(context)
        )
        var fruit = fruitList.random()
        fruit.posX = newSpawnLocation(fruit)

        return fruit
    }

    /**
     *  Returns a random picked enemy.
     */
    private fun getBadStuff(): BadStuff {
        val enemyList = listOf(
            Spider(context),
            Snake(context)
        )
        var enemy = enemyList.random()
        enemy.posX = newSpawnLocation(enemy)

        return enemy
    }

    /**
     *  Changes the position of the fruit.
     */
    private fun addStepToFruits(stepIO: Int) {
        for (fruitPair in goodStuffIO) {
            var fruit: GoodStuff? = fruitPair.value
            if (fruit != null) {
                fruit.posY += stepIO
            }
        }
    }

    /**
     *  Changes the position of the enemy.
     */
    private fun addStepToEnemies(stepIO: Int) {
        for (enemyPair in badStuffIO) {
            var enemy: BadStuff? = enemyPair.value
            if (enemy != null) {
                enemy.posY += stepIO
            }
        }
    }
}

