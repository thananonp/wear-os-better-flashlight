package com.thananonp.betterflashligt

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.wear.input.WearableButtons
import com.thananonp.betterflashligt.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private var isRedFlashlightOn: Boolean = false
    private var isNormalFlashlightOn: Boolean = false


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
            binding.imageViewFlashlight.visibility = View.GONE
            binding.imageViewFlashlightOff.visibility = View.VISIBLE
        } else {
            setBackgroundColor(Color.BLACK)
            isRedFlashlightOn = false
            isNormalFlashlightOn = false
            binding.imageViewFlashlight.visibility = View.VISIBLE
            binding.imageViewFlashlightOff.visibility = View.GONE
        }
    }

    private fun toggleNormalFlashLight() {
        if (!isNormalFlashlightOn) {
            setBackgroundColor(Color.WHITE)
            isNormalFlashlightOn = true
            isRedFlashlightOn = false
            binding.imageViewFlashlight.visibility = View.GONE
            binding.imageViewFlashlightOff.visibility = View.VISIBLE
        } else {
            setBackgroundColor(Color.BLACK)
            isRedFlashlightOn = false
            isNormalFlashlightOn = false
            binding.imageViewFlashlight.visibility = View.VISIBLE
            binding.imageViewFlashlightOff.visibility = View.GONE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.apply {
            buttonNormalFlashlight.setOnClickListener {
                toggleNormalFlashLight()
            }
            buttonRedFlashlight.setOnClickListener {
                toggleRedFlashLight()
            }
            layoutMain.setOnClickListener {
                setBackgroundColor(Color.BLACK)
                isRedFlashlightOn = false
                isNormalFlashlightOn = false
                binding.imageViewFlashlight.visibility = View.VISIBLE
                binding.imageViewFlashlightOff.visibility = View.INVISIBLE
            }

        }

    }
}