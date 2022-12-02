package com.example.heartrategame

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest

class GameViewModel : ViewModel() {
    lateinit var dataClient: DataClient

    fun sendTimeToWear(timeLeftText: String) {
        val request = PutDataMapRequest.create("/gameInfo").run {
            dataMap.putString("timeLeft", timeLeftText)
            asPutDataRequest()
        }

        request.setUrgent()
        val putTask = dataClient.putDataItem(request)
    }
}