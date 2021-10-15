package com.example.continuewatch

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView

    private var appVisible = false

    private var backgroundThread = Thread {
        while (true) {
            Thread.sleep(1000)
            textSecondsElapsed.post {
                if (appVisible) {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        backgroundThread.start()
    }

    override fun onStart() {
        super.onStart()
        appVisible = true
    }

    override fun onStop() {
        super.onStop()
        appVisible = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(STATE_TIME, secondsElapsed)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        secondsElapsed = savedInstanceState.getInt(STATE_TIME, 0)
        textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
        super.onRestoreInstanceState(savedInstanceState)
    }

    private companion object {
        private const val STATE_TIME = "secondsElapsed"
    }
}
