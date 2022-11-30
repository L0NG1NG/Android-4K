package com.longing.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation.INFINITE
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }


    private val sunYStart: Float by lazy {
        sunView.top.toFloat()
    }
    private val sunYEnd: Float by lazy {
        skyView.bottom.toFloat()
    }

    private var currentAnimator: AnimatorSet? = null
    private var isNight = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)

        val sunAnimator = ValueAnimator.ofFloat(1f, 0.8f, 1f).apply {
            repeatCount = INFINITE
            duration = 2000
            addUpdateListener {
                sunView.scaleX = it.animatedValue as Float
                sunView.scaleY = it.animatedValue as Float
            }
        }
        sunAnimator.start()

        sceneView.setOnClickListener {
            currentAnimator?.let {
                it.removeAllListeners()
                it.cancel()
            }
            if (!isNight) {
                startSunsetAnimation()
            } else {
                startSunriseAnimation()
            }

        }

    }

    private fun startSunsetAnimation() {
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        currentAnimator = AnimatorSet().apply {
            play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator)

            addListener(onEnd = {
                isNight = true
            })

        }.also { it.start() }

    }

    private fun startSunriseAnimation() {
        //reset sunView position
        sunView.y = sunYEnd

        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYEnd, sunYStart)
            .setDuration(3000)
        heightAnimator.interpolator = DecelerateInterpolator()

        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        currentAnimator = AnimatorSet().apply {
            play(sunriseSkyAnimator)
                .with(heightAnimator)
                .after(nightSkyAnimator)

            addListener(onEnd = {
                isNight = false
            })

        }.also { it.start() }

    }
}