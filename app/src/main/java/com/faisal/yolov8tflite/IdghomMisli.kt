package com.faisal.yolov8tflite

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.io.IOException

class IdghomMisli : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idghom_misli)

        val playButton: Button = findViewById(R.id.button)

        playButton.setOnClickListener {
            if (isPlaying) {
                stopAudio()
            } else {
                playAudio()
            }
        }
    }

    private fun playAudio() {
        // Release any existing MediaPlayer
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer()

        try {
            // Load the audio file from the assets folder
            val assetFileDescriptor = assets.openFd("idghommimi.mp3")
            mediaPlayer?.apply {
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()

                // Prepare and start the MediaPlayer
                prepare()
                start()
                this@IdghomMisli.isPlaying = true
                Toast.makeText(this@IdghomMisli, "Playing Audio", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error playing audio: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopAudio() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isPlaying = false
        Toast.makeText(this@IdghomMisli, "Audio Stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}