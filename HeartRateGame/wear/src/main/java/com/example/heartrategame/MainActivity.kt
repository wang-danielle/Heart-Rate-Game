package com.example.heartrategame

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.example.heartrategame.databinding.ActivityMainBinding
import com.example.heartrategame.models.Exercise
import com.google.android.gms.wearable.*
import java.lang.Math.sqrt

class MainActivity : Activity(), SensorEventListener, DataClient.OnDataChangedListener {

    data class Vector(var x: Float, var y: Float, var z: Float)

    private lateinit var binding: ActivityMainBinding
    private var heartRate = 0
    private var accelMagnitude = 0F
    private var gravity = Vector(0F, 0F, 0F)
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission("android.permission.BODY_SENSORS")
            == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf("android.permission.BODY_SENSORS"), 0)
        }

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)?.also { heartRate ->
            sensorManager.registerListener(this, heartRate,
                SensorManager.SENSOR_DELAY_UI)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it,
                SensorManager.SENSOR_DELAY_UI)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()

        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.filter { it.dataItem.uri.path == "/newTimeRequest" }
            .forEach { event ->
                val timeLeft = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getString("timeLeft")

                binding.bottomTextView.text = timeLeft

                sendHRToMobile()
            }
        dataEvents.filter { it.dataItem.uri.path == "/newExerciseRequest" }
            .forEach { event ->
                val newExercise = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getInt("newExercise")
                val exercise = Exercise.values()[newExercise]
                displayExercise(exercise)
            }
        dataEvents.filter { it.dataItem.uri.path == "/quitRequest" }
            .forEach {
                displayHome()
            }
        dataEvents.filter { it.dataItem.uri.path == "/resultsRequest" }
            .forEach { event ->
                val score = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getDouble("score")
                displayResults(score)
            }
    }

    private fun displayExercise(exercise: Exercise) {
        binding.topTextView.text = exercise.title
        binding.image.setImageResource(exercise.imageResource)
        binding.image.setColorFilter(Color.WHITE)
        binding.parentView.setBackgroundColor(
            ResourcesCompat.getColor(resources, R.color.pink_600, null)
        )
        vibrator.vibrate(250)
    }

    private fun displayHome() {
        binding.topTextView.text = resources.getString(R.string.welcome_text)
        binding.bottomTextView.text = ""
        binding.image.setImageResource(R.drawable.ic_logo)
        binding.image.clearColorFilter()
        binding.parentView.setBackgroundColor(
            ResourcesCompat.getColor(resources, R.color.black, null)
        )
    }

    private fun displayResults(score: Double) {
        binding.topTextView.text = "Results"
        binding.bottomTextView.text = "Score: $score"
        binding.image.setImageResource(R.drawable.ic_logo)
        binding.image.clearColorFilter()
        binding.parentView.setBackgroundColor(
            ResourcesCompat.getColor(resources, R.color.black, null)
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val sensor = it.sensor
            when (sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val alpha = 0.8F

                    gravity.x = alpha * gravity.x + (1 - alpha) * it.values[0]
                    gravity.y = alpha * gravity.y + (1 - alpha) * it.values[1]
                    gravity.z = alpha * gravity.z + (1 - alpha) * it.values[2]

                    val x = it.values[0] - gravity.x
                    val y = it.values[1] - gravity.y
                    val z = it.values[2] - gravity.z
                    accelMagnitude = kotlin.math.sqrt(x*x + y*y + z*z)
                }
                Sensor.TYPE_HEART_RATE -> {
                    val heartRateReceived = it.values[0].toInt()
                    heartRate = heartRateReceived
                }
            }
            return@let
        }
    }

    private fun sendHRToMobile() {
        val dataClient: DataClient = Wearable.getDataClient(this)
        val request = PutDataMapRequest.create("/heartRate").run {
            val timestamp = System.currentTimeMillis()
            dataMap.putLong("timestamp", timestamp)
            dataMap.putBoolean("isMoving", accelMagnitude >= 0.5F)
            dataMap.putInt("heartRate", heartRate)
            asPutDataRequest()
        }
        request.setUrgent()
        dataClient.putDataItem(request)
    }
}