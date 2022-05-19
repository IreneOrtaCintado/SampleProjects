package ioc.mediaplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ioc.mediaplayer.multimedia.AudioFile

/////////////////////////////////////////////////////////////////////
//  ADAPTER CLASS
/////////////////////////////////////////////////////////////////////

class MediaPlayerAdapter(trackActionListenerIO: OnTrackActionListener) :
    RecyclerView.Adapter<MediaPlayerAdapter.AudioHolder>() {

    //  Views in the ViewHolder
    var audioTitleIO: TextView? = null

    //  Holds the position in the recyclerView of the current
    //  track playing/paused (null if stopped)
    var trackPlayingIO: Int? = null

    //  Interface: OnTrackActionListener
    private var trackActionListenerIO: OnTrackActionListener? = trackActionListenerIO

    //  Audio file data
    var dataIO = listOf<AudioFile>()
        set(value) {
            field = value
            //  informs recycleView when the data is changed
            //  and redraws everything on the screen
            notifyDataSetChanged()
        }

    /**
     * Return amount of items in the adapter
     */
    override fun getItemCount(): Int = dataIO.size

    /**
     * Generates the view of each element
     */
    override fun onCreateViewHolder(parentIO: ViewGroup, viewTypeIO: Int): AudioHolder {
        //  Generate element
        val inflater: LayoutInflater =
            LayoutInflater.from(parentIO.context)
        val elementView: View = inflater.inflate(
            R.layout.element_track_list, parentIO, false
        )

        return AudioHolder(elementView, trackActionListenerIO)
    }

    /**
     * Binds views to each element. Adds functionality to the button
     */
    override fun onBindViewHolder(holderIO: AudioHolder, positionIO: Int) {
        //  Bind views
        holderIO.playButtonIO =
            holderIO.itemView.findViewById(R.id.imageButton_play_pause_button)
        audioTitleIO =
            holderIO.itemView.findViewById(R.id.textView_title_list_element)

        //  Set the text to the title
        audioTitleIO?.text = dataIO[positionIO].titleIO

        //  Set the correct icon for play button
        if (trackPlayingIO == positionIO)
            holderIO.playButtonIO?.setImageResource(R.drawable.ic_pause_button)
        else
            holderIO.playButtonIO?.setImageResource(R.drawable.ic_play_button)

        /**
         * onClick: Play/Stop audio from element button
         */
        holderIO.playButtonIO?.setOnClickListener {

            if (!holderIO.playingIO) {   //  PLAY
                //  Starts the code implemented in the main activity
                trackActionListenerIO?.onTrackPlay(positionIO, trackPlayingIO)

            } else {                 //  PAUSE
                //  Change button image
                holderIO.playButtonIO?.setImageResource(R.drawable.ic_play_button)

                //  Starts the code implemented in the main activity
                trackActionListenerIO?.onTrackPaused(positionIO)

                //  Set holder to not playing
                holderIO.playingIO = false
            }
        }
    }

    /////////////////////////////////////////////////////////////////////
    //  INNER CLASS CUSTOM RecyclerView.ViewHolder: AUDIO HOLDER
    /////////////////////////////////////////////////////////////////////

    inner class AudioHolder(v: View, onTrackActionListenerIO: OnTrackActionListener?) :
        RecyclerView.ViewHolder(v),
        View.OnClickListener {

        //  Is the track from this element playing?
        var playingIO: Boolean = false

        //  To control the button of each element separately
        var playButtonIO: ImageButton? = null

        //  Interface: OnTrackActionListener
        var onTrackActionListenerIO: OnTrackActionListener? = null

        init {
            //  Sets a listener when the element is clicked
            v.setOnClickListener(this)
            //  Init onTrackActionListener from constructor
            this.onTrackActionListenerIO = onTrackActionListenerIO
            //  Sets the correct view for the button in case the track
            //  is already playing when the element is generated
            if (adapterPosition == trackPlayingIO)
                playButtonIO?.setImageResource(R.drawable.ic_pause_button)
        }

        fun getImageButton(): ImageButton? {
            return playButtonIO
        }

        /**
         * Starts playing when this element is clicked
         */
        override fun onClick(v: View?) {
            if (!playingIO) {
                //  Starts the code implemented in the main activity
                onTrackActionListenerIO?.onTrackPlay(adapterPosition, trackPlayingIO)
            }
        }
    }

    /////////////////////////////////////////////////////////////////////
    //  INTERFACE to communicate with the mainActivity
    //  (INTER-FRAGMENT COMMUNICATION)
    /////////////////////////////////////////////////////////////////////

    interface OnTrackActionListener {
        /**
         * program functionality to start playing
         */
        fun onTrackPlay(positionIO: Int, oldPositionIO: Int?)

        /**
         * program functionality to paused playing
         */
        fun onTrackPaused(positionIO: Int)
    }

}