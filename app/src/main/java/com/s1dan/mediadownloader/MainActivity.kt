package com.s1dan.mediadownloader

import android.media.AudioManager
import android.media.MediaParser
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import kotlinx.coroutines.delay
import java.io.IOException


const val API_key = "AIzaSyDEcOLgtPfPhYkhNbZugO7tb-dJWOjxaao"
const val YT = "https://www.youtube.com/watch?v=L0WGZSiOZsM"

class MainActivity : AppCompatActivity() {

    private lateinit var editTextGetURL: EditText
    private lateinit var imageViewPicture: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnFind: Button
    private lateinit var btnAdding: Button
    private lateinit var mediaPlayer: MediaPlayer

    lateinit var runnable: Runnable
    private var handler = Handler()
    private lateinit var URL: String


    val audioURL = "https://www.youtube.com/watch?v=Wld5taGa1ag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaPlayer = MediaPlayer.create(this, R.raw.music)

        btnPlay = findViewById(R.id.imBtnPlay)
        btnPrevious = findViewById(R.id.imBtnBack)
        btnNext = findViewById(R.id.imBtnNext)
        editTextGetURL = findViewById(R.id.edGetURL)
        btnFind = findViewById(R.id.btnFind)

        val skBar = findViewById<SeekBar>(R.id.sbGetURl)
        skBar.progress = 0
        skBar.max = mediaPlayer.duration

        btnPlay.setOnClickListener {

            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                btnPlay.setImageResource(R.drawable.ic_pause_48)
//                playAudio()
            } else {
                mediaPlayer.pause()
                btnPlay.setImageResource(R.drawable.ic_play_48)

            }
        }

        btnPrevious.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.reset()
                btnPlay.setImageResource(R.drawable.ic_play_48)

                mediaPlayer.start()
            }

        }

        btnNext.setOnClickListener { }


        btnFind.setOnClickListener { }

        skBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })

        runnable = Runnable {
            skBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)
        }

        handler.postDelayed(runnable, 1000)

        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageResource(R.drawable.ic_pause_48)
            skBar.progress = 0
        }
    }



    private fun playAudio() {

        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer.setDataSource(audioURL)
            mediaPlayer.prepare()
            mediaPlayer.start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(this, "Audio started playing", Toast.LENGTH_SHORT).show()
    }

    private fun pauseAudio() { }



}