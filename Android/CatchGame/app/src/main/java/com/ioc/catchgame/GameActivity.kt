package com.ioc.catchgame

import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import com.ioc.catchgame.gameEngine.*
import com.ioc.catchgame.rendering.GameScreen
import com.ioc.catchgame.rendering.screenHeight
import com.ioc.catchgame.rendering.screenWidth
import com.ioc.catchgame.rendering.setScale
import java.util.*

/**
 * Activity where the game is played.
 */
class GameActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {
    var gameScreenIO: GameScreen? = null
    var handlerIO = Handler()
    var backgroundCoverIO: View? = null

    var timerFallingIO = Timer()
    var timerAddEnemiesIO = Timer()
    var timerAddFruitIO = Timer()

    var mediaPlayerIO = MediaPlayer()

    /**
     *  Binds views. Initializes values when all elements are drawn.
     *  Starts music and timers.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //  BIND VIEWS
        backgroundCoverIO = findViewById(R.id.view_background_cover_menu)
        gameScreenIO = findViewById(R.id.game_screen)

        //  INITIALIZES VALUES
        var observer: ViewTreeObserver? = gameScreenIO?.viewTreeObserver
        //  on gameScreen view finished drawing
        observer?.addOnGlobalLayoutListener {
            //  save width and height (only available after onDraw)
            screenWidth = gameScreenIO?.width ?: 0
            screenHeight = gameScreenIO?.height ?: 0
            //  generate scale to adjust game to device screen
            setScale()
            //  set falling speed
            gameScreenIO?.stepsIO = startingStep
        }

        // play music
        initMediaPlayer()
        playMusic()

        //  start timers
        startTimerAddFruit()
        startTimerAddEnemies()
        startTimerFalling()
    }

    //  TIMERS    ///////////////////////////////////////////////////////

    /**
     *  Sets up a timer to control the movement of the fruit and the enemies.
     */
    fun startTimerFalling() {
        //  MOVE FRUITS AND ENEMIES
        //  Execute every 1000/frameRate milliseconds

        timerFallingIO.schedule(object : TimerTask() {
            override fun run() {
                handlerIO.post {

                    //  refresh screen (onDraw)
                    gameScreenIO?.invalidate()

                    if (gameScreenIO?.livesIO!! <= 0) gameOver()

                    gameScreenIO?.timePlayedIO = gameScreenIO?.timePlayedIO?.plus(1)
                }
            }
        }, 10, (1000 / frameRate).toLong())
    }

    /**
     *  Sets up a timer to control the addition of enemies.
     */
    fun startTimerAddEnemies() {
        //  ADD ENEMIES
        var counter = 0
        timerAddEnemiesIO.schedule(object : TimerTask() {
            override fun run() {
                //  sets a flag to increase the amounts of enemies on the screen
                if (gameScreenIO?.badStuffIO?.size!! <= maxEnemies) {
                    gameScreenIO?.addEnemyIO = true
                }
                //  adds a life after a while playing
                val lives = gameScreenIO?.livesIO
                if (lives != null) {
                    if (counter == 5 && lives < 5) {
                        gameScreenIO?.livesIO = lives + 1
                    }
                }
                counter++
            }
        }, delayEnemyAppearance * 1000, (intervalAddingEnemies * 1000).toLong())
    }

    /**
     *  Sets up a timer to control the addition of fruit.
     */
    fun startTimerAddFruit() {
        //  ADD FRUITS
        //seconds
        timerAddFruitIO.schedule(object : TimerTask() {
            override fun run() {
                //  sets a flag to increase the amounts of fruits on the screen
                if (gameScreenIO?.goodStuffIO?.size!! <= maxFruit) {
                    gameScreenIO?.addFruitIO = true
                }
                //  increase the speed of the game
                if (gameScreenIO != null) {
                    gameScreenIO!!.stepsIO += stepIncrease
                }
            }

        }, 10 * 1000, (intervalAddingFruit * 1000).toLong())
    }

    //  MUSIC    ///////////////////////////////////////////////////////

    /**
     *  Initializes the MediaPlayer values.
     */
    fun initMediaPlayer(){
        //  Make the music start again when it finishes
        mediaPlayerIO.isLooping = true

        /*mediaPlayerIO?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            it // this is MediaPlayer type
        })*/

        //  Sets the content type in mediaPlayer to Music and the usage to Media
        mediaPlayerIO?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    /**
     *  Starts the process to start playing music. When ready, MediaPlayer will trigger
     *  the method onPrepared.
     */
    fun playMusic(){

        //  Prepare mediaPlayer to start playing
        //  load from res.raw
        var uriIO: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.fruit_music)

        //  Set and load audioFile
        mediaPlayerIO?.setDataSource(applicationContext, uriIO)   // throws IOException

        //  Use listener to start audio when ready
        mediaPlayerIO?.setOnPreparedListener(this)

        //  prepares mediaPlayer
        mediaPlayerIO?.prepareAsync()

    }

    /**
     *  When the MediaPlayer is ready, play the audio.
     */
    override fun onPrepared(playerIO: MediaPlayer) {  //  Problems when using app in phone
        //  starts playing
        playerIO.start()
        //  sets the status to playing
        //mediaPlayerStatusIO = MEDIA_PLAYER_PLAYING
    }

    //  END GAME    ///////////////////////////////////////////////////////

    /**
     *  Stops the fruit and enemy timers, stops the music and shows a dialog with the score.
     */
    fun gameOver() {
        //  stop timers
        timerFallingIO.cancel()
        timerAddEnemiesIO.cancel()
        timerAddFruitIO.cancel()

        //  stop music
        mediaPlayerIO?.stop()
        mediaPlayerIO?.reset()

        //  show score
        val message = "PUNTUACIÓN: " + gameScreenIO?.scoreIO
        var gameOverDialog = AlertDialog.Builder(this)
            .setTitle("GAME OVER")
            .setMessage(message)
            .setNegativeButton("SALIR",
                DialogInterface.OnClickListener { dialog, id ->
                    exitGame()
                })
            .setOnDismissListener { exitGame() }
        gameOverDialog.show()
    }

    /**
     *  Changes the screen to the start menu.
     */
    fun exitGame() {
        val intentIO = Intent(this, MainActivity::class.java)
        //intent.putExtra(EXTRA_MESSAGE, "")
        startActivity(intentIO)
    }

    /**
     *  Finishes the game when the back button is pressed
     */
    override fun onBackPressed() {
        //super.onBackPressed()
        gameOver()
    }
}

/*
EXTRAS
Save higher scores
change background
add bonus points for time
 */