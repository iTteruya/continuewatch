package com.example.continuewatch

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView

    private var appVisible = false

    private val sharedPref: SharedPreferences? by lazy { getPreferences(Context.MODE_PRIVATE) }

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
        secondsElapsed = sharedPref?.getInt(STATE_TIME, 0) ?: 0
        textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)

    }

    override fun onStop() {
        super.onStop()
        appVisible = false
        sharedPref?.edit()
            ?.putInt(STATE_TIME, secondsElapsed)
            ?.apply()
    }


    private companion object {
        private const val STATE_TIME = "secondsElapsed"
    }
}
