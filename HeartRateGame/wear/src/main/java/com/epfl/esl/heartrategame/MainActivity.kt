package com.epfl.esl.heartrategame

import android.app.Activity
import android.os.Bundle
import com.epfl.esl.heartrategame.databinding.ActivityMainBinding
import com.google.android.gms.wearable.*

class MainActivity : Activity(), DataClient.OnDataChangedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        dataEvents.filter { it.type == DataEvent.TYPE_CHANGED
                && it.dataItem.uri.path == "/gameInfo" }
            .forEach { event ->
                val timeLeft = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getString("timeLeft")

                binding.topTextView.text = timeLeft
            }
    }
}