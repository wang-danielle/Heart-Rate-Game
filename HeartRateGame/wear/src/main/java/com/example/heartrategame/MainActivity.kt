package com.example.heartrategame

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.example.heartrategame.databinding.ActivityMainBinding
import com.example.heartrategame.models.Exercise
import com.google.android.gms.wearable.*

class MainActivity : Activity(), SensorEventListener, DataClient.OnDataChangedListener {

    private lateinit var binding: ActivityMainBinding
    private var heartRate = 0

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
                val timeLeft = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getString("timeLeft")
                binding.bottomTextView.text = timeLeft

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
                    .getInt("score")
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

    private fun displayResults(score: Int) {
        binding.topTextView.text = "Results"
        binding.bottomTextView.text = "Score: ${score.toString()}"
        binding.image.setImageResource(R.drawable.ic_logo)
        binding.image.clearColorFilter()
        binding.parentView.setBackgroundColor(
            ResourcesCompat.getColor(resources, R.color.black, null)
        )
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val heartRateReceived = event!!.values[0].toInt()
        heartRate = heartRateReceived
    }

    private fun sendHRToMobile() {
        val dataClient: DataClient = Wearable.getDataClient(this)
        val request = PutDataMapRequest.create("/heartRate").run {
            dataMap.putInt("heartRate", heartRate)
            asPutDataRequest()
        }
        request.setUrgent()
        dataClient.putDataItem(request)
    }
}