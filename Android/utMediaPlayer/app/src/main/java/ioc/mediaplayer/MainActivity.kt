package ioc.mediaplayer

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ioc.mediaplayer.MediaPlayerAdapter.OnTrackActionListener
import ioc.mediaplayer.multimedia.AudioFile
import ioc.mediaplayer.multimedia.millisecondsToShortTime
import android.media.AudioAttributes
import android.media.AudioManager
import androidx.core.content.res.ResourcesCompat
import java.io.IOException


class MainActivity : AppCompatActivity(), OnTrackActionListener, MediaPlayer.OnPreparedListener {

    //  GUI
    private var recyclerViewMediaPlayerIO: RecyclerView? = null
    var recyclerViewMediaPlayerAdapterIO: MediaPlayerAdapter? = null
    private lateinit var linearLayoutManagerIO: LinearLayoutManager

    private var currentSongTitleIO: TextView? = null
    private var currentSongArtistIO: TextView? = null

    private var playPauseButtonIO: ImageButton? = null
    private var previousButtonIO: ImageButton? = null
    private var nextButtonIO: ImageButton? = null
    private var stopButtonIO: ImageButton? = null
    private var volumeButtonIO: ImageButton? = null

    //  Permissions
    val REQUEST_EXTERNAL = 1
    val STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.WAKE_LOCK
    )
    var canREAD_IO: Boolean? = null
    var canWRITE_IO: Boolean? = null
    var canAccessMediaLocation: Boolean? = null

    // Data
    var audioFilesDataIO = mutableListOf<AudioFile>()

    //  Multimedia
    private var mediaPlayerIO: MediaPlayer? = null
    private var mediaPlayerStatusIO: Int = 0
    private val MEDIA_PLAYER_STOPPED = 0
    private val MEDIA_PLAYER_PLAYING = 1
    private val MEDIA_PLAYER_PAUSED = 2


    /**
     * ON CREATE: Methods called when this view is generated
     */
    override fun onCreate(savedInstanceStateIO: Bundle?) {
        super.onCreate(savedInstanceStateIO)
        setContentView(R.layout.activity_main)
        requestPermissions()
        bindViews()

        audioFilesDataIO = getDeviceAudioFiles()
        initRecyclerView()
        addDataToRecyclerView()
        setOnClickListeners()

        createMediaPlayer()
    }

    /////////////////////////////////////////////////////////////////////
    //  INITIALIZE VALUES
    /////////////////////////////////////////////////////////////////////

    /**
     * Bind view objects
     */
    private fun bindViews() {
        //  Text View
        currentSongTitleIO = findViewById(R.id.textView_current_title)
        currentSongArtistIO = findViewById(R.id.textView_current_artist)
        //  Buttons
        playPauseButtonIO = findViewById(R.id.imageButton_play_pause_button)
        previousButtonIO = findViewById(R.id.imageView_previous_button)
        nextButtonIO = findViewById(R.id.imageView_next_button)
        stopButtonIO = findViewById(R.id.imageView_stop_button)
        volumeButtonIO = findViewById(R.id.imageView_volume_button)

    }

    /**
     * Bind the recyclerView and adds a custom adapter to it
     */
    private fun initRecyclerView() {
        //  bind RecyclerView
        recyclerViewMediaPlayerIO = findViewById(R.id.recyclerview_audio_list)

        //  Create the custom adapter for the recycleView
        recyclerViewMediaPlayerAdapterIO =
            MediaPlayerAdapter(this)
        //  this refers to the interface OnTrackActionListener
        //  (implemented in this class and declared in the MediaPlayerAdapter class)
    }

    /**
     * Handles permissions requests
     */
    private fun requestPermissions() {
        //  Request permissions to the user
        ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, REQUEST_EXTERNAL)
        //  Save permission's status (accepted / denied)
        canREAD_IO = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        canWRITE_IO = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        /*//  Print permissions
        println("READ PERMISSION GRANTED = $canREAD")
        println("WRITE PERMISSION GRANTED = $canWRITE")
        println("MEDIA LOCATION PERMISSION GRANTED = $canAccessMediaLocation")*/
    }

    /**
     * Returns a MutableList with AudioFile objects containing the information
     * of the audio files on the device
     */
    private fun getDeviceAudioFiles(uriIO: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI):
            MutableList<AudioFile> {

        //  Get audio files in Alarms/, Audiobooks/, Music/, Musica/,
        //  Notifications/, Podcasts/, (Recordings/) and Ringtones/ directories

        val audioURI_IO = uriIO
        val audioFilesIO = mutableListOf<AudioFile>()

        //  Create a projection to extract specific metadata
        val projectionIO = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.RELATIVE_PATH
        )
        //  Create a cursor to obtain the info of individual files
        val audioCursorIO: Cursor? =
            this.contentResolver.query(
                audioURI_IO, projectionIO,
                null, null, null
            )

        if (audioCursorIO != null) {
            //  Go throw every item in the cursor
            while (audioCursorIO.moveToNext()) {
                //  Store the path of the file in var path
                val pathIO = audioCursorIO.getString(5)

                //  Filter audio files outside of music folders
                if (pathIO.contains("Music/", true) || pathIO.contains(
                        "Musica/",
                        true
                    ) || pathIO.endsWith("Música/", true)
                ) {
                    //  Store Values from the cursor
                    val id: Long = audioCursorIO.getInt(0).toLong()
                    val title: String = audioCursorIO.getString(1)
                    val duration = audioCursorIO.getString(2)
                    val artist = audioCursorIO.getString(3)
                    var year = audioCursorIO.getString(4)

                    //  Convert duration (controlling exceptions) to Long
                    val durationMilliseconds: Long = try {
                        duration.toLong()
                    } catch (e: NumberFormatException) {
                        0
                    } catch (e: NullPointerException) {
                        0
                    }
                    //  Format the duration from milliseconds to mm:ss
                    val formattedDuration = millisecondsToShortTime(durationMilliseconds)

                    // Remove null years
                    if (year.isNullOrBlank()) year = ""

                    // Show control data in console
                    //  println("$title $year - $artist - $formattedDuration")
                    //  println(path)

                    //  Store data in a MutableList<AudioFile> (attribute audioFiles)
                    audioFilesIO.add(AudioFile(id, title, artist, durationMilliseconds, year))
                }
            }
            audioCursorIO.close()
        }
        return audioFilesIO
    }

    /**
     *  Adds audio file data to the recyclerView
     */
    private fun addDataToRecyclerView() {
        // Creates a Layout Manager
        linearLayoutManagerIO = LinearLayoutManager(this)
        //  Adds the layoutManager to the recyclerView
        recyclerViewMediaPlayerIO?.layoutManager = linearLayoutManagerIO
        //  Adds the data to the recyclerView
        recyclerViewMediaPlayerAdapterIO?.dataIO = audioFilesDataIO
        //  Assigns the adapter to the recycleView
        recyclerViewMediaPlayerIO?.adapter = recyclerViewMediaPlayerAdapterIO

    }

    /////////////////////////////////////////////////////////////////////
    ////    FORMAT CONTROLS
    /////////////////////////////////////////////////////////////////////
    /**
     *  Changes the Title and Artist displayed on the controls on the main controls.
     *  Also controls the visibility of the text.
     */
    fun changeDisplayToCurrentSong(positionIO: Int) {
        //  Sets the title
        currentSongTitleIO?.text = audioFilesDataIO[positionIO].titleIO
        //  Sets the visibility of the title
        if (currentSongTitleIO?.isVisible == false)
            currentSongTitleIO?.visibility = View.VISIBLE
        //  Sets the artist
        currentSongArtistIO?.text = audioFilesDataIO[positionIO].artistIO
        //  Sets the visibility of the artist
        if (currentSongArtistIO?.isVisible == false && currentSongArtistIO?.text != "<unknown>")
            currentSongArtistIO?.visibility = View.VISIBLE
        else if (currentSongArtistIO?.text == "<unknown>") currentSongArtistIO?.visibility =
            View.GONE
    }

    /**
     * When the player is stopped, the values of the title and artist on the main controls
     * are set to default values and hidden
     */
    fun changeControlsToDefaultValues() {
        //  change title
        currentSongTitleIO?.text = getString(R.string.default_title)
        //  hide title
        if (currentSongTitleIO?.isGone == false)
            currentSongTitleIO?.visibility = View.GONE
        //  change artist
        currentSongArtistIO?.text = getString(R.string.default_artist)
        //  hide artist
        if (currentSongArtistIO?.isGone == false)
            currentSongArtistIO?.visibility = View.GONE
    }


    /////////////////////////////////////////////////////////////////////
    ////    MAIN CONTROLS ON CLICK LISTENERS
    /////////////////////////////////////////////////////////////////////

    /**
     *  On click listeners for play/pause,stop, previous, next and volume buttons
     */
    private fun setOnClickListeners() {

        playPauseButtonIO?.setOnClickListener {
            playPauseButtonClicked()
        }
        previousButtonIO?.setOnClickListener {
            previousButtonClicked()
        }
        nextButtonIO?.setOnClickListener {
            nextButtonClicked()
        }
        stopButtonIO?.setOnClickListener {
            stopButtonClicked()
        }
        volumeButtonIO?.setOnClickListener {
            showVolumeControls()
        }
    }

    /**
     * play/pause button: play stop the current track
     */
    fun playPauseButtonClicked() {
        //  Obtains if mediaPlayer is playing (false if null)
        val playingIO: Boolean = mediaPlayerIO?.isPlaying ?: false
        //  Obtains the position on th recyclerView of the track that is playing or that is paused
        val positionIO: Int? = recyclerViewMediaPlayerAdapterIO?.trackPlayingIO
        //  Checks if there is no track playing or paused
        if (positionIO != null) {
            if (!playingIO) {
                try {
                    //  starts playing
                    playAudio(positionIO)
                    //  changes the play button to pause
                    playPauseButtonIO?.setImageResource(R.drawable.ic_pause_button)
                } catch (e: IllegalStateException) {
                    handleNoFileFound(positionIO)
                } catch (e: IOException) {
                    handleNoFileFound(positionIO)
                }
            } else {
                //  pauses playing
                pauseAudio(positionIO)
                //  changes the pause button to play
                playPauseButtonIO?.setImageResource(R.drawable.ic_play_button)
            }
        } else if (audioFilesDataIO.size == 0)
        //  if no files found, sends error message
            noAudioFilesAvailableError()
        else {
            //  player stopped, and there are audio files available
            try {
                //  starts playing
                playAudio(0)
                //  changes the play button to pause
                playPauseButtonIO?.setImageResource(R.drawable.ic_pause_button)
                changeDisplayToCurrentSong(0)

            } catch (e: IllegalStateException) {
                handleNoFileFound(0)
            } catch (e:IOException){
                handleNoFileFound(0)
            }
        }
    }

    /**
     * previous button: plays the previous track
     */
    fun previousButtonClicked() {
        var newPositionIO: Int
        //  gets the track currently playing or paused
        val oldPositionIO: Int? = recyclerViewMediaPlayerAdapterIO?.trackPlayingIO
        //  Gets the number of tracks available
        val audioFilesAmountIO = recyclerViewMediaPlayerAdapterIO?.itemCount ?: 0

        if (audioFilesAmountIO > 0) {
            //  Selects the previous track. If no audio selected, starts from the last track
            newPositionIO = if (oldPositionIO == null) audioFilesAmountIO - 1
            else oldPositionIO - 1
            //  If starting from position 0, the previous track will be the last track
            if (newPositionIO < 0) newPositionIO = audioFilesAmountIO - 1

            //  starts playing
            onTrackPlay(newPositionIO, oldPositionIO)
            //  change play button to pause image
            playPauseButtonIO?.setImageResource(R.drawable.ic_pause_button)
        } else {
            noAudioFilesAvailableError()
        }
    }

    /**
     * next button: plays the next track
     */
    fun nextButtonClicked() {
        var newPositionIO: Int
        //  gets the track currently playing or paused
        val oldPositionIO: Int? = recyclerViewMediaPlayerAdapterIO?.trackPlayingIO
        //  Gets the number of tracks available
        val audioFilesAmountIO = recyclerViewMediaPlayerAdapterIO?.itemCount ?: 0

        if (audioFilesAmountIO > 0) {
            //  Selects the next track. If no audio selected, starts from the first track
            newPositionIO = if (oldPositionIO == null) 0
            else oldPositionIO + 1
            //  If starting from last item, the next track will be the first
            if (newPositionIO >= audioFilesAmountIO) newPositionIO = 0

            //  starts playing
            onTrackPlay(newPositionIO, oldPositionIO)
            //  change play button to pause image
            playPauseButtonIO?.setImageResource(R.drawable.ic_pause_button)
        } else {
            noAudioFilesAvailableError()
        }
    }

    /**
     *  stop button: stops the music and resets the player
     */
    fun stopButtonClicked() {
        //  gets the track currently playing or paused
        val positionIO: Int = recyclerViewMediaPlayerAdapterIO?.trackPlayingIO ?: -1

        //  if there was an active track
        if (positionIO != -1) {
            //  resets controls
            changeControlsToDefaultValues()
            //  stops audio, changing the recyclerView item of the current track
            //  to display a play button
            stopAudio(positionIO)

            //  change main play button to play image
            playPauseButtonIO?.setImageResource(R.drawable.ic_play_button)
            //  sets the attribute of the recyclerView adapter to null
            //  (no track playing/paused)
            recyclerViewMediaPlayerAdapterIO?.trackPlayingIO = null
        }
        if (audioFilesDataIO.size == 0) noAudioFilesAvailableError()
    }

    /**
     * volume button: opens the system volume control
     */
    fun showVolumeControls() {
        //  Creates and audioManager linked to the OS
        val audioManagerIO: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //  Opens the volume control for media of the OS and sets it to the current volume
        //  (to open the controls without making changes)
        audioManagerIO.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManagerIO.getStreamVolume(AudioManager.STREAM_MUSIC),
            1
        )
    }

    /////////////////////////////////////////////////////////////////////
    ////    INTERFACE: OnTrackPlayedListener
    /////////////////////////////////////////////////////////////////////

    /**
     *  Play started from one of the items on the audio track list
     * (Code to be executed when the function is called from the adapter)
     */
    override fun onTrackPlay(positionIO: Int, oldPositionIO: Int?) {

        //  first, stop last track and reset
        if (oldPositionIO != null && oldPositionIO != positionIO) {
            stopAudio(oldPositionIO)
        }
        // play and set new track
        try {
            //  Change play/pause button
            playAudio(positionIO)

            //  Change displayed title and artist
            changeDisplayToCurrentSong(positionIO)
            //  change main controls to pause image
            playPauseButtonIO?.setImageResource(R.drawable.ic_pause_button)

        } catch (e: IllegalStateException) {
            handleNoFileFound(positionIO)
        } catch (e: IOException) {
            handleNoFileFound(positionIO)
        }

    }

    /**
     *  Paused pressed on one of the items on the audio track list
     * (Code to be executed when the function is called from the adapter)
     */
    override fun onTrackPaused(positionIO: Int) {
        //  change main play button to play image
        playPauseButtonIO?.setImageResource(R.drawable.ic_play_button)

        if (mediaPlayerIO != null) {
            //  Pauses the audio
            mediaPlayerIO?.pause()
            //  sets status to paused (needed because mediaPlayer.isPlaying
            //  doesn't differentiate between paused and stopped)
            mediaPlayerStatusIO = MEDIA_PLAYER_PAUSED
        }
    }

    /////////////////////////////////////////////////////////////////////
    ////    MEDIA PLAYER SETUP
    /////////////////////////////////////////////////////////////////////

    /**
     * Creates a mediaPlayer object and assigns it to the attribute mediaPlayer
     * Sets up an onCompletionListener
     */
    private fun createMediaPlayer() {
        mediaPlayerIO = MediaPlayer()

        mediaPlayerIO?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            it // this is MediaPlayer type

            //  stops playing when the track finishes
            stopButtonClicked()
        })
    }

    /**
     * Sets the attributes of mediaPlayer
     */
    private fun setAudioAttributesOnMediaPlayer() {
        //  Sets the content type in mediaPlayer to Music and the usage to Media
        mediaPlayerIO?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    /**
     * Prepares mediaPlayer to start playing
     */
    @Throws(IllegalStateException::class, IOException::class)
    private fun setUpMediaPlayer(): Boolean {
        //  gets the track currently playing or paused (-1 if stopped)
        val positionIO: Int = recyclerViewMediaPlayerAdapterIO?.trackPlayingIO ?: -1
        //  Gets the number of tracks available
        val numOfTracksIO: Int = recyclerViewMediaPlayerAdapterIO?.dataIO?.size ?: 0
        val uriIO: Uri

        // controls if the position is not out of range
        if (positionIO < numOfTracksIO) {
            //  Convert id Long to Uri
            uriIO = ContentUris
                .withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    (audioFilesDataIO[positionIO].uriIdIO)
                )

            //  If doesn't find any audio when it should, for testing, comment the former instruction and uncomment the next
            //uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.meritocracy_idea)   //  load from res.raw

            setAudioAttributesOnMediaPlayer()
            //  Set and load audioFile
            mediaPlayerIO?.setDataSource(applicationContext, uriIO)   // throws IOException

            //  Use listener to start audio when ready
            mediaPlayerIO?.setOnPreparedListener(this)

            return true
        } else
            trackNotLoadedError(positionIO, numOfTracksIO)
        return false
    }

    /**
     * Sends message to the user, reloads audio files information and rests the interface.
     */
    fun handleNoFileFound(positionIO: Int) {
        val uriIO = ContentUris
            .withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                (audioFilesDataIO[positionIO].uriIdIO)
            )
        noFileFoundAtUriError(uriIO)
        audioFilesDataIO = getDeviceAudioFiles()
        addDataToRecyclerView()
        //  hides the title and artist
        changeControlsToDefaultValues()
        //  changes main play button to play image
        playPauseButtonIO?.setImageResource(R.drawable.ic_play_button)
        //  sets the attribute of the recyclerView adapter to null
        //  (no track playing/paused)
        recyclerViewMediaPlayerAdapterIO?.trackPlayingIO = null
    }

    /////////////////////////////////////////////////////////////////////
    ////    MEDIA PLAYER CONTROL FUNCTIONS
    /////////////////////////////////////////////////////////////////////

    /**
     * Setup & Start playing
     */
    @Throws(IllegalStateException::class, IOException::class)
    fun playAudio(positionIO: Int) {
        //  Checks if the viewHolder for the position received exists
        if (recyclerViewMediaPlayerIO?.findViewHolderForAdapterPosition(positionIO) != null) {
            //  Updates the status and image on the recyclerView item
            updateAudioHolder(
                positionIO,
                true,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_pause_button, null)
            )
            //  The position of track playing is updated
            recyclerViewMediaPlayerAdapterIO?.trackPlayingIO = positionIO
            //  If stopped
            if (mediaPlayerStatusIO == MEDIA_PLAYER_STOPPED) {
                setUpMediaPlayer()

                //  prepares mediaPlayer
                mediaPlayerIO?.prepareAsync()
            } else {
                //  if paused, start playing
                mediaPlayerIO?.start()
            }
        }
    }

    /**
     *  When the MediaPlayer is ready, play the audio
     */
    override fun onPrepared(playerIO: MediaPlayer) {  //  Problems when using app in phone
        //  starts playing
        playerIO.start()
        //  sets the status to playing
        mediaPlayerStatusIO = MEDIA_PLAYER_PLAYING
    }

    /**
     *  Pause
     */
    fun pauseAudio(positionIO: Int) {
        //  Checks if the viewHolder for the position received exists
        if (recyclerViewMediaPlayerIO?.findViewHolderForAdapterPosition(positionIO) != null) {
            //  Updates the status and image on the recyclerView item
            updateAudioHolder(
                positionIO,
                false,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_play_button, null)
            )
        }
        //  Checks if mediaPlayer is playing
        if (mediaPlayerIO != null && mediaPlayerIO?.isPlaying == true) {
            //  pauses the audio
            mediaPlayerIO?.pause()
            //  sets the status to paused
            mediaPlayerStatusIO = MEDIA_PLAYER_PAUSED
        }
    }

    /**
     * Stop & Release
     */
    fun stopAudio(positionIO: Int) {
        //  Checks if the viewHolder for the position received exists
        if (recyclerViewMediaPlayerIO?.findViewHolderForAdapterPosition(positionIO) != null) {
            //  Updates the status and image on the recyclerView item
            updateAudioHolder(
                positionIO,
                false,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_play_button, null)
            )
        }
        //  stops mediaPlayer
        if (mediaPlayerIO != null) {
            mediaPlayerIO?.stop()
            mediaPlayerIO?.reset()
            //  sets status to stopped
            mediaPlayerStatusIO = MEDIA_PLAYER_STOPPED
        }
    }

    /**
     *  Updates the status and image on the recyclerView item
     */
    fun updateAudioHolder(positionIO: Int, playingIO: Boolean, buttonImageIO: Drawable?) {
        //  creates a variable to store the AudioHolder (a ViewHolder of the custom
        //  adapter of the recyclerView) on the position received,
        //  to be able to interact with its components
        val audioHolderLastTrack: MediaPlayerAdapter.AudioHolder =
            recyclerViewMediaPlayerIO?.findViewHolderForAdapterPosition(positionIO) as MediaPlayerAdapter.AudioHolder

        //  changes track controls image
        audioHolderLastTrack.playButtonIO?.setImageDrawable(buttonImageIO)
        //  sets the attribute of the AudioHolder to playing or not
        audioHolderLastTrack.playingIO = playingIO
    }

    /////////////////////////////////////////////////////////////////////
    ////     ON CLOSING APP
    /////////////////////////////////////////////////////////////////////
    /**
     * Stops and releases the audio when the app is closed
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayerIO != null) {
            mediaPlayerIO?.stop()
            mediaPlayerIO?.release()
            mediaPlayerIO = null
        }
    }

    /////////////////////////////////////////////////////////////////////
    ////    ERROR MESSAGES
    /////////////////////////////////////////////////////////////////////
    /**
     *  Creates a toast message: there are no audio files available
     */
    fun noAudioFilesAvailableError() {
        Toast.makeText(
            applicationContext,
            "No audio files available in your Music folder.",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     *  Creates a toast message: the uri doesn't have a file
     */
    fun noFileFoundAtUriError(uriIO: Uri) {
        Toast.makeText(
            applicationContext,
            "File not found at $uriIO",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     *  Creates a toast message: the track at the position x of total tracks couldn't be loaded
     */
    fun trackNotLoadedError(positionIO: Int, numOfTracksIO: Int) {
        Toast.makeText(
            applicationContext,
            "Track not loaded. Position $positionIO/$numOfTracksIO",
            Toast.LENGTH_SHORT
        ).show()
    }
}

//  IF TIME
//  TODO    add seek bar
//  TODO    add current time and total duration
//  TODO    control what happens when moving audio files during runtime