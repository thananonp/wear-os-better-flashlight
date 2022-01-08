package com.thananonp.betterflashligt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.InputDeviceCompat
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewConfigurationCompat
import com.thananonp.betterflashligt.databinding.ActivityMainBinding


class MainActivity : Activity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private var isRedFlashlightOn: Boolean = false
    private var isNormalFlashlightOn: Boolean = false

    private val brightnessKey = "BRIGHTNESS"
    private var seekBarValue: Int = 0


    private fun setBackgroundColor(color: Int) {
//        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        binding.layoutMain.setBackgroundColor(color)
        binding.layoutMain.invalidate()
    }

    private fun toggleRedFlashLight() {
        if (!isRedFlashlightOn) {
            setBackgroundColor(Color.RED)
            isRedFlashlightOn = true
            isNormalFlashlightOn = false
        } else {
            setBackgroundColor(Color.BLACK)
            isRedFlashlightOn = false
            isNormalFlashlightOn = false
        }
    }

    private fun toggleNormalFlashLight() {
        if (!isNormalFlashlightOn) {
            setBackgroundColor(Color.WHITE)
            isNormalFlashlightOn = true
            isRedFlashlightOn = false
        } else {
            setBackgroundColor(Color.BLACK)
            isRedFlashlightOn = false
            isNormalFlashlightOn = false
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (event?.repeatCount == 0) {
            when (keyCode) {
                KeyEvent.KEYCODE_STEM_1 -> {
                    toggleNormalFlashLight()
                    true
                }
                KeyEvent.KEYCODE_STEM_2 -> {
                    toggleRedFlashLight()
                    true
                }
                else -> {
                    super.onKeyDown(keyCode, event)
                }
            }
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun checkSystemWritePermission(packageName: String) {
        if (!Settings.System.canWrite(this)) {
            Toast.makeText(
                this,
                "Please allow the app to change system brightness",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse(packageName)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        checkSystemWritePermission("package:$packageName")
        val savedBrightness = this.getPreferences(MODE_PRIVATE).getInt(brightnessKey, 50)

        binding.apply {
            buttonNormalFlashlight.setOnClickListener {
                toggleNormalFlashLight()
            }
            buttonRedFlashlight.setOnClickListener {
                toggleRedFlashLight()
            }
            layoutMain.apply {
                requestFocus()
                setOnClickListener {
                    setBackgroundColor(Color.BLACK)
                    isRedFlashlightOn = false
                    isNormalFlashlightOn = false
                }
                setOnGenericMotionListener { _, ev ->
                    if (ev.action == MotionEvent.ACTION_SCROLL &&
                        ev.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)
                    ) {
                        val delta = -ev.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                                ViewConfigurationCompat.getScaledVerticalScrollFactor(
                                    ViewConfiguration.get(context), context
                                )
                        seekBarBrightness.progress += (delta / 3).toInt()
                        seekBarValue = seekBarBrightness.progress
                        true
                    } else {
                        false
                    }
                }
            }
            buttonSetDefaultBrightness.setOnClickListener {
                val sharedPref = this@MainActivity.getPreferences(Context.MODE_PRIVATE)
                    ?: return@setOnClickListener
                with(sharedPref.edit()) {
                    putInt(brightnessKey, seekBarValue)
                    apply()
                    Toast.makeText(this@MainActivity, "Saved: $seekBarValue", Toast.LENGTH_SHORT)
                        .show()
                }
                setScreenBrightness(seekBarValue)
            }
            if (savedBrightness != 0) {
                seekBarBrightness.progress = savedBrightness
                setScreenBrightness(savedBrightness)
            }


        }

    }

    private fun setScreenBrightness(value: Int) {
        val lp = window.attributes
        lp.screenBrightness = value / 100.toFloat()
        window.attributes = lp
    }
}