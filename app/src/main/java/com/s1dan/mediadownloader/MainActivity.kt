package com.s1dan.mediadownloader

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.widget.*

import com.s1dan.mediadownloader.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var editTextGetURL: EditText
    private lateinit var imageViewPicture: ImageView
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnFind: ImageButton
    private lateinit var mediaPlayer: MediaPlayer

    lateinit var runnable: Runnable
    private var handler = Handler()

    //way 1
    private val STORAGE_PERMISSION_CODE: Int = 1000

    val audioURL = "https://www.youtube.com/watch?v=Wld5taGa1ag"
    val imageURL = "https://yandex.ru/images/search?utm_source=main_stripe_big&text=Совообразные&nl=1&source=morda&pos=4&rpt=simage&img_url=http%3A%2F%2Fs1.1zoom.ru%2Fbig3%2F888%2FEyes_Owls_Bubo_502568.jpg&lr=103817"
    val testURL = "https://atmiyauni.ac.in/wp-content/uploads/2020/04/AU-Brochure-update-March-2020.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        

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

        btnNext.setOnClickListener {  }

        val btnOpen = findViewById<Button>(R.id.btn_Adding_to_BD)
        btnOpen.setOnClickListener {

        }




        btnFind.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    // permission denied, request it

                    // show popup for runtime permission
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                } else {
                    // permission already granted, perform download
                    startDownloading()
                }

            } else {
                // system os is less than marshmallow, runtime permission not required, perform download
                startDownloading()
            }
        }

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

    private fun startDownloading() {
        // get text/url from edit text
        val url = editTextGetURL.text.toString()

        // download request
        val request = DownloadManager.Request(Uri.parse(url))
        // allow type of network to download file(s) by default both are allowed
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")


        // get download service, and enqueue file
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup was granted, perform download
                    startDownloading()
                } else {
                    // permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun OpenFile() {

    }
}