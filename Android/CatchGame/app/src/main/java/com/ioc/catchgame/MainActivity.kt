package com.ioc.catchgame

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.ioc.catchgame.gameEngine.difficulty
import com.ioc.catchgame.gameEngine.setDifficultyEasy
import com.ioc.catchgame.gameEngine.setDifficultyHard
import com.ioc.catchgame.gameEngine.setDifficultyNormal

class MainActivity : AppCompatActivity() {
    private var playButtonIO: Button? = null
    private var radioButtonEasyIO: RadioButton? = null
    private var radioButtonNormalIO: RadioButton? = null
    private var radioButtonHardIO: RadioButton? = null
    private var infoButtonIO: ImageView? = null

    val EASY: Int = 1
    val NORMAL: Int = 2
    val HARD: Int = 3

    /**
     *  Initializes the screen.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setOnClickListeners()

        //  set difficulty values
        when (difficulty){
            EASY    ->  setDifficultyEasy()
            NORMAL  ->  setDifficultyNormal()
            HARD    ->  setDifficultyHard()
        }
    }

    /**
     *  Binds views.
     */
    private fun bindViews() {
        radioButtonEasyIO = findViewById(R.id.radioButtonEasy)
        radioButtonNormalIO = findViewById(R.id.radioButtonNormal)
        radioButtonHardIO = findViewById(R.id.radioButtonHard)

        infoButtonIO = findViewById(R.id.infoButton)

        playButtonIO = findViewById(R.id.startButton)
    }

    /**
     *  Sets on Click listeners.
     */
    private fun setOnClickListeners() {

        radioButtonEasyIO?.setOnClickListener() {
            changeRadioButtons()
            difficulty = EASY
            setDifficultyEasy()
        }
        radioButtonNormalIO?.setOnClickListener() {
            changeRadioButtons()
            difficulty = NORMAL
            setDifficultyNormal()
        }
        radioButtonHardIO?.setOnClickListener() {
            changeRadioButtons()
            difficulty = HARD
            setDifficultyHard()
        }

        infoButtonIO?.setOnClickListener {
            //  shows a dialog with the rules of the game
            val message = "Encestar frutas suma puntos." +
                    "\nDejar caer frutas al suelo resta puntos." +
                    "\nEncestar enemigos hace perder vidas."
            var info = AlertDialog.Builder(this)
                .setTitle("INSTRUCCIONES")
                .setMessage(message)
                .setNeutralButton("OK",
                    DialogInterface.OnClickListener { dialog, id ->
                    }).show()
        }

        playButtonIO?.setOnClickListener {
            onClickStartButton()
        }
    }

    /**
     *  Starts the game activity.
     */
    private fun onClickStartButton() {
        val intentIO = Intent(this, GameActivity::class.java)
        //intent.putExtra(EXTRA_MESSAGE, "")
        startActivity(intentIO)
    }

    /**
     *  Modifies display of all radioButtons depending on selection.
     */
    private fun changeRadioButtons() {

        changeRadioButton(radioButtonEasyIO)
        changeRadioButton(radioButtonNormalIO)
        changeRadioButton(radioButtonHardIO)
    }

    /**
     * Modifies display of the radioButton passed as a parameter.
     */
    private fun changeRadioButton(radioButtonIO: RadioButton?) {
        if (radioButtonIO?.isChecked == true)
            radioButtonIO.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        else
            radioButtonIO?.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_green))
    }
}